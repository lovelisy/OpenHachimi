package pw.hachimi.client.utils.movement;

import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.module.movement.ElytraFly;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class ElytraFlyUtils {
   public static int a_;
   public static boolean a;

   @EventListener
   public static void auN(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (a_ <= 0) {
         EventBusImpl.INSTANCE.rH(ElytraFlyUtils.class);
      } else {
         ElytraFly.di().do_();
         a_--;
      }
   }
}
