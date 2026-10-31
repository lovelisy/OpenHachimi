package pw.hachimi.client.event.render;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.UnknownEvent2;

@UnidentifiedInterface2
public class RenderEvent8 extends UnknownEvent2 {
   private final String text;
   public static boolean fj;

   public RenderEvent8(String str) {
      this.text = str;
   }

   public String getText() {
      return this.text;
   }
}
