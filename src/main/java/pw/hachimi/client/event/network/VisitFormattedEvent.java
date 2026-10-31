package pw.hachimi.client.event.network;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class VisitFormattedEvent extends Event {
   private String text;
   public static boolean dw;

   public VisitFormattedEvent(String str) {
      this.text = str;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String str) {
      this.text = str;
   }
}
