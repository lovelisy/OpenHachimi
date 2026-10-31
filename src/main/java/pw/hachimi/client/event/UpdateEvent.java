package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class UpdateEvent extends Event {
   private final double a;
   private final double b;

   public UpdateEvent(double d, double d2) {
      this.a = d;
      this.b = d2;
   }

   public double lR() {
      return this.a;
   }

   public double lS() {
      return this.b;
   }
}
