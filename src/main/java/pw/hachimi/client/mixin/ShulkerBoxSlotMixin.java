package pw.hachimi.client.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.ShulkerBoxSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.CanInsertEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin {
   @Inject(method = "canInsert(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
   private void hookCanInsert(ItemStack stack, CallbackInfoReturnable callbackInfoReturnable) {
      CanInsertEvent canInsertEvent = new CanInsertEvent();
      EventBusImpl.INSTANCE.rC(canInsertEvent);
      if (canInsertEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(true);
      }
   }
}
