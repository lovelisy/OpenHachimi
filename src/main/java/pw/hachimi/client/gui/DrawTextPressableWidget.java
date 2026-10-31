package pw.hachimi.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.api.UnidentifiedInterface7;
import pw.hachimi.client.handler.MutableTextHandler;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.FontLoader;
import pw.hachimi.client.utils.render.FramebufferWindowUtils;
import pw.hachimi.client.utils.render.TextTooltipHelper;

public class DrawTextPressableWidget extends PressableWidget {
   public static final MutableTextHandler a_ = supplier -> (MutableText)supplier.get();
   protected final UnidentifiedInterface7 unidentifiedInterface7;
   protected final String text;
   protected final MutableTextHandler mutableTextHandler;
   private final Color color = new Color(0, 0, 0, 80);
   private final Color color2 = new Color(255, 255, 255, 140);
   UnidentifiedHelper7 unidentifiedHelper7 = new UnidentifiedHelper7();
   private static final long a = 0L;

   public DrawTextPressableWidget(int n, int n2, int n3, int n4, Text text2, UnidentifiedInterface7 unidentifiedInterface72, MutableTextHandler mutableTextHandler2, String str) {
      super(n, n2, n3, n4, text2);
      this.unidentifiedInterface7 = unidentifiedInterface72;
      this.mutableTextHandler = mutableTextHandler2;
      this.text = str;
   }

   public static TextTooltipHelper apm(Text text2, UnidentifiedInterface7 unidentifiedInterface72) {
      return new TextTooltipHelper(text2, unidentifiedInterface72);
   }

   protected static void apn(DrawContext context2, Text text2, String str, int n, int n2, int n3, int n4, int n5) {
      apo(context2, text2, str, (n + n3) / 2, n2, n4, n5);
   }

   protected static void apo(DrawContext context2, Text text2, String str, int n, int n2, int n3, int n4) {
      int n5 = RenderUtils.acF(text2.getString());
      int n6 = (n2 + n3 - 9) / 2 + 1;
      int n7 = n - n5 / 2;
      if (str != null) {
         int n8 = (int)FontLoader.fontRenderer3.abc(str);
         FontLoader.fontRenderer3.aaS(context2.getMatrices(), str, n7 - n8 * 1.5, n6 + 0.5, n4);
      }

      RenderUtils.acE(context2, text2.getString(), n7, n6, n4);
   }

   public void onPress() {
      this.unidentifiedInterface7.onPress(this);
   }

   protected MutableText getNarrationMessage() {
      return this.mutableTextHandler.createNarrationMessage(this::apq);
   }

   public void appendClickableNarrations(NarrationMessageBuilder builder) {
   }

   protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
      this.unidentifiedHelper7.qV(this.isSelected());
      MinecraftClient client2 = MinecraftClient.getInstance();
      RenderSystem.enableBlend();
      RenderSystem.enableDepthTest();
      UnidentifiedHelper7 unidentifiedHelper72 = this.unidentifiedHelper7;
      Color color3 = this.color2;
      Color color4 = this.color;
      double d = unidentifiedHelper72.qX() * this.unidentifiedHelper7.qX();
      Color color5 = ColorUtils.iD(color4, color3, d);
      final Color color6 = color5;
      final DrawContext context2 = context;
      final DrawTextPressableWidget drawTextPressableWidget = this;
      FramebufferWindowUtils.wF(new Runnable() {
         @Override
         public void run() {
            drawTextPressableWidget.app(context2, color6);
         }
      }, 10.0F, delta);
      int n;
      if (this.active) {
         n = 16777215;
      } else {
         n = 10526880;
      }

      int n2 = n;
      this.drawMessage(context, client2.textRenderer, n2 | MathHelper.ceil(this.alpha * 255.0F) << 24);
   }

   public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
      this.drawScrollableText(context, textRenderer, 2, color);
   }

   protected void drawScrollableText(DrawContext context, TextRenderer textRenderer, int xMargin, int color) {
      int n = this.getX() + 2;
      int n2 = this.getX() + this.getWidth() - 2;
      apn(context, this.getMessage(), this.text, n, this.getY(), n2, this.getY() + this.getHeight(), color);
   }

   void app(DrawContext context2, Color color3) {
      RenderUtils.acH(context2.getMatrices(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 4.0F, color3);
   }

   private MutableText apq() {
      return super.getNarrationMessage();
   }
}
