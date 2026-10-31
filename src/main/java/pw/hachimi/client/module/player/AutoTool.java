package pw.hachimi.client.module.player;

import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.event.combat.AttackBlockEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.utils.player.EnchantmentUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public final class AutoTool extends ClientModule {
   private static AutoTool INSTANCE;

   public AutoTool() {
      super("AutoTool", "Automatically switches to a tool before mining", ModuleCategory.PLAYER);
      INSTANCE = this;
   }

   public static AutoTool UY() {
      return INSTANCE;
   }

   public static int UZ(BlockState state) {
      int n = Va(state);
      return n != -1 ? n : client.player.getInventory().selectedSlot;
   }

   public static int Va(BlockState state) {
      float f = Float.MIN_VALUE;
      int n = -1;
      int n2 = 0;

      do {
         PlayerInventory temp = client.player.getInventory();
         int n3 = n2;
         ItemStack stack = temp.getStack(n3);
         if (!stack.isEmpty()) {
            int n4 = EnchantmentUtils.VN(stack, Enchantments.EFFICIENCY);
            float f2 = stack.getMiningSpeedMultiplier(state);
            if (n4 + f2 > f && f2 > 1.0F) {
               f = n4 + f2;
               n = n2;
            }
         }
      } while (++n2 < 9);

      return n;
   }

   @EventListener
   public void onAttackBlock(AttackBlockEvent attackBlockEvent) {
      ClientWorld temp = client.world;
      BlockPos pos = attackBlockEvent.getPos();
      int n = UZ(temp.getBlockState(pos));
      if (n != -1) {
         client.player.getInventory().selectedSlot = n;
      }
   }
}
