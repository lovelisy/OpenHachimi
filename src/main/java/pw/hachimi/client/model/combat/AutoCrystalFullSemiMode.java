package pw.hachimi.client.model.combat;

public enum AutoCrystalFullSemiMode {
   FULL,
   SEMI,
   OFF;

   private static final AutoCrystalFullSemiMode[] d = abY();

   private static AutoCrystalFullSemiMode[] abY() {
      return new AutoCrystalFullSemiMode[]{FULL, SEMI, OFF};
   }
}
