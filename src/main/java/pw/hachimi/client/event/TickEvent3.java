package pw.hachimi.client.event;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class TickEvent3 extends Event {
   private final FireworkRocketEntity fireworkRocketEntity;
   public static boolean fy;

   public TickEvent3(FireworkRocketEntity fireworkRocketEntity2) {
      this.fireworkRocketEntity = fireworkRocketEntity2;
   }

   public FireworkRocketEntity getFireworkRocketEntity() {
      return this.fireworkRocketEntity;
   }
}
