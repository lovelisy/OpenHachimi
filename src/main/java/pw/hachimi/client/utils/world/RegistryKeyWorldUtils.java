package pw.hachimi.client.utils.world;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import pw.hachimi.client.Mc;

public class RegistryKeyWorldUtils {
   public static boolean a;

   public static int OS() {
      RegistryKey registryKey = Mc.client.world.getRegistryKey();
      if (registryKey == World.OVERWORLD) {
         return 1;
      } else if (registryKey == World.NETHER) {
         return 0;
      } else {
         return (RegistryKey)registryKey == World.END ? -1 : 1;
      }
   }
}
