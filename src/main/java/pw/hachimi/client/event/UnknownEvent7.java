package pw.hachimi.client.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.eventbus.EventBusImpl;

@UnidentifiedInterface2
public class UnknownEvent7 extends Event {
   public static void abC(LivingEntity living, int n, boolean flag, Operation operation) {
      if (!flag) {
         UnknownEvent7 unknownEvent7 = new UnknownEvent7();
         EventBusImpl.INSTANCE.rC(unknownEvent7);
         if (unknownEvent7.aik()) {
            return;
         }
      }

      operation.call(new Object[]{living, n, flag});
   }
}
