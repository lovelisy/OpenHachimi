package pw.hachimi.client.model;

public enum AntiPortalNetherOverworldMode {
   NETHER,
   OVERWORLD,
   ANY;

   private static final AntiPortalNetherOverworldMode[] d = aqs();

   private static AntiPortalNetherOverworldMode[] aqs() {
      return new AntiPortalNetherOverworldMode[]{NETHER, OVERWORLD, ANY};
   }
}
