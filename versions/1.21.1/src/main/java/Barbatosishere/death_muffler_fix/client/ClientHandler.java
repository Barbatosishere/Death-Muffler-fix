package Barbatosishere.death_muffler_fix.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

/**
 * 客户端处理器。
 *
 * 负责 Boss 血条隐藏逻辑：读取玩家持久数据中的 MGU_WitherMuffle /
 * MGU_DragonMuffle 开关，取消对应 Boss（凋灵 / 末影龙）血条的渲染。
 * 该功能替代 MGU 原版 BossBarHidingEvent 的缺失实现（见 DoClientStuffMixin）。
 */
public final class ClientHandler {

    private ClientHandler() {
    }

    /** 在 Boss 血条渲染时按消声机开关取消渲染（游戏事件总线，仅客户端）。 */
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