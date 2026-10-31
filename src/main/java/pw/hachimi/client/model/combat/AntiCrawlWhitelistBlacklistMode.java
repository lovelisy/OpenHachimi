package pw.hachimi.client.model.combat;

public enum AntiCrawlWhitelistBlacklistMode {
   Whitelist,
   Blacklist,
   All;

   private static final AntiCrawlWhitelistBlacklistMode[] d = w();

   private static AntiCrawlWhitelistBlacklistMode[] w() {
      return new AntiCrawlWhitelistBlacklistMode[]{Whitelist, Blacklist, All};
   }
}
