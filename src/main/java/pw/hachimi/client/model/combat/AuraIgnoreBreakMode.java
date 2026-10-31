package pw.hachimi.client.model.combat;

public enum AuraIgnoreBreakMode {
   Ignore,
   Break,
   None;

   private static final AuraIgnoreBreakMode[] d = pv();

   private static AuraIgnoreBreakMode[] pv() {
      return new AuraIgnoreBreakMode[]{Ignore, Break, None};
   }
}
