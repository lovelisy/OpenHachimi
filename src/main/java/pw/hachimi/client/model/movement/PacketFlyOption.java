package pw.hachimi.client.model.movement;

public enum PacketFlyOption {
   FACTOR,
   FAST,
   SETBACK;

   private static final PacketFlyOption[] d = Bx();

   private static PacketFlyOption[] Bx() {
      return new PacketFlyOption[]{FACTOR, FAST, SETBACK};
   }
}
