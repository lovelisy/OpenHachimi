package pw.hachimi.client.model.render;

public enum HUDLegacyNormalOldMode {
   NORMAL,
   OLD,
   OFF;

   private static final HUDLegacyNormalOldMode[] d = oe();

   private static HUDLegacyNormalOldMode[] oe() {
      return new HUDLegacyNormalOldMode[]{NORMAL, OLD, OFF};
   }
}
