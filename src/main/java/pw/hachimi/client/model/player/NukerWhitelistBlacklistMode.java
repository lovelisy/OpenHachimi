package pw.hachimi.client.model.player;

public enum NukerWhitelistBlacklistMode {
   WHITELIST,
   BLACKLIST,
   ALL;

   private static final NukerWhitelistBlacklistMode[] d = apl();

   private static NukerWhitelistBlacklistMode[] apl() {
      return new NukerWhitelistBlacklistMode[]{WHITELIST, BLACKLIST, ALL};
   }
}
