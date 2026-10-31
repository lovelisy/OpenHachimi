package pw.hachimi.client.event.network;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.UnknownEvent10;

@UnidentifiedInterface2
public class SendMessageEvent extends UnknownEvent10 {
   public SendMessageEvent(String str) {
      super(str);
   }
}
