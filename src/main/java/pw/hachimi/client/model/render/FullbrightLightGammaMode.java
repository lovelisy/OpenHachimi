package pw.hachimi.client.model.render;

public enum FullbrightLightGammaMode {
   LIGHT,
   GAMMA,
   POTION;

   private static final FullbrightLightGammaMode[] d = Dm();

   private static FullbrightLightGammaMode[] Dm() {
      return new FullbrightLightGammaMode[]{LIGHT, GAMMA, POTION};
   }
}
