package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class UnknownEvent5 extends Event {
   private double a;

   public double MD() {
      return this.a;
   }

   public void ME(double d) {
      this.a = d;
   }
}
