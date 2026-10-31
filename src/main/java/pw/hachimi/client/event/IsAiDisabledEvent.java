package pw.hachimi.client.event;

import net.minecraft.entity.passive.PigEntity;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class IsAiDisabledEvent extends Event {
   private final PigEntity pigEntity;
   public static boolean gt;

   public IsAiDisabledEvent(PigEntity pigEntity2) {
      this.pigEntity = pigEntity2;
   }

   public PigEntity getPigEntity() {
      return this.pigEntity;
   }
}
