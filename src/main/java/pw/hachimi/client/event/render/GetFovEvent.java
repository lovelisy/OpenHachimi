package pw.hachimi.client.event.render;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class GetFovEvent extends Event {
   private double a;
   public static boolean cy;

   public double Ab() {
      return this.a;
   }

   public void Ac(double d) {
      this.a = d;
   }
}
