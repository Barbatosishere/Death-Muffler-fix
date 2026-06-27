package Barbatosishere.death_muffler_fix;

import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("death_muffler_fix")
public class Death_muffler_fix {
    public static final String MOD_ID = "death_muffler_fix";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Death_muffler_fix() {
        LOGGER.info("Death Muffler Fix mod loaded");
    }
}
