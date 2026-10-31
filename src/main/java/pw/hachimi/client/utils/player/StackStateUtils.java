package pw.hachimi.client.utils.player;

import java.util.Iterator;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.BreakManager;
import pw.hachimi.client.manager.Managers;

public class StackStateUtils {
   public static boolean a;

   public static double ald(BlockPos pos, boolean flag) {
      ItemStack stack = ale(pos);
      double d;
      if (flag) {
         ItemStack temp = stack;
         d = 1.0;
         stack = temp;
      } else {
         ItemStack stack2 = stack;
         d = BreakManager.a_;
         stack = stack2;
      }

      return alf(pos, stack, d);
   }

   private static ItemStack ale(BlockPos pos) {
      PlayerInventory temp = Mc.client.player.getInventory();
      int n = Managers.inventoryTracker.JS();
      ItemStack stack = temp.getStack(n);
      float f = Float.MIN_VALUE;
      Iterator iterator2 = Mc.client.player.currentScreenHandler.slots.iterator();

      while (iterator2.hasNext()) {
         ItemStack stack2 = ((Slot)iterator2.next()).getStack();
         if (!stack2.isEmpty()) {
            int n2 = EnchantmentUtils.VN(stack2, Enchantments.EFFICIENCY);
            ItemStack stack3 = stack2;
            BlockState state = Mc.client.world.getBlockState(pos);
            float f2 = stack3.getMiningSpeedMultiplier(state);
            if (n2 + f2 > f && f2 > 1.0F) {
               f = n2 + f2;
               stack = stack2;
            }
         }
      }

      return stack;
   }

   private static double alf(BlockPos pos, ItemStack stack, double d) {
      return 1.0F / alg(pos, stack) / 20.0F * 1000.0F * d;
   }

   private static float alg(BlockPos pos, ItemStack stack) {
      BlockState state = Mc.client.world.getBlockState(pos);
      BlockState temp = state;
      ClientWorld world2 = Mc.client.world;
      float f = temp.getHardness((BlockView)world2, pos);
      if (f < 0.0F) {
         return 0.0F;
      }

      float f2;
      if (state.isToolRequired() && !stack.isSuitableFor(state)) {
         f2 = 100.0F;
      } else {
         f2 = 30.0F;
      }

      float f3 = f2;
      return alh(state, stack) / f / f3;
   }

   private static float alh(BlockState state, ItemStack stack) {
      float f = ali(state, stack);
      if (f > 1.0F) {
         int n = EnchantmentUtils.VN(stack, Enchantments.EFFICIENCY);
         if (n > 0 && !stack.isEmpty()) {
            f += (float)(StrictMath.pow(n, 2.0) + 1.0);
         }
      }

      float f2;
      if (f < 0.0F) {
         f2 = 0.0F;
      } else {
         f2 = f;
      }

      return f2;
   }

   private static float ali(BlockState state, ItemStack stack) {
      float f = 1.0F;
      if (stack != null && !stack.isEmpty()) {
         f = 1.0F * stack.getMiningSpeedMultiplier(state);
      }

      return f;
   }
}
