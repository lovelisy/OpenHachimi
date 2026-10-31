package pw.hachimi.client.model.render;

public enum HUDLegacyMoveHideMode {
   MOVE,
   HIDE,
   KEEP;

   private static final HUDLegacyMoveHideMode[] d = ex();

   private static HUDLegacyMoveHideMode[] ex() {
      return new HUDLegacyMoveHideMode[]{MOVE, HIDE, KEEP};
   }
}
