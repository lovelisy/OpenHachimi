package pw.hachimi.client.event.player;

import net.minecraft.util.math.Vec2f;
import pw.hachimi.client.event.Event;

public class UpdateRotationEvent extends Event {
   private final float a;
   private float b;
   private float c;

   public UpdateRotationEvent(float f, float f2, float f3) {
      this.b = f;
      this.c = f2;
      this.a = f3;
   }

   public float ET() {
      return this.b;
   }

   public void EU(float f) {
      this.b = f;
   }

   public float EV() {
      return this.c;
   }

   public void EW(float f) {
      this.c = f;
   }

   public void EX(Vec2f vec2f) {
      this.b = vec2f.x;
      this.c = vec2f.y;
   }

   public float EY() {
      return this.a;
   }
}
