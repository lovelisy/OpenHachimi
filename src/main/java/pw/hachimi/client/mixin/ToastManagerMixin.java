package pw.hachimi.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.DrawEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
   @Inject(method = "draw(Lnet/minecraft/client/gui/DrawContext;)V", at = @At("HEAD"), cancellable = true)
   private void hookDraw(DrawContext context, CallbackInfo callbackInfo) {
      DrawEvent drawEvent = new DrawEvent();
      EventBusImpl.INSTANCE.rC(drawEvent);
      if (drawEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
