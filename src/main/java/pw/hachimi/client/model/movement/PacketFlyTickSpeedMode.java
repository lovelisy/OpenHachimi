package pw.hachimi.client.model.movement;

public enum PacketFlyTickSpeedMode {
   TICK,
   SPEED,
   BOTH,
   OFF;

   private static final PacketFlyTickSpeedMode[] e = agb();

   private static PacketFlyTickSpeedMode[] agb() {
      return new PacketFlyTickSpeedMode[]{TICK, SPEED, BOTH, OFF};
   }
}
