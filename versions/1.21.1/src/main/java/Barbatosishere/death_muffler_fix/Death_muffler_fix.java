package Barbatosishere.death_muffler_fix;

import Barbatosishere.death_muffler_fix.client.ClientHandler;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModColourManager;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

/**
 * Death Muffler Fix 主类。
 *
 * 本模组修复实用设备（Mob Grinding Utils）消声机的 Boss 血条隐藏逻辑：
 * MGU 1.1.10 因构建缺陷（BossBarHidingEvent 未编译，只发布了 .java 源文件）
 * 导致客户端初始化时加载缺失类而崩溃。本模组通过 Mixin 在缺失类实例化前
 * 终止 doClientStuff()（其前段 3 个事件注册不受影响），并在此处补注册
 * 被跳过的 XP 流体渲染层与颜色处理器，同时重新实现等价的血条隐藏功能。
 *
 * 此外，Mixin 取消 doClientStuff() 后会丢失 worldUnload 监听注册（原用于
 * 在切换世界时重建 SPIKE_DAMAGE 缓存），本模组通过反射在 LevelEvent.Unload
 * 时将该缓存置 null，使 MUG 在进入新世界时按新 registryAccess 重建。
 *
 * 注意：不能采用同包同名 shim 补类方案，NeoForge 1.21.1 的 JPMS 模块层
 * 禁止多个 mod jar 导出同一包（ResolutionException: ... export package
 * mob_grinding_utils.events），实测启动即崩。故采用 Miixin 方案。
 */
@Mod("death_muffler_fix")
public final class Death_muffler_fix {
    public static final String MOD_ID = "death_muffler_fix";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Death_muffler_fix(IEventBus modEventBus) {
        if (FMLEnvironment.dist.isClient()) {
            // 游戏事件总线：Boss 血条隐藏 + worldUnload 缓存补偿
            NeoForge.EVENT_BUS.addListener(ClientHandler::onBossBarRender);
            NeoForge.EVENT_BUS.addListener(Death_muffler_fix::onWorldUnload);
            // 模组事件总线：补注册 MUG doClientStuff() 被跳过的尾部逻辑
            modEventBus.addListener(Death_muffler_fix::onClientSetup);
        }

        LOGGER.info("Death Muffler Fix mod loaded");
    }

    /** 客户端初始化：复刻 MUG doClientStuff() 中被跳过的尾部逻辑。 */
    private static void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_XP_FLOWING.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_XP.get(), RenderType.translucent());
        event.enqueueWork(ModColourManager::registerColourHandlers);
    }

    /**
     * worldUnload 补偿：Mixin 取消 doClientStuff() 后，MUG 的 worldUnload
     * 监听未被注册，切换世界时 SPIKE_DAMAGE 缓存不会被清空重建。
     * 通过反射在卸载世界时将 MobGrindingUtils.SPIKE_DAMAGE 置 null，
     * 下次访问时 MUG 会按新世界的 registryAccess 重新构建。
     */
    private static void onWorldUnload(LevelEvent.Unload event) {
        try {
            Field spikeField = mob_grinding_utils.MobGrindingUtils.class.getDeclaredField("SPIKE_DAMAGE");
            spikeField.setAccessible(true);
            spikeField.set(null, null);
        } catch (NoSuchFieldException e) {
            // MUG 更新后字段名可能变化，安全忽略
            LOGGER.debug("SPIKE_DAMAGE field not found in MobGrindingUtils, skipping worldUnload compensation");
        } catch (Exception e) {
            LOGGER.warn("Failed to clear SPIKE_DAMAGE on world unload", e);
        }
    }
}
