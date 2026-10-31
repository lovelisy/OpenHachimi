package pw.hachimi.client.utils.player;

import java.util.Iterator;
import java.util.function.Predicate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.player.SwapMode;

public class StackSlotHelper {
   public ItemStack stack;
   Item item;
   SwapMode swapMode;
   Predicate predicate;
   Slot slot;
   int f = -1;
   int g = -1;
   public static boolean a;

   public StackSlotHelper(Predicate predicate2, SwapMode swapMode2) {
      this.swapMode = swapMode2;
      this.predicate = predicate2;
   }

   public StackSlotHelper(Item item2, SwapMode swapMode2) {
      this.item = item2;
      this.swapMode = swapMode2;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public StackSlotHelper LK() {
      switch (UnidentifiedHelper6.a[this.swapMode.ordinal()]) {
         case 1:
            this.g = this.LS();
            break;
         case 2:
            this.f = this.LQ();
            break;
         case 3:
            this.slot = this.LR();
      }

      return this;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public boolean LL() {
      switch (UnidentifiedHelper6.a[this.swapMode.ordinal()]) {
         case 1:
            boolean flag;
            if (this.g != -1) {
               flag = true;
            } else {
               flag = false;
            }

            return flag;
         case 2:
            boolean flag2;
            if (this.f != -1) {
               flag2 = true;
            } else {
               flag2 = false;
            }

            return flag2;
         case 3:
            boolean flag3;
            if (this.slot != null) {
               flag3 = true;
            } else {
               flag3 = false;
            }

            return flag3;
         default:
            return false;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public int LM() {
      return switch (UnidentifiedHelper6.a[this.swapMode.ordinal()]) {
         case 1 -> this.g;
         case 2 -> this.f;
         case 3 -> this.slot.id;
         default -> throw a();
      };
   }

   public int LN() {
      int n = this.LM();
      int n2;
      if (n < 9 && this.swapMode != SwapMode.Screen) {
         n2 = n + 36;
      } else {
         n2 = n;
      }

      return n2;
   }

   public void LO(int n) {
      int n2 = this.LM();
      if (n2 != -1) {
         this.swapMode.er().ba(n2, n);
      }
   }

   public void LP(int n) {
      int n2 = this.LM();
      if (n2 != -1) {
         this.swapMode.er().bb(n2, n);
      }
   }

   public int LQ() {
      int n = 0;

      do {
         PlayerInventory temp = Mc.client.player.getInventory();
         int n2 = n;
         ItemStack stack2 = temp.getStack(n2);
         if (this.predicate == null) {
            if (stack2.getItem().equals(this.item)) {
               this.stack = stack2;
               return n;
            }
         } else if (this.predicate.test(stack2)) {
            this.stack = stack2;
            return n;
         }
      } while (++n < 9);

      return -1;
   }

   public Slot LR() {
      Iterator iterator2 = Mc.client.player.currentScreenHandler.slots.iterator();

      while (iterator2.hasNext()) {
         Slot slot2 = (Slot)iterator2.next();
         ItemStack stack2 = slot2.getStack();
         if (this.predicate == null) {
            if (stack2.getItem().equals(this.item)) {
               this.stack = stack2;
               return slot2;
            }
         } else if (this.predicate.test(stack2)) {
            this.stack = stack2;
            return slot2;
         }
      }

      return null;
   }

   public int LS() {
      int n = 0;

      do {
         PlayerInventory temp = Mc.client.player.getInventory();
         int n2 = n;
         ItemStack stack2 = temp.getStack(n2);
         if (this.predicate == null) {
            if (stack2.getItem().equals(this.item)) {
               this.stack = stack2;
               return n;
            }
         } else if (this.predicate.test(stack2)) {
            this.stack = stack2;
            return n;
         }
      } while (++n < 36);

      return -1;
   }

   private static MatchException a() {
      return new MatchException(null, null);
   }
}
