package pw.hachimi.client.model.render;

public enum HUDLegacyKMHBPSMode {
   K_M_H,
   B_P_S,
   OFF;

   private static final HUDLegacyKMHBPSMode[] d = Ae();

   private static HUDLegacyKMHBPSMode[] Ae() {
      return new HUDLegacyKMHBPSMode[]{K_M_H, B_P_S, OFF};
   }
}
