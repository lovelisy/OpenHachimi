package pw.hachimi.client.utils.render;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import pw.hachimi.client.api.UnidentifiedInterface7;
import pw.hachimi.client.gui.DrawTextPressableWidget;
import pw.hachimi.client.handler.MutableTextHandler;

public class TextTooltipHelper {
   private final Text text;
   private final UnidentifiedInterface7 unidentifiedInterface7;
   private Tooltip tooltip;
   private int d;
   private int e;
   private int f = 150;
   private int g = 20;
   private String text2;
   private MutableTextHandler mutableTextHandler = DrawTextPressableWidget.a_;
   public static boolean a;

   public TextTooltipHelper(Text text3, UnidentifiedInterface7 unidentifiedInterface72) {
      this.text = text3;
      this.unidentifiedInterface7 = unidentifiedInterface72;
   }

   public TextTooltipHelper Ey(int n, int n2) {
      this.d = n;
      this.e = n2;
      return this;
   }

   public TextTooltipHelper Ez(int n) {
      this.f = n;
      return this;
   }

   public TextTooltipHelper EA(int n) {
      this.f = n;
      this.g = 20;
      return this;
   }

   public TextTooltipHelper EB(int n, int n2, int n3) {
      return this.Ey(n, n2).EA(n3);
   }

   public TextTooltipHelper EC(@Nullable Tooltip tooltip2) {
      this.tooltip = tooltip2;
      return this;
   }

   public TextTooltipHelper ED(MutableTextHandler mutableTextHandler2) {
      this.mutableTextHandler = mutableTextHandler2;
      return this;
   }

   public TextTooltipHelper EE(String str) {
      this.text2 = str;
      return this;
   }

   public DrawTextPressableWidget EF() {
      DrawTextPressableWidget drawTextPressableWidget = new DrawTextPressableWidget(
         this.d, this.e, this.f, this.g, this.text, this.unidentifiedInterface7, this.mutableTextHandler, this.text2
      );
      DrawTextPressableWidget temp = drawTextPressableWidget;
      Tooltip tooltip2 = this.tooltip;
      temp.setTooltip(tooltip2);
      return drawTextPressableWidget;
   }
}
