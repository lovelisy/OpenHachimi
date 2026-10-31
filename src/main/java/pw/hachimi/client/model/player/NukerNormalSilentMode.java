package pw.hachimi.client.model.player;

public enum NukerNormalSilentMode {
   NORMAL,
   SILENT,
   SILENT_ALT,
   OFF;

   private static final NukerNormalSilentMode[] e = abS();

   private static NukerNormalSilentMode[] abS() {
      return new NukerNormalSilentMode[]{NORMAL, SILENT, SILENT_ALT, OFF};
   }
}
