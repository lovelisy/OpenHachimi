package pw.hachimi.client.module.combat;

import pw.hachimi.client.event.combat.DoAttackEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoHitDelay extends ClientModule {
   public NoHitDelay() {
      super("NoHitDelay", "Removes vanilla attack delay", ModuleCategory.COMBAT);
   }

   @EventListener
   public void onDoAttack(DoAttackEvent doAttackEvent) {
      client.attackCooldown = 0;
   }
}
