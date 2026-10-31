package pw.hachimi.client.model.combat;

public enum OffhandDisableAlwaysMode {
   Disable,
   Always,
   Delay;

   private static final OffhandDisableAlwaysMode[] d = Co();

   private static OffhandDisableAlwaysMode[] Co() {
      return new OffhandDisableAlwaysMode[]{Disable, Always, Delay};
   }
}
