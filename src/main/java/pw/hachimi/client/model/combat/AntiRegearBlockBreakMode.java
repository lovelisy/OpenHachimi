package pw.hachimi.client.model.combat;

public enum AntiRegearBlockBreakMode {
   Block,
   Break,
   Both;

   private static final AntiRegearBlockBreakMode[] d = akC();

   private static AntiRegearBlockBreakMode[] akC() {
      return new AntiRegearBlockBreakMode[]{Block, Break, Both};
   }
}
