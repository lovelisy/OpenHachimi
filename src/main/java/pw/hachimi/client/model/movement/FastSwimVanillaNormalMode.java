package pw.hachimi.client.model.movement;

public enum FastSwimVanillaNormalMode {
   VANILLA,
   NORMAL;

   private static final FastSwimVanillaNormalMode[] c = oK();

   private static FastSwimVanillaNormalMode[] oK() {
      return new FastSwimVanillaNormalMode[]{VANILLA, NORMAL};
   }
}
