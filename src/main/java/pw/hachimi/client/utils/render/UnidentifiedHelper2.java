package pw.hachimi.client.utils.render;

import pw.hachimi.client.model.render.Easing;

public class UnidentifiedHelper2 {
   public long a;
   private long b;

   public UnidentifiedHelper2() {
      this.a = 0L;
      this.hR();
   }

   public void hR() {
      this.b = System.currentTimeMillis();
   }

   public boolean hS() {
      return this.hT() >= this.a;
   }

   protected long hT() {
      return System.currentTimeMillis() - this.b;
   }

   public void hU(long l) {
      this.a = l;
   }

   public double hV() {
      if (this.hS()) {
         return 1.0;
      }

      return (double)this.hT() / (double)this.a;
   }

   public double hW(Easing easing) {
      return easing.Yy(this.hV());
   }
}
