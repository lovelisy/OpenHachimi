package pw.hachimi.client.model.render;

public enum TracersFeetTorsoMode {
   FEET,
   TORSO,
   HEAD;

   private static final TracersFeetTorsoMode[] d = yy();

   private static TracersFeetTorsoMode[] yy() {
      return new TracersFeetTorsoMode[]{FEET, TORSO, HEAD};
   }
}
