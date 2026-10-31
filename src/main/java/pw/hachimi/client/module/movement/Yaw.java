package pw.hachimi.client.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.world.ChangeLookDirectionEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class Yaw extends ClientModule {
   Setting lock = this.registerSetting(new BooleanSetting("Lock", "Locks the yaw in cardinal direction", false));
   public static boolean ep;

   public Yaw() {
      super("Yaw", "Locks player yaw to a cardinal axis", ModuleCategory.MOVEMENT);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         float f = Math.round(client.player.getYaw() / 45.0F) * 45.0F;
         Entity entity2 = client.player.getVehicle();
         if (entity2 != null) {
            entity2.setYaw(f);
            if (entity2 instanceof LlamaEntity) {
               ((LlamaEntity)entity2).setHeadYaw(f);
            }

            return;
         }

         client.player.setYaw(f);
         client.player.setHeadYaw(f);
      }
   }

   @EventListener
   public void onChangeLookDirection(ChangeLookDirectionEvent changeLookDirectionEvent) {
      if ((Boolean)this.lock.atx()) {
         changeLookDirectionEvent.aim();
         float f = (float)changeLookDirectionEvent.kR() * 0.15F;
         ClientPlayerEntity temp = client.player;
         float f2 = MathHelper.clamp(client.player.getPitch() + f, -90.0F, 90.0F);
         temp.setPitch(f2);
      }
   }
}
