package pw.hachimi.client.event.player;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public final class GetHandSwingDurationEvent extends Event {
   int a;
   boolean b;

   public int Mv() {
      return this.a;
   }

   public void Mw(int n) {
      this.a = n;
   }

   public boolean Mx() {
      return this.b;
   }

   public void My(boolean flag) {
      this.b = flag;
   }
}
