package pw.hachimi.client.event.movement;

import net.minecraft.entity.LivingEntity;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class TravelPreEvent extends Event {
   private final LivingEntity living;
   private final boolean b;
   public static boolean fv;

   public TravelPreEvent(LivingEntity living2, boolean flag) {
      this.living = living2;
      this.b = flag;
   }

   public LivingEntity getLiving() {
      return this.living;
   }

   public boolean BZ() {
      return this.b;
   }
}
