package Barbatosishere.death_muffler_fix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("death_muffler_fix")
public class Death_muffler_fix {
    public static final String MOD_ID = "death_muffler_fix";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Death_muffler_fix() {
        LOGGER.info("Death Muffler Fix mod loaded");
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onBossBarRender(CustomizeGuiOverlayEvent.BossEventProgress event) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            var nbt = player.getPersistentData();
            boolean hideWither = nbt.getBoolean("MGU_WitherMuffle");
            boolean hideDragon = nbt.getBoolean("MGU_DragonMuffle");

            if (!hideWither && !hideDragon) return;

            String bossName = event.getBossEvent().getName().getString();
            String witherName = I18n.get("entity.minecraft.wither");
            String dragonName = I18n.get("entity.minecraft.ender_dragon");

            if (hideWither && bossName.contains(witherName)) {
                event.setCanceled(true);
            } else if (hideDragon && bossName.contains(dragonName)) {
                event.setCanceled(true);
            }
        }
    }
}
