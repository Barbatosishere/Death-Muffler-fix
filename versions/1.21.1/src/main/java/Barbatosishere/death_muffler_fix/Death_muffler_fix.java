package Barbatosishere.death_muffler_fix;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Death Muffler Fix 主类 - Shim补丁版本。
 *
 * 本模组修复实用设备（Mob Grinding Utils）模组的Boss血条隐藏功能：
 * MGU 1.1.10 因构建缺陷（BossBarHidingEvent 未编译，只发布了 .java 源文件）
 * 导致游戏启动时加载缺失类而崩溃。本模组通过提供BossBarHidingEvent的编译版本，
 * 补全MGU缺失的类文件，修复客户端崩溃问题。
 * 
 * 修复原理：提供与MGU 1.1.10中BossBarHidingEvent同包同名的shim类，
 * 补全缺失的编译版本，防止游戏运行时找不到类的错误。
 */
@Mod("death_muffler_fix")
public final class Death_muffler_fix {
    public static final String MOD_ID = "death_muffler_fix";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Death_muffler_fix(IEventBus modEventBus) {
        LOGGER.info("Death Muffler Fix mod loaded - BossBarHidingEvent shim implemented");
    }
}
