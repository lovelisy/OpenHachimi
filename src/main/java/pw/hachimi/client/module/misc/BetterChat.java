package pw.hachimi.client.module.misc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.text.Text;
import pw.hachimi.client.event.ClearEvent;
import pw.hachimi.client.event.InitEvent;
import pw.hachimi.client.event.network.AddMessageEvent;
import pw.hachimi.client.event.network.AddVisibleMessageEvent;
import pw.hachimi.client.event.render.DrawTextWithShadowHookEvent;
import pw.hachimi.client.event.render.DrawTextWithShadowHookEvent2;
import pw.hachimi.client.event.render.RenderEvent13;
import pw.hachimi.client.model.chat.BetterChatNormalGrayMode;
import pw.hachimi.client.model.render.BetterChatSlideFadeMode;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.chat.BetterChatHelper;
import pw.hachimi.client.utils.chat.StyleFormattingUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class BetterChat extends ClientModule {
   public final Map map = new HashMap();
   Setting chatFont = this.registerSetting(new BooleanSetting("ChatFont", "Uses custom font for chat text", false));
   Setting timestamp = this.registerSetting(new EnumSetting("Timestamp", "Shows chat timestamps", BetterChatNormalGrayMode.OFF, BetterChatNormalGrayMode.values()));
   Setting animation = this.registerSetting(new EnumSetting("Animation", "Animates the chat", BetterChatSlideFadeMode.OFF, BetterChatSlideFadeMode.values()));
   Setting animTime = this.registerSetting(new NumberSetting("Anim-Time", "Time for the animation", 0, 200, 1000));
   Setting noSignatureIndicator = this.registerSetting(new BooleanSetting("NoSignatureIndicator", "Removes the message signature indicator", false));
   Setting infinite = this.registerSetting(new BooleanSetting("Infinite", "Makes chat length infinite", false));
   Setting keepChat = this.registerSetting(new BooleanSetting("KeepChat", "Maintains chat history", false));

   public BetterChat() {
      super("BetterChat", "Modifications for the chat", ModuleCategory.MISC);
   }

   @EventListener
   public void ne(AddMessageEvent addMessageEvent) {
      if (this.timestamp.atx() != BetterChatNormalGrayMode.OFF) {
         String str = StyleFormattingUtils.Er(addMessageEvent.getText());
         if (!str.contains("§s[Hachimi] §r")) {
            String str2 = c().format(new Date());
            String str3 = switch ((BetterChatNormalGrayMode)this.timestamp.atx()) {
               case NORMAL -> "<" + str2 + ">§r ";
               case GRAY -> "§8<§7" + str2 + "§8>§r ";
               case COLOR -> "§s<" + str2 + ">§r ";
               case OFF -> "";
            };
            addMessageEvent.aim();
            addMessageEvent.setText(Text.of(str3 + str));
         }
      }
   }

   @EventListener
   public void onInit(InitEvent initEvent) {
      if (this.animation.atx() != BetterChatSlideFadeMode.OFF) {
         BetterChatHelper betterChatHelper = a(initEvent.Db(), ((Integer)this.animTime.atx()).intValue(), Easing.CUBIC_IN_OUT);
         Map temp = this.map;
         Visible visible = initEvent.getVisible();
         temp.put(visible, betterChatHelper);
         betterChatHelper.ga();
      }
   }

   @EventListener
   public void ng(DrawTextWithShadowHookEvent2 drawTextWithShadowHookEvent2) {
      if (this.animation.atx() != BetterChatSlideFadeMode.OFF && drawTextWithShadowHookEvent2.getVisible() != null) {
         Map temp = this.map;
         Visible visible = drawTextWithShadowHookEvent2.getVisible();
         BetterChatHelper betterChatHelper = (BetterChatHelper)temp.getOrDefault(visible, null);
         if (betterChatHelper != null) {
            drawTextWithShadowHookEvent2.aim();
            if (this.animation.atx() == BetterChatSlideFadeMode.SLIDE) {
               drawTextWithShadowHookEvent2.Hl(betterChatHelper.gd());
            } else {
               drawTextWithShadowHookEvent2.Hl(betterChatHelper.gb());
            }

            boolean flag;
            if (this.animation.atx() == BetterChatSlideFadeMode.SLIDE) {
               flag = true;
            } else {
               flag = false;
            }

            drawTextWithShadowHookEvent2.Hm(flag);
         }
      }
   }

   @EventListener
   public void onRenderEvent13(RenderEvent13 renderEvent13) {
      if ((Boolean)this.noSignatureIndicator.atx()) {
         renderEvent13.aim();
      }
   }

   @EventListener
   public void onClear(ClearEvent clearEvent) {
      if ((Boolean)this.keepChat.atx()) {
         clearEvent.aim();
      }
   }

   @EventListener
   public void nj(AddVisibleMessageEvent addVisibleMessageEvent) {
      if ((Boolean)this.infinite.atx()) {
         addVisibleMessageEvent.aim();
      }
   }

   @EventListener
   public void nk(DrawTextWithShadowHookEvent drawTextWithShadowHookEvent) {
      if ((Boolean)this.chatFont.atx()) {
         drawTextWithShadowHookEvent.aim();
      }
   }

   private static MatchException b_1() {
      return new MatchException(null, null);
   }

   private static SimpleDateFormat c() {
      return new SimpleDateFormat("k:mm");
   }

   private static BetterChatHelper a(double d, float f, Easing easing) {
      return new BetterChatHelper(d, 0.0, f, easing);
   }
}
