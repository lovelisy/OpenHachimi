package pw.hachimi.client.event.player;

import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import pw.hachimi.client.event.Event;

public class OnEntityEquipmentUpdateEvent extends Event {
   private final EntityEquipmentUpdateS2CPacket entityEquipmentUpdateS2CPacket;
   public static boolean ce;

   public OnEntityEquipmentUpdateEvent(EntityEquipmentUpdateS2CPacket entityEquipmentUpdateS2CPacket2) {
      this.entityEquipmentUpdateS2CPacket = entityEquipmentUpdateS2CPacket2;
   }

   public EntityEquipmentUpdateS2CPacket getEntityEquipmentUpdateS2CPacket() {
      return this.entityEquipmentUpdateS2CPacket;
   }
}
