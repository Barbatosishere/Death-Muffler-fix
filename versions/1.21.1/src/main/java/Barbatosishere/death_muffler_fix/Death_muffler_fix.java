package Barbatosishere.death_muffler_fix;

import Barbatosishere.death_muffler_fix.client.ClientHandler;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Death Muffler Fix 主类。
 *
 * 本模组修复实用设备（Mob Grinding Utils）消声机的 Boss 血条隐藏逻辑：
 * MGU 1.1.10 因构建缺陷（BossBarHidingEvent 未编译，只发布了 .java 源文件）
 * 导致客户端初始化时加载缺失类而崩溃。本模组通过 Mixin 阻止 MGU 的
 * doClientStuff() 执行，并在游戏事件总线上重新实现等价的血条隐藏功能。
 */
@Mod("death_muffler_fix")
public final class Death_muffler_fix {
    public static final String MOD_ID = "death_muffler_fix";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Death_muffler_fix() {
        // 客户端专属：注册 Boss 血条隐藏事件（游戏事件总线）
        if (FMLEnvironment.dist.isClient()) {
            NeoForge.EVENT_BUS.addListener(ClientHandler::onBossBarRender);
        }

        LOGGER.info("Death Muffler Fix mod loaded");
    }
}
