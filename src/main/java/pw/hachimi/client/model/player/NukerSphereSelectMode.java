package pw.hachimi.client.model.player;

public enum NukerSphereSelectMode {
   SPHERE,
   SELECT;

   private static final NukerSphereSelectMode[] c = MG();

   private static NukerSphereSelectMode[] MG() {
      return new NukerSphereSelectMode[]{SPHERE, SELECT};
   }
}
