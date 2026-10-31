package pw.hachimi.client.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.OnKeyEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(Keyboard.class)
public class KeyboardMixin {
   @Shadow
   @Final
   private MinecraftClient client;

   @Inject(method = "onKey(JIIII)V", at = @At("HEAD"), cancellable = true)
   private void hookOnKey(long l, int n, int n2, int n3, int n4, CallbackInfo callbackInfo) {
      if (this.client.getWindow().getHandle() == l) {
         OnKeyEvent onKeyEvent = new OnKeyEvent(n, n3);
         EventBusImpl.INSTANCE.rC(onKeyEvent);
         if (onKeyEvent.aik()) {
            callbackInfo.cancel();
         }
      }
   }
}
