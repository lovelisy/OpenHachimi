package pw.hachimi.client.model.render;

public enum TracersOnScreenOffScreenMode {
   ON_SCREEN,
   OFF_SCREEN,
   NORMAL;

   private static final TracersOnScreenOffScreenMode[] d = abB();

   private static TracersOnScreenOffScreenMode[] abB() {
      return new TracersOnScreenOffScreenMode[]{ON_SCREEN, OFF_SCREEN, NORMAL};
   }
}
