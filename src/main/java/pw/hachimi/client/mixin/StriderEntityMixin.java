package pw.hachimi.client.mixin;

import net.minecraft.entity.passive.StriderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.IsSaddledEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(StriderEntity.class)
public class StriderEntityMixin {
   @Inject(method = "isSaddled()Z", at = @At("HEAD"), cancellable = true)
   private void hookIsSaddled(CallbackInfoReturnable callbackInfoReturnable) {
      IsSaddledEvent isSaddledEvent = new IsSaddledEvent();
      EventBusImpl.INSTANCE.rC(isSaddledEvent);
      if (isSaddledEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(true);
      }
   }
}
