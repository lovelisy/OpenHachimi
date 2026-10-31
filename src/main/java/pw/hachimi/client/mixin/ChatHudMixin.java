package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.hud.ChatHud.ChatState;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.api.UnidentifiedInterface3;
import pw.hachimi.client.api.UnidentifiedInterface6;
import pw.hachimi.client.event.ClearEvent;
import pw.hachimi.client.event.network.AddMessageEvent;
import pw.hachimi.client.event.network.AddVisibleMessageEvent;
import pw.hachimi.client.event.render.DrawTextWithShadowHookEvent;
import pw.hachimi.client.event.render.DrawTextWithShadowHookEvent2;
import pw.hachimi.client.event.render.RenderEvent13;
import pw.hachimi.client.handler.TextHandler;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.utils.chat.StyleFormattingUtils;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin implements TextHandler {
   @Shadow
   @Final
   private List messages;
   @Shadow
   @Final
   private List visibleMessages;
   @Unique
   private Visible j = null;
   @Unique
   private int n;

   @Shadow
   public abstract int getWidth();

   @Shadow
   public abstract void addMessage(Text var1, @Nullable MessageSignatureData var2, @Nullable MessageIndicator var3);

   @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;addedTime()I"))
   private void hookTimeAdded(CallbackInfo callbackInfo, @Local(ordinal = 13) int n2) {
      try {
         this.j = (Visible)this.visibleMessages.get(n2);
      } catch (Exception exception) {
      }
   }

   @WrapOperation(
      method = "render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I")
   )
   private int drawTextWithShadowHook(DrawContext context, TextRenderer textRenderer, OrderedText orderedText, int n2, int n3, int n4, Operation operation) {
      DrawTextWithShadowHookEvent2 drawTextWithShadowHookEvent2 = new DrawTextWithShadowHookEvent2(this.j);
      EventBusImpl.INSTANCE.rC(drawTextWithShadowHookEvent2);
      DrawTextWithShadowHookEvent drawTextWithShadowHookEvent = new DrawTextWithShadowHookEvent();
      EventBusImpl.INSTANCE.rC(drawTextWithShadowHookEvent);
      if (drawTextWithShadowHookEvent2.aik()) {
         if (drawTextWithShadowHookEvent2.Ho()) {
            if (drawTextWithShadowHookEvent.aik()) {
               RenderUtils.acE(context, StyleFormattingUtils.Es(orderedText), (int)drawTextWithShadowHookEvent2.Hk(), n3, n4);
               return RenderUtils.acF(StyleFormattingUtils.Es(orderedText));
            } else {
               return context.drawTextWithShadow(textRenderer, orderedText, (int)drawTextWithShadowHookEvent2.Hk(), n3, n4);
            }
         } else {
            float f = (float)MathHelper.clamp(drawTextWithShadowHookEvent2.Hk(), 0.0, 1.0);
            float f2 = n4 >> 24 & 0xFF;
            f = Math.clamp(f, 0.0F, 1.0F);
            int n5 = Math.max(10, (int)(f2 * f));
            int n6 = f == 1.0F ? n4 : n5 << 24 | n4 & 16777215;
            if (drawTextWithShadowHookEvent.aik()) {
               RenderUtils.acE(context, StyleFormattingUtils.Es(orderedText), 0.0F, n3, n6);
               return RenderUtils.acF(StyleFormattingUtils.Es(orderedText));
            } else {
               return context.drawTextWithShadow(textRenderer, orderedText, 0, n3, n6);
            }
         }
      } else if (drawTextWithShadowHookEvent.aik()) {
         RenderUtils.acE(context, StyleFormattingUtils.Es(orderedText), 0.0F, n3, n4);
         return RenderUtils.acF(StyleFormattingUtils.Es(orderedText));
      } else {
         return (Integer)operation.call(new Object[]{context, textRenderer, orderedText, n2, n3, n4});
      }
   }

   @WrapOperation(method = "render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;indicator()Lnet/minecraft/client/gui/hud/MessageIndicator;"))
   private MessageIndicator hookRender(Visible visible, Operation operation) {
      RenderEvent13 renderEvent13 = new RenderEvent13();
      EventBusImpl.INSTANCE.rC(renderEvent13);
      return renderEvent13.aik() ? null : (MessageIndicator)operation.call(new Object[]{visible});
   }

   @Inject(method = "clear(Z)V", at = @At("HEAD"), cancellable = true)
   private void onClear(boolean flag, CallbackInfo callbackInfo) {
      if (flag) {
         ClearEvent clearEvent = new ClearEvent();
         EventBusImpl.INSTANCE.rC(clearEvent);
         if (clearEvent.aik()) {
            callbackInfo.cancel();
         }
      }
   }

   @Inject(method = "restoreChatState(Lnet/minecraft/client/gui/hud/ChatHud$ChatState;)V", at = @At("HEAD"), cancellable = true)
   private void onRestore(ChatState chatState, CallbackInfo callbackInfo) {
      ClearEvent clearEvent = new ClearEvent();
      EventBusImpl.INSTANCE.rC(clearEvent);
      if (clearEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"))
   private void addMessage$Head(Text text2, MessageSignatureData messageSignatureData, MessageIndicator messageIndicator, CallbackInfo callbackInfo) {
      if (this.n != 0) {
         try {
            this.visibleMessages.removeIf(visible -> ((UnidentifiedInterface6)(Object)visible).Dh() == this.n);
            this.messages.removeIf(chatHudLine -> ((UnidentifiedInterface3)(Object)chatHudLine).et() == this.n);
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }
   }

   @WrapOperation(method = "addVisibleMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2))
   public int addVisibleMessage$size(List list, Operation operation) {
      AddVisibleMessageEvent addVisibleMessageEvent = new AddVisibleMessageEvent();
      EventBusImpl.INSTANCE.rC(addVisibleMessageEvent);
      boolean flag = addVisibleMessageEvent.aik();
      return flag ? -2147483647 : (Integer)operation.call(new Object[]{list});
   }

   @WrapOperation(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At(value = "NEW", target = "Lnet/minecraft/client/gui/hud/ChatHudLine;"))
   private ChatHudLine addMessage$new(int n2, Text text2, MessageSignatureData messageSignatureData, MessageIndicator messageIndicator, Operation operation) {
      AddMessageEvent addMessageEvent = new AddMessageEvent(text2);
      EventBusImpl.INSTANCE.rC(addMessageEvent);
      return (ChatHudLine)operation.call(new Object[]{n2, addMessageEvent.getText(), messageSignatureData, messageIndicator});
   }

   @WrapOperation(method = "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
   public int addMessage$size(List list, Operation operation) {
      AddVisibleMessageEvent addVisibleMessageEvent = new AddVisibleMessageEvent();
      EventBusImpl.INSTANCE.rC(addVisibleMessageEvent);
      boolean flag = addVisibleMessageEvent.aik();
      return flag ? -2147483647 : (Integer)operation.call(new Object[]{list});
   }

   @WrapOperation(method = "addVisibleMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "NEW", target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;"))
   private Visible addVisibleMessage$addChatLine(int n2, OrderedText orderedText, MessageIndicator messageIndicator, boolean flag, Operation operation) {
      Visible visible = (Visible)operation.call(new Object[]{n2, orderedText, messageIndicator, flag});
      ((UnidentifiedInterface6)(Object)visible).Di(this.n);
      return visible;
   }

   @Override
   public void Rn(Text text2, MessageIndicator messageIndicator, int n2) {
      this.n = n2;
      this.addMessage(text2, null, messageIndicator);
      this.n = 0;
   }
}
