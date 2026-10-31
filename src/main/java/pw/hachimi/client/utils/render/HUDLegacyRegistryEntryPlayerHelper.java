package pw.hachimi.client.utils.render;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import pw.hachimi.client.config.ConfigContainer;

public class HUDLegacyRegistryEntryPlayerHelper implements Comparable {
   private final RegistryEntry registryEntry;
   private final int b;
   private int c;
   public static boolean k;

   public HUDLegacyRegistryEntryPlayerHelper(RegistryEntry registryEntry2, int n, int n2) {
      this.registryEntry = registryEntry2;
      this.b = n;
      this.c = n2;
   }

   public void gj() {
      RegistryEntry registryEntry2 = this.registryEntry;
      StatusEffectInstance statusEffectInstance = ConfigContainer.client.player.getStatusEffect(registryEntry2);
      if (statusEffectInstance != null) {
         this.c = statusEffectInstance.getDuration();
      }
   }

   public String gk() {
      return ((StatusEffect)this.getRegistryEntry().value()).getName().getString();
   }

   public RegistryEntry getRegistryEntry() {
      return this.registryEntry;
   }

   public int gm() {
      return this.b;
   }

   public int gn() {
      return this.c;
   }

   public int go(HUDLegacyRegistryEntryPlayerHelper hudLegacyRegistryEntryPlayerHelper) {
      String temp = hudLegacyRegistryEntryPlayerHelper.gk();
      String str = this.gk();
      return temp.compareTo(str);
   }

   @Override
   public int compareTo(Object obj) {
      return this.go((HUDLegacyRegistryEntryPlayerHelper)obj);
   }
}
