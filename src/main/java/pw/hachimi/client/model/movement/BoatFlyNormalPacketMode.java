package pw.hachimi.client.model.movement;

public enum BoatFlyNormalPacketMode {
   NORMAL,
   PACKET,
   OFF;

   private static final BoatFlyNormalPacketMode[] d = vJ();

   private static BoatFlyNormalPacketMode[] vJ() {
      return new BoatFlyNormalPacketMode[]{NORMAL, PACKET, OFF};
   }
}
