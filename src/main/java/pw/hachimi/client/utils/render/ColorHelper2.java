package pw.hachimi.client.utils.render;

import java.awt.Color;
import pw.hachimi.client.model.render.Easing;

public class ColorHelper2 {
   private Color color;
   private Color color2;
   private final UnidentifiedHelper2 unidentifiedHelper2 = new UnidentifiedHelper2();

   public Color arQ(Color color3, long l, Easing easing) {
      if (this.color == null) {
         this.color = color3;
         this.color2 = color3;
      } else {
         Color color4 = this.color2;
         if (!color3.equals(color4)) {
            this.color = this.arR(easing);
            this.color2 = color3;
            this.unidentifiedHelper2.hR();
         }
      }

      this.unidentifiedHelper2.hU(l);
      return this.arR(easing);
   }

   private Color arR(Easing easing) {
      double d = this.unidentifiedHelper2.hW(easing);
      int n = this.arS(this.color.getRed(), this.color2.getRed(), d);
      int n2 = this.arS(this.color.getGreen(), this.color2.getGreen(), d);
      int n3 = this.arS(this.color.getBlue(), this.color2.getBlue(), d);
      int n4 = this.arS(this.color.getAlpha(), this.color2.getAlpha(), d);
      return new Color(n, n2, n3, n4);
   }

   private int arS(int n, int n2, double d) {
      return (int)(n + (n2 - n) * d);
   }
}
