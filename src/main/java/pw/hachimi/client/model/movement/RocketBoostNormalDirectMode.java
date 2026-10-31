package pw.hachimi.client.model.movement;

public enum RocketBoostNormalDirectMode {
   Normal,
   Direct,
   Grim;

   private static final RocketBoostNormalDirectMode[] d = nE();

   private static RocketBoostNormalDirectMode[] nE() {
      return new RocketBoostNormalDirectMode[]{Normal, Direct, Grim};
   }
}
