package pw.hachimi.client.event.world;

import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.Event;

public class UpdatePositionEvent extends Event {
   private final float a;
   private double b;
   private double c;
   private double d;

   public UpdatePositionEvent(double d2, double d3, double d4, float f) {
      this.b = d2;
      this.c = d3;
      this.d = d4;
      this.a = f;
   }

   public double Dx() {
      return this.b;
   }

   public void Dy(double d2) {
      this.b = d2;
   }

   public double Dz() {
      return this.c;
   }

   public void DA(double d2) {
      this.c = d2;
   }

   public double DB() {
      return this.d;
   }

   public void DC(double d2) {
      this.d = d2;
   }

   public float DD() {
      return this.a;
   }

   public Vec3d DE() {
      return new Vec3d(this.b, this.c, this.d);
   }

   public void DF(Vec3d vec) {
      this.b = vec.getX();
      this.c = vec.getY();
      this.d = vec.getZ();
   }
}
