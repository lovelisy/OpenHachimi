package pw.hachimi.client.mixin;

import net.minecraft.world.biome.FoliageColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.render.GetDefaultColorEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(FoliageColors.class)
public class FoliageColorsMixin {
   @Inject(method = "getDefaultColor()I", at = @At("HEAD"), cancellable = true)
   private static void hookGetDefaultColor(CallbackInfoReturnable callbackInfoReturnable) {
      GetDefaultColorEvent getDefaultColorEvent = new GetDefaultColorEvent();
      EventBusImpl.INSTANCE.rC(getDefaultColorEvent);
      if (getDefaultColorEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getDefaultColorEvent.MQ());
      }
   }
}
