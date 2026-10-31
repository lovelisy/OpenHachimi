package pw.hachimi.client.model.movement;

public enum NoSlowDisabledVanillaMode2 {
   Disabled,
   Vanilla,
   NCP,
   Grim50,
   Grim95,
   GrimLegacy;

   private static final NoSlowDisabledVanillaMode2[] g = CV();

   private static NoSlowDisabledVanillaMode2[] CV() {
      return new NoSlowDisabledVanillaMode2[]{Disabled, Vanilla, NCP, Grim50, Grim95, GrimLegacy};
   }
}
