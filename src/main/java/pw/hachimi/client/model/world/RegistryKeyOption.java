package pw.hachimi.client.model.world;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKey;

public enum RegistryKeyOption {
   BLAST_PROTECTION(Enchantments.BLAST_PROTECTION),
   PROTECTION(Enchantments.PROTECTION),
   PROJECTILE_PROTECTION(Enchantments.PROJECTILE_PROTECTION);

   private final RegistryKey d;
   private static final RegistryKeyOption[] e = aS();

   RegistryKeyOption(RegistryKey registryKey) {
      this.d = registryKey;
   }

   public RegistryKey aR() {
      return this.d;
   }

   private static RegistryKeyOption[] aS() {
      return new RegistryKeyOption[]{BLAST_PROTECTION, PROTECTION, PROJECTILE_PROTECTION};
   }
}
