package pw.hachimi.client.model.render;

public enum ParticlesOffRemoveMode {
   OFF,
   REMOVE,
   COLOR;

   private static final ParticlesOffRemoveMode[] d = yw();

   private static ParticlesOffRemoveMode[] yw() {
      return new ParticlesOffRemoveMode[]{OFF, REMOVE, COLOR};
   }
}
