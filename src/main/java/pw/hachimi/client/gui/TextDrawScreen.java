package pw.hachimi.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.render.PhantomView;
import pw.hachimi.client.render.FramebufferFramebuffer;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.UnidentifiedUtils2;
import pw.hachimi.satin.ManagedShaderEffect;

public class TextDrawScreen extends Screen {
   private static final double a = 1.7777777777777777;
   private static final Identifier b = Identifier.of("hachimiclient", "textures/background.png");
   private static final Identifier c = Identifier.of("hachimiclient", "textures/logo.png");
   private static final Stopwatch d = new StopwatchImpl();
   public static boolean e;
   private static double f = 0.5;
   private static double g = 0.5;
   private boolean h;
   private long i;

   public TextDrawScreen(boolean flag) {
      super(Text.of("CustomTitleScreen"));
      this.h = flag;
   }

   public static void ey(int n, int n2, int n3, int n4) {
      if (n != 0 || n2 != 0) {
         double d2;
         if (ColorHelper3.auO().auP()) {
            d2 = 0.01;
         } else {
            d2 = 0.02;
         }

         double d3 = d2;
         f = PhantomView.DH(f, (double)n / n3, d3);
         g = PhantomView.DH(g, (float)n2 / n4, d3);
         f = Math.min(f, 1.0);
         g = Math.min(g, 1.0);
         f = Math.max(f, 0.0);
         g = Math.max(g, 0.0);
      }
   }

   public static void ez(DrawContext context2, float f2, float f3) {
      double d2 = f2 / f3;
      float f4;
      float f5;
      if (d2 < 1.7777777777777777) {
         float f6 = (float)(f3 * 1.7777777777777777);
         f4 = 200.0F + (f6 - f2);
         f5 = 200.0F;
      } else if (d2 > 1.7777777777777777) {
         float f7 = (float)(f2 / 1.7777777777777777);
         f4 = 200.0F;
         f5 = 200.0F + (f7 - f3);
      } else {
         f4 = 200.0F;
         f5 = 200.0F;
      }

      if (ColorHelper3.auO().auP()) {
         ManagedShaderEffect managedShaderEffect = Managers.framebufferBuilderHelper.getManagedShaderEffect2();
         managedShaderEffect.setUniformValue("resolution", Mc.client.getWindow().getScaledWidth(), Mc.client.getWindow().getScaledHeight());
         managedShaderEffect.setUniformValue("time", (float)d.afs() / 1000.0F);
         managedShaderEffect.setUniformValue("mouse", (float)f, (float)(1.0 - g));
         managedShaderEffect.render(Mc.client.getRenderTickCounter().getTickDelta(true));
      } else {
         RenderSystem.setShaderTexture(0, b);
         final float f8 = f3;
         final float f9 = f2;
         final float f10 = f5;
         final float f11 = f4;
         final DrawContext context3 = context2;
         FramebufferFramebuffer.adP(16, new Runnable() {
            @Override
            public void run() {
               TextDrawScreen.eL(context3, f11, f10, f9, f8);
            }
         });
      }
   }

   public static void eA(DrawContext context2, float f2, float f3, float f4, float f5, float f6, float f7) {
      eB(context2, f2, f3, f4, f5, f4, f5, f6, f7);
   }

   private static void eB(DrawContext context2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
      eC(context2, f2, f2 + f4, f3, f3 + f5, f6, f7, f8, f9);
   }

   private static void eC(DrawContext context2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
      eD(context2.getMatrices().peek().getPositionMatrix(), f2, f3, f4, f5, 0.0F / f8, (0.0F + f6) / f8, 0.0F / f9, (0.0F + f7) / f9);
   }

   private static void eD(Matrix4f matrix4f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
      RenderSystem.setShader(GameRenderer::getPositionTexProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_TEXTURE;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      bufferBuilder.vertex(matrix4f, f2, f4, 0.0F).texture(f6, f8);
      bufferBuilder.vertex(matrix4f, f2, f5, 0.0F).texture(f6, f9);
      bufferBuilder.vertex(matrix4f, f3, f5, 0.0F).texture(f7, f9);
      bufferBuilder.vertex(matrix4f, f3, f4, 0.0F).texture(f7, f8);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
   }

   public boolean shouldPause() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   protected void init() {
      Text text = Text.of(("Minecraft " + SharedConstants.getGameVersion().getName()).toLowerCase());
      int n = RenderUtils.acF(text.getString());
      int n2 = this.width / 2 - 200 / 2;
      int n3 = this.height / 2 - 48 + 30;
      boolean flag = Bootstrap.NY();
      if (flag) {
         try {
            Class.forName("ru.vidtu.ias.screen.AccountScreen");
            n3 += -12;
         } catch (Exception exception) {
            Bootstrap.Ob("Not found IAS");
            flag = false;
         }
      }

      this.addDrawableChild(DrawTextPressableWidget.apm(Text.of("singleplayer"), this::eK).EE("5").EB(n2, n3, 200).EF());
      this.addDrawableChild(DrawTextPressableWidget.apm(Text.of("multiplayer"), this::eJ).EE("4").EB(n2, n3 + 24, 200).EF());
      this.addDrawableChild(DrawTextPressableWidget.apm(Text.of("options"), this::eI).EE("7").EB(n2, n3 + 72, 98).EF());
      this.addDrawableChild(DrawTextPressableWidget.apm(Text.of("quit"), this::eH).EE("X").EB(n2 + 102, n3 + 72, 98).EF());
      if (flag) {
         this.addDrawableChild(DrawTextPressableWidget.apm(Text.of("altmanager"), this::eG).EE("c").EB(n2, n3 + 48, 200).EF());
      }

      int n4 = RenderUtils.acG();
      this.addDrawableChild(new TextDrawButtonWidget(this.height - n4 - 4, n, text, this::eF));
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      ey(mouseX, mouseY, this.width, this.height);
      if (this.i == 0L && this.h) {
         this.i = Util.getMeasuringTimeMs();
      }

      float f2 = 1.0F;
      if (this.h) {
         float f3 = (float)(Util.getMeasuringTimeMs() - this.i) / 2000.0F;
         if (f3 > 1.0F) {
            this.h = false;
         } else {
            f2 = MathHelper.clampedMap(MathHelper.clamp(f3, 0.0F, 1.0F), 0.5F, 1.0F, 0.0F, 1.0F);
         }

         this.eE(f2);
      }

      this.renderPanoramaBackground(context, delta);
      int n = MathHelper.ceil(f2 * 255.0F) << 24;
      if ((n & -67108864) != 0) {
         Iterator iterator2 = this.drawables.iterator();

         while (iterator2.hasNext()) {
            ((Drawable)iterator2.next()).render(context, mouseX, mouseY, delta);
         }

         int n2 = RenderUtils.acG();
         Object[] objs = new Object[]{"Hachimi", "2.0"};
         String temp = String.format("%s %s", objs).toLowerCase();
         float temp2 = this.height - n2 * 2 - 4;
         int n3 = 16777215 | n;
         float f4 = temp2;
         String str = temp;
         RenderUtils.acE(context, str, 5.0F, f4, n3);
      }

      int n4 = this.height / 2 - 48 - 30;
      RenderSystem.enableBlend();
      Color color = ColorHelper3.auO().auU();
      float f5 = color.getRed() / 255.0F;
      float temp3 = color.getGreen() / 255.0F;
      float temp4 = color.getBlue() / 255.0F;
      float f6 = f2;
      float f7 = temp4;
      float f8 = temp3;
      float f9 = f5;
      context.setShaderColor(f9, f8, f7, f6);
      RenderSystem.setShaderTexture(0, c);
      eA(context, (int)(this.width / 2.0 - 76.0), n4 - 14, 150.0F, 54.0F, 150.0F, 54.0F);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
   }

   private void eE(float f2) {
      Iterator iterator2 = this.children().iterator();

      while (iterator2.hasNext()) {
         Element element = (Element)iterator2.next();
         if (element instanceof ClickableWidget) {
            ((ClickableWidget)element).setAlpha(f2);
         }
      }
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }

   protected void renderPanoramaBackground(DrawContext context, float delta) {
      if (ColorHelper3.auO().auQ()) {
         ez(context, this.width, this.height);
      } else {
         int n = this.height;
         int n2 = this.width;
         ROTATING_PANORAMA_RENDERER.render(context, n2, n, 1.0F, delta);
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return super.mouseClicked(mouseX, mouseY, button);
   }

   private void eF(ButtonWidget buttonWidget) {
      e = true;
      MinecraftClient temp = this.client;
      TitleScreen titleScreen = new TitleScreen();
      temp.setScreen((Screen)titleScreen);
      e = false;
   }

   private void eG(DrawTextPressableWidget drawTextPressableWidget) {
      UnidentifiedUtils2.mM(this);
   }

   private void eH(DrawTextPressableWidget drawTextPressableWidget) {
      this.client.scheduleStop();
   }

   private void eI(DrawTextPressableWidget drawTextPressableWidget) {
      MinecraftClient temp = this.client;
      OptionsScreen optionsScreen = new OptionsScreen(this, this.client.options);
      temp.setScreen((Screen)optionsScreen);
   }

   private void eJ(DrawTextPressableWidget drawTextPressableWidget) {
      MinecraftClient temp = this.client;
      MultiplayerScreen multiplayerScreen = new MultiplayerScreen(this);
      temp.setScreen((Screen)multiplayerScreen);
   }

   private void eK(DrawTextPressableWidget drawTextPressableWidget) {
      MinecraftClient temp = this.client;
      SelectWorldScreen selectWorldScreen = new SelectWorldScreen(this);
      temp.setScreen((Screen)selectWorldScreen);
   }

   static void eL(DrawContext context2, float f2, float f3, float f4, float f5) {
      eA(context2, (float)(-f2 * f), (float)(-f3 * g), f4 + f2, f5 + f3, f4 + f2, f5 + f3);
   }
}
