package pw.hachimi.client.model.movement;

public enum ElytraFlyStaticTimerMode {
   Static,
   Timer,
   Tick;

   private static final ElytraFlyStaticTimerMode[] d = bk();

   private static ElytraFlyStaticTimerMode[] bk() {
      return new ElytraFlyStaticTimerMode[]{Static, Timer, Tick};
   }
}
