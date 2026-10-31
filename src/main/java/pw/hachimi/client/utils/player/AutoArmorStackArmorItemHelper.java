package pw.hachimi.client.utils.player;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ArmorItem.Type;
import net.minecraft.registry.RegistryKey;
import pw.hachimi.client.model.world.RegistryKeyOption;
import pw.hachimi.client.module.player.AutoArmor;

public class AutoArmorStackArmorItemHelper implements Comparable {
   private final int a;
   private final int b;
   private final ItemStack stack;
   final AutoArmor autoArmor;

   public AutoArmorStackArmorItemHelper(AutoArmor autoArmor2, int n, int n2, ItemStack stack2) {
      this.autoArmor = autoArmor2;
      this.a = n;
      this.b = n2;
      this.stack = stack2;
   }

   public int anM(AutoArmorStackArmorItemHelper autoArmorStackArmorItemHelper) {
      if (this.a != autoArmorStackArmorItemHelper.a) {
         return 0;
      }

      ItemStack stack2 = autoArmorStackArmorItemHelper.getStack();
      ArmorItem armorItem = (ArmorItem)this.stack.getItem();
      ArmorItem armorItem2 = (ArmorItem)stack2.getItem();
      ArmorMaterial temp = (ArmorMaterial)armorItem.getMaterial().value();
      Type type = armorItem.getType();
      int n = temp.getProtection(type);
      ArmorMaterial temp2 = (ArmorMaterial)armorItem2.getMaterial().value();
      Type type2 = armorItem2.getType();
      int n2 = n - temp2.getProtection(type2);
      if (n2 != 0) {
         return n2;
      }

      RegistryKey registryKey = ((RegistryKeyOption)this.autoArmor.priority.atx()).aR();
      if ((Boolean)this.autoArmor.leggingsBlastPriority.atx() && this.a == 2) {
         RegistryKey registryKey2 = Enchantments.BLAST_PROTECTION;
         ItemStack stack3 = this.stack;
         if (this.autoArmor.u(stack3, registryKey2)) {
            return -1;
         }
      }

      if (this.autoArmor.u(this.stack, registryKey)) {
         byte b2;
         if (this.autoArmor.u(stack2, registryKey)) {
            b2 = 0;
         } else {
            b2 = -1;
         }

         return b2;
      } else {
         byte b3;
         if (this.autoArmor.u(stack2, registryKey)) {
            b3 = 1;
         } else {
            b3 = 0;
         }

         return b3;
      }
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public int anO() {
      return this.a;
   }

   public int anP() {
      return this.b;
   }

   @Override
   public int compareTo(Object obj) {
      return this.anM((AutoArmorStackArmorItemHelper)obj);
   }
}
