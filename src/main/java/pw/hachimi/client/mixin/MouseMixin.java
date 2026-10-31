package pw.hachimi.client.mixin;

import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.MouseButtonEvent;
import pw.hachimi.client.event.UpdateEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(Mouse.class)
public class MouseMixin {
   @Inject(method = "onMouseButton(JIII)V", at = @At("HEAD"), cancellable = true)
   private void onMouseButton(long l, int n, int n2, int n3, CallbackInfo callbackInfo) {
      MouseButtonEvent mouseButtonEvent = new MouseButtonEvent(n, n2);
      EventBusImpl.INSTANCE.rC(mouseButtonEvent);
      if (mouseButtonEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Redirect(method = "updateMouse(D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
   public void onUpdate(ClientPlayerEntity player, double d, double d2) {
      UpdateEvent updateEvent = new UpdateEvent(d, d2);
      EventBusImpl.INSTANCE.rC(updateEvent);
      if (!updateEvent.aik()) {
         player.changeLookDirection(d, d2);
      }
   }
}
