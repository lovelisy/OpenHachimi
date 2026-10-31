package pw.hachimi.client.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.model.movement.UnidentifiedInterface14;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class Parkour extends ClientModule {
   private boolean a;

   public Parkour() {
      super("Parkour", "Automatically jumps at the edge of blocks", ModuleCategory.MOVEMENT);
   }

   @Override
   public void awe() {
      super.awe();
      if (this.a) {
         this.a = false;
         ((UnidentifiedInterface14)client.options.jumpKey).WK();
      }
   }

   @EventListener
   public void agB(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (client.player.isOnGround() && !client.player.isSneaking()) {
         ClientPlayerEntity player2 = client.player;
         ClientWorld temp = client.world;
         Box box = player2.getBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001);
         if (temp.isSpaceEmpty(box)) {
            client.options.jumpKey.setPressed(true);
            this.a = true;
            return;
         }
      }

      if (this.a) {
         this.a = false;
         ((UnidentifiedInterface14)client.options.jumpKey).WK();
      }
   }
}
