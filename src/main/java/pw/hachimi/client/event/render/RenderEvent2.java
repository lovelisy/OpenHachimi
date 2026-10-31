package pw.hachimi.client.event.render;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class RenderEvent2 extends Event {
   private int a;

   public int gI() {
      return this.a;
   }

   public void gJ(int n) {
      this.a = n;
   }
}
