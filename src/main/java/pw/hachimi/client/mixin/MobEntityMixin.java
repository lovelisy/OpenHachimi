package pw.hachimi.client.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PigEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.IsAiDisabledEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(MobEntity.class)
public class MobEntityMixin {
   @Inject(method = "isAiDisabled()Z", at = @At("HEAD"), cancellable = true)
   private void hookIsAiDisabled(CallbackInfoReturnable callbackInfoReturnable) {
      if ((Object)this instanceof PigEntity) {
         IsAiDisabledEvent isAiDisabledEvent = new IsAiDisabledEvent((PigEntity)(Object)this);
         EventBusImpl.INSTANCE.rC(isAiDisabledEvent);
         if (isAiDisabledEvent.aik()) {
            callbackInfoReturnable.cancel();
            callbackInfoReturnable.setReturnValue(true);
         }
      }
   }
}
