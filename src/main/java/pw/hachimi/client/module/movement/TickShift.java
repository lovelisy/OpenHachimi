package pw.hachimi.client.module.movement;

import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.network.SendMovementPacketsTailEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class TickShift extends ClientModule {
   Setting maxTicks = this.registerSetting(new NumberSetting("MaxTicks", "Maximum charge ticks", 1, 20, 40));
   Setting packets = this.registerSetting(new NumberSetting("Packets", "Packets to release from storage every tick", 1, 1, 5));
   Setting chargeSpeed = this.registerSetting(new NumberSetting("ChargeSpeed", "The speed to charge the stored packets", 1, 1, 5));
   private int d;

   public TickShift() {
      super("TickShift", "Exploits NCP to speed up ticks", ModuleCategory.MOVEMENT);
   }

   @Override
   public String hy() {
      return String.valueOf(this.d);
   }

   @Override
   public void awe() {
      Managers.entityManager.rT(1.0F);
   }

   @EventListener
   public void onSendMovementPacketsTail(SendMovementPacketsTailEvent sendMovementPacketsTailEvent) {
      if (sendMovementPacketsTailEvent.getEventPhase() == EventPhase.PRE) {
         if (!MovementUtils.awJ() && client.player.isOnGround()) {
            this.d = this.d + (Integer)this.chargeSpeed.atx();
            if (this.d > (Integer)this.maxTicks.atx()) {
               this.d = (Integer)this.maxTicks.atx();
            }
         } else {
            this.d--;
            if (this.d <= 0) {
               this.d = 0;
               Managers.entityManager.rT(1.0F);
               return;
            }

            Managers.entityManager.rT(((Integer)this.packets.atx()).intValue() + 1.0F);
         }
      }
   }
}
