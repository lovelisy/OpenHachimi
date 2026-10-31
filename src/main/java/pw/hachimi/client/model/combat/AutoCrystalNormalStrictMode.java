package pw.hachimi.client.model.combat;

public enum AutoCrystalNormalStrictMode {
   NORMAL,
   STRICT,
   NONE;

   private static final AutoCrystalNormalStrictMode[] d = bA();

   private static AutoCrystalNormalStrictMode[] bA() {
      return new AutoCrystalNormalStrictMode[]{NORMAL, STRICT, NONE};
   }
}
