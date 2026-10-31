package pw.hachimi.client.event.player;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class ApplyEatOrDrinkTransformationEvent extends Event {
   private float a;

   public float Fx() {
      return this.a;
   }

   public void Fy(float f) {
      this.a = f;
   }
}
