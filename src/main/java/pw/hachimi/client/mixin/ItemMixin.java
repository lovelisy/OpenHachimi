package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.FinishUsingEvent;
import pw.hachimi.client.event.player.UseEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(Item.class)
public class ItemMixin {
   @Inject(method = "finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
   private void hookFinishUsing(ItemStack stack, World world2, LivingEntity living, CallbackInfoReturnable callbackInfoReturnable) {
      FinishUsingEvent finishUsingEvent = new FinishUsingEvent(stack);
      EventBusImpl.INSTANCE.rC(finishUsingEvent);
   }

   @WrapOperation(
      method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;", ordinal = 0)
   )
   public ItemStack hookUse(PlayerEntity player2, Hand hand, Operation operation) {
      if (hand == Hand.MAIN_HAND) {
         UseEvent useEvent = new UseEvent();
         EventBusImpl.INSTANCE.rC(useEvent);
         if (Mc.client.player != null && useEvent.aik()) {
            return useEvent.getStack();
         }
      }

      return (ItemStack)operation.call(new Object[]{player2, hand});
   }
}
