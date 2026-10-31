package pw.hachimi.client.model.movement;

public enum ElytraFlyControlVanillaMode {
   CONTROL,
   VANILLA;

   private static final ElytraFlyControlVanillaMode[] c = mO();

   private static ElytraFlyControlVanillaMode[] mO() {
      return new ElytraFlyControlVanillaMode[]{CONTROL, VANILLA};
   }
}
