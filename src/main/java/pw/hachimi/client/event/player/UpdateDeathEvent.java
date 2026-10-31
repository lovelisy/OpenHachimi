package pw.hachimi.client.event.player;

import net.minecraft.entity.LivingEntity;
import pw.hachimi.client.event.Event;

public class UpdateDeathEvent extends Event {
   private final LivingEntity living;

   public UpdateDeathEvent(LivingEntity living2) {
      this.living = living2;
   }

   public LivingEntity getLiving() {
      return this.living;
   }
}
