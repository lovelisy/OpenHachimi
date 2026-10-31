package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class RecastOnLandEvent extends Event {
   private boolean a;

   public RecastOnLandEvent(boolean flag) {
      this.a = flag;
   }

   public boolean yh() {
      return this.a;
   }

   public void yi() {
      this.a = true;
   }
}
