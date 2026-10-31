package pw.hachimi.client.module.player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class Replenish extends ClientModule {
   private static Replenish INSTANCE;
   private final Map map = new ConcurrentHashMap();
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting percent = this.registerSetting(new NumberSetting("Percent", "The minimum percent of total stack before replenishing", 1, 25, 80));
   Setting pauseInScreen = this.registerSetting(new BooleanSetting("PauseInScreen", ":)", false));

   public Replenish() {
      super("Replenish", "Automatically replaces items in your hotbar", ModuleCategory.PLAYER);
      INSTANCE = this;
   }

   public static Replenish ajn() {
      return INSTANCE;
   }

   public static boolean ajo() {
      boolean flag;
      if (!(client.currentScreen instanceof GenericContainerScreen)
         && !(client.currentScreen instanceof ShulkerBoxScreen)
         && !(client.currentScreen instanceof InventoryScreen)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   @Override
   public void awe() {
      this.map.clear();
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.map.clear();
   }

   @EventListener
   public void ajq(UpdateDeathEvent updateDeathEvent) {
      if (updateDeathEvent.getLiving() instanceof ClientPlayerEntity) {
         this.map.clear();
      }
   }

   @EventListener
   public void ajr(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (client.options.dropKey.isPressed()) {
         this.stopwatch.afr(29432, 5265492, (byte)40);
      }

      if (client.player.currentScreenHandler.syncId == client.player.playerScreenHandler.syncId
         && (!(client.currentScreen instanceof InventoryScreen) || !(Boolean)this.pauseInScreen.atx())) {
         for (int n = 0; n < 9; n++) {
            ItemStack stack = client.player.getInventory().getStack(n);
            if (stack.isEmpty()) {
               ItemStack stack2 = (ItemStack)this.map.getOrDefault(n, null);
               if (stack2 == null || stack2.isEmpty() || !this.ajs(n, stack2)) {
                  continue;
               }
               break;
            }

            if (stack.isStackable()) {
               double d = (double)stack.getCount() / (double)stack.getMaxCount() * 100.0;
               if (!(d > ((Integer)this.percent.atx()).intValue()) && this.ajs(n, stack)) {
                  break;
               }
            }
         }
      }

      boolean flag = true;
      if (!(client.currentScreen instanceof InventoryScreen) && this.stopwatch.afq(100)) {
         flag = false;
      }

      for (int n2 = 0; n2 < 9; n2++) {
         ItemStack stack3 = client.player.getInventory().getStack(n2);
         if (!stack3.isEmpty() || flag) {
            if (this.map.containsKey(n2)) {
               this.map.replace(n2, stack3.copy());
            } else {
               this.map.put(n2, stack3.copy());
            }
         }
      }
   }

   private boolean ajs(int n, ItemStack stack) {
      int n2 = 9;

      int n3;
      label83:
      while (true) {
         if (n2 >= 36) {
            for (n2 = 0; n2 < 4; n2++) {
               int n4 = n2;
               ItemStack stack2 = ((Slot)client.player.currentScreenHandler.slots.get(n4)).getStack();
               if (!stack2.isEmpty() && this.ajt(stack, stack2) && stack2.isStackable()) {
                  n3 = n2++;
                  break label83;
               }
            }

            return false;
         }

         PlayerInventory temp = client.player.getInventory();
         int n5 = n2;
         ItemStack stack3 = temp.getStack(n5);
         if (!stack3.isEmpty() && this.ajt(stack, stack3) && stack3.isStackable()) {
            label77:
            while (true) {
               n3 = n2;

               while (++n2 < 36) {
                  stack3 = client.player.getInventory().getStack(n2);
                  if (!stack3.isEmpty() && this.ajt(stack, stack3) && stack3.isStackable()) {
                     continue label77;
                  }
               }

               n2 = 0;
               break label83;
            }
         }

         n2++;
      }

      while (n2 < 4) {
         int n6 = n2;
         ItemStack stack4 = ((Slot)client.player.currentScreenHandler.slots.get(n6)).getStack();
         if (!stack4.isEmpty() && this.ajt(stack, stack4) && stack4.isStackable()) {
            n3 = n2++;
         } else {
            n2++;
         }
      }

      if (n3 != -1) {
         ClientPlayerEntity player2 = client.player;
         SlotActionType slotActionType = SlotActionType.PICKUP;
         int n7 = n3;
         client.interactionManager.clickSlot(0, n7, 0, slotActionType, (PlayerEntity)player2);
         int temp2 = n + 36;
         ClientPlayerEntity player3 = client.player;
         SlotActionType slotActionType2 = SlotActionType.PICKUP;
         int n8 = temp2;
         client.interactionManager.clickSlot(0, n8, 0, slotActionType2, (PlayerEntity)player3);
         if (!client.player.currentScreenHandler.getCursorStack().isEmpty()) {
            ClientPlayerEntity player4 = client.player;
            SlotActionType slotActionType3 = SlotActionType.PICKUP;
            int n9 = n3;
            client.interactionManager.clickSlot(0, n9, 0, slotActionType3, (PlayerEntity)player4);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean ajt(ItemStack stack, ItemStack stack2) {
      return ItemStack.areItemsAndComponentsEqual(stack, stack2);
   }
}
