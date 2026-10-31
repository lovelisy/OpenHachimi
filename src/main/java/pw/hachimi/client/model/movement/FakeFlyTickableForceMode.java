package pw.hachimi.client.model.movement;

public enum FakeFlyTickableForceMode {
   Tickable,
   Force,
   None;

   private static final FakeFlyTickableForceMode[] d = agp();

   private static FakeFlyTickableForceMode[] agp() {
      return new FakeFlyTickableForceMode[]{Tickable, Force, None};
   }
}
