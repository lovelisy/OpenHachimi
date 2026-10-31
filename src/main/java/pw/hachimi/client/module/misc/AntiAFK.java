package pw.hachimi.client.module.misc;

import java.util.function.Supplier;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class AntiAFK extends Module5 {
   private final Stopwatch stopwatch = new StopwatchImpl();
   private final Stopwatch stopwatch2 = new StopwatchImpl();
   Setting aFKTime = this.registerSetting(new NumberSetting("AFKTime", "The time before attempting actions", 5.0F, 20.0F, 120.0F));
   Setting message = this.registerSetting(new BooleanSetting("Message", "Messages in chat to prevent AFK kick", true));
   Setting tabComplete = this.registerSetting(new BooleanSetting("TabComplete", "Uses tab complete in chat to prevent AFK kick", true));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotates the player to prevent AFK kick", true));
   Setting jump = this.registerSetting(new BooleanSetting("Jump", "Jumps to prevent AFK kick", true));
   Setting autoReply = this.registerSetting(new BooleanSetting("AutoReply", "Replies to players messaging you in chat", false));
   Setting reply = this.registerSetting(createStringSetting("Reply", "The reply message for AutoReply", this::isReplyVisible));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "The delay between actions", 5.0F, 100.0F, 1000.0F));
   public static boolean am;

   public AntiAFK() {
      super("AntiAFK", "Prevents the player from being kicked for AFK", ModuleCategory.MISC);
   }

   @Override
   public void awd() {
      this.stopwatch.afr(29432, 5265492, (byte)40);
   }

   @EventListener
   public void onOnGameJoin(OnGameJoinEvent onGameJoinEvent) {
      this.stopwatch.afr(29432, 5265492, (byte)40);
   }

   @EventListener
   public void ahU(TickEntitiesPreEvent tickEntitiesPreEvent) {
      KeyBinding[] keyBindings = client.options.allKeys;
      int n = keyBindings.length;

      for (int n2 = 0; n2 < n; n2++) {
         if (keyBindings[n2].isPressed()) {
            this.stopwatch.afr(29432, 5265492, (byte)40);
            break;
         }
      }

      if (this.stopwatch.afq((Float)this.aFKTime.atx() * 1000.0F) && this.stopwatch2.afq((Number)this.delay.atx())) {
         if ((Boolean)this.jump.atx() && client.player.isOnGround()) {
            client.player.jump();
         }

         if ((Boolean)this.rotate.atx()) {
            float temp = client.player.getYaw();
            float temp2 = random2.nextFloat(90.0F);
            boolean flag = random2.nextBoolean();
            float f = temp2;
            float f2 = temp;
            float f3;
            if (flag) {
               f = 1.0F;
               f2 = f;
               f3 = f2;
            } else {
               f = -1.0F;
               f2 = f;
               f3 = f2;
            }

            temp = f3 + f2 * f;
            temp2 = client.player.getPitch();
            float temp3 = random2.nextFloat(90.0F);
            flag = random2.nextBoolean();
            f = temp3;
            f2 = temp2;
            f3 = temp;
            float f4;
            if (flag) {
               f = 1.0F;
               f2 = f;
               f3 = f2;
               f4 = f3;
            } else {
               f = -1.0F;
               f2 = f;
               f3 = f2;
               f4 = f3;
            }

            f = f3 + f2 * f;
            this.ajN(f4, f);
         }

         this.stopwatch2.afr(29432, 5265492, (byte)40);
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      Packet packet2 = channelRead0Event.getPacket();
      if (packet2 instanceof ChatMessageS2CPacket
         && (Boolean)this.autoReply.atx()
         && ((ChatMessageS2CPacket)packet2).body().content().split(" ")[1].startsWith("whispers:")) {
         ChatUtils.UI("r " + (String)this.reply.atx());
      }
   }

   private Boolean isReplyVisible() {
      return (Boolean)this.autoReply.atx();
   }

   private static StringSetting createStringSetting(String str, String str2, Supplier supplier) {
      return new StringSetting(str, str2, "I am currently AFK.", supplier);
   }
}
