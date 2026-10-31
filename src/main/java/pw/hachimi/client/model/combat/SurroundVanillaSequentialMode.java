package pw.hachimi.client.model.combat;

public enum SurroundVanillaSequentialMode {
   VANILLA,
   SEQUENTIAL;

   private static final SurroundVanillaSequentialMode[] c = aoB();

   private static SurroundVanillaSequentialMode[] aoB() {
      return new SurroundVanillaSequentialMode[]{VANILLA, SEQUENTIAL};
   }
}
