package pw.hachimi.client.module.player;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.InventoryTracker;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.DefaultDegreesMode;
import pw.hachimi.client.model.world.RegistryKeyOption;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.player.AutoArmorStackArmorItemHelper;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoArmor extends ClientModule {
   private final Queue queue = new PriorityQueue();
   private final Queue queue2 = new PriorityQueue();
   private final Queue queue3 = new PriorityQueue();
   private final Queue queue4 = new PriorityQueue();
   public Setting priority = this.registerSetting(new EnumSetting("Priority", "Armor enchantment priority", RegistryKeyOption.BLAST_PROTECTION, RegistryKeyOption.values()));
   Setting minDurability = this.registerSetting(new NumberSetting("MinDurability", "Durability percent to replace armor", 0.0F, 0.0F, 20.0F, DefaultDegreesMode.PERCENT));
   Setting elytraPriority = this.registerSetting(new BooleanSetting("ElytraPriority", "Prioritizes existing elytras in the chestplate armor slot", true));
   public Setting leggingsBlastPriority = this.registerSetting(new BooleanSetting("Leggings-BlastPriority", "Prioritizes Blast Protection leggings", true));
   Setting noBinding = this.registerSetting(new BooleanSetting("NoBinding", "Avoids armor with the Curse of Binding enchantment", true));
   Setting allowInventory = this.registerSetting(new BooleanSetting("AllowInventory", "Allows armor to be swapped while in the inventory menu", false));

   public AutoArmor() {
      super("AutoArmor", "Automatically replaces armor pieces", ModuleCategory.PLAYER);
   }

   @EventListener
   public void s(TickEntitiesPreEvent tickEntitiesPreEvent) {
      int n;
      if (client.currentScreen != null) {
         if (!(client.currentScreen instanceof InventoryScreen) || !(Boolean)this.allowInventory.atx()) {
            return;
         }

         this.queue.clear();
         this.queue2.clear();
         this.queue3.clear();
         this.queue4.clear();
         n = 0;
         ItemStack stack = client.player.getInventory().getStack(0);
         if (!stack.isEmpty()) {
            Item item2 = stack.getItem();
            if (item2 instanceof ArmorItem && (!(Boolean)this.noBinding.atx() || !this.u(stack, Enchantments.BINDING_CURSE))) {
               int n2 = ((ArmorItem)item2).getSlotType().getEntitySlotId();
               if (!((float)(stack.getMaxDamage() - stack.getDamage()) / stack.getMaxDamage() < (Float)this.minDurability.atx())) {
                  AutoArmorStackArmorItemHelper autoArmorStackArmorItemHelper = new AutoArmorStackArmorItemHelper(this, n2, 0, stack);
                  switch (n2) {
                     case 0:
                        this.queue.add(autoArmorStackArmorItemHelper);
                        break;
                     case 1:
                        this.queue2.add(autoArmorStackArmorItemHelper);
                        break;
                     case 2:
                        this.queue3.add(autoArmorStackArmorItemHelper);
                        break;
                     case 3:
                        this.queue4.add(autoArmorStackArmorItemHelper);
                  }
               }
            }
         }

         n++;
      } else {
         this.queue.clear();
         this.queue2.clear();
         this.queue3.clear();
         this.queue4.clear();
         n = 0;
      }

      for (; n < 36; n++) {
         PlayerInventory temp = client.player.getInventory();
         int n3 = n;
         ItemStack stack2 = temp.getStack(n3);
         if (!stack2.isEmpty()) {
            Item item3 = stack2.getItem();
            if (item3 instanceof ArmorItem && (!(Boolean)this.noBinding.atx() || !this.u(stack2, Enchantments.BINDING_CURSE))) {
               int n4 = ((ArmorItem)item3).getSlotType().getEntitySlotId();
               if (!((float)(stack2.getMaxDamage() - stack2.getDamage()) / stack2.getMaxDamage() < (Float)this.minDurability.atx())) {
                  AutoArmorStackArmorItemHelper autoArmorStackArmorItemHelper2 = new AutoArmorStackArmorItemHelper(this, n4, n, stack2);
                  switch (n4) {
                     case 0:
                        this.queue.add(autoArmorStackArmorItemHelper2);
                        break;
                     case 1:
                        this.queue2.add(autoArmorStackArmorItemHelper2);
                        break;
                     case 2:
                        this.queue3.add(autoArmorStackArmorItemHelper2);
                        break;
                     case 3:
                        this.queue4.add(autoArmorStackArmorItemHelper2);
                  }
               }
            }
         }
      }

      for (int n5 = 0; n5 < 4; n5++) {
         PlayerInventory playerInventory = client.player.getInventory();
         int n6 = n5;
         ItemStack stack3 = playerInventory.getArmorStack(n6);
         if (!(Boolean)this.elytraPriority.atx() || stack3.getItem() != Items.ELYTRA) {
            float f = (float)(stack3.getMaxDamage() - stack3.getDamage()) / stack3.getMaxDamage();
            if (stack3.isEmpty() && !(f >= (Float)this.minDurability.atx())) {
               switch (n5) {
                  case 0:
                     if (!this.queue.isEmpty()) {
                        AutoArmorStackArmorItemHelper autoArmorStackArmorItemHelper3 = (AutoArmorStackArmorItemHelper)this.queue.poll();
                        this.t(autoArmorStackArmorItemHelper3.anO(), autoArmorStackArmorItemHelper3.anP());
                     }
                     break;
                  case 1:
                     if (!this.queue2.isEmpty()) {
                        AutoArmorStackArmorItemHelper autoArmorStackArmorItemHelper4 = (AutoArmorStackArmorItemHelper)this.queue2.poll();
                        this.t(autoArmorStackArmorItemHelper4.anO(), autoArmorStackArmorItemHelper4.anP());
                     }
                     break;
                  case 2:
                     if (!this.queue3.isEmpty()) {
                        AutoArmorStackArmorItemHelper autoArmorStackArmorItemHelper5 = (AutoArmorStackArmorItemHelper)this.queue3.poll();
                        this.t(autoArmorStackArmorItemHelper5.anO(), autoArmorStackArmorItemHelper5.anP());
                     }
                     break;
                  case 3:
                     if (!this.queue4.isEmpty()) {
                        AutoArmorStackArmorItemHelper autoArmorStackArmorItemHelper6 = (AutoArmorStackArmorItemHelper)this.queue4.poll();
                        this.t(autoArmorStackArmorItemHelper6.anO(), autoArmorStackArmorItemHelper6.anP());
                     }
               }
            }
         }
      }
   }

   public void t(int n, int n2) {
      ItemStack stack = client.player.getInventory().getArmorStack(n);
      n = 8 - n;
      InventoryTracker inventoryTracker2 = Managers.inventoryTracker;
      int n3;
      if (n2 < 9) {
         InventoryTracker temp = inventoryTracker2;
         n3 = n2 + 36;
         inventoryTracker2 = temp;
      } else {
         InventoryTracker inventoryTracker3 = inventoryTracker2;
         n3 = n2;
         inventoryTracker2 = inventoryTracker3;
      }

      inventoryTracker2.JM(n3);
      if (!stack.isEmpty()) {
         Managers.inventoryTracker.JM(n);
         inventoryTracker2 = Managers.inventoryTracker;
         if (n2 < 9) {
            InventoryTracker inventoryTracker4 = inventoryTracker2;
            n3 = n2 + 36;
            inventoryTracker2 = inventoryTracker4;
         } else {
            InventoryTracker inventoryTracker5 = inventoryTracker2;
            n3 = n2;
            inventoryTracker2 = inventoryTracker5;
         }

         inventoryTracker2.JM(n3);
      } else {
         Managers.inventoryTracker.JM(n);
      }
   }

   public boolean u(ItemStack stack, RegistryKey registryKey) {
      ComponentMap temp = stack.getComponents();
      ComponentType componentType = DataComponentTypes.ENCHANTMENTS;
      if (temp.contains(componentType)) {
         temp = stack.getComponents();
         ComponentType componentType2 = DataComponentTypes.ENCHANTMENTS;
         Iterator iterator2 = ((ItemEnchantmentsComponent)temp.get(componentType2)).getEnchantments().iterator();

         while (iterator2.hasNext()) {
            RegistryEntry registryEntry = (RegistryEntry)iterator2.next();
            if (registryEntry.getKey().isPresent() && ((RegistryKey)registryEntry.getKey().get()).equals(registryKey)) {
               return true;
            }
         }
      }

      return false;
   }
}
