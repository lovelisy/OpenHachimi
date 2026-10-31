package pw.hachimi.client.mixin;

import java.awt.Color;
import net.minecraft.client.render.DimensionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.render.GetFogColorOverrideEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(DimensionEffects.class)
public class DimensionEffectsMixin {
   @Inject(method = "getFogColorOverride(FF)[F", at = @At("HEAD"), cancellable = true)
   private void hookGetFogColorOverride(float f, float f2, CallbackInfoReturnable callbackInfoReturnable) {
      GetFogColorOverrideEvent getFogColorOverrideEvent = new GetFogColorOverrideEvent(f2);
      EventBusImpl.INSTANCE.rC(getFogColorOverrideEvent);
      if (getFogColorOverrideEvent.aik()) {
         Color color = getFogColorOverrideEvent.getColor();
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(new float[]{color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F});
      }
   }
}
