package pw.hachimi.client.model.combat;

public enum AutoCrystalNoneBypassMode {
   None,
   Bypass;

   private static final AutoCrystalNoneBypassMode[] c = cN();

   private static AutoCrystalNoneBypassMode[] cN() {
      return new AutoCrystalNoneBypassMode[]{None, Bypass};
   }
}
