package pw.hachimi.client.model.render;

public enum DisableNormalMode {
   DISABLE,
   NORMAL,
   OUTLINE;

   private static final DisableNormalMode[] d = aql();

   private static DisableNormalMode[] aql() {
      return new DisableNormalMode[]{DISABLE, NORMAL, OUTLINE};
   }
}
