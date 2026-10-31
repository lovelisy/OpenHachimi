package pw.hachimi.client.render;

import java.awt.Color;
import pw.hachimi.client.api.UnidentifiedInterface13;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.ColorHelper2;
import pw.hachimi.client.utils.render.RenderHelper2;

public abstract class ColorBase extends BufferVertexBase implements UnidentifiedInterface13 {
   public final RenderHelper2 renderHelper2;
   public final ColorHelper2 colorHelper2 = new ColorHelper2();
   private static final Color o = new Color(255, 255, 255, 40);
   private static final Color p = new Color(0, 0, 0, 0);
   private static final long a = 0L;
   private static final long h = 0L;

   public ColorBase(RenderHelper2 renderHelper22, float f, float f2, float f3) {
      this.renderHelper2 = renderHelper22;
      this.j = f;
      this.k = f2;
      this.l = f3;
      this.m = 13.0F;
   }

   public Color jj(boolean flag, boolean flag2) {
      Color color;
      if (flag) {
         int n = UI.atK().atW(this.k);
         if (flag2) {
            color = new Color(ColorUtils.iG(n, 200), true);
         } else {
            color = new Color(ColorUtils.iG(n, 160), true);
         }
      } else if (flag2) {
         int n2 = UI.atK().atW(this.k);
         color = new Color(ColorUtils.iG(n2, 60), true);
      } else {
         color = p;
      }

      return this.colorHelper2.arQ(color, 150L, Easing.QUINT_OUT);
   }

   @Override
   public abstract void UU(int var1, int var2, int var3);

   @Override
   public abstract void UV();

   @Override
   public abstract void UW(int var1);

   public RenderHelper2 getRenderHelper2() {
      return this.renderHelper2;
   }
}
