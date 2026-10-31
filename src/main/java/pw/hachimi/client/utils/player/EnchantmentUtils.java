package pw.hachimi.client.utils.player;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public class EnchantmentUtils {
   public static void VK(ItemStack stack, Object2IntMap object2IntMap) {
      object2IntMap.clear();
      if (!stack.isEmpty()) {
         Set set;
         if (stack.getItem() == Items.ENCHANTED_BOOK) {
            ItemEnchantmentsComponent itemEnchantmentsComponent = ItemEnchantmentsComponent.DEFAULT;
            ComponentType componentType = DataComponentTypes.STORED_ENCHANTMENTS;
            set = ((ItemEnchantmentsComponent)stack.getOrDefault(componentType, itemEnchantmentsComponent)).getEnchantmentEntries();
         } else {
            set = stack.getEnchantments().getEnchantmentEntries();
         }

         Iterator iterator2 = set.iterator();

         while (iterator2.hasNext()) {
            Entry entry2 = (Entry)iterator2.next();
            object2IntMap.put((RegistryEntry)entry2.getKey(), entry2.getIntValue());
         }
      }
   }

   public static int VL(ItemStack stack, RegistryKey registryKey) {
      if (stack.isEmpty()) {
         return 0;
      }

      Object2IntArrayMap object2IntArrayMap = new Object2IntArrayMap();
      VK(stack, object2IntArrayMap);
      return VM(object2IntArrayMap, registryKey);
   }

   public static int VM(Object2IntMap object2IntMap, RegistryKey registryKey) {
      ObjectIterator objectIterator = Object2IntMaps.fastIterable(object2IntMap).iterator();

      while (objectIterator.hasNext()) {
         Entry entry2 = (Entry)objectIterator.next();
         if (((RegistryEntry)entry2.getKey()).matchesKey(registryKey)) {
            return entry2.getIntValue();
         }
      }

      return 0;
   }

   public static int VN(ItemStack stack, RegistryKey registryKey) {
      ComponentMap temp = stack.getComponents();
      ComponentType componentType = DataComponentTypes.ENCHANTMENTS;
      if (!temp.contains(componentType)) {
         return 0;
      }

      temp = stack.getComponents();
      ComponentType componentType2 = DataComponentTypes.ENCHANTMENTS;
      Iterator iterator2 = ((ItemEnchantmentsComponent)temp.get(componentType2)).getEnchantmentEntries().iterator();

      while (iterator2.hasNext()) {
         Entry entry2 = (Entry)iterator2.next();
         if (((RegistryEntry)entry2.getKey()).getKey().isPresent() && ((RegistryKey)((RegistryEntry)entry2.getKey()).getKey().get()).equals(registryKey)) {
            return entry2.getIntValue();
         }
      }

      return 0;
   }

   public static boolean VO(ItemStack stack) {
      Set set = EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries();
      if (set.size() > 1) {
         return false;
      }

      Iterator iterator2 = set.iterator();

      while (iterator2.hasNext()) {
         Entry entry2 = (Entry)iterator2.next();
         RegistryEntry registryEntry = (RegistryEntry)entry2.getKey();
         if (entry2.getIntValue() != 0) {
            continue;
         }

         if (registryEntry.getKey().isPresent() && registryEntry.getKey().get() == Enchantments.PROTECTION) {
            return true;
         }
      }

      return false;
   }
}
