package pw.hachimi.client.module.combat;

import java.util.Iterator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.DefaultDegreesMode;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper22;
import pw.hachimi.client.utils.player.EnchantmentUtils;
import pw.hachimi.client.utils.player.StackPlayerUtils2;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoXP extends Module5 {
   private static AutoXP INSTANCE;
   private final UnidentifiedHelper22 unidentifiedHelper22 = new UnidentifiedHelper22();
   Setting multiTask = this.registerSetting(new BooleanSetting("MultiTask", "Allows you to throw xp while using items", false));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Delay to throw xp in ticks", 1.0F, 1.0F, 10.0F, DefaultDegreesMode.DEFAULT));
   Setting shiftTicks = this.registerSetting(new NumberSetting("ShiftTicks", "The number of xp bottles to throw in one tick", 1, 1, 64));
   Setting durabilityCheck = this.registerSetting(new BooleanSetting("DurabilityCheck", "Check if your armor and held item durability is full then disables if it is", true));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotates the player while throwing xp", false));
   Setting throwAtFeet = this.registerSetting(new BooleanSetting("ThrowAtFeet", "Throw the XP bottles at your feet", true));
   Setting mendHandItem = this.registerSetting(new BooleanSetting("MendHandItem", "Mend item in your hand", true));
   Setting swing = this.registerSetting(new EnumSetting("Swing", "Swing side for throw", SwingMode.Normal, SwingMode.values()));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting autoDisable = this.registerSetting(new BooleanSetting("AutoDisable", "Auto disable the module when mending finish or no xp bottles", false));
   public static boolean ej;

   public AutoXP() {
      super("AutoXP", "Automatically throws xp silently.", ModuleCategory.COMBAT, 850);
      INSTANCE = this;
   }

   public static AutoXP Ra() {
      return INSTANCE;
   }

   @Override
   public String hy() {
      return String.valueOf(StackPlayerUtils2.abK(Items.EXPERIENCE_BOTTLE));
   }

   @EventListener
   public void Rb(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if ((Boolean)this.durabilityCheck.atx() && this.Rc()) {
         if ((Boolean)this.autoDisable.atx()) {
            this.awc();
         }
      } else if (!Blink.bm()) {
         if (client.player != null && this.unidentifiedHelper22.afq((Number)this.delay.atx())) {
            if (!client.player.isUsingItem() || (Boolean)this.multiTask.atx()) {
               StackSlotHelper stackSlotHelper = new StackSlotHelper(Items.EXPERIENCE_BOTTLE, (SwapMode)this.swapAction.atx()).LK();
               if (stackSlotHelper.LL()) {
                  if ((Boolean)this.rotate.atx()) {
                     float temp = client.player.getYaw();
                     boolean flag = (Boolean)this.throwAtFeet.atx();
                     float f = temp;
                     float f2;
                     if (flag) {
                        f = 90.0F;
                        f2 = f;
                     } else {
                        f = client.player.getPitch();
                        f2 = f;
                     }

                     this.ajM(f2, f);
                     if (this.ajO()) {
                        return;
                     }

                     float f3 = Managers.movementTracker.Mt();
                     if ((Boolean)this.throwAtFeet.atx() && f3 < 70.0F) {
                        return;
                     }
                  }

                  int n = Managers.inventoryTracker.JS();
                  stackSlotHelper.LO(n);

                  for (int n2 = 0; n2 < (Integer)this.shiftTicks.atx(); n2++) {
                     Managers.serverManager.iQ(this::Re);
                     ((SwingMode)this.swing.atx()).avX(Hand.MAIN_HAND);
                  }

                  stackSlotHelper.LP(n);
                  this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
               } else if ((Boolean)this.autoDisable.atx()) {
                  this.awc();
               }
            }
         }
      }
   }

   private boolean Rc() {
      if ((this.Rd(Managers.inventoryTracker.JW()) || !(Boolean)this.mendHandItem.atx()) && this.Rd(client.player.getOffHandStack())) {
         Iterator iterator2 = client.player.getArmorItems().iterator();

         while (iterator2.hasNext()) {
            ItemStack stack = (ItemStack)iterator2.next();
            if (!this.Rd(stack)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean Rd(ItemStack stack) {
      if (stack.isEmpty()) {
         return true;
      }

      if (EnchantmentUtils.VN(stack, Enchantments.MENDING) == 0) {
         return true;
      }

      int n = stack.getMaxDamage();
      boolean flag;
      if (stack.getDamage() != 0 && n != 0) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private Packet Re(int n) {
      Hand temp = Hand.MAIN_HAND;
      float temp2 = client.player.getYaw();
      boolean flag = (Boolean)this.throwAtFeet.atx();
      float f = temp2;
      Hand hand = temp;
      float f2;
      if (flag) {
         temp = hand;
         f = 90.0F;
         f2 = f;
         hand = temp;
      } else {
         temp = hand;
         f = client.player.getPitch();
         f2 = f;
         hand = temp;
      }

      return new PlayerInteractItemC2SPacket(hand, n, f2, f);
   }
}
