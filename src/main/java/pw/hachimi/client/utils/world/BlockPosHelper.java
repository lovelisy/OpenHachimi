package pw.hachimi.client.utils.world;

import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.utils.UnidentifiedHelper12;

public class BlockPosHelper {
   public double a_;
   public double b;
   public double c;
   public double d;
   public double e;
   public double f;
   public static boolean a;

   public BlockPosHelper(double d2, double d3, double d4, double d5, double d6, int n, double d7) {
      if (n > 0) {
         this.a_ = Math.min(d2, d5);
         this.b = Math.min(d3, d6);
         if (n > 0 && n >= 0) {
            this.c = Math.min(d4, d7);
            if (n >= 0) {
               this.d = Math.max(d2, d5);
               this.e = Math.max(d3, d6);
               if (n >= 0) {
                  this.f = Math.max(d4, d7);
                  if (n >= 0) {
                     return;
                  }
               }
            }
         }
      }
   }

   public BlockPosHelper(BlockPos pos, int n, char c2, char c3) {
      this((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), 585733871, (double)(pos.getZ() + 1));
   }

   public BlockPosHelper(UnidentifiedHelper12 unidentifiedHelper12, UnidentifiedHelper12 unidentifiedHelper122) {
      this(unidentifiedHelper12.a_, unidentifiedHelper12.b_, unidentifiedHelper12.c, unidentifiedHelper122.a_, unidentifiedHelper122.b_, 585733871, unidentifiedHelper122.c);
   }

   public BlockPosHelper pU() {
      double d2 = Math.min(this.a_, this.d);
      double d3 = Math.min(this.b, this.e);
      double d4 = Math.min(this.c, this.f);
      double d5 = Math.max(this.a_, this.d);
      double d6 = Math.max(this.b, this.e);
      double d7 = Math.max(this.c, this.f);
      this.a_ = d2;
      this.b = d3;
      this.c = d4;
      this.d = d5;
      this.e = d6;
      this.f = d7;
      return this;
   }

   public BlockPosHelper pV(double d2, double d3, double d4) {
      this.a_ += d2;
      this.b += d3;
      this.c += d4;
      return this;
   }

   public BlockPosHelper pW(double d2, double d3, double d4) {
      this.d += d2;
      this.e += d3;
      this.f += d4;
      return this;
   }

   public UnidentifiedHelper12 pX() {
      return a(this.d, this.e, this.f);
   }

   public UnidentifiedHelper12 pY() {
      return a(this.a_, this.b, this.c);
   }

   private static UnidentifiedHelper12 a(double d2, double d3, double d4) {
      return new UnidentifiedHelper12(d2, d3, d4);
   }
}
