package pw.hachimi.client.utils.render;

import pw.hachimi.client.model.render.LinesTrianglesMode;
import pw.hachimi.client.model.render.UnidentifiedOption;
import pw.hachimi.client.render.ColorManager;

public class UnidentifiedHelper16 extends Vec3dGameRendererHelper {
   private final ColorManager colorManager;

   public UnidentifiedHelper16(ColorManager colorManager2, LinesTrianglesMode linesTrianglesMode, UnidentifiedOption... value) {
      super(linesTrianglesMode, value);
      this.colorManager = colorManager2;
   }

   @Override
   protected void pL() {
      this.colorManager.ajB();
   }
}
