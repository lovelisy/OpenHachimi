package pw.hachimi.client.model.player;

public enum DisableLimitMode {
   Disable,
   Limit,
   Force;

   private static final DisableLimitMode[] d = aib();

   private static DisableLimitMode[] aib() {
      return new DisableLimitMode[]{Disable, Limit, Force};
   }
}
