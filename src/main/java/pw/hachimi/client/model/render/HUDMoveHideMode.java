package pw.hachimi.client.model.render;

public enum HUDMoveHideMode {
   MOVE,
   HIDE,
   KEEP;

   private static final HUDMoveHideMode[] d = aJ();

   private static HUDMoveHideMode[] aJ() {
      return new HUDMoveHideMode[]{MOVE, HIDE, KEEP};
   }
}
