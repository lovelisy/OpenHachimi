package pw.hachimi.client.module.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.StackPotionContentsComponentManager;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ItemListSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper22;
import pw.hachimi.client.utils.player.UnidentifiedUtils6;
import pw.hachimi.eventbus.annotation.EventListener;

public class InventoryManager extends ClientModule {
   private static InventoryManager INSTANCE;
   private final UnidentifiedHelper22 unidentifiedHelper22 = new UnidentifiedHelper22();
   Setting stack = this.registerSetting(new BooleanSetting("Stack", "Automatically sorts stacks in inventory", true));
   Setting kitMode = this.registerSetting(new BooleanSetting("KitMode", "Manage the items according to the kit", false));
   Setting sort = this.registerSetting(new BooleanSetting("Sort", "Automatically sorts stacks in inventory", false));
   Setting sortDelay = this.registerSetting(new NumberSetting("SortDelay", "The delay for dropping items", 0, 0, 20, this::isSortDelayVisible));
   Setting cleaner = this.registerSetting(new BooleanSetting("Cleaner", "Automatically cleans the player inventory", true));
   Setting rename = this.registerSetting(new BooleanSetting("Rename", "Items to drop", false, this::isRenameVisible));
   Setting hotbar = this.registerSetting(new BooleanSetting("Hotbar", "Cleans the hotbar inventory slots", true, this::isHotbarVisible));
   Setting cleanerDelay = this.registerSetting(new NumberSetting("CleanerDelay", "The delay between removing items from the inventory", 0, 0, 20, this::isCleanerDelayVisible));
   Setting blacklist = this.registerSetting(new ItemListSetting("Blacklist", "The items to throw"));
   Setting whiteList = this.registerSetting(new ItemListSetting("WhiteList", "The white list of items"));
   public static boolean fd;

   public InventoryManager() {
      super("InventoryManager", "Automatically organizes your inventory", ModuleCategory.PLAYER);
      INSTANCE = this;
   }

   public static InventoryManager aiK() {
      return INSTANCE;
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE && client.player.currentScreenHandler.syncId == client.player.playerScreenHandler.syncId) {
         if (this.unidentifiedHelper22.afq((Number)this.cleanerDelay.atx()) && (Boolean)this.cleaner.atx()) {
            HashMap hashMap = null;
            if ((Boolean)this.kitMode.atx()) {
               hashMap = new HashMap();
               aiO(hashMap);
            }

            if (this.aiN(hashMap)) {
               return;
            }
         }

         if (this.unidentifiedHelper22.afq((Number)this.sortDelay.atx()) && (!(Boolean)this.stack.atx() || !this.aiM()) && (Boolean)this.sort.atx()) {
            if (!(Boolean)this.kitMode.atx()) {
               int n = 9;

               do {
                  PlayerInventory temp = client.player.getInventory();
                  int n2 = n;
                  int n3 = Item.getRawId(temp.getStack(n2).getItem());
                  temp = client.player.getInventory();
                  int n4 = n;
                  if (temp.getStack(n4).isEmpty()) {
                     n3 = 114514;
                  }

                  int n5 = this.aiP(n, n3);
                  if (n5 < n3) {
                     for (int n6 = 35; n6 > n; n6--) {
                        temp = client.player.getInventory();
                        int n7 = n6;
                        ItemStack stack2 = temp.getStack(n7);
                        if (!stack2.isEmpty() && Item.getRawId(stack2.getItem()) == n5) {
                           this.aiR(n, n6);
                           this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                           return;
                        }
                     }
                  }
               } while (++n < 36);
            } else {
               ItemStack[] stacks = new ItemStack[36];
               UnidentifiedUtils6[] unidentifiedUtils6s = new UnidentifiedUtils6[36];
               UnidentifiedUtils6[] unidentifiedUtils6s2 = new UnidentifiedUtils6[36];
               int n8 = 0;

               do {
                  PlayerInventory playerInventory = client.player.getInventory();
                  int n9 = n8;
                  ItemStack stack3 = playerInventory.getStack(n9);
                  stacks[n8] = stack3;
                  unidentifiedUtils6s[n8] = !stack3.isEmpty() ? StackPotionContentsComponentManager.Pb(stack3) : null;
                  HashMap temp2 = StackPotionContentsComponentManager.a_;
                  Integer integer = n8;
                  unidentifiedUtils6s2[n8] = (UnidentifiedUtils6)temp2.get(integer);
               } while (++n8 < 36);

               n8 = 0;

               do {
                  UnidentifiedUtils6 unidentifiedUtils6 = unidentifiedUtils6s2[n8];
                  UnidentifiedUtils6 unidentifiedUtils62 = unidentifiedUtils6s[n8];
                  if (unidentifiedUtils6 != null) {
                     if (unidentifiedUtils62 != null) {
                        String str = unidentifiedUtils62.text;
                        if (unidentifiedUtils6.text.equals(str)) {
                           continue;
                        }
                     }

                     int n10 = 0;

                     do {
                        if (!stacks[n10].isEmpty()) {
                           UnidentifiedUtils6 unidentifiedUtils63 = unidentifiedUtils6s[n10];
                           if (unidentifiedUtils63 != null) {
                              UnidentifiedUtils6 unidentifiedUtils64 = unidentifiedUtils6s2[n10];
                              if (unidentifiedUtils64 != null) {
                                 String str2 = unidentifiedUtils63.text;
                                 if (unidentifiedUtils64.text.equals(str2)) {
                                    continue;
                                 }
                              }

                              String str3 = unidentifiedUtils63.text;
                              if (unidentifiedUtils6.text.equals(str3)) {
                                 this.aiR(n8, n10);
                                 this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                                 return;
                              }
                           }
                        }
                     } while (++n10 < 36);
                  }
               } while (++n8 < 36);
            }
         }
      }
   }

   private boolean aiM() {
      for (int n = 9; n < 36; n++) {
         PlayerInventory temp = client.player.getInventory();
         int n2 = n;
         ItemStack stack2 = temp.getStack(n2);
         if (!stack2.isEmpty() && stack2.isStackable() && stack2.getCount() != stack2.getMaxCount()) {
            for (int n3 = 35; n3 >= 9; n3--) {
               if (n != n3) {
                  temp = client.player.getInventory();
                  int n4 = n3;
                  ItemStack stack3 = temp.getStack(n4);
                  if (stack3.getCount() != stack3.getMaxCount() && this.aiQ(stack2, stack3)) {
                     this.aiR(n, n3);
                     this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean aiN(Map map) {
      List list = (List)this.whiteList.atx();
      List list2 = (List)this.blacklist.atx();
      boolean flag = (Boolean)this.rename.atx();
      boolean flag2 = (Boolean)this.kitMode.atx();
      byte b2;
      if ((Boolean)this.hotbar.atx()) {
         b2 = 0;
      } else {
         b2 = 9;
      }

      byte b3 = b2;

      for (int n = 35; n >= b3; n--) {
         PlayerInventory temp = client.player.getInventory();
         int n2 = n;
         ItemStack stack2 = temp.getStack(n2);
         if (!stack2.isEmpty()) {
            List list3 = list;
            Item item2 = stack2.getItem();
            if (!list3.contains(item2)) {
               if (flag && this.aiU(stack2)) {
                  this.aiS(n);
                  this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                  return true;
               }

               if (flag2) {
                  UnidentifiedUtils6 unidentifiedUtils6 = StackPotionContentsComponentManager.Pb(stack2);
                  if (unidentifiedUtils6 == null) {
                     this.aiS(n);
                     this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                     return true;
                  }

                  Integer integer = 0;
                  int n3 = (Integer)map.getOrDefault(unidentifiedUtils6, integer);
                  if (n3 < 0 && (n3 <= -stack2.getMaxCount() || stack2.getCount() <= Math.abs(n3))) {
                     this.aiS(n);
                     this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                     return true;
                  }
               }

               List list4 = list2;
               Item item3 = stack2.getItem();
               if (list4.contains(item3)) {
                  this.aiS(n);
                  this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                  return true;
               }
            }
         }
      }

      return false;
   }

   private static void aiO(Map map) {
      HashMap hashMap = new HashMap();
      Iterator iterator2 = StackPotionContentsComponentManager.a_.values().iterator();
      int n;
      HashMap hashMap2;
      if (iterator2.hasNext()) {
         while (true) {
            UnidentifiedUtils6 unidentifiedUtils6 = (UnidentifiedUtils6)iterator2.next();
            HashMap temp = hashMap;
            String temp2 = unidentifiedUtils6.text;
            Integer temp3 = unidentifiedUtils6.b;
            BiFunction biFunction = new BiFunction() {
               @Override
               public Object apply(Object obj, Object obj2) {
                  return new Integer(Integer.sum(((Integer)obj).intValue(), ((Integer)obj2).intValue()));
               }
            };
            Integer integer = temp3;
            String str = temp2;
            temp.merge(str, integer, biFunction);
            if (!iterator2.hasNext()) {
               hashMap2 = new HashMap();
               n = 0;
               ItemStack stack2 = client.player.getInventory().getStack(0);
               UnidentifiedUtils6 unidentifiedUtils62 = StackPotionContentsComponentManager.Pb(stack2);
               if (unidentifiedUtils62 != null) {
                  temp = hashMap2;
                  temp2 = unidentifiedUtils62.text;
                  temp3 = stack2.getCount();
                  BiFunction biFunction2 = new BiFunction() {
                     @Override
                     public Object apply(Object obj, Object obj2) {
                        return new Integer(Integer.sum(((Integer)obj).intValue(), ((Integer)obj2).intValue()));
                     }
                  };
                  Integer integer2 = temp3;
                  String str2 = temp2;
                  temp.merge(str2, integer2, biFunction2);
               }

               n++;
               break;
            }
         }
      } else {
         hashMap2 = new HashMap();
         n = 0;
      }

      for (; n < 41; n++) {
         PlayerInventory playerInventory = client.player.getInventory();
         int n2 = n;
         ItemStack stack3 = playerInventory.getStack(n2);
         UnidentifiedUtils6 unidentifiedUtils63 = StackPotionContentsComponentManager.Pb(stack3);
         if (unidentifiedUtils63 != null) {
            HashMap hashMap3 = hashMap2;
            String str3 = unidentifiedUtils63.text;
            Integer integer3 = stack3.getCount();
            BiFunction biFunction3 = new BiFunction() {
               @Override
               public Object apply(Object obj, Object obj2) {
                  return new Integer(Integer.sum(((Integer)obj).intValue(), ((Integer)obj2).intValue()));
               }
            };
            Integer integer4 = integer3;
            String str4 = str3;
            hashMap3.merge(str4, integer4, biFunction3);
         }
      }

      for (int n3 = 0; n3 < 36; n3++) {
         PlayerInventory playerInventory2 = client.player.getInventory();
         int n4 = n3;
         ItemStack stack4 = playerInventory2.getStack(n4);
         if (!stack4.isEmpty()) {
            UnidentifiedUtils6 unidentifiedUtils64 = StackPotionContentsComponentManager.Pb(stack4);
            if (unidentifiedUtils64 != null && !map.containsKey(unidentifiedUtils64)) {
               HashMap hashMap4 = hashMap;
               String str5 = unidentifiedUtils64.text;
               Integer integer5 = 0;
               String str6 = str5;
               int n5 = (Integer)hashMap4.getOrDefault(str6, integer5);
               HashMap hashMap5 = hashMap2;
               str5 = unidentifiedUtils64.text;
               Integer integer6 = 0;
               String str7 = str5;
               int n6 = (Integer)hashMap5.getOrDefault(str7, integer6);
               int n7 = n5 - n6;
               Integer integer7 = n7;
               map.put(unidentifiedUtils64, integer7);
            }
         }
      }
   }

   private int aiP(int n, int n2) {
      int n3 = n2;

      for (int n4 = n + 1; n4 < 36; n4++) {
         ItemStack stack2 = client.player.getInventory().getStack(n4);
         if (!stack2.isEmpty()) {
            int n5 = Item.getRawId(stack2.getItem());
            if (n5 < n3) {
               n3 = n5;
            }
         }
      }

      return n3;
   }

   private boolean aiQ(ItemStack stack2, ItemStack stack3) {
      return ItemStack.areItemsAndComponentsEqual(stack2, stack3);
   }

   private void aiR(int n, int n2) {
      int n3 = client.player.playerScreenHandler.syncId;
      int n4 = this.aiT(n);
      int n5 = this.aiT(n2);
      ClientPlayerEntity player2 = client.player;
      SlotActionType slotActionType = SlotActionType.PICKUP;
      client.interactionManager.clickSlot(n3, n4, 0, slotActionType, (PlayerEntity)player2);
      ClientPlayerEntity player3 = client.player;
      SlotActionType slotActionType2 = SlotActionType.PICKUP;
      client.interactionManager.clickSlot(n3, n5, 0, slotActionType2, (PlayerEntity)player3);
      ClientPlayerEntity player4 = client.player;
      SlotActionType slotActionType3 = SlotActionType.PICKUP;
      client.interactionManager.clickSlot(n3, n4, 0, slotActionType3, (PlayerEntity)player4);
   }

   private void aiS(int n) {
      ClientPlayerInteractionManager temp = client.interactionManager;
      int temp2 = client.player.currentScreenHandler.syncId;
      int temp3 = this.aiT(n);
      ClientPlayerEntity player2 = client.player;
      SlotActionType slotActionType = SlotActionType.THROW;
      int n2 = temp3;
      int n3 = temp2;
      temp.clickSlot(n3, n2, 1, slotActionType, (PlayerEntity)player2);
   }

   private int aiT(int n) {
      int n2;
      if (n < 9) {
         n2 = n + 36;
      } else {
         n2 = n;
      }

      return n2;
   }

   private boolean aiU(ItemStack stack2) {
      Item item2 = stack2.getItem();
      if (item2 != Items.COMPASS && item2 != Items.EMERALD) {
         if (stack2.isStackable()) {
            String temp = stack2.getName().getString();
            String str = item2.getName().getString();
            if (!temp.equals(str)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private Boolean isCleanerDelayVisible() {
      return (Boolean)this.cleaner.atx();
   }

   private Boolean isHotbarVisible() {
      return (Boolean)this.cleaner.atx();
   }

   private Boolean isRenameVisible() {
      return (Boolean)this.cleaner.atx();
   }

   private Boolean isSortDelayVisible() {
      boolean flag;
      if (!(Boolean)this.sort.atx() && !(Boolean)this.stack.atx()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }
}
