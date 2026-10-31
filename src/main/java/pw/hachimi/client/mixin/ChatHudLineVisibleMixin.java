package pw.hachimi.client.mixin;

import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.Mc;
import pw.hachimi.client.api.UnidentifiedInterface6;
import pw.hachimi.client.event.InitEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(Visible.class)
public class ChatHudLineVisibleMixin implements UnidentifiedInterface6 {
   @Unique
   private int t;

   @Inject(method = "<init>", at = @At("RETURN"))
   private void hookInit(int n, OrderedText orderedText, MessageIndicator messageIndicator, boolean flag, CallbackInfo callbackInfo) {
      InitEvent initEvent = new InitEvent((Visible)(Object)this, -Mc.client.textRenderer.getWidth(orderedText));
      EventBusImpl.INSTANCE.rC(initEvent);
   }

   @Override
   public int Dh() {
      return this.t;
   }

   @Override
   public void Di(int n) {
      this.t = n;
   }
}
