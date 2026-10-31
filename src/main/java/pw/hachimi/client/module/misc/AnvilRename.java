package pw.hachimi.client.module.misc;

import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.model.AnvilRenameAllWhitelistMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.ItemListSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.client.utils.UnidentifiedHelper22;
import pw.hachimi.eventbus.annotation.EventListener;

public final class AnvilRename extends ClientModule {
   Setting selection = this.registerSetting(new EnumSetting("Selection", "The selection of items to rename", AnvilRenameAllWhitelistMode.ALL, AnvilRenameAllWhitelistMode.values()));
   Setting whitelist;
   Setting blacklist;
   Setting text;
   Setting delay;
   Setting debug;
   private final UnidentifiedHelper22 unidentifiedHelper22;
   public static boolean ar;

   public AnvilRename() {
      super("AnvilRename", "Automatically renames items in anvils.", ModuleCategory.MISC);
      Item[] items2 = new Item[]{Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE};
      this.whitelist = this.registerSetting(new ItemListSetting("Whitelist", "The items to rename.", items2));
      items2 = new Item[]{Items.EXPERIENCE_BOTTLE};
      this.blacklist = this.registerSetting(new ItemListSetting("Blacklist", "The items to not rename.", items2));
      this.text = this.registerSetting(createStringSetting("Text", "The text to rename the items to."));
      this.delay = this.registerSetting(new NumberSetting("Delay", "The delay between renaming items.", 0, 10, 100));
      this.debug = this.registerSetting(new BooleanSetting("Debug", "Prints debug information to chat.", false));
      this.unidentifiedHelper22 = new UnidentifiedHelper22();
   }

   @EventListener
   public void afg(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (client.player != null && client.world != null && client.interactionManager != null) {
         Screen screen2 = client.currentScreen;
         if (screen2 instanceof AnvilScreen) {
            AnvilScreen anvilScreen = (AnvilScreen)screen2;
            if (!this.unidentifiedHelper22.afq((Number)this.delay.atx())) {
               return;
            }

            this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
            if (client.player.experienceLevel <= 0 && !client.player.isCreative()) {
               if ((Boolean)this.debug.atx()) {
                  this.hr("Not enough experience levels.");
               }

               return;
            }

            AnvilScreenHandler anvilScreenHandler = (AnvilScreenHandler)((AnvilScreen)screen2).getScreenHandler();
            if (!anvilScreenHandler.getSlot(1).getStack().isEmpty()) {
               this.afh(anvilScreenHandler, 1);
               return;
            }

            if (!anvilScreenHandler.getSlot(0).getStack().isEmpty()) {
               this.afh(anvilScreenHandler, 0);
               return;
            }

            if (!anvilScreenHandler.getSlot(2).getStack().isEmpty()) {
               this.afh(anvilScreenHandler, 2);
               return;
            }

            for (int n = 3; n < 39; n++) {
               AnvilScreenHandler temp = anvilScreenHandler;
               int n2 = n;
               ItemStack stack = temp.getSlot(n2).getStack();
               String str;
               if (!((String)this.text.atx()).trim().isEmpty()) {
                  str = (String)this.text.atx();
               } else {
                  str = "";
               }

               if (!stack.isEmpty() && !this.afi(stack, str)) {
                  if (this.selection.atx() == AnvilRenameAllWhitelistMode.BLACKLIST) {
                     List list = (List)this.blacklist.atx();
                     Item item2 = stack.getItem();
                     if (list.contains(item2)) {
                        continue;
                     }
                  }

                  if (this.selection.atx() == AnvilRenameAllWhitelistMode.WHITELIST) {
                     List list2 = (List)this.whitelist.atx();
                     Item item3 = stack.getItem();
                     if (!list2.contains(item3)) {
                        continue;
                     }
                  }

                  ClientPlayerEntity player2 = client.player;
                  SlotActionType slotActionType = SlotActionType.PICKUP;
                  int n3 = n;
                  int n4 = anvilScreenHandler.syncId;
                  client.interactionManager.clickSlot(n4, n3, 0, slotActionType, (PlayerEntity)player2);
                  ClientPlayerInteractionManager interaction = client.interactionManager;
                  int temp2 = anvilScreenHandler.syncId;
                  ClientPlayerEntity player3 = client.player;
                  SlotActionType slotActionType2 = SlotActionType.PICKUP;
                  int n5 = anvilScreenHandler.getSlot(0).id;
                  int n6 = temp2;
                  interaction.clickSlot(n6, n5, 0, slotActionType2, (PlayerEntity)player3);
                  anvilScreen.nameField.setText(str);
                  if ((Boolean)this.debug.atx()) {
                     this.hr("Successfully renamed item in slot: " + n + ".");
                  }

                  ClientPlayerInteractionManager interaction2 = client.interactionManager;
                  temp2 = anvilScreenHandler.syncId;
                  ClientPlayerEntity player4 = client.player;
                  SlotActionType slotActionType3 = SlotActionType.PICKUP;
                  int n7 = anvilScreenHandler.getSlot(2).id;
                  int n8 = temp2;
                  interaction2.clickSlot(n8, n7, 0, slotActionType3, (PlayerEntity)player4);
                  ClientPlayerEntity player5 = client.player;
                  SlotActionType slotActionType4 = SlotActionType.PICKUP;
                  int n9 = n;
                  int n10 = anvilScreenHandler.syncId;
                  client.interactionManager.clickSlot(n10, n9, 0, slotActionType4, (PlayerEntity)player5);
                  break;
               }
            }

            return;
         }
      }
   }

   private void afh(AnvilScreenHandler anvilScreenHandler, int n) {
      if (client.interactionManager != null) {
         for (int n2 = 3; n2 < 39; n2++) {
            int n3 = n2;
            if (anvilScreenHandler.getSlot(n3).getStack().isEmpty()) {
               ClientPlayerInteractionManager temp = client.interactionManager;
               int temp2 = anvilScreenHandler.syncId;
               ClientPlayerEntity player2 = client.player;
               SlotActionType slotActionType = SlotActionType.PICKUP;
               int n4 = anvilScreenHandler.getSlot(n).id;
               int n5 = temp2;
               temp.clickSlot(n5, n4, 0, slotActionType, (PlayerEntity)player2);
               ClientPlayerEntity player3 = client.player;
               SlotActionType slotActionType2 = SlotActionType.PICKUP;
               int n6 = n2;
               int n7 = anvilScreenHandler.syncId;
               client.interactionManager.clickSlot(n7, n6, 0, slotActionType2, (PlayerEntity)player3);
               return;
            }
         }

         ClientPlayerInteractionManager interaction = client.interactionManager;
         int n8 = anvilScreenHandler.syncId;
         ClientPlayerEntity player4 = client.player;
         SlotActionType slotActionType3 = SlotActionType.THROW;
         int n9 = anvilScreenHandler.getSlot(n).id;
         int n10 = n8;
         interaction.clickSlot(n10, n9, 0, slotActionType3, (PlayerEntity)player4);
      }
   }

   private boolean afi(ItemStack stack, String str) {
      if (str.trim().isEmpty()) {
         ComponentType componentType = DataComponentTypes.CUSTOM_NAME;
         boolean flag;
         if (stack.get(componentType) == null) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         return stack.getName().getString().equals(str);
      }
   }

   private static StringSetting createStringSetting(String str, String str2) {
      return new StringSetting(str, str2, "hachimiclient.me");
   }
}
