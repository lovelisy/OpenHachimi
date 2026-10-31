package pw.hachimi.client.event;

import net.minecraft.entity.effect.StatusEffectInstance;

public class StatusEffectInstanceEvent extends Event {
   private final StatusEffectInstance statusEffectInstance;

   public StatusEffectInstanceEvent(StatusEffectInstance statusEffectInstance2) {
      this.statusEffectInstance = statusEffectInstance2;
   }

   public StatusEffectInstance getStatusEffectInstance() {
      return this.statusEffectInstance;
   }
}
