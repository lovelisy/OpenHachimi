package pw.hachimi.client.model.world;

import net.minecraft.network.packet.Packet;

public record PacketTime(Packet packet, long time) {
   public static boolean u;

   public long getTime() {
      return this.time;
   }
}
