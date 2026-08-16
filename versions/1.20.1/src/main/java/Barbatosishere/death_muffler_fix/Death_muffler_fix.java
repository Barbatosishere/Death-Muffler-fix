package Barbatosishere.death_muffler_fix;

import Barbatosishere.death_muffler_fix.client.ClientHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Death Muffler Fix 主类（Forge 1.20.1）。
 *
 * MUG 1.20.1 的 BossBarHidingEvent 编译完整、原生功能可用，但其按硬编码的
 * 英文 Boss 名（"Wither"/"Dragon"）匹配，非英文环境下血条隐藏失效。
 * 本模组在游戏事件总线上注册多语言（游戏语言系统）名称匹配的增强实现。
 */
@Mod("death_muffler_fix")
public final class Death_muffler_fix {
    public static final String MOD_ID = "death_muffler_fix";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Death_muffler_fix() {
        // 客户端专属：注册 Boss 血条隐藏事件（游戏事件总线）
        if (FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.addListener(ClientHandler::onBossBarRender);
        }

        LOGGER.info("Death Muffler Fix mod loaded");
    }
}
