package pw.hachimi.client.event;

import net.minecraft.entity.effect.StatusEffectInstance;

public class AddStatusEffectEvent extends StatusEffectInstanceEvent {
   public static boolean ev;

   public AddStatusEffectEvent(StatusEffectInstance statusEffectInstance) {
      super(statusEffectInstance);
   }
}
