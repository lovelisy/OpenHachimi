package pw.hachimi.client.model.movement;

public enum FlightNormalPacketMode {
   NORMAL,
   PACKET,
   OFF;

   private static final FlightNormalPacketMode[] d = Sp();

   private static FlightNormalPacketMode[] Sp() {
      return new FlightNormalPacketMode[]{NORMAL, PACKET, OFF};
   }
}
