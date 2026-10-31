package pw.hachimi.client.module.player;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import pw.hachimi.client.event.InputEvents_isPreesedEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.movement.UnidentifiedInterface14;
import pw.hachimi.client.model.player.UnidentifiedInterface5;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoEat extends ClientModule {
   Setting hunger = this.registerSetting(new NumberSetting("Hunger", "The minimum hunger level before eating", 1.0F, 19.0F, 20.0F));
   boolean b;

   public AutoEat() {
      super("AutoEat", "Automatically eats when losing hunger", ModuleCategory.PLAYER);
   }

   @Override
   public void awe() {
      if (client.options != null) {
         ((UnidentifiedInterface14)client.options.useKey).WK();
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (client.player.getHungerManager().getFoodLevel() <= (Float)this.hunger.atx()) {
         int n = this.Sb();
         if (n != -1) {
            if (n == 45) {
               Hand hand = Hand.OFF_HAND;
               client.player.setCurrentHand(hand);
            } else {
               Managers.inventoryTracker.JU(n);
            }

            if (!client.player.isUsingItem()) {
               ((UnidentifiedInterface5)client).Bv();
            }

            this.b = true;
            return;
         }
      }

      if (this.b) {
         ((UnidentifiedInterface14)client.options.useKey).WK();
         this.b = false;
      }
   }

   @EventListener
   public void onInputEvents_isPreesed(InputEvents_isPreesedEvent inputEventsisPreesedEvent) {
      if (this.b) {
         inputEventsisPreesedEvent.aim();
      }
   }

   public int Sb() {
      int n = -1;
      int n2 = -1;
      int n3 = 0;

      do {
         PlayerInventory temp = client.player.getInventory();
         int n4 = n3;
         ItemStack stack = temp.getStack(n4);
         ComponentMap componentMap = stack.getItem().getComponents();
         ComponentType componentType = DataComponentTypes.FOOD;
         if (componentMap.contains(componentType) && stack.getItem() != Items.PUFFERFISH && stack.getItem() != Items.CHORUS_FRUIT) {
            ComponentMap componentMap2 = stack.getItem().getComponents();
            ComponentType componentType2 = DataComponentTypes.FOOD;
            int n5 = ((FoodComponent)componentMap2.get(componentType2)).nutrition();
            if (n5 > n) {
               n2 = n3;
               n = n5;
            }
         }
      } while (++n3 < 9);

      ItemStack stack2 = client.player.getOffHandStack();
      ComponentMap componentMap3 = stack2.getItem().getComponents();
      ComponentType componentType3 = DataComponentTypes.FOOD;
      if (componentMap3.contains(componentType3)) {
         if (stack2.getItem() == Items.PUFFERFISH || stack2.getItem() == Items.CHORUS_FRUIT) {
            return n2;
         }

         componentMap3 = stack2.getItem().getComponents();
         ComponentType componentType4 = DataComponentTypes.FOOD;
         if (((FoodComponent)componentMap3.get(componentType4)).nutrition() > n) {
            n2 = 45;
         }
      }

      return n2;
   }
}
