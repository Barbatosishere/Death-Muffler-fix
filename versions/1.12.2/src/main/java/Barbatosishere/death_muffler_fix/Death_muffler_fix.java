package Barbatosishere.death_muffler_fix;

import Barbatosishere.death_muffler_fix.client.ClientHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Death Muffler Fix 主类（Forge 1.12.x：1.12 / 1.12.1 / 1.12.2，构建工具链为 1.12.2）。
 *
 * MUG 0.3.13 的 BossBarHidingEvent 编译完整、原生血条隐藏功能可用，
 * 但其按硬编码 Boss 名 "Wither"/"Ender Dragon" 匹配，非英文环境
 * 失效。本模组注册语言系统名称匹配的客户端监听器（叠加式增强，
 * 与原版监听互不冲突，见 ClientHandler）。
 */
@Mod(modid = Death_muffler_fix.MOD_ID, version = Death_muffler_fix.VERSION, useMetadata = true,
        acceptedMinecraftVersions = "[1.12,1.13)")
public final class Death_muffler_fix {
    public static final String MOD_ID = "death_muffler_fix";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // 客户端专属：注册语言匹配的 Boss 血条隐藏监听（游戏事件总线）
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(ClientHandler.class);
        }

        LOGGER.info("Death Muffler Fix mod loaded");
    }
}
