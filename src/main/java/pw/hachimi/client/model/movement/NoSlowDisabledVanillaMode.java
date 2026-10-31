package pw.hachimi.client.model.movement;

public enum NoSlowDisabledVanillaMode {
   Disabled,
   Vanilla,
   Grim,
   GrimMineable;

   private static final NoSlowDisabledVanillaMode[] e = ep();

   private static NoSlowDisabledVanillaMode[] ep() {
      return new NoSlowDisabledVanillaMode[]{Disabled, Vanilla, Grim, GrimMineable};
   }
}
