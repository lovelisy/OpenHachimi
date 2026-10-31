package pw.hachimi.client.model.combat;

public enum AuraNoneNormalMode {
   None,
   Normal,
   Silent;

   private static final AuraNoneNormalMode[] d = yR();

   private static AuraNoneNormalMode[] yR() {
      return new AuraNoneNormalMode[]{None, Normal, Silent};
   }
}
