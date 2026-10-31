package pw.hachimi.client.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.DisconnectEvent2;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {
   @Inject(method = "disconnect()V", at = @At("HEAD"), cancellable = true)
   private void hookDisconnect(CallbackInfo callbackInfo) {
      DisconnectEvent2 disconnectEvent2 = new DisconnectEvent2();
      EventBusImpl.INSTANCE.rC(disconnectEvent2);
      if (disconnectEvent2.aik()) {
         callbackInfo.cancel();
      }
   }
}
