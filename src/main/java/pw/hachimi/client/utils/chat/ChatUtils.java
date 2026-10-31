package pw.hachimi.client.utils.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pw.hachimi.client.Mc;
import pw.hachimi.client.handler.TextHandler;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.Notification;

public class ChatUtils {
   public static final String a = "§s[Hachimi] §r";

   public static void UB(String str) {
      UC(str, 0);
   }

   public static void UC(String str, int n) {
      MinecraftClient temp = Mc.client;
      final int n2 = n;
      final String str2 = str;
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            ChatUtils.UO(str2, n2);
         }
      };
      temp.executeSync(runnable);
   }

   public static void UD(String str, Object... value) {
      UB(String.format(str, value));
   }

   public static void UE(String str) {
      UF(str, 0);
   }

   public static void UF(String str, int n) {
      MinecraftClient temp = Mc.client;
      final int n2 = n;
      final String str2 = str;
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            ChatUtils.UN(str2, n2);
         }
      };
      temp.executeSync(runnable);
   }

   public static void UG(String str, Object... value) {
      UE(String.format(str, value));
   }

   public static void UH(String str) {
      if (Mc.client.player != null) {
         Mc.client.player.networkHandler.sendChatMessage(str);
      }
   }

   public static void UI(String str) {
      if (Mc.client.player != null) {
         Mc.client.player.networkHandler.sendChatCommand(str);
      }
   }

   public static void UJ(PlayerEntity player2, String str) {
      if (Mc.client.player != null) {
         String str2 = "/msg " + player2.getName().getString() + " ";
         String str3 = str2 + str;
         Mc.client.player.networkHandler.sendChatMessage(str3);
      }
   }

   public static void UK(PlayerEntity player2, String str, Object... value) {
      UJ(player2, String.format(str, value));
   }

   public static void UL(String str) {
      UB(Formatting.RED + str);
   }

   public static void UM(String str, Object... value) {
      UD(Formatting.RED + str, value);
   }

   static void UN(String str, int n) {
      Notification.INSTANCE.ZS(str, n);
      if (Notification.INSTANCE.ZT()) {
         MessageIndicator messageIndicator = a(ColorHelper3.auO().auX(), Text.empty());
         ((TextHandler)Mc.client.inGameHud.getChatHud()).Rn(Text.of(str), messageIndicator, n);
      }
   }

   static void UO(String str, int n) {
      Notification.INSTANCE.ZS(str, n);
      if (Notification.INSTANCE.ZT()) {
         MessageIndicator messageIndicator = a(ColorHelper3.auO().auX(), Text.empty());
         ((TextHandler)Mc.client.inGameHud.getChatHud()).Rn(Text.of("§s[Hachimi] §r" + str), messageIndicator, n);
      }
   }

   private static MessageIndicator a(int n, Text text2) {
      return new MessageIndicator(n, null, text2, null);
   }
}
