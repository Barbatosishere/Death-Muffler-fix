package Barbatosishere.death_muffler_fix.mixin;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to prevent MGU's MobGrindingUtils.doClientStuff() from executing.
 * The original method references BossBarHidingEvent which was not compiled
 * in MGU 1.1.10 (build bug - .java shipped instead of .class).
 * By cancelling at HEAD, the missing class is never resolved.
 * The boss bar hiding functionality is provided by Death_muffler_fix.ClientEvents instead.
 */
@Mixin(targets = "mob_grinding_utils.MobGrindingUtils")
public class DoClientStuffMixin {

    @Inject(method = "doClientStuff", at = @At("HEAD"), cancellable = true, remap = false)
    private void onDoClientStuff(FMLClientSetupEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
