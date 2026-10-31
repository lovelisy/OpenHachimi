package pw.hachimi.client.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import pw.hachimi.client.module.misc.Tooltips;
import pw.hachimi.client.utils.render.ColorUtils;

public record ShulkerCompact(ItemStack shulker, boolean compact, int color, int slot, List stacks) {
   public static boolean i;

   public static ShulkerCompact DO(ItemStack stack, int n) {
      ShulkerBoxBlock shulkerBoxBlock = DR(stack);
      if (shulkerBoxBlock == null) {
         return null;
      }

      DefaultedList defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
      ComponentMap temp = stack.getComponents();
      ComponentType componentType = DataComponentTypes.CONTAINER;
      ContainerComponent containerComponent = (ContainerComponent)temp.get(componentType);
      boolean flag = (Boolean)Tooltips.INSTANCE.compact.atx();
      if (containerComponent != null) {
         List list = containerComponent.stream().toList();

         label56:
         for (int n2 = 0; n2 < list.size(); n2++) {
            List list2 = list;
            int n3 = n2;
            ItemStack stack2 = (ItemStack)list2.get(n3);
            DefaultedList defaultedList2 = defaultedList;
            int n4 = n2;
            ((List)defaultedList2).set(n4, stack2);
            if (stack2.getMaxCount() == 1) {
               Item item2;
               do {
                  item2 = stack2.getItem();

                  do {
                     if (++n2 >= list.size()) {
                        break label56;
                     }

                     stack2 = (ItemStack)list.get(n2);
                     ((DefaultedList)defaultedList).set(n2, stack2);
                  } while (stack2.getMaxCount() != 1);
               } while ((Item)item2 == null || stack2.getItem().equals(item2));

               while (true) {
                  flag = false;

                  do {
                     item2 = stack2.getItem();

                     do {
                        if (++n2 >= list.size()) {
                           return new ShulkerCompact(stack, flag, DP(shulkerBoxBlock), n, defaultedList);
                        }

                        stack2 = (ItemStack)list.get(n2);
                        ((DefaultedList)defaultedList).set(n2, stack2);
                     } while (stack2.getMaxCount() != 1);
                  } while ((Item)item2 == null || stack2.getItem().equals(item2));
               }
            }
         }
      }

      if (flag) {
         DQ(defaultedList);
      }

      return new ShulkerCompact(stack, flag, DP(shulkerBoxBlock), n, defaultedList);
   }

   public static int DP(ShulkerBoxBlock shulkerBoxBlock) {
      return shulkerBoxBlock.getColor() != null ? ColorUtils.iG(shulkerBoxBlock.getColor().getMapColor().color, 255) : -6728784;
   }

   private static void DQ(List list) {
      HashMap hashMap = new HashMap();
      Iterator iterator2 = list.iterator();

      while (iterator2.hasNext()) {
         ItemStack stack = (ItemStack)iterator2.next();
         if (!stack.isEmpty()) {
            HashMap temp = hashMap;
            Item temp2 = stack.getItem();
            Integer temp3 = stack.getCount();
            BiFunction biFunction = new BiFunction() {
               @Override
               public Object apply(Object obj, Object obj2) {
                  return new Integer(Integer.sum(((Integer)obj).intValue(), ((Integer)obj2).intValue()));
               }
            };
            Integer integer = temp3;
            Item item2 = temp2;
            temp.merge(item2, integer, biFunction);
         }
      }

      list.clear();
      int n = 0;
      Iterator iterator3 = hashMap.entrySet().iterator();

      while (iterator3.hasNext()) {
         Entry entry = (Entry)iterator3.next();
         int n2 = n++;
         ItemStack stack2 = new ItemStack((ItemConvertible)entry.getKey(), (Integer)entry.getValue());
         int n3 = n2;
         list.set(n3, stack2);
      }
   }

   private static ShulkerBoxBlock DR(ItemStack stack) {
      Item item2 = stack.getItem();
      if (item2 instanceof BlockItem) {
         Block block2 = ((BlockItem)item2).getBlock();
         if (block2 instanceof ShulkerBoxBlock) {
            return (ShulkerBoxBlock)block2;
         }
      }

      return null;
   }

   public int getSlot() {
      return this.slot;
   }
}
