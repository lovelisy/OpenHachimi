package pw.hachimi.client.model.player;

public enum ScaffoldNormalGreatestMode {
   NORMAL,
   GREATEST;

   private static final ScaffoldNormalGreatestMode[] c = OH();

   private static ScaffoldNormalGreatestMode[] OH() {
      return new ScaffoldNormalGreatestMode[]{NORMAL, GREATEST};
   }
}
