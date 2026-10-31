package pw.hachimi.client.model.movement;

public enum NoFallAntiLimitMode {
   ANTI,
   LIMIT,
   PACKET,
   STRICT,
   GRIM,
   GRIM_JUMP,
   GRIM_LATEST;

   private static final NoFallAntiLimitMode[] h = ais();

   private static NoFallAntiLimitMode[] ais() {
      return new NoFallAntiLimitMode[]{ANTI, LIMIT, PACKET, STRICT, GRIM, GRIM_JUMP, GRIM_LATEST};
   }
}
