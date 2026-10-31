package pw.hachimi.client.module.player;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoSlotSet extends ClientModule {
   private final StopwatchImpl stopwatchImpl = new StopwatchImpl();
   public static boolean fp;

   public NoSlotSet() {
      super("NoSlotSet", "Prevents server from forcing slot set", ModuleCategory.PLAYER);
   }

   @EventListener
   public void onOnGameJoin(OnGameJoinEvent onGameJoinEvent) {
      this.stopwatchImpl.afr(29432, 5265492, (byte)40);
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (this.stopwatchImpl.afq(500) && client.player != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof UpdateSelectedSlotS2CPacket) {
            channelRead0Event.aim();
            Managers.inventoryTracker.JE();
            ClientPlayNetworkHandler temp = client.player.networkHandler;
            UpdateSelectedSlotC2SPacket updateSelectedSlotC2SPacket = new UpdateSelectedSlotC2SPacket(((UpdateSelectedSlotS2CPacket)packet2).getSlot());
            temp.sendPacket((Packet)updateSelectedSlotC2SPacket);
            Managers.inventoryTracker.JE();
            temp = client.player.networkHandler;
            UpdateSelectedSlotC2SPacket updateSelectedSlotC2SPacket2 = new UpdateSelectedSlotC2SPacket(client.player.getInventory().selectedSlot);
            temp.sendPacket((Packet)updateSelectedSlotC2SPacket2);
         }
      }
   }
}
