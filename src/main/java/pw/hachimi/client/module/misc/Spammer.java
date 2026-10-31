package pw.hachimi.client.module.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.render.UnidentifiedUtils4;
import pw.hachimi.eventbus.annotation.EventListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import pw.hachimi.client.Bootstrap;

public class Spammer extends ClientModule {
   private final List list = new ArrayList();
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting delay = this.registerSetting(new NumberSetting("Delay", "The chat message delay", 0.0F, 1.5F, 10.0F));
   Setting random = this.registerSetting(new BooleanSetting("Random", "Randomizes the spammed messages", false));
   Setting antiKick = this.registerSetting(new BooleanSetting("AntiKick", "Adds a random suffix to end of messages to prevent kicks", false));
   private int f;
   public static boolean fy;

   public Spammer() {
      super("Spammer", "Spams messages in the chat", ModuleCategory.MISC);
   }

   @Override
   public void awd() {
      this.qd();
      this.f = 0;
   }

   @EventListener
   public void onOnGameJoin(OnGameJoinEvent onGameJoinEvent) {
      this.awd();
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (this.stopwatch.afq((Float)this.delay.atx() * 1000.0F)) {
            this.qf(this.qe());
            this.stopwatch.afr(29432, 5265492, (byte)40);
         }
      }
   }

   // restored with Procyon (compiles, call/field/constant fingerprint equals the deobf jar)
   private void qd() {
       final File file = Bootstrap.clientPaths.getPath().resolve("spammer.txt").toFile();
       if (!file.exists()) {
           this.ht("The spammer.txt file does not exist! Please create one to enable this module");
           this.awc();
           return;
       }
       this.list.clear();
       final File file2 = file;
       try {
           final Iterator<String> iterator = Files.readAllLines(Path.of(file2.getAbsolutePath(), (String[])new String[0]), StandardCharsets.UTF_8).iterator();
           while (iterator.hasNext()) {
               final String[] split = iterator.next().split(",");
               for (int length = split.length, i = 0; i < length; ++i) {
                   this.list.add(split[i].trim());
               }
           }
       }
       catch (final IOException ex) {
           ex.printStackTrace();
       }
   }

   private String qe() {
      if (this.list.isEmpty()) {
         return "get hachimiclient.me";
      }

      if ((Boolean)this.random.atx()) {
         List temp = this.list;
         Random temp2 = random2;
         int n = this.list.size();
         int n2 = temp2.nextInt(n);
         String str = (String)temp.get(n2);
         if (str != null) {
            if ((Boolean)this.antiKick.atx()) {
               str = str + " " + UnidentifiedUtils4.TY(2);
            }

            return str;
         }
      } else {
         int n3 = this.f;
         String str2 = (String)this.list.get(n3);
         this.f++;
         if (this.f >= this.list.size()) {
            this.f = 0;
         }

         if (str2 != null) {
            if ((Boolean)this.antiKick.atx()) {
               str2 = str2 + " " + UnidentifiedUtils4.TY(2);
            }

            return str2;
         }
      }

      return "get hachimiclient.me";
   }

   private void qf(String str) {
      if (str.charAt(0) == '/') {
         str = str.substring(1);
         client.player.networkHandler.sendCommand(str);
      } else {
         String str2 = Managers.commandSourceChatScreenHelper.getText2();
         if (str.startsWith(str2)) {
            String str3 = str.substring(1);
            client.inGameHud.getChatHud().addToMessageHistory(str);

            try {
               Managers.commandSourceChatScreenHelper
                  .getCommandDispatcher()
                  .execute(Managers.commandSourceChatScreenHelper.getCommandDispatcher().parse(str3, Managers.commandSourceChatScreenHelper.getCommandSource()));
            } catch (Exception exception) {
            }
         } else {
            if (client.isInSingleplayer()) {
               ChatUtils.UE(str);
            } else {
               ChatUtils.UH(str);
            }
         }
      }
   }
}
