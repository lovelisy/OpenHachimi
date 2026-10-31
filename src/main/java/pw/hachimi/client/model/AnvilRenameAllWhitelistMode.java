package pw.hachimi.client.model;

public enum AnvilRenameAllWhitelistMode {
   ALL,
   WHITELIST,
   BLACKLIST;

   private static final AnvilRenameAllWhitelistMode[] d = arp();

   private static AnvilRenameAllWhitelistMode[] arp() {
      return new AnvilRenameAllWhitelistMode[]{ALL, WHITELIST, BLACKLIST};
   }
}
