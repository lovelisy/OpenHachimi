package pw.hachimi.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEvent4;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
   @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;)V", at = @At("HEAD"), cancellable = true)
   private void hookRender(DrawContext context, CallbackInfo callbackInfo) {
      RenderEvent4 renderEvent4 = new RenderEvent4(context);
      EventBusImpl.INSTANCE.rC(renderEvent4);
      if (renderEvent4.aik()) {
         callbackInfo.cancel();
      }
   }
}
