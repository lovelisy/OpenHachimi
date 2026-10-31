package pw.hachimi.client.event.network;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.UnknownEvent10;

@UnidentifiedInterface2
public class SendChatMessageEvent extends UnknownEvent10 {
   public SendChatMessageEvent(String str) {
      super(str);
   }

   public static boolean dc;
}
