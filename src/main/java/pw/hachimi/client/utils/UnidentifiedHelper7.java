package pw.hachimi.client.utils;

import pw.hachimi.client.model.render.Easing;

public class UnidentifiedHelper7 {
   private Easing easing;
   private float b;
   private long c;
   private boolean d;
   public static boolean a;

   public UnidentifiedHelper7() {
      this(false, 150.0F);
   }

   public UnidentifiedHelper7(boolean flag, float f) {
      this(flag, '\u0000', f, 496665058, '楚', Easing.LINEAR);
   }

   public UnidentifiedHelper7(boolean flag, char c2, float f, int n, char c3, Easing easing2) {
      this.d = flag;
      this.b = f;
      this.easing = easing2;
      this.c = 0L;
   }

   public boolean qU() {
      return this.d;
   }

   public void qV(boolean flag) {
      double d2;
      if (flag) {
         d2 = (double)System.currentTimeMillis() - this.qW() * (double)this.b;
      } else {
         d2 = (double)System.currentTimeMillis() - (1.0 - this.qW()) * (double)this.b;
      }

      this.c = (long)d2;
      this.d = flag;
   }

   public double qW() {
      return this.easing.Yy(this.qX());
   }

   public double qX() {
      double d2;
      if (this.d) {
         d2 = this.qY((float)(System.currentTimeMillis() - this.c) / this.b);
      } else {
         d2 = this.qY(1.0F - (float)(System.currentTimeMillis() - this.c) / this.b);
      }

      return d2;
   }

   private double qY(double d2) {
      double d3;
      if (d2 < 0.0) {
         d3 = 0.0;
      } else {
         d3 = Math.min(d2, 1.0);
      }

      return d3;
   }

   public double qZ() {
      return this.b;
   }

   public void ra(float f) {
      this.b = f;
   }

   public boolean rb() {
      return this.qU() ? this.qW() == 1.0 : this.qW() == 0.0;
   }

   public void rc() {
      this.c = System.currentTimeMillis();
   }

   public void setEasing(Easing easing2) {
      this.easing = easing2;
   }
}
