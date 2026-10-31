package pw.hachimi.client.module.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.model.world.PacketTime;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class PacketLogger extends ClientModule {
   private final List list = new CopyOnWriteArrayList();
   Setting log = this.registerSetting(new BooleanSetting("Log", "Adds packets to the logs", false));
   Setting logChat = this.registerSetting(new BooleanSetting("LogChat", "Logs packets in the chats", false));
   Setting logDisconnect = this.registerSetting(new BooleanSetting("LogDisconnect", "Logs packets on client disconnect", true));
   Setting playerMoveFull = this.registerSetting(new BooleanSetting("PlayerMoveFull", "Logs PlayerMoveC2SPacket", false));
   Setting playerMoveLook = this.registerSetting(new BooleanSetting("PlayerMoveLook", "Logs PlayerMoveC2SPacket", false));
   Setting playerMovePosition = this.registerSetting(new BooleanSetting("PlayerMovePosition", "Logs PlayerMoveC2SPacket", false));
   Setting playerMoveGround = this.registerSetting(new BooleanSetting("PlayerMoveGround", "Logs PlayerMoveC2SPacket", false));
   Setting vehicleMove = this.registerSetting(new BooleanSetting("VehicleMove", "Logs VehicleMoveC2SPacket", false));
   Setting playerAction = this.registerSetting(new BooleanSetting("PlayerAction", "Logs PlayerActionC2SPacket", false));
   Setting updateSelectedSlot = this.registerSetting(new BooleanSetting("UpdateSelectedSlot", "Logs UpdateSelectedSlotC2SPacket", false));
   Setting clickSlot = this.registerSetting(new BooleanSetting("ClickSlot", "Logs ClickSlotC2SPacket", false));
   Setting pickInventory = this.registerSetting(new BooleanSetting("PickInventory", "Logs PickFromInventoryC2SPacket", false));
   Setting handSwing = this.registerSetting(new BooleanSetting("HandSwing", "Logs HandSwingC2SPacket", false));
   Setting interactEntity = this.registerSetting(new BooleanSetting("InteractEntity", "Logs PlayerInteractEntityC2SPacket", false));
   Setting interactBlock = this.registerSetting(new BooleanSetting("InteractBlock", "Logs PlayerInteractBlockC2SPacket", false));
   Setting interactItem = this.registerSetting(new BooleanSetting("InteractItem", "Logs PlayerInteractItemC2SPacket", false));
   Setting clientCommand = this.registerSetting(new BooleanSetting("ClientCommand", "Logs ClientCommandC2SPacket", false));
   Setting clientStatus = this.registerSetting(new BooleanSetting("ClientStatus", "Logs ClientStatusC2SPacket", false));
   Setting closeScreen = this.registerSetting(new BooleanSetting("CloseScreen", "Logs CloseHandledScreenC2SPacket", false));
   Setting teleportConfirm = this.registerSetting(new BooleanSetting("TeleportConfirm", "Logs TeleportConfirmC2SPacket", false));
   Setting pong = this.registerSetting(new BooleanSetting("Pong", "Logs CommonPongC2SPacket", false));

   public PacketLogger() {
      super("PacketLogger", "Logs client packets", ModuleCategory.MISC);
   }

   @Override
   public void awd() {
      Bootstrap.NZ("PacketLogger enabled ..");
   }

   @Override
   public void awe() {
      Bootstrap.NZ("PacketLogger disabled ..");
   }

   private void akw(Packet packet2, String str, Object... value) {
      String str2 = String.format(str, value);
      if ((Boolean)this.log.atx()) {
         Bootstrap.NZ(str2);
      }

      if ((Boolean)this.logChat.atx()) {
         this.hr(str2);
      }

      List temp = this.list;
      PacketTime packetTime = new PacketTime(packet2, System.currentTimeMillis());
      temp.add(packetTime);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         List temp = this.list;
         Predicate predicate = new Predicate() {
            @Override
            public boolean test(Object tickPreEvent) {
               return PacketLogger.akA((PacketTime)tickPreEvent);
            }
         };
         temp.removeIf(predicate);
      }
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      if ((Boolean)this.logDisconnect.atx()) {
         HashMap hashMap = new HashMap();
         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            Identifier id2 = ((PacketTime)iterator2.next()).packet().getPacketId().id();
            if (hashMap.containsKey(id2)) {
               Integer integer = (Integer)hashMap.get(id2) + 1;
               hashMap.replace(id2, integer);
            } else {
               HashMap temp = hashMap;
               Integer integer2 = 1;
               temp.put(id2, integer2);
            }
         }

         ArrayList arrayList = new ArrayList();
         Iterator iterator3 = hashMap.entrySet().iterator();

         while (iterator3.hasNext()) {
            Entry entry = (Entry)iterator3.next();
            Identifier id3 = (Identifier)entry.getKey();
            ArrayList arrayList2 = arrayList;
            String str = id3.toShortTranslationKey() + ": " + entry.getValue();
            arrayList2.add(str);
         }

         Bootstrap.NZ(String.join(",", arrayList));
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      Packet packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof Full) {
         Full full = (Full)packet2;
         if ((Boolean)this.playerMoveFull.atx()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PlayerMove Full - ");
            if (full.changesPosition()) {
               StringBuilder temp = stringBuilder.append("x: ");
               double d2 = full.getX(0.0);
               temp = temp.append(d2).append(", y: ");
               double d3 = full.getY(0.0);
               temp = temp.append(d3).append(", z: ");
               double d4 = full.getZ(0.0);
               temp.append(d4).append(" ");
            }

            if (full.changesLook()) {
               StringBuilder stringBuilder2 = stringBuilder.append("yaw: ");
               float f = full.getYaw(0.0F);
               stringBuilder2 = stringBuilder2.append(f).append(", pitch: ");
               float f2 = full.getPitch(0.0F);
               stringBuilder2.append(f2).append(" ");
            }

            StringBuilder stringBuilder3 = stringBuilder.append(" onground: ");
            boolean flag = full.isOnGround();
            stringBuilder3.append(flag);
            this.akw(full, stringBuilder.toString());
         }
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof PositionAndOnGround) {
         PositionAndOnGround positionAndOnGround = (PositionAndOnGround)packet2;
         if ((Boolean)this.playerMovePosition.atx()) {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("PlayerMove PosGround - ");
            if (positionAndOnGround.changesPosition()) {
               StringBuilder stringBuilder5 = stringBuilder4.append("x: ");
               double d5 = positionAndOnGround.getX(0.0);
               stringBuilder5 = stringBuilder5.append(d5).append(", y: ");
               double d6 = positionAndOnGround.getY(0.0);
               stringBuilder5 = stringBuilder5.append(d6).append(", z: ");
               double d7 = positionAndOnGround.getZ(0.0);
               stringBuilder5.append(d7).append(" ");
            }

            StringBuilder stringBuilder6 = stringBuilder4.append(" onground: ");
            boolean flag2 = positionAndOnGround.isOnGround();
            stringBuilder6.append(flag2);
            this.akw(positionAndOnGround, stringBuilder4.toString());
         }
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof LookAndOnGround) {
         LookAndOnGround lookAndOnGround = (LookAndOnGround)packet2;
         if ((Boolean)this.playerMoveLook.atx()) {
            StringBuilder stringBuilder7 = new StringBuilder();
            stringBuilder7.append("PlayerMove LookGround - ");
            if (lookAndOnGround.changesLook()) {
               StringBuilder stringBuilder8 = stringBuilder7.append("yaw: ");
               float f3 = lookAndOnGround.getYaw(0.0F);
               stringBuilder8 = stringBuilder8.append(f3).append(", pitch: ");
               float f4 = lookAndOnGround.getPitch(0.0F);
               stringBuilder8.append(f4).append(" ");
            }

            StringBuilder stringBuilder9 = stringBuilder7.append(" onground: ");
            boolean flag3 = lookAndOnGround.isOnGround();
            stringBuilder9.append(flag3);
            this.akw(lookAndOnGround, stringBuilder7.toString());
         }
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof OnGroundOnly) {
         OnGroundOnly onGroundOnly = (OnGroundOnly)packet2;
         if ((Boolean)this.playerMoveGround.atx()) {
            String str = "PlayerMove Ground - onground: " + ((OnGroundOnly)packet2).isOnGround();
            this.akw(onGroundOnly, str);
         }
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof VehicleMoveC2SPacket && (Boolean)this.vehicleMove.atx()) {
         this.akw(
            (VehicleMoveC2SPacket)packet2,
            "VehicleMove - x: %s, y: %s, z: %s, yaw: %s, pitch: %s",
            ((VehicleMoveC2SPacket)packet2).getX(),
            ((VehicleMoveC2SPacket)packet2).getY(),
            ((VehicleMoveC2SPacket)packet2).getZ(),
            ((VehicleMoveC2SPacket)packet2).getYaw(),
            ((VehicleMoveC2SPacket)packet2).getPitch()
         );
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof PlayerActionC2SPacket && (Boolean)this.playerAction.atx()) {
         this.akw(
            (PlayerActionC2SPacket)packet2,
            "PlayerAction - action: %s, direction: %s, pos: %s",
            ((PlayerActionC2SPacket)packet2).getAction().name(),
            ((PlayerActionC2SPacket)packet2).getDirection().name(),
            ((PlayerActionC2SPacket)packet2).getPos().toShortString()
         );
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof UpdateSelectedSlotC2SPacket && (Boolean)this.updateSelectedSlot.atx()) {
         this.akw((UpdateSelectedSlotC2SPacket)packet2, "UpdateSlot - slot: %d", ((UpdateSelectedSlotC2SPacket)packet2).getSelectedSlot());
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof HandSwingC2SPacket && (Boolean)this.handSwing.atx()) {
         this.akw((HandSwingC2SPacket)packet2, "HandSwing - hand: %s", ((HandSwingC2SPacket)packet2).getHand().name());
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof CommonPongC2SPacket && (Boolean)this.pong.atx()) {
         this.akw((CommonPongC2SPacket)packet2, "Pong - %d", ((CommonPongC2SPacket)packet2).getParameter());
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof PlayerInteractEntityC2SPacket && client.world != null && (Boolean)this.interactEntity.atx()) {
         this.akw((PlayerInteractEntityC2SPacket)packet2, "InteractEntity");
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof PlayerInteractBlockC2SPacket) {
         PlayerInteractBlockC2SPacket playerInteractBlockC2SPacket = (PlayerInteractBlockC2SPacket)packet2;
         if ((Boolean)this.interactBlock.atx()) {
            BlockHitResult blockHitResult = ((PlayerInteractBlockC2SPacket)packet2).getBlockHitResult();
            this.akw(playerInteractBlockC2SPacket, "InteractBlock - pos: %s, dir: %s, hand: %s", blockHitResult.getBlockPos().toShortString(), blockHitResult.getSide().name(), playerInteractBlockC2SPacket.getHand().name());
         }
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof PlayerInteractItemC2SPacket && (Boolean)this.interactItem.atx()) {
         this.akw((PlayerInteractItemC2SPacket)packet2, "InteractItem - hand: %s", ((PlayerInteractItemC2SPacket)packet2).getHand().name());
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof CloseHandledScreenC2SPacket && (Boolean)this.closeScreen.atx()) {
         this.akw((CloseHandledScreenC2SPacket)packet2, "CloseScreen - id: %s", ((CloseHandledScreenC2SPacket)packet2).getSyncId());
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof ClientCommandC2SPacket && (Boolean)this.clientCommand.atx()) {
         this.akw((ClientCommandC2SPacket)packet2, "ClientCommand - mode: %s", ((ClientCommandC2SPacket)packet2).getMode().name());
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof ClientStatusC2SPacket && (Boolean)this.clientStatus.atx()) {
         this.akw((ClientStatusC2SPacket)packet2, "ClientStatus - mode: %s", ((ClientStatusC2SPacket)packet2).getMode().name());
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof ClickSlotC2SPacket && (Boolean)this.clickSlot.atx()) {
         this.akw(
            (ClickSlotC2SPacket)packet2,
            "ClickSlot - type: %s, slot: %s, button: %s, id: %s",
            ((ClickSlotC2SPacket)packet2).getActionType().name(),
            ((ClickSlotC2SPacket)packet2).getSlot(),
            ((ClickSlotC2SPacket)packet2).getButton(),
            ((ClickSlotC2SPacket)packet2).getSyncId()
         );
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof PickFromInventoryC2SPacket && (Boolean)this.pickInventory.atx()) {
         this.akw((PickFromInventoryC2SPacket)packet2, "PickInventory - slot: %s", ((PickFromInventoryC2SPacket)packet2).getSlot());
      }

      packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof TeleportConfirmC2SPacket && (Boolean)this.teleportConfirm.atx()) {
         this.akw((TeleportConfirmC2SPacket)packet2, "TeleportConfirm - id: %s", ((TeleportConfirmC2SPacket)packet2).getTeleportId());
      }
   }

   static boolean akA(PacketTime packetTime) {
      boolean flag;
      if (System.currentTimeMillis() - packetTime.getTime() > 3000L) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
