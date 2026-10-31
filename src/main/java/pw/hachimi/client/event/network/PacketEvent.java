package pw.hachimi.client.event.network;

import net.minecraft.network.packet.Packet;
import pw.hachimi.client.event.Event;

public class PacketEvent extends Event {
   private final Packet packet;
   public static boolean dm;

   public PacketEvent(Packet packet2) {
      this.packet = packet2;
   }

   public Packet getPacket() {
      return this.packet;
   }
}
