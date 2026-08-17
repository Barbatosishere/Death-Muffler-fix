package Barbatosishere.death_muffler_fix.client;

import mob_grinding_utils.capability.bossbars.BossBarPlayerCapability;
import mob_grinding_utils.capability.bossbars.IBossBarCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

/**
 * 客户端处理器（Forge 1.12.x）。
 *
 * MUG 0.3.13 原版 BossBarHidingEvent 按硬编码英文 Boss 名 "Wither" /
 * "Ender Dragon" 匹配，非英文环境（如中文）血条隐藏失效。本监听器
 * 叠加注册，改用 Minecraft 语言系统名称匹配，兼容多语言环境。
 * 原版监听器仍保留（覆盖 isWitherCrumbsBoss 等额外逻辑），互不冲突。
 */
public final class ClientHandler {

    private ClientHandler() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBossBarRender(RenderGameOverlayEvent.BossInfo event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player == null) return;

        IBossBarCapability cap = player.getCapability(BossBarPlayerCapability.CAPABILITY_PLAYER_BOSS_BAR, null);
        if (cap == null) return;

        // MGU semantics: return=false means "should hide", so check if capability wants to hide
        boolean shouldHideWither = !cap.renderWitherBar();
        boolean shouldHideDragon = !cap.renderEnderDragonBar();
        
        if (!shouldHideWither && !shouldHideDragon) return;

        String bossName = event.getBossInfo().getName().getUnformattedText();
        String witherName = I18n.format("entity.Wither.name");
        String dragonName = I18n.format("entity.EnderDragon.name");

        // DEBUG: Log diagnostic information
        Death_muffler_fix.LOGGER.info("Boss Bar Event: bossName='{}', shouldHideWither={}, shouldHideDragon={}, witherName='{}', dragonName='{}'", 
            bossName, shouldHideWither, shouldHideDragon, witherName, dragonName);

        if (shouldHideWither && bossName.contains(witherName)) {
            Death_muffler_fix.LOGGER.info("CANCELING WITHER boss bar: matched '{}'", bossName);
            event.setCanceled(true);
        } else if (shouldHideDragon && bossName.contains(dragonName)) {
            Death_muffler_fix.LOGGER.info("CANCELING DRAGON boss bar: matched '{}'", bossName);
            event.setCanceled(true);
        }
    }
}
