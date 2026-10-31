package pw.hachimi.client.model.combat;

public enum CrasherArmorSelectorMode {
   ARMOR,
   SELECTOR,
   CONSOLE,
   OUT_OF_BOUNDS,
   UNICODE;

   private static final CrasherArmorSelectorMode[] f = xX();

   private static CrasherArmorSelectorMode[] xX() {
      return new CrasherArmorSelectorMode[]{ARMOR, SELECTOR, CONSOLE, OUT_OF_BOUNDS, UNICODE};
   }
}
