package pw.hachimi.client.event.player;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class PlayerPositionLookEvent extends Event {
   private final PlayerPositionLookS2CPacket playerPositionLookS2CPacket;

   public PlayerPositionLookEvent(PlayerPositionLookS2CPacket playerPositionLookS2CPacket2) {
      this.playerPositionLookS2CPacket = playerPositionLookS2CPacket2;
   }

   public PlayerPositionLookS2CPacket getPlayerPositionLookS2CPacket() {
      return this.playerPositionLookS2CPacket;
   }
}
