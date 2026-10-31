package pw.hachimi.client.utils.player;

import java.util.Iterator;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import pw.hachimi.client.module.exploit.SpearFix;

public class StackPlayerUtils {
   public static boolean a;

   public static float oQ(PlayerEntity player2, ItemStack stack, BlockState state, boolean flag, boolean flag2, boolean flag3) {
      if (SpearFix.nt(stack)) {
         return 0.0F;
      }

      Item item2 = stack.getItem();
      if (player2.isCreative()) {
         return !((Item)item2 instanceof SwordItem) && (Item)item2 != Items.TRIDENT && (Item)item2 != Items.DEBUG_STICK && (Item)item2 != Items.MACE ? 1.0F : 0.0F;
      }

      float f = state.getBlock().getHardness();
      if (f == -1.0F) {
         return 0.0F;
      }

      float f2 = oR(state, stack);
      if (StatusEffectUtil.hasHaste((LivingEntity)player2)) {
         f2 *= 1.0F + (StatusEffectUtil.getHasteAmplifier((LivingEntity)player2) + 1) * 0.2F;
      }

      RegistryEntry registryEntry = StatusEffects.MINING_FATIGUE;
      if (player2.hasStatusEffect(registryEntry)) {
         RegistryEntry registryEntry2 = StatusEffects.MINING_FATIGUE;
         switch (player2.getStatusEffect(registryEntry2).getAmplifier()) {
            case 0:
               f2 *= 0.3F;
               break;
            case 1:
               f2 *= 0.09F;
               break;
            case 2:
               f2 *= 0.0027F;
               break;
            default:
               f2 *= 8.1E-4F;
         }
      }

      if (flag3) {
         Iterator iterator2 = player2.getArmorItems().iterator();

         label76:
         while (true) {
            if (!iterator2.hasNext()) {
               f2 /= 5.0F;
               break;
            }

            ComponentMap temp = ((ItemStack)iterator2.next()).getComponents();
            ComponentType componentType = DataComponentTypes.ENCHANTMENTS;
            ItemEnchantmentsComponent itemEnchantmentsComponent = (ItemEnchantmentsComponent)temp.get(componentType);
            if (itemEnchantmentsComponent != null) {
               Iterator iterator3 = itemEnchantmentsComponent.getEnchantments().iterator();

               while (iterator3.hasNext()) {
                  RegistryEntry registryEntry3 = (RegistryEntry)iterator3.next();
                  if (registryEntry3.getKey().isPresent() && ((RegistryKey)registryEntry3.getKey().get()).equals(Enchantments.AQUA_AFFINITY)) {
                     break label76;
                  }
               }
            }
         }
      }

      if (!flag || !player2.isOnGround() && flag2) {
         f2 /= 5.0F;
      }

      float f3 = f2 / f;
      float f4;
      if (state.isToolRequired() && !stack.isSuitableFor(state)) {
         f4 = 100.0F;
      } else {
         f4 = 30.0F;
      }

      return f3 / f4;
   }

   public static float oR(BlockState state, ItemStack stack) {
      if (SpearFix.nt(stack)) {
         return 0.0F;
      }

      float f = stack.getMiningSpeedMultiplier(state);
      if (f > 1.0F && stack.isSuitableFor(state)) {
         int n = EnchantmentUtils.VN(stack, Enchantments.EFFICIENCY);
         if (n > 0) {
            f += n * n + 1;
         }
      }

      return f;
   }
}
