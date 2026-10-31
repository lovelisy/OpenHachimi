package pw.hachimi.client.utils.player;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pw.hachimi.client.Mc;

public class StackPlayerUtils2 {
   public static boolean a;

   public static boolean abF() {
      return abG();
   }

   public static boolean abG() {
      boolean flag;
      if (EnchantmentUtils.VN(Mc.client.player.getMainHandStack(), Enchantments.SHARPNESS) >= 1000) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static int abH(Item item2) {
      int n = 0;

      do {
         PlayerInventory temp = Mc.client.player.getInventory();
         int n2 = n;
         if (Item.getRawId(temp.getStack(n2).getItem()) == Item.getRawId(item2)) {
            return n;
         }
      } while (++n < 9);

      return -1;
   }

   public static boolean abI(Item item2) {
      int n = 0;

      do {
         PlayerInventory temp = Mc.client.player.getInventory();
         int n2 = n;
         ItemStack stack = temp.getStack(n2);
         if (!stack.isEmpty() && stack.getItem() == item2) {
            return true;
         }
      } while (++n < 36);

      return false;
   }

   public static boolean abJ(Item item2) {
      for (int n = 0; n < 9; n++) {
         ItemStack stack = Mc.client.player.getInventory().getStack(n);
         if (!stack.isEmpty() && stack.getItem() == item2) {
            return true;
         }
      }

      return false;
   }

   public static int abK(Item item2) {
      if (Mc.client.player == null) {
         return 0;
      }

      ItemStack stack = Mc.client.player.getOffHandStack();
      int n;
      int n2;
      if (stack.getItem() == item2) {
         n = stack.getCount();
         n2 = 0;
         ItemStack stack2 = Mc.client.player.getInventory().getStack(0);
         if (stack2.getItem() == item2) {
            n += stack2.getCount();
         }

         n2++;
      } else {
         n = 0;
         n2 = 0;
      }

      for (; n2 < 36; n2++) {
         PlayerInventory temp = Mc.client.player.getInventory();
         int n3 = n2;
         ItemStack stack3 = temp.getStack(n3);
         if (stack3.getItem() == item2) {
            n += stack3.getCount();
         }
      }

      return n;
   }
}
