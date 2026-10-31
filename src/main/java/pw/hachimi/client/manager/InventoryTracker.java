package pw.hachimi.client.manager;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.event.player.SyncSelectedSlotEvent;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.event.player.UseEvent;
import pw.hachimi.client.mixin.BundlePacketMixin;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.module.player.Replenish;
import pw.hachimi.client.utils.UnidentifiedHelper;
import pw.hachimi.client.utils.player.InventoryTrackerStackHelper;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class InventoryTracker {
   private final List list = new CopyOnWriteArrayList();
   private int b = -1;

   public InventoryTracker() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onOnGameJoin(OnGameJoinEvent onGameJoinEvent) {
      this.JE();
   }

   @EventListener
   public void onSyncSelectedSlot(SyncSelectedSlotEvent syncSelectedSlotEvent) {
      if (BlockPosBoxMultitask.alv().awg() && BlockPosBoxMultitask.alv().alx()) {
         syncSelectedSlotEvent.aim();
      }
   }

   @EventListener
   public void Jy(UnidentifiedHelper unidentifiedHelper) {
      if (!BlockPosBoxMultitask.alv().awg() || !BlockPosBoxMultitask.alv().alx()) {
         if (Mc.client.player != null) {
            if (this.b != Mc.client.player.getInventory().selectedSlot) {
               this.JH(Mc.client.player.getInventory().selectedSlot);
               ClientPlayerInteractionManager temp = Mc.client.interactionManager;
               int n = Mc.client.player.getInventory().selectedSlot;
               temp.lastSelectedSlot = n;
            }
         }
      }
   }

   @EventListener(priority = -9999)
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      Packet packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof UpdateSelectedSlotC2SPacket) {
         int n = ((UpdateSelectedSlotC2SPacket)packet2).getSelectedSlot();
         if (!PlayerInventory.isValidHotbarIndex(n) || this.b == n && HitResultWorldHelper.Sq().SI()) {
            sendImmediatelyEvent.aim();
            return;
         }

         this.b = n;
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (!Replenish.ajo() && HitResultWorldHelper.Sq().SI()) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof BundleS2CPacket) {
            BundleS2CPacket bundleS2CPacket = (BundleS2CPacket)packet2;
            ArrayList arrayList = new ArrayList();
            Iterator iterator2 = bundleS2CPacket.getPackets().iterator();

            while (iterator2.hasNext()) {
               Packet packet3 = (Packet)iterator2.next();
               if (!(packet3 instanceof ScreenHandlerSlotUpdateS2CPacket)) {
                  arrayList.add(packet3);
               }
            }

            ((BundlePacketMixin)bundleS2CPacket).setIterable(arrayList);
         }

         if (Mc.client.player != null) {
            packet2 = channelRead0Event.getPacket();
            if (packet2 instanceof ScreenHandlerSlotUpdateS2CPacket
               && Mc.client.player.currentScreenHandler.syncId == Mc.client.player.playerScreenHandler.syncId) {
               int n = ((ScreenHandlerSlotUpdateS2CPacket)packet2).getSlot() - 36;
               if (n < 0 || n > 8) {
                  return;
               }

               if (((ScreenHandlerSlotUpdateS2CPacket)packet2).getStack().isEmpty()) {
                  return;
               }

               Iterator iterator3 = this.list.iterator();

               while (iterator3.hasNext()) {
                  InventoryTrackerStackHelper inventoryTrackerStackHelper = (InventoryTrackerStackHelper)iterator3.next();
                  if (inventoryTrackerStackHelper.QZ() == n || inventoryTrackerStackHelper.QY() == n) {
                     ItemStack stack = inventoryTrackerStackHelper.QX(n);
                     if (stack.isEmpty()) {
                        if (n == inventoryTrackerStackHelper.QY() && this.JX(((ScreenHandlerSlotUpdateS2CPacket)packet2).getStack(), inventoryTrackerStackHelper.QX(inventoryTrackerStackHelper.QZ()))) {
                           channelRead0Event.aim();
                           break;
                        }
                     } else if (!this.JX(stack, ((ScreenHandlerSlotUpdateS2CPacket)packet2).getStack())) {
                        channelRead0Event.aim();
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void onUse(UseEvent useEvent) {
      if (Mc.client.player != null) {
         if (this.JJ()) {
            ItemStack stack = this.JV();
            if (stack != null) {
               useEvent.aim();
               useEvent.setStack(stack);
            }
         }
      }
   }

   @EventListener
   public void JC(UpdateDeathEvent updateDeathEvent) {
      if (updateDeathEvent.getLiving() == Mc.client.player) {
         this.JI();
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      List temp = this.list;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object tickPreEvent) {
            return ((InventoryTrackerStackHelper)tickPreEvent).QW();
         }
      };
      temp.removeIf(predicate);
   }

   public void JE() {
      this.b = -1;
   }

   public void JF(int n) {
      int n2 = this.b;
      if (n2 != n && PlayerInventory.isValidHotbarIndex(n)) {
         this.JH(n);
         ItemStack[] stacks = new ItemStack[9];
         int n3 = 0;

         do {
            ItemStack[] temp = stacks;
            PlayerInventory temp2 = Mc.client.player.getInventory();
            int n4 = n3;
            temp[n3] = temp2.getStack(n4);
         } while (++n3 < 9);

         List list2 = this.list;
         InventoryTrackerStackHelper inventoryTrackerStackHelper = new InventoryTrackerStackHelper(stacks, n2, n);
         list2.add(inventoryTrackerStackHelper);
      }
   }

   public void JG(int n) {
      if (PlayerInventory.isValidHotbarIndex(n)) {
         int temp = n + 36;
         ClientPlayerEntity player2 = Mc.client.player;
         SlotActionType slotActionType = SlotActionType.SWAP;
         int n2 = this.b;
         int n3 = temp;
         int n4 = Mc.client.player.playerScreenHandler.syncId;
         Mc.client.interactionManager.clickSlot(n4, n3, n2, slotActionType, (PlayerEntity)player2);
      }
   }

   public void JH(int n) {
      Managers.serverManager.iO('\uf560', new UpdateSelectedSlotC2SPacket(n));
   }

   public void JI() {
      if (this.JJ()) {
         this.JF(Mc.client.player.getInventory().selectedSlot);
      }
   }

   public boolean JJ() {
      boolean flag;
      if (Mc.client.player.getInventory().selectedSlot != this.b) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public void JK(int n, int n2) {
      if (n < 9) {
         n += 36;
      } else if (n == 40) {
         n = 45;
      }

      ClientPlayerEntity player2 = Mc.client.player;
      SlotActionType slotActionType = SlotActionType.SWAP;
      Mc.client.interactionManager.clickSlot(0, n, n2, slotActionType, (PlayerEntity)player2);
   }

   public void JL() {
      Managers.serverManager.iO('\uf560', new CloseHandledScreenC2SPacket(Mc.client.player.currentScreenHandler.syncId));
   }

   public int JM(int n) {
      return this.JQ(n, SlotActionType.PICKUP);
   }

   public void JN(int n) {
      this.JQ(n, SlotActionType.QUICK_MOVE);
   }

   public void JO(int n) {
      this.JQ(n, SlotActionType.THROW);
   }

   public int JP() {
      for (int n = 9; n < 36; n++) {
         PlayerInventory temp = Mc.client.player.getInventory();
         int n2 = n;
         if (temp.getStack(n2).isEmpty()) {
            return n;
         }
      }

      return -1;
   }

   public int JQ(int n, SlotActionType slotActionType) {
      if (n < 0) {
         return -1;
      }

      ScreenHandler screenHandler = Mc.client.player.currentScreenHandler;
      DefaultedList defaultedList = screenHandler.slots;
      int n2 = defaultedList.size();
      ArrayList arrayList = Lists.newArrayListWithCapacity(n2);
      Iterator iterator2 = defaultedList.iterator();

      while (iterator2.hasNext()) {
         Slot slot2 = (Slot)iterator2.next();
         ArrayList temp = arrayList;
         ItemStack stack = slot2.getStack().copy();
         temp.add(stack);
      }

      ScreenHandler screenHandler2 = screenHandler;
      ClientPlayerEntity player2 = Mc.client.player;
      screenHandler2.onSlotClick(n, 0, slotActionType, (PlayerEntity)player2);
      Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();

      for (int n3 = 0; n3 < n2; n3++) {
         ArrayList arrayList2 = arrayList;
         int n4 = n3;
         ItemStack stack2 = (ItemStack)arrayList2.get(n4);
         DefaultedList defaultedList2 = defaultedList;
         int n5 = n3;
         ItemStack stack3;
         ItemStack stack4 = stack3 = ((Slot)defaultedList2.get(n5)).getStack();
         if (!ItemStack.areEqual(stack2, stack4)) {
            int2ObjectOpenHashMap.put(n3, stack3.copy());
         }
      }

      ClientPlayNetworkHandler network2 = Mc.client.player.networkHandler;
      ClickSlotC2SPacket clickSlotC2SPacket = new ClickSlotC2SPacket(screenHandler.syncId, screenHandler.getRevision(), n, 0, slotActionType, screenHandler.getCursorStack().copy(), int2ObjectOpenHashMap);
      network2.sendPacket((Packet)clickSlotC2SPacket);
      return screenHandler.getRevision();
   }

   public int JR(int n, int n2, SlotActionType slotActionType) {
      if (n < 0) {
         return -1;
      }

      ScreenHandler screenHandler = Mc.client.player.currentScreenHandler;
      DefaultedList defaultedList = screenHandler.slots;
      int n3 = defaultedList.size();
      ArrayList arrayList = Lists.newArrayListWithCapacity(n3);
      Iterator iterator2 = defaultedList.iterator();

      while (iterator2.hasNext()) {
         Slot slot2 = (Slot)iterator2.next();
         ArrayList temp = arrayList;
         ItemStack stack = slot2.getStack().copy();
         temp.add(stack);
      }

      Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();

      for (int n4 = 0; n4 < n3; n4++) {
         ArrayList arrayList2 = arrayList;
         int n5 = n4;
         ItemStack stack2 = (ItemStack)arrayList2.get(n5);
         DefaultedList defaultedList2 = defaultedList;
         int n6 = n4;
         ItemStack stack3;
         ItemStack stack4 = stack3 = ((Slot)defaultedList2.get(n6)).getStack();
         if (!ItemStack.areEqual(stack2, stack4)) {
            int2ObjectOpenHashMap.put(n4, stack3.copy());
         }
      }

      ClientPlayNetworkHandler network2 = Mc.client.player.networkHandler;
      ClickSlotC2SPacket clickSlotC2SPacket = new ClickSlotC2SPacket(screenHandler.syncId, screenHandler.getRevision(), n, n2, slotActionType, screenHandler.getCursorStack().copy(), int2ObjectOpenHashMap);
      network2.sendPacket((Packet)clickSlotC2SPacket);
      return screenHandler.getRevision();
   }

   public int JS() {
      int n;
      if (this.b != -1) {
         n = this.b;
      } else {
         n = Mc.client.player.getInventory().selectedSlot;
      }

      return n;
   }

   public int JT() {
      return Mc.client.player.getInventory().selectedSlot;
   }

   public void JU(int n) {
      if (Mc.client.player.getInventory().selectedSlot != n && PlayerInventory.isValidHotbarIndex(n)) {
         Mc.client.player.getInventory().selectedSlot = n;
         this.JH(n);
      }
   }

   public ItemStack JV() {
      if (Mc.client.player != null && this.JS() != -1) {
         PlayerInventory temp = Mc.client.player.getInventory();
         int n = this.JS();
         return temp.getStack(n);
      } else {
         return null;
      }
   }

   public ItemStack JW() {
      if (Mc.client.player != null && this.JS() != -1) {
         PlayerInventory temp = Mc.client.player.getInventory();
         int n = this.JS();
         return temp.getStack(n);
      } else {
         return Mc.client.player.getMainHandStack();
      }
   }

   private boolean JX(ItemStack stack, ItemStack stack2) {
      return ItemStack.areItemsAndComponentsEqual(stack, stack2);
   }
}
