package pw.hachimi.client.model.combat;

public enum AutoCrystalNormalSilentMode {
   NORMAL,
   SILENT,
   OFF;

   private static final AutoCrystalNormalSilentMode[] d = oN();

   private static AutoCrystalNormalSilentMode[] oN() {
      return new AutoCrystalNormalSilentMode[]{NORMAL, SILENT, OFF};
   }
}
