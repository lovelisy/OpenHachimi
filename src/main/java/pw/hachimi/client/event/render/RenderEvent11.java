package pw.hachimi.client.event.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.UnknownEvent2;

@UnidentifiedInterface2
public class RenderEvent11 extends UnknownEvent2 {
   private final Text text;
   private final DrawContext context;
   private final TextRenderer textRenderer;
   private final double d;
   private final double e;
   private final int f;
   public static boolean er;

   public RenderEvent11(Text text2, DrawContext context2, TextRenderer textRenderer2, double d2, double d3, int n) {
      this.context = context2;
      this.textRenderer = textRenderer2;
      this.text = text2;
      this.d = d2;
      this.e = d3;
      this.f = n;
   }

   public DrawContext getContext() {
      return this.context;
   }

   public MatrixStack afk() {
      return this.context.getMatrices();
   }

   public TextRenderer getTextRenderer() {
      return this.textRenderer;
   }

   public double afm() {
      return this.d;
   }

   public double afn() {
      return this.e;
   }

   public int afo() {
      return this.f;
   }

   public Text getText() {
      return this.text;
   }
}
