package pw.hachimi.client.event;

import net.minecraft.registry.entry.RegistryEntry;

public class RemoveStatusEffectEvent extends StatusEffectInstanceEvent {
   private final RegistryEntry registryEntry;
   public static boolean bb;

   public RemoveStatusEffectEvent(RegistryEntry registryEntry2) {
      super(null);
      this.registryEntry = registryEntry2;
   }

   public RegistryEntry getRegistryEntry() {
      return this.registryEntry;
   }
}
