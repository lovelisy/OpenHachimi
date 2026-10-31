package pw.hachimi.client.event.movement;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class GetMountJumpStrengthEvent extends Event {
   private float a;

   public float Dc() {
      return this.a;
   }

   public void Dd(float f) {
      this.a = f;
   }
}
