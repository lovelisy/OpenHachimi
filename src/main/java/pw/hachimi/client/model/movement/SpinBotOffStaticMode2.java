package pw.hachimi.client.model.movement;

public enum SpinBotOffStaticMode2 {
   OFF,
   STATIC,
   ZERO,
   SPIN,
   JITTER;

   private static final SpinBotOffStaticMode2[] f = WM();

   private static SpinBotOffStaticMode2[] WM() {
      return new SpinBotOffStaticMode2[]{OFF, STATIC, ZERO, SPIN, JITTER};
   }
}
