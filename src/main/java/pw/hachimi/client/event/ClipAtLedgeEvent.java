package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class ClipAtLedgeEvent extends Event {
   private boolean a;

   public boolean pZ() {
      return this.a;
   }

   public void qa() {
      this.a = true;
   }
}
