package Barbatosishere.death_muffler_fix;

import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Death Muffler Fix 主类。
 *
 * 本模组修复实用设备（Mob Grinding Utils）1.1.10 的构建缺陷：BossBarHidingEvent
 * 只发布了 .java 源文件、没有编译 .class，客户端初始化执行到
 * new BossBarHidingEvent() 即抛 NoClassDefFoundError。
 *
 * 修复方式为将缺失类编译进本模组 jar（同包同名 shim，
 * 见 mob_grinding_utils/events/BossBarHidingEvent），MGU 的 doClientStuff()
 * 从此完整执行，无需 Mixin 或逻辑补偿。
 */
@Mod("death_muffler_fix")
public final class Death_muffler_fix {
    public static final String MOD_ID = "death_muffler_fix";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Death_muffler_fix() {
        LOGGER.info("Death Muffler Fix mod loaded");
    }
}
