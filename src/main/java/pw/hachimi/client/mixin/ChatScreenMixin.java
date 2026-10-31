package pw.hachimi.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.FillEvent;
import pw.hachimi.client.event.network.SendMessageEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends ScreenMixin {
   @Shadow
   protected TextFieldWidget chatField;

   @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
   private void hookSendMessage(String str, boolean flag, CallbackInfo callbackInfo) {
      SendMessageEvent sendMessageEvent = new SendMessageEvent(str);
      EventBusImpl.INSTANCE.rC(sendMessageEvent);
      if (sendMessageEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Redirect(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
   private void hookFill(DrawContext context, int n, int n2, int n3, int n4, int n5) {
      FillEvent fillEvent = new FillEvent();
      EventBusImpl.INSTANCE.rC(fillEvent);
      float f = fillEvent.aik() ? 12.0F * fillEvent.x() : 12.0F;
      context.fill(
         2,
         (int)(this.height - 2.0F),
         this.width - 2,
         (int)(this.height - 2.0F - f),
         this.client.options.getTextBackgroundColor(Integer.MIN_VALUE)
      );
   }

   @Override
   protected Element addDrawableChild(Element element) {
      return null;
   }
}
