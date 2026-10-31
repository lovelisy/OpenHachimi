package pw.hachimi.client.model;

public enum GrimNCPMode {
   GRIM,
   N_C_P,
   VANILLA;

   private static final GrimNCPMode[] d = bY();

   private static GrimNCPMode[] bY() {
      return new GrimNCPMode[]{GRIM, N_C_P, VANILLA};
   }
}
