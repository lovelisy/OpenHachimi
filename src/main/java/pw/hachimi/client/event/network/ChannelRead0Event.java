package pw.hachimi.client.event.network;

import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class ChannelRead0Event extends PacketEvent {
   private final PacketListener packetListener;
   public static boolean bi;

   public ChannelRead0Event(PacketListener packetListener2, Packet packet2) {
      super(packet2);
      this.packetListener = packetListener2;
   }

   public PacketListener getPacketListener() {
      return this.packetListener;
   }
}
