package pw.hachimi.client.event.movement;

import net.minecraft.entity.LivingEntity;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class GetStepHeightEvent extends Event {
   private final LivingEntity living;
   private float b;
   public static boolean ac;

   public GetStepHeightEvent(LivingEntity living2, float f) {
      this.living = living2;
      this.b = f;
   }

   public LivingEntity getLiving() {
      return this.living;
   }

   public float Df() {
      return this.b;
   }

   public void Dg(float f) {
      this.b = f;
   }
}
