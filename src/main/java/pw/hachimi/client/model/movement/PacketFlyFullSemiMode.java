package pw.hachimi.client.model.movement;

public enum PacketFlyFullSemiMode {
   FULL,
   SEMI,
   OFF;

   private static final PacketFlyFullSemiMode[] d = VE();

   private static PacketFlyFullSemiMode[] VE() {
      return new PacketFlyFullSemiMode[]{FULL, SEMI, OFF};
   }
}
