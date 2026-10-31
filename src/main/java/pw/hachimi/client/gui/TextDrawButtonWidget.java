package pw.hachimi.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.manager.RenderUtils;

public class TextDrawButtonWidget extends ButtonWidget {
   private final Text text;
   public static boolean a;

   public TextDrawButtonWidget(int n, int n2, Text text2, PressAction pressAction) {
      super(5, n, n2, 10, text2, pressAction, DEFAULT_NARRATION_SUPPLIER);
      this.text = text2;
   }

   public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
      Text text2 = this.text;
      RenderUtils.acE(context, text2.getString(), this.getX(), this.getY(), 16777215 | MathHelper.ceil(this.alpha * 255.0F) << 24);
      if (this.isSelected()) {
         int temp = this.getX();
         int temp2 = this.getY() + this.height - 1;
         int temp3 = this.getX() + this.width;
         int n = this.getY() + this.height - 2;
         int n2 = temp3;
         int n3 = temp2;
         int n4 = temp;
         context.fill(n4, n3, n2, n, -1);
      }
   }
}
