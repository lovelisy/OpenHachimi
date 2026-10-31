package pw.hachimi.client.module.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class AntiSpam extends ClientModule {
   private final Map map = new HashMap();
   Setting unicode = this.registerSetting(new BooleanSetting("Unicode", "Prevents unicode characters from being rendered in chat", false));

   public AntiSpam() {
      super("AntiSpam", "Prevents players from spamming the game chat", ModuleCategory.MISC);
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof ChatMessageS2CPacket) {
            ChatMessageS2CPacket chatMessageS2CPacket = (ChatMessageS2CPacket)packet2;
            if ((Boolean)this.unicode.atx()) {
               String str = ((ChatMessageS2CPacket)packet2).body().content();
               if (Pattern.compile("[^\\x00-\\x7F\\u0400-\\u04FF]+", 2).matcher(str).find()) {
                  channelRead0Event.aim();
                  return;
               }
            }

            UUID uuid = chatMessageS2CPacket.sender();
            String str2 = chatMessageS2CPacket.body().content();
            String str3 = (String)this.map.get(uuid);
            if (str2.equalsIgnoreCase(str3)) {
               channelRead0Event.aim();
            } else if (str3 != null) {
               this.map.replace(uuid, str2);
            } else {
               this.map.put(uuid, str2);
            }
         }
      }
   }
}
