package pw.hachimi.client.event.network;

import net.minecraft.network.packet.Packet;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class SendImmediately_2Event extends SendImmediatelyEvent {
   public SendImmediately_2Event(Packet packet2) {
      super(packet2);
   }
}
