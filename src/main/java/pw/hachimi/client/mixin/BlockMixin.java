package pw.hachimi.client.mixin;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.GetSlipperinessEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(Block.class)
public class BlockMixin {
   @Inject(method = "getSlipperiness()F", at = @At("RETURN"), cancellable = true)
   private void hookGetSlipperiness(CallbackInfoReturnable callbackInfoReturnable) {
      GetSlipperinessEvent getSlipperinessEvent = new GetSlipperinessEvent((Block)(Object)this, callbackInfoReturnable.getReturnValueF());
      EventBusImpl.INSTANCE.rC(getSlipperinessEvent);
      if (getSlipperinessEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getSlipperinessEvent.lU());
      }
   }
}
