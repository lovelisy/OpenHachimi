package pw.hachimi.client.model.render;

public enum OffGradientMode {
   OFF,
   GRADIENT,
   HUE,
   STATIC,
   ASTOLFO,
   GAY;

   private static final OffGradientMode[] g = auA();

   private static OffGradientMode[] auA() {
      return new OffGradientMode[]{OFF, GRADIENT, HUE, STATIC, ASTOLFO, GAY};
   }
}
