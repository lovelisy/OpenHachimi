package pw.hachimi.client.model.render;

public enum NoRenderClearLiquidVisionMode {
   CLEAR,
   LIQUID_VISION,
   OFF;

   private static final NoRenderClearLiquidVisionMode[] d = qh();

   private static NoRenderClearLiquidVisionMode[] qh() {
      return new NoRenderClearLiquidVisionMode[]{CLEAR, LIQUID_VISION, OFF};
   }
}
