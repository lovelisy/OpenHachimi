package pw.hachimi.client.utils;

import org.jetbrains.annotations.NotNull;
import pw.hachimi.client.utils.movement.UnidentifiedUtils7;

public class UnidentifiedHelper12 {
   public double a_;
   public double b_;
   public double c;
   public static boolean a;
   private static final long b = 0L;

   public UnidentifiedHelper12(double d, double d2, double d3) {
      this.a_ = d;
      this.b_ = d2;
      this.c = d3;
   }

   public final double Qg() {
      return this.a_;
   }

   public final double Qh() {
      return this.b_;
   }

   public final double Qi() {
      return this.c;
   }

   public UnidentifiedHelper12 Qj(double d) {
      this.a_ = d;
      return this;
   }

   public UnidentifiedHelper12 Qk() {
      this.b_ = 0.0;
      return this;
   }

   public UnidentifiedHelper12 Ql(double d) {
      this.c = d;
      return this;
   }

   @NotNull
   public UnidentifiedHelper12 Qm(@NotNull UnidentifiedHelper12 unidentifiedHelper12, long l) {
      long temp = b ^ l ^ 125759440582978L;
      int n = (int)((b ^ l ^ 125759440582978L) >>> 48);
      int n2 = (int)((b ^ l ^ 125759440582978L) << 16 >>> 32);
      int n3 = (int)(temp << 48 >>> 48);
      return this.Qn(unidentifiedHelper12.a_, unidentifiedHelper12.b_, (char)n, n2, unidentifiedHelper12.c, (short)n3);
   }

   @NotNull
   public UnidentifiedHelper12 Qn(double d, double d2, char c2, int n, double d3, short s) {
      this.a_ += d;
      this.b_ += d2;
      this.c += d3;
      return this;
   }

   @NotNull
   public UnidentifiedHelper12 Qo(@NotNull UnidentifiedHelper12 unidentifiedHelper12, long l) {
      long l2 = b ^ l ^ 23123024306646L;
      return this.Qp(unidentifiedHelper12.a_, unidentifiedHelper12.b_, unidentifiedHelper12.c, l2);
   }

   @NotNull
   public UnidentifiedHelper12 Qp(double d, double d2, double d3, long l) {
      this.a_ -= d;
      this.b_ -= d2;
      this.c -= d3;
      return this;
   }

   @NotNull
   public UnidentifiedHelper12 Qq(long l) {
      return a(this.a_, this.b_, this.c);
   }

   @NotNull
   public UnidentifiedHelper12 Qr(long l, @NotNull UnidentifiedHelper12 unidentifiedHelper12) {
      long l2 = b ^ l ^ 101500461862623L;
      return this.Qs(l2, unidentifiedHelper12.a_, unidentifiedHelper12.b_, unidentifiedHelper12.c);
   }

   @NotNull
   public UnidentifiedHelper12 Qs(long l, double d, double d2, double d3) {
      this.a_ *= d;
      this.b_ *= d2;
      this.c *= d3;
      return this;
   }

   public double Qt() {
      return Math.sqrt(UnidentifiedUtils7.akH(this.a_) + UnidentifiedUtils7.akH(this.b_) + UnidentifiedUtils7.akH(this.c));
   }

   @NotNull
   @Override
   public Object clone() throws CloneNotSupportedException {
      long l = b ^ 57613161550149L ^ 117607960721825L;
      return this.Qq(l);
   }

   private static UnidentifiedHelper12 a(double d, double d2, double d3) {
      return new UnidentifiedHelper12(d, d2, d3);
   }
}
