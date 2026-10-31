package pw.hachimi.client.mixin;

import net.minecraft.entity.LimbAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.movement.GetSpeedEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(LimbAnimator.class)
public final class LimbAnimatorMixin {
   @Inject(method = "getSpeed()F", at = @At("HEAD"), cancellable = true)
   private void hookGetSpeed(CallbackInfoReturnable callbackInfoReturnable) {
      GetSpeedEvent getSpeedEvent = new GetSpeedEvent();
      EventBusImpl.INSTANCE.rC(getSpeedEvent);
      if (getSpeedEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getSpeedEvent.ar());
      }
   }

   @Inject(method = "getSpeed(F)F", at = @At("HEAD"), cancellable = true)
   private void hookGetSpeed(float f, CallbackInfoReturnable callbackInfoReturnable) {
      GetSpeedEvent getSpeedEvent = new GetSpeedEvent();
      EventBusImpl.INSTANCE.rC(getSpeedEvent);
      if (getSpeedEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getSpeedEvent.ar());
      }
   }
}
