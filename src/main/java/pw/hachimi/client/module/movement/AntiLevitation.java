package pw.hachimi.client.module.movement;

import pw.hachimi.client.event.HasStatusEffectEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class AntiLevitation extends ClientModule {
   public static boolean fw;

   public AntiLevitation() {
      super("AntiLevitation", "Prevents the player from being levitated", ModuleCategory.MOVEMENT);
   }

   @EventListener
   public void onHasStatusEffect(HasStatusEffectEvent hasStatusEffectEvent) {
      hasStatusEffectEvent.aim();
   }
}
