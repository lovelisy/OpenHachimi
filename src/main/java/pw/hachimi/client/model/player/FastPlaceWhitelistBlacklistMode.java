package pw.hachimi.client.model.player;

public enum FastPlaceWhitelistBlacklistMode {
   WHITELIST,
   BLACKLIST,
   ALL;

   private static final FastPlaceWhitelistBlacklistMode[] d = lO();

   private static FastPlaceWhitelistBlacklistMode[] lO() {
      return new FastPlaceWhitelistBlacklistMode[]{WHITELIST, BLACKLIST, ALL};
   }
}
