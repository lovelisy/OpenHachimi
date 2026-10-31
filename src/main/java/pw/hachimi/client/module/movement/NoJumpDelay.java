package pw.hachimi.client.module.movement;

import pw.hachimi.client.event.movement.TickMovementEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoJumpDelay extends ClientModule {
   public NoJumpDelay() {
      super("NoJumpDelay", "Removes the vanilla jump delay", ModuleCategory.MOVEMENT);
   }

   @EventListener(priority = -999)
   public void onTickMovement(TickMovementEvent tickMovementEvent) {
      tickMovementEvent.aim();
   }
}
