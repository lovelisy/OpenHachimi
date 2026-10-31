package pw.hachimi.client.model.combat;

public enum AuraSwordAxeMode {
   Sword,
   Axe,
   Mace,
   Trident,
   All,
   Any;

   private static final AuraSwordAxeMode[] g = cP();

   private static AuraSwordAxeMode[] cP() {
      return new AuraSwordAxeMode[]{Sword, Axe, Mace, Trident, All, Any};
   }
}
