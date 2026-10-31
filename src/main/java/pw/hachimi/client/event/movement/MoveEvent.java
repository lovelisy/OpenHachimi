package pw.hachimi.client.event.movement;

import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class MoveEvent extends Event {
   private final MovementType movementType;
   private double b;
   private double c;
   private double d;

   public MoveEvent(MovementType movementType2, Vec3d vec) {
      this.movementType = movementType2;
      this.b = vec.getX();
      this.c = vec.getY();
      this.d = vec.getZ();
   }

   public MovementType getMovementType() {
      return this.movementType;
   }

   public Vec3d yk() {
      return new Vec3d(this.b, this.c, this.d);
   }

   public double yl() {
      return this.b;
   }

   public void ym(double d2) {
      this.b = d2;
   }

   public double yn() {
      return this.c;
   }

   public void yo(double d2) {
      this.c = d2;
   }

   public double yp() {
      return this.d;
   }

   public void yq(double d2) {
      this.d = d2;
   }
}
