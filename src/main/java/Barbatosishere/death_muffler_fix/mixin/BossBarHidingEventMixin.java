package Barbatosishere.death_muffler_fix.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(targets = "mob_grinding_utils.events.BossBarHidingEvent")
@SuppressWarnings("UnresolvedMixinReference")
public class BossBarHidingEventMixin {

    @Inject(method = "onRenderHUD", at = @At("HEAD"), cancellable = true, remap = false)
    public void onRenderHUD(CustomizeGuiOverlayEvent.BossEventProgress event, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        var nbt = player.getPersistentData();
        boolean hideWither = nbt.getBoolean("MGU_WitherMuffle");
        boolean hideDragon = nbt.getBoolean("MGU_DragonMuffle");

        if (!hideWither && !hideDragon) return;

        // 通过内置原版语言系统解析实体名，再与血条显示名比对
        // Boss Event UUID ≠ 实体 UUID，不能用 UUID 查找实体
        String bossName = event.getBossEvent().getName().getString();
        String witherName = Component.translatable("entity.minecraft.wither").getString();
        String dragonName = Component.translatable("entity.minecraft.ender_dragon").getString();

        if (hideWither && bossName.equals(witherName)) {
            event.setCanceled(true);
            ci.cancel();
        } else if (hideDragon && bossName.equals(dragonName)) {
            event.setCanceled(true);
            ci.cancel();
        }
    }
}
