package pw.hachimi.client.utils.chat;

import pw.hachimi.client.model.render.Easing;

public class BetterChatHelper {
   private final Easing easing;
   private double b;
   private double c;
   private float d;
   private long e;
   private boolean f;

   public BetterChatHelper(double d2, double d3, float f2, long l) {
      this(false, d2, d3, l, f2);
   }

   public BetterChatHelper(boolean flag, double d2, double d3, long l, float f2) {
      this(d2, d3, f2, Easing.LINEAR);
   }

   public BetterChatHelper(double d2, double d3, float f2, Easing easing2) {
      this.e = 0L;
      this.b = d2;
      this.c = d3;
      this.d = f2;
      this.f = false;
      this.easing = easing2;
   }

   public double fV() {
      return this.b;
   }

   public void fW(double d2) {
      this.b = d2;
   }

   public double fX() {
      return this.c;
   }

   public void fY(double d2) {
      this.c = d2;
   }

   public boolean fZ() {
      return this.f;
   }

   public void ga() {
      double d2 = System.currentTimeMillis() - this.gb() * this.d;
      this.e = (long)d2;
      this.f = true;
   }

   public double gb() {
      return this.easing.Yy(this.gc());
   }

   public double gc() {
      double d2;
      if (this.f) {
         d2 = this.ge((float)(System.currentTimeMillis() - this.e) / this.d);
      } else {
         d2 = this.ge(1.0F - (float)(System.currentTimeMillis() - this.e) / this.d);
      }

      return d2;
   }

   public double gd() {
      return this.b + (this.c - this.b) * this.gb();
   }

   private double ge(double d2) {
      double d3;
      if (d2 < 0.0) {
         d3 = 0.0;
      } else {
         d3 = Math.min(d2, 1.0);
      }

      return d3;
   }

   public double gf() {
      return this.d;
   }

   public void gg(float f2) {
      this.d = f2;
   }

   public boolean gh() {
      boolean flag;
      if ((this.fZ() || this.gb() != 0.0) && (!this.fZ() || this.gb() != 1.0)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }
}
