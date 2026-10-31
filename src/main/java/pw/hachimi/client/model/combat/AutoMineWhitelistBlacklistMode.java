package pw.hachimi.client.model.combat;

public enum AutoMineWhitelistBlacklistMode {
   Whitelist,
   Blacklist,
   All;

   private static final AutoMineWhitelistBlacklistMode[] d = ase();

   private static AutoMineWhitelistBlacklistMode[] ase() {
      return new AutoMineWhitelistBlacklistMode[]{Whitelist, Blacklist, All};
   }
}
