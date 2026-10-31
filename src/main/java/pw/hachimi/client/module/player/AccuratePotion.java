package pw.hachimi.client.module.player;

import java.util.Iterator;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.movement.TravelHeadEvent;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class AccuratePotion extends Module5 {
   public static AccuratePotion INSTANCE;
   Setting swapAlternative = this.registerSetting(new BooleanSetting("SwapAlternative", "Uses inventory swap for swapping to potions", true));
   Setting always = this.registerSetting(new BooleanSetting("Always", "Always inventory swap", false, this.swapAlternative::atx));
   Setting swing = this.registerSetting(new EnumSetting("Swing", "Swing side for throw", SwingMode.Normal, SwingMode.values()));
   Setting multiTask = this.registerSetting(new BooleanSetting("MultiTask", "Allows throwing while using items", true));
   private static int e;
   private static boolean f;
   private static StopwatchImpl stopwatchImpl = new StopwatchImpl();

   public AccuratePotion() {
      super("AccuratePotion", "Freeze and throw RESISTANCE potion.", ModuleCategory.PLAYER, 900);
      INSTANCE = this;
   }

   @Override
   protected void awd() {
      e = 2;
      f = false;
   }

   public int FG(RegistryEntry registryEntry) {
      int n = 0;
      if ((Boolean)this.swapAlternative.atx()) {
      }

      int n2;
      do {
         PlayerInventory temp = client.player.getInventory();
         int n3 = n;
         ItemStack stack = temp.getStack(n3);
         if (stack.getItem() == Items.SPLASH_POTION) {
            ItemStack stack2 = stack;
            PotionContentsComponent potionContentsComponent = PotionContentsComponent.DEFAULT;
            ComponentType componentType = DataComponentTypes.POTION_CONTENTS;
            Iterator iterator2 = ((PotionContentsComponent)stack2.getOrDefault(componentType, potionContentsComponent)).getEffects().iterator();

            while (iterator2.hasNext()) {
               if (((StatusEffectInstance)iterator2.next()).getEffectType() == registryEntry) {
                  if (n < 9) {
                     n2 = n + 36;
                  } else {
                     n2 = n;
                  }

                  return n2;
               }
            }
         }

         n++;
         if ((Boolean)this.swapAlternative.atx()) {
            n2 = 45;
         } else {
            n2 = 9;
         }
      } while (n < n2);

      return -1;
   }

   @EventListener
   public void onTravelHead(TravelHeadEvent travelHeadEvent) {
      travelHeadEvent.aim();
   }

   @EventListener
   public void FI(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (this.FG(StatusEffects.RESISTANCE) == -1) {
         this.awc();
      } else {
         e = 1;
      }
   }

   private static Packet FJ(float f2, int n) {
      return c(Hand.MAIN_HAND, n, f2);
   }

   private static PlayerInteractItemC2SPacket c(Hand hand, int n, float f2) {
      return new PlayerInteractItemC2SPacket(hand, n, f2, -90.0F);
   }
}
