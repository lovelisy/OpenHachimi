package pw.hachimi.client.model.player;

public enum GhostHandIgnoreTargetMode {
   IGNORE,
   TARGET;

   private static final GhostHandIgnoreTargetMode[] c = afZ();

   private static GhostHandIgnoreTargetMode[] afZ() {
      return new GhostHandIgnoreTargetMode[]{IGNORE, TARGET};
   }
}
