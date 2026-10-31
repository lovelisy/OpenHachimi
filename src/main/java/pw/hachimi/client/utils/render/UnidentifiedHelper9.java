package pw.hachimi.client.utils.render;

import pw.hachimi.client.model.render.Easing;

public class UnidentifiedHelper9 {
   public double a;
   public double b;
   private final UnidentifiedHelper2 unidentifiedHelper2 = new UnidentifiedHelper2();

   public double MH(double d, long l, Easing easing) {
      if (d != this.b) {
         this.a = this.a + (this.b - this.a) * this.unidentifiedHelper2.hW(easing);
         this.b = d;
         this.unidentifiedHelper2.hR();
      }

      this.unidentifiedHelper2.hU(l);
      return this.a + (this.b - this.a) * this.unidentifiedHelper2.hW(easing);
   }
}
