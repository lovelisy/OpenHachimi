package pw.hachimi.client.model.player;

public enum WhitelistBlacklistMode {
   Whitelist,
   Blacklist,
   All;

   private static final WhitelistBlacklistMode[] d = arP();

   private static WhitelistBlacklistMode[] arP() {
      return new WhitelistBlacklistMode[]{Whitelist, Blacklist, All};
   }
}
