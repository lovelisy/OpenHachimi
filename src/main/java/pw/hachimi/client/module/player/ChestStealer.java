package pw.hachimi.client.module.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.model.player.ChestStealerEarlyLateMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.StackPotionContentsComponentManager;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.ItemListSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.player.UnidentifiedUtils6;
import pw.hachimi.eventbus.annotation.EventListener;

public class ChestStealer extends ClientModule {
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting kit = this.registerSetting(new BooleanSetting("Kit", "Stealing items according to the kit", false));
   Setting sort = this.registerSetting(new EnumSetting("Sort", "Sort your inventory", ChestStealerEarlyLateMode.None, ChestStealerEarlyLateMode.values(), this::isSortVisible));
   Setting putIn = this.registerSetting(new BooleanSetting("PutIn", "Put items in chests according to the kit", true, this::isPutInVisible));
   Setting items = this.registerSetting(new ItemListSetting("Items", "Items to steal", Items.SHULKER_BOX));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "The item steal delay", 0.0F, 0.15F, 2.0F));
   Setting shift = this.registerSetting(new NumberSetting("Shift", "The item steal shift", 1, 5, 27));
   private static final int[] h = new int[]{8, 0, 35, 9};
   public static boolean i;

   public ChestStealer() {
      super("ChestStealer", "Steals items from chests", ModuleCategory.PLAYER);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() != EventPhase.PRE || !this.stopwatch.afq(((Float)this.delay.atx()).floatValue() * 1000.0F)) {
         return;
      }

      int n = this.Ks(client.player.currentScreenHandler);
      if (n == 0) {
         return;
      }

      if (!(Boolean)this.kit.atx()) {
         int n2 = 0;

         for (int n3 = 0; n3 < n && n2 < ((Integer)this.shift.atx()).intValue(); n3++) {
            Slot slot2 = client.player.currentScreenHandler.getSlot(n3);
            if (slot2.hasStack() && this.Kx(slot2.getStack().getItem())) {
               this.Kt(client.player.currentScreenHandler.syncId, slot2);
               this.stopwatch.afr(29432, 5265492, (byte)40);
               n2++;
            }
         }

         return;
      }

      HashMap hashMap = new HashMap();
      Kr(n, hashMap);
      ScreenHandler screenHandler = client.player.currentScreenHandler;
      int n4 = screenHandler.getStacks().size();
      List list = screenHandler.slots;
      int n5 = ((Integer)this.shift.atx()).intValue();
      boolean flag = (Boolean)this.putIn.atx();
      ItemStack[] stacks = new ItemStack[36];
      UnidentifiedUtils6[] unidentifiedUtils6s = new UnidentifiedUtils6[36];
      for (int n6 = 0; n6 < 36; n6++) {
         ItemStack stack = client.player.getInventory().getStack(n6);
         stacks[n6] = stack;
         unidentifiedUtils6s[n6] = stack.isEmpty() ? null : StackPotionContentsComponentManager.Pb(stack);
      }

      int n7 = 0;
      if (this.sort.atx() == ChestStealerEarlyLateMode.Early) {
         n7 = this.Ky(n7, n5, n, stacks, unidentifiedUtils6s);
         if (n7 >= n5) {
            return;
         }
      }

      boolean flag2;
      do {
         flag2 = false;
         boolean flag3 = false;
         for (int n8 = 0; n8 < n; n8++) {
            if (n7 >= n5) {
               return;
            }

            Slot slot3 = (Slot)list.get(n8);
            if (!slot3.hasStack()) {
               continue;
            }

            ItemStack stack2 = slot3.getStack();
            UnidentifiedUtils6 unidentifiedUtils62 = StackPotionContentsComponentManager.Pb(stack2);
            if (unidentifiedUtils62 == null) {
               continue;
            }

            int n9 = ((Integer)hashMap.getOrDefault(unidentifiedUtils62, Integer.valueOf(0))).intValue();
            if (n9 <= 0) {
               continue;
            }

            int n10 = this.Ku(screenHandler.syncId, slot3, n9, n4, stack2);
            if (n10 > 0) {
               n7++;
               hashMap.put(unidentifiedUtils62, Integer.valueOf(n9 - n10));
            } else {
               flag3 = true;
            }
         }

         if (flag3 && flag) {
            for (int n11 = n; n11 < list.size(); n11++) {
               if (n7 >= n5) {
                  return;
               }

               Slot slot4 = (Slot)list.get(n11);
               if (!slot4.hasStack()) {
                  continue;
               }

               ItemStack stack3 = slot4.getStack();
               if (stack3.getItem() instanceof BlockItem && ((BlockItem)stack3.getItem()).getBlock() instanceof ShulkerBoxBlock) {
                  continue;
               }

               if (Kp(slot4, StackPotionContentsComponentManager.Pd(stack3), n)) {
                  n7++;
                  flag2 = true;
                  break;
               }
            }
         }
      } while (flag2);

      if (this.sort.atx() == ChestStealerEarlyLateMode.Late) {
         this.Ky(n7, n5, n, stacks, unidentifiedUtils6s);
      }
   }

   private int Ky(int n, int n2, int n3, ItemStack[] stacks, UnidentifiedUtils6[] unidentifiedUtils6s) {
      for (int n4 = 0; n4 < 36; n4++) {
         if (n >= n2) {
            return n;
         }

         UnidentifiedUtils6 unidentifiedUtils6 = (UnidentifiedUtils6)StackPotionContentsComponentManager.a_.get(Integer.valueOf(n4));
         UnidentifiedUtils6 unidentifiedUtils62 = unidentifiedUtils6s[n4];
         if (unidentifiedUtils6 == null) {
            continue;
         }

         if (unidentifiedUtils62 != null && unidentifiedUtils6.text.equals(unidentifiedUtils62.text)) {
            continue;
         }

         for (int n5 = 0; n5 < 36; n5++) {
            if (stacks[n5].isEmpty()) {
               continue;
            }

            UnidentifiedUtils6 unidentifiedUtils63 = unidentifiedUtils6s[n5];
            if (unidentifiedUtils63 == null) {
               continue;
            }

            UnidentifiedUtils6 unidentifiedUtils64 = (UnidentifiedUtils6)StackPotionContentsComponentManager.a_.get(Integer.valueOf(n5));
            if (unidentifiedUtils64 != null && unidentifiedUtils64.text.equals(unidentifiedUtils63.text)) {
               continue;
            }

            if (unidentifiedUtils6.text.equals(unidentifiedUtils63.text)) {
               Kv(n3, n4, n5);
               this.stopwatch.afr(29432, 5265492, (byte)40);
               n++;
               break;
            }
         }
      }

      return n;
   }

   private static boolean Kp(Slot slot2, int n, int n2) {
      if (Kq(slot2) > n) {
         for (int n3 = 0; n3 < n2; n3++) {
            int n4 = n3;
            Slot slot3 = client.player.currentScreenHandler.getSlot(n4);
            if (!slot3.hasStack()) {
               ClientPlayerEntity player2 = client.player;
               SlotActionType slotActionType = SlotActionType.QUICK_MOVE;
               int n5 = slot2.id;
               int n6 = client.player.currentScreenHandler.syncId;
               client.interactionManager.clickSlot(n6, n5, 0, slotActionType, (PlayerEntity)player2);
               return true;
            }

            ItemStack stack = slot3.getStack();
            ItemStack temp = stack;
            ItemStack stack2 = slot2.getStack();
            if (ItemStack.areItemsAndComponentsEqual(temp, stack2) && stack.getCount() < stack.getMaxCount()) {
               ClientPlayerEntity player3 = client.player;
               SlotActionType slotActionType2 = SlotActionType.QUICK_MOVE;
               int n7 = slot2.id;
               int n8 = client.player.currentScreenHandler.syncId;
               client.interactionManager.clickSlot(n8, n7, 0, slotActionType2, (PlayerEntity)player3);
               return true;
            }
         }
      }

      return false;
   }

   private static int Kq(Slot slot2) {
      int n = 0;

      for (int n2 = 0; n2 < client.player.playerScreenHandler.slots.size(); n2++) {
         int n3 = n2;
         Slot slot3 = client.player.playerScreenHandler.getSlot(n3);
         if (slot3.hasStack()) {
            ItemStack temp = slot2.getStack();
            ItemStack stack = slot3.getStack();
            if (ItemStack.areItemsAndComponentsEqual(temp, stack)) {
               n++;
            }
         }
      }

      return n;
   }

   private static void Kr(int n, Map map) {
      HashMap hashMap = new HashMap();
      Iterator iterator2 = StackPotionContentsComponentManager.a_.values().iterator();
      int n2;
      HashMap hashMap2;
      if (iterator2.hasNext()) {
         while (true) {
            UnidentifiedUtils6 unidentifiedUtils6 = (UnidentifiedUtils6)iterator2.next();
            HashMap temp = hashMap;
            String temp2 = unidentifiedUtils6.text;
            Integer temp3 = unidentifiedUtils6.b;
            BiFunction biFunction = new BiFunction() {
               @Override
               public Object apply(Object map, Object obj) {
                  return new Integer(Integer.sum(((Integer)map).intValue(), ((Integer)obj).intValue()));
               }
            };
            Integer integer = temp3;
            String str = temp2;
            temp.merge(str, integer, biFunction);
            if (!iterator2.hasNext()) {
               hashMap2 = new HashMap();
               n2 = 0;
               ItemStack stack = client.player.getInventory().getStack(0);
               UnidentifiedUtils6 unidentifiedUtils62 = StackPotionContentsComponentManager.Pb(stack);
               if (unidentifiedUtils62 != null) {
                  temp = hashMap2;
                  temp2 = unidentifiedUtils62.text;
                  temp3 = stack.getCount();
                  BiFunction biFunction2 = new BiFunction() {
                     @Override
                     public Object apply(Object map, Object obj) {
                        return new Integer(Integer.sum(((Integer)map).intValue(), ((Integer)obj).intValue()));
                     }
                  };
                  Integer integer2 = temp3;
                  String str2 = temp2;
                  temp.merge(str2, integer2, biFunction2);
               }

               n2++;
               break;
            }
         }
      } else {
         hashMap2 = new HashMap();
         n2 = 0;
      }

      for (; n2 < 41; n2++) {
         PlayerInventory playerInventory = client.player.getInventory();
         int n3 = n2;
         ItemStack stack2 = playerInventory.getStack(n3);
         UnidentifiedUtils6 unidentifiedUtils63 = StackPotionContentsComponentManager.Pb(stack2);
         if (unidentifiedUtils63 != null) {
            HashMap hashMap3 = hashMap2;
            String str3 = unidentifiedUtils63.text;
            Integer integer3 = stack2.getCount();
            BiFunction biFunction3 = new BiFunction() {
               @Override
               public Object apply(Object map, Object obj) {
                  return new Integer(Integer.sum(((Integer)map).intValue(), ((Integer)obj).intValue()));
               }
            };
            Integer integer4 = integer3;
            String str4 = str3;
            hashMap3.merge(str4, integer4, biFunction3);
         }
      }

      for (int n4 = 0; n4 < n; n4++) {
         int n5 = n4;
         Slot slot2 = client.player.currentScreenHandler.getSlot(n5);
         if (slot2.hasStack()) {
            UnidentifiedUtils6 unidentifiedUtils64 = StackPotionContentsComponentManager.Pb(slot2.getStack());
            if (unidentifiedUtils64 != null && !map.containsKey(unidentifiedUtils64)) {
               HashMap hashMap4 = hashMap;
               String str5 = unidentifiedUtils64.text;
               Integer integer5 = 0;
               String str6 = str5;
               int n6 = (Integer)hashMap4.getOrDefault(str6, integer5);
               hashMap4 = hashMap2;
               str5 = unidentifiedUtils64.text;
               Integer integer6 = 0;
               String str7 = str5;
               int n7 = (Integer)hashMap4.getOrDefault(str7, integer6);
               int n8 = n6 - n7;
               Integer integer7 = n8;
               map.put(unidentifiedUtils64, integer7);
            }
         }
      }
   }

   private int Ks(ScreenHandler screenHandler) {
      if (screenHandler instanceof GenericContainerScreenHandler) {
         return ((GenericContainerScreenHandler)screenHandler).getInventory().size();
      } else {
         return screenHandler instanceof ShulkerBoxScreenHandler ? 27 : 0;
      }
   }

   private void Kt(int n, Slot slot2) {
      ClientPlayerEntity player2 = client.player;
      SlotActionType slotActionType = SlotActionType.QUICK_MOVE;
      int n2 = slot2.id;
      client.interactionManager.clickSlot(n, n2, 0, slotActionType, (PlayerEntity)player2);
   }

   private int Ku(int n, Slot slot2, int n2, int n3, ItemStack stack) {
      int n4;
      int n5;
      int n6;
      int n7;
      boolean flag;
      PlayerInventory playerInventory;
      int n8;
      int n9;
      int n10;
      byte b2;
      int n11;
      ItemStack stack2;
      int n12;
      int n13;
      int n14;
      byte b3;
      int n15;
      int n16;
      int n17;
      label273: {
         label272: {
            label271: {
               label287: {
                  label288: {
                     label276: {
                        label277: {
                           n12 = 0;
                           n13 = 0;
                           n14 = 0;
                           n4 = stack.getMaxCount();
                           n5 = stack.getCount();
                           n6 = n5;
                           n7 = -1;
                           flag = false;
                           playerInventory = client.player.getInventory();
                           n8 = 0;
                           int temp = h.length;
                           n9 = h[0];
                           n10 = h[0 + 1];
                           b3 = -1;
                           b2 = -1;
                           n11 = n9;
                           stack2 = playerInventory.getStack(n9);
                           if (stack2.isEmpty()) {
                              n7 = n9;
                              n11 = n9 + -1;
                              stack2 = playerInventory.getStack(n11);
                              if (!stack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                                 n12 = stack2.getMaxCount();
                                 n13 = stack2.getCount();
                                 if (n13 < n12) {
                                    n14 = n12 - n13;
                                    n6 = n5 - n14;
                                    if (n6 <= 0) {
                                       break label276;
                                    }
                                 }
                              }
                           } else {
                              if (!ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                                 break label287;
                              }

                              n12 = stack2.getMaxCount();
                              n13 = stack2.getCount();
                              if (n13 >= n12) {
                                 break label272;
                              }

                              n14 = n12 - n13;
                              n6 = n5 - n14;
                              flag = true;
                              if (n6 <= 0) {
                                 break label288;
                              }

                              n11 = n9 + -1;
                              stack2 = playerInventory.getStack(n11);
                              if (!stack2.isEmpty()) {
                                 if (!ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                                    break label287;
                                 }

                                 n12 = stack2.getMaxCount();
                                 n13 = stack2.getCount();
                                 if (n13 >= n12) {
                                    break label272;
                                 }

                                 n14 = n12 - n13;
                                 n6 -= n14;
                                 flag = true;
                                 if (n6 <= 0) {
                                    break label288;
                                 }

                                 n11 += -1;
                                 stack2 = playerInventory.getStack(n11);
                                 if (!stack2.isEmpty()) {
                                    break label271;
                                 }

                                 n7 = n11;
                                 flag = true;
                                 n11 += -1;
                                 stack2 = playerInventory.getStack(n11);
                                 if (!stack2.isEmpty()) {
                                    break label271;
                                 }

                                 n11 += -1;
                                 stack2 = playerInventory.getStack(n11);
                                 if (!stack2.isEmpty()) {
                                    break label271;
                                 }

                                 n11 += -1;
                                 stack2 = playerInventory.getStack(n11);
                                 if (!stack2.isEmpty()) {
                                    break label271;
                                 }

                                 n11 += -1;
                                 stack2 = playerInventory.getStack(n11);
                                 if (!stack2.isEmpty()) {
                                    break label271;
                                 }

                                 n11 += -1;
                                 stack2 = playerInventory.getStack(n11);
                                 if (!stack2.isEmpty()) {
                                    n16 = 1832350603;
                                    int n18 = (1708348543);
                                    n15 = 1770607374
                                       + -1693832214 * ~(~-880499413 | ~n18)
                                       + 1248624586 * ~(-880499413 | n18)
                                       + 941527888 * ((n18 | n18 | n18 | n18) ^ (~-880499413 | n18))
                                       + -143784462 * ~((-880499413 ^ n18) & (-880499413 ^ n18))
                                       + -1132568126 * (((-880499413)) & n18 & -880499413 | ~(n18 & n18))
                                       + (-1347268562)
                                       + 1188334447 * n18
                                       + -844114154 * ~(-880499413 & n18 | n18 | -880499413)
                                       + (-699807440)
                                       + -1118648714 * (n18 | n18 & ~-880499413)
                                       + 1767930942 * (n18 & -880499413 & n18 & n18 | -880499413 & n18 & n18)
                                       + (2134041825);
                                    n17 = 0;
                                    break label273;
                                 }
                                 break label277;
                              }

                              n7 = n11;
                           }

                           n11 += -1;
                           stack2 = playerInventory.getStack(n11);
                           if (!stack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                              n12 = stack2.getMaxCount();
                              n13 = stack2.getCount();
                              if (n13 < n12) {
                                 n14 = n12 - n13;
                                 n6 -= n14;
                                 if (n6 <= 0) {
                                    break label276;
                                 }
                              }
                           }

                           n11 += -1;
                           stack2 = playerInventory.getStack(n11);
                           if (!stack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                              n12 = stack2.getMaxCount();
                              n13 = stack2.getCount();
                              if (n13 < n12) {
                                 n14 = n12 - n13;
                                 n6 -= n14;
                                 if (n6 <= 0) {
                                    break label276;
                                 }
                              }
                           }

                           n11 += -1;
                           stack2 = playerInventory.getStack(n11);
                           if (!stack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                              n12 = stack2.getMaxCount();
                              n13 = stack2.getCount();
                              if (n13 < n12) {
                                 n14 = n12 - n13;
                                 n6 -= n14;
                                 if (n6 <= 0) {
                                    break label276;
                                 }
                              }
                           }

                           n11 += -1;
                           stack2 = playerInventory.getStack(n11);
                           if (!stack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                              n12 = stack2.getMaxCount();
                              n13 = stack2.getCount();
                              if (n13 < n12) {
                                 n14 = n12 - n13;
                                 n6 -= n14;
                                 if (n6 <= 0) {
                                    break label276;
                                 }
                              }
                           }

                           n11 += -1;
                           stack2 = playerInventory.getStack(n11);
                           if (!stack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                              n12 = stack2.getMaxCount();
                              n13 = stack2.getCount();
                              if (n13 < n12) {
                                 n14 = n12 - n13;
                                 n6 -= n14;
                                 if (n6 <= 0) {
                                    break label276;
                                 }
                              }
                           }

                           n11 += -1;
                           stack2 = playerInventory.getStack(n11);
                           if (!stack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                              n12 = stack2.getMaxCount();
                              n13 = stack2.getCount();
                              if (n13 < n12) {
                                 n14 = n12 - n13;
                                 n6 -= n14;
                                 if (n6 <= 0) {
                                    break label276;
                                 }
                              }
                           }
                        }

                        n11 += -1;
                        stack2 = playerInventory.getStack(n11);
                        if (!stack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                           n12 = stack2.getMaxCount();
                           n13 = stack2.getCount();
                           if (n13 < n12) {
                              n14 = n12 - n13;
                              n6 -= n14;
                              if (n6 <= 0) {
                                 break label276;
                              }
                           }
                        }

                        label201:
                        while (true) {
                           n8 += 2;
                           if (n8 >= h.length) {
                              break;
                           }

                           n9 = h[n8];
                           n10 = h[n8 + 1];
                           if (n9 > n10) {
                              b3 = -1;
                           } else {
                              b3 = 1;
                           }

                           b2 = b3;

                           for (int n19 = n9; n19 != n10 + b2; n19 += b2) {
                              PlayerInventory playerInventory2 = playerInventory;
                              int n20 = n19;
                              stack2 = playerInventory2.getStack(n20);
                              if (!stack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                                 n12 = stack2.getMaxCount();
                                 n13 = stack2.getCount();
                                 if (n13 < n12) {
                                    n14 = n12 - n13;
                                    n6 -= n14;
                                    if (n6 <= 0) {
                                       break label201;
                                    }
                                 }
                              }
                           }
                        }
                     }

                     ClientPlayerEntity player2 = client.player;
                     SlotActionType slotActionType = SlotActionType.QUICK_MOVE;
                     int n21 = slot2.id;
                     client.interactionManager.clickSlot(n, n21, 0, slotActionType, (PlayerEntity)player2);
                     if (n2 < n4 && n6 > 0 && n5 > n2) {
                        n8 = Kw(n3, n7) - 36;
                        ClientPlayerEntity player3 = client.player;
                        SlotActionType slotActionType2 = SlotActionType.QUICK_MOVE;
                        client.interactionManager.clickSlot(n, n8, 0, slotActionType2, (PlayerEntity)player3);
                     }

                     return n5;
                  }

                  ClientPlayerEntity player4 = client.player;
                  SlotActionType slotActionType3 = SlotActionType.QUICK_MOVE;
                  int n22 = slot2.id;
                  client.interactionManager.clickSlot(n, n22, 0, slotActionType3, (PlayerEntity)player4);
                  if (n2 < n4 && n6 > 0 && n5 > n2) {
                  }

                  return n5;
               }

               n15 = 1872346888;
               n17 = -1909193605;
               n16 = 1758443437;
               break label273;
            }

            n15 = 1784773422;
            n17 = -880499413;
            n16 = 1832350603;
            break label273;
         }

         n15 = -1797846654;
         n17 = -730123719;
         n16 = 1758443437;
      }

      while (true) {
         int temp2 = n16;
         if (n17 == 0) {
            n16 *= 1184802991;
         }

         switch (n15 ^ temp2) {
            case -1891627375:
               flag = true;
               n15 = -683316596;
               n17 = 0;
               n16 = 1758443437;
               break;
            case -1681669833:
               if (n2 < n4) {
                  if (n6 > 0) {
                     if (n5 > n2) {
                        if (n7 != -1) {
                           int n23 = n16 * -1265029889;
                           n15 = -2139122384;
                           n17 = -716693103;
                           n16 = n23;
                        } else {
                           n15 = 1128847626;
                           n17 = 0;
                           n16 = 1933618549;
                        }
                     } else {
                        n15 = 1128847626;
                        n17 = 0;
                        n16 = 1933618549;
                     }
                  } else {
                     n15 = 1128847626;
                     n17 = 0;
                     n16 = 1933618549;
                  }
               } else {
                  n15 = 1128847626;
                  n17 = 0;
                  n16 = 1933618549;
               }
               break;
            case -1435283205:
               n8 = Kw(n3, n7) - 36;
               int n24 = n16 * 585003145;
               n15 = -1737534954;
               n17 = -1962847539;
               n16 = n24;
               break;
            case -1330370399:
               n10 = h[n8 + 1];
               int n25 = n16 * 1390412519;
               n15 = -1515763792;
               n17 = -2004722769;
               n16 = n25;
               break;
            case -1088704837:
               playerInventory = client.player.getInventory();
               int n26 = n16 * 1060458069;
               n15 = 524900592;
               n17 = -231452835;
               n16 = n26;
               break;
            case -1081418463:
               n11 += b2;
               n15 = -1453838510;
               n17 = 0;
               n16 = 1363267729;
               break;
            case -867527713:
               if (flag) {
                  int n27 = n16 * -332547795;
                  n15 = 1984776780;
                  n17 = 893420519;
                  n16 = n27;
               } else {
                  n15 = 1657741342;
                  n17 = 0;
                  n16 = 1624880051;
               }
               break;
            case -815422139:
               n8 += 2;
               n15 = 1401290274;
               n17 = 0;
               n16 = 1930101499;
               break;
            case -794832413:
               flag = true;
               int n28 = n16 * -491905479;
               n15 = 1809295994;
               n17 = -514805119;
               n16 = n28;
               break;
            case -734992683:
               n12 = stack2.getMaxCount();
               int n29 = n16 * -52533899;
               n15 = 490347974;
               n17 = 1549297197;
               n16 = n29;
               break;
            case -578123683:
               n15 = -2010175940;
               n17 = 0;
               n16 = 1147516387;
               break;
            case -454139965:
               n7 = n11;
               int n30 = n16 * 1895981243;
               n15 = 37416080;
               n17 = 1145528623;
               n16 = n30;
               break;
            case -395578683:
               n13 = stack2.getCount();
               int n31 = n16 * -1083101741;
               n15 = -1448153424;
               n17 = 1762948955;
               n16 = n31;
               break;
            case -344636447:
               flag = false;
               int n32 = n16 * -1933551547;
               n15 = 530424394;
               n17 = 1781711775;
               n16 = n32;
               break;
            case -231112957:
               b2 = b3;
               int n33 = n16 * 1423248087;
               n15 = 1882123398;
               n17 = 153693081;
               n16 = n33;
               break;
            case -192841641:
               if (ItemStack.areItemsAndComponentsEqual(stack2, stack)) {
                  int n34 = n16 * 1053130813;
                  n15 = 522830332;
                  n17 = 44248631;
                  n16 = n34;
               } else {
                  n15 = -683316596;
                  n17 = 0;
                  n16 = 1758443437;
               }
               break;
            case -167995291:
               n5 = stack.getCount();
               int n35 = n16 * 102806285;
               n15 = -1077965908;
               n17 = 1113052027;
               n16 = n35;
               break;
            case -132513853:
               if (n11 != n10 + b2) {
                  int n36 = n16 * 1534093679;
                  n15 = -1732352162;
                  n17 = -1990437219;
                  n16 = n36;
               } else {
                  n15 = -1557288452;
                  n17 = 0;
                  n16 = 1816661177;
               }
               break;
            case -65487313:
               int n37 = (1896218452);
               n15 = (835099935 + (n17 ^ n37));
               n17 = 0;
               break;
            case 35034029:
               return 0;
            case 56861529:
               if (n6 <= 0) {
                  int n38 = n16 * -516710979;
                  n15 = 1951342280;
                  n17 = -1494820905;
                  n16 = n38;
               } else {
                  n15 = -683316596;
                  n17 = 0;
                  n16 = 1758443437;
               }
               break;
            case 59842199:
               n9 = h[n8];
               int n39 = n16 * 1859980173;
               n15 = 832893066;
               n17 = 1217992501;
               n16 = n39;
               break;
            case 123078821:
               int n40 = (1708348543);
               n15 = (-346154360 + (n17 ^ n40));
               n17 = 0;
               break;
            case 130340931:
               n8 = 0;
               int n41 = n16 * -1056331367;
               n15 = 1952254558;
               n17 = 234141669;
               n16 = n41;
               break;
            case 364016959:
               if (stack2.isEmpty()) {
                  int n42 = n16 * -1454961733;
                  n15 = 175562838;
                  n17 = -368988601;
                  n16 = n42;
               } else {
                  n15 = -1716122660;
                  n17 = 0;
                  n16 = 1832350603;
               }
               break;
            case 477001137:
               if (n13 < n12) {
                  int n43 = n16 * 789206345;
                  n15 = 641478580;
                  n17 = -940204315;
                  n16 = n43;
               } else {
                  n15 = -683316596;
                  n17 = 0;
                  n16 = 1758443437;
               }
               break;
            case 529558563:
               b3 = 1;
               int n44 = n16 * 419585413;
               n15 = -1745989932;
               n17 = 420285837;
               n16 = n44;
               break;
            case 546241753:
               if (n8 < h.length) {
                  int n45 = n16 * -1777522599;
                  n15 = 696365056;
                  n17 = -308763207;
                  n16 = n45;
               } else {
                  n15 = -2010175940;
                  n17 = 0;
                  n16 = 1147516387;
               }
               break;
            case 650116301:
               n6 = n5;
               int n46 = n16 * -1044935981;
               n15 = 1656214162;
               n17 = 378517797;
               n16 = n46;
               break;
            case 718096367:
               if (n7 == -1) {
                  int n47 = n16 * -534213083;
                  n15 = 1582510796;
                  n17 = -1218568355;
                  n16 = n47;
               } else {
                  n15 = -683316596;
                  n17 = 0;
                  n16 = 1758443437;
               }
               break;
            case 720599151:
               n14 = n12 - n13;
               int n48 = n16 * 495495737;
               n15 = -1808987248;
               n17 = -2018086173;
               n16 = n48;
               break;
            case 790658637:
               n11 = n9;
               int n49 = n16 * 1474742965;
               n15 = 334558300;
               n17 = 584309409;
               n16 = n49;
               break;
            case 805860479:
               return n5;
            case 995810773:
               n7 = -1;
               int n50 = n16 * -2064280099;
               n15 = -1237723590;
               n17 = 512258433;
               n16 = n50;
               break;
            case 1078959977:
               n6 -= n14;
               int n51 = n16 * -1407814327;
               n15 = -1789831818;
               n17 = -116759857;
               n16 = n51;
               break;
            case 1118905549:
               int n52 = (-1698543414);
               n15 = (-247030553 + (n17 ^ n52));
               n17 = 0;
               break;
            case 1256894665:
               ClientPlayerEntity player5 = client.player;
               SlotActionType slotActionType4 = SlotActionType.QUICK_MOVE;
               int n53 = slot2.id;
               client.interactionManager.clickSlot(n, n53, 0, slotActionType4, (PlayerEntity)player5);
               int n54 = n16 * 739134873;
               n15 = 1316629194;
               n17 = -2109563729;
               n16 = n54;
               break;
            case 1317445529:
               PlayerInventory playerInventory3 = playerInventory;
               int n55 = n11;
               stack2 = playerInventory3.getStack(n55);
               int n56 = n16 * 306199697;
               n15 = 1143372554;
               n17 = 246462583;
               n16 = n56;
               break;
            case 1337763457:
               ClientPlayerEntity player6 = client.player;
               SlotActionType slotActionType5 = SlotActionType.QUICK_MOVE;
               int n57 = n8;
               client.interactionManager.clickSlot(n, n57, 0, slotActionType5, (PlayerEntity)player6);
               int n58 = n16 * 1620274163;
               n15 = 837913016;
               n17 = -926911255;
               n16 = n58;
               break;
            case 1928444805:
               if (n9 > n10) {
                  b3 = -1;
                  n15 = -1714450440;
                  n17 = 0;
                  n16 = 1811345659;
               } else {
                  n15 = 1939501330;
                  n17 = 0;
                  n16 = 1812602161;
               }
               break;
            case 2022194127:
               n4 = stack.getMaxCount();
               int n59 = n16 * -1066835509;
               n15 = -1764518450;
               n17 = 477423921;
               n16 = n59;
               break;
            default:
               return 0;
         }
      }
   }

   private static void Kv(int n, int n2, int n3) {
      int n4 = client.player.currentScreenHandler.syncId;
      int n5 = Kw(n, n2);
      int n6 = Kw(n, n3);
      ClientPlayerEntity player2 = client.player;
      SlotActionType slotActionType = SlotActionType.PICKUP;
      client.interactionManager.clickSlot(n4, n5, 0, slotActionType, (PlayerEntity)player2);
      ClientPlayerEntity player3 = client.player;
      SlotActionType slotActionType2 = SlotActionType.PICKUP;
      client.interactionManager.clickSlot(n4, n6, 0, slotActionType2, (PlayerEntity)player3);
      ClientPlayerEntity player4 = client.player;
      SlotActionType slotActionType3 = SlotActionType.PICKUP;
      client.interactionManager.clickSlot(n4, n5, 0, slotActionType3, (PlayerEntity)player4);
   }

   private static int Kw(int n, int n2) {
      int n3;
      if (n2 < 9) {
         n3 = n2 + 27;
      } else {
         n3 = n2 - 9;
      }

      return n + n3;
   }

   private boolean Kx(Item item2) {
      return ((ItemListSetting)this.items).al(item2);
   }

   private Boolean isPutInVisible() {
      return (Boolean)this.kit.atx();
   }

   private Boolean isSortVisible() {
      return (Boolean)this.kit.atx();
   }
}
