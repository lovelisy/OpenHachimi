package pw.hachimi.client.model.movement;

public enum SpinBotOffStaticMode {
   OFF,
   STATIC,
   ZERO,
   UP,
   DOWN,
   JITTER;

   private static final SpinBotOffStaticMode[] g = oF();

   private static SpinBotOffStaticMode[] oF() {
      return new SpinBotOffStaticMode[]{OFF, STATIC, ZERO, UP, DOWN, JITTER};
   }
}
