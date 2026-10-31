package pw.hachimi.client.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.player.UseEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ItemUsageContext.class)
public final class ItemUsageContextMixin {
   @Inject(method = "getStack()Lnet/minecraft/item/ItemStack;", at = @At("RETURN"), cancellable = true)
   public void hookGetStack(CallbackInfoReturnable callbackInfoReturnable) {
      UseEvent useEvent = new UseEvent();
      EventBusImpl.INSTANCE.rC(useEvent);
      if (Mc.client.player != null && ((ItemStack)callbackInfoReturnable.getReturnValue()).equals(Mc.client.player.getMainHandStack()) && useEvent.aik()) {
         callbackInfoReturnable.setReturnValue(useEvent.getStack());
      }
   }
}
