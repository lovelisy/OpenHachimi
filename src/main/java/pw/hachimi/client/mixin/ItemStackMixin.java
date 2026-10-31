package pw.hachimi.client.mixin;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.player.InitItemEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
   @Shadow
   public abstract int getDamage();

   @Shadow
   public abstract void setDamage(int var1);

   @Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("RETURN"))
   private void hookInitItem(ItemConvertible itemConvertible, int n, CallbackInfo callbackInfo) {
      InitItemEvent initItemEvent = new InitItemEvent(this.getDamage());
      EventBusImpl.INSTANCE.rC(initItemEvent);
      if (initItemEvent.aik()) {
         this.setDamage(initItemEvent.abM());
      }
   }
}
