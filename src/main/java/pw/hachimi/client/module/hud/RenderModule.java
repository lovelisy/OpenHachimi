package pw.hachimi.client.module.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import org.joml.Matrix4f;
import pw.hachimi.client.manager.DrawMatrixManager;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.LeftMiddleMode;
import pw.hachimi.client.model.render.TopMiddleMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.client.HUD;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.client.utils.render.RenderModuleHelper;

public abstract class RenderModule extends ClientModule {
   public Setting numberSetting1 = this.registerSetting(new NumberSetting("X-Position", "", 0.0F, 100.0F, 9999.0F, RenderModule::isNumberSetting1Visible));
   public Setting numberSetting2 = this.registerSetting(new NumberSetting("Y-Position", "", 0.0F, 100.0F, 9999.0F, RenderModule::isNumberSetting2Visible));
   public Setting leftMiddleMode = this.registerSetting(new EnumSetting("X-Anchor", "", LeftMiddleMode.LEFT, LeftMiddleMode.values(), RenderModule::isLeftMiddleModeVisible));
   public Setting topMiddleMode = this.registerSetting(new EnumSetting("Y-Anchor", "", TopMiddleMode.TOP, TopMiddleMode.values(), RenderModule::isTopMiddleModeVisible));
   protected Setting textSetting1 = this.registerSetting(new StringSetting("SnapAt", "", "", RenderModule::isTextSetting1Visible));
   protected Setting booleanSetting1_2 = this.registerSetting(new BooleanSetting("BackGround", "", false));
   public Setting numberSetting3 = this.registerSetting(new NumberSetting("Round", "", 0.0F, 4.0F, 10.0F, this::isNumberSetting3Visible));
   public Setting colorSetting1 = this.registerSetting(new ColorSetting("BGColor", "", new Color(0, 0, 0, 100), true, false, this::isColorSetting1Visible));
   protected Setting booleanSetting2_2 = this.registerSetting(new BooleanSetting("PostEffect", "", true, this::isBooleanSetting2Visible));

   public RenderModule(String str, String str2) {
      super(str, str2, ModuleCategory.HUD);
      this.OK(this.getBooleanSetting1());
   }

   public void acY(DrawContext context) {
      float f = this.add();
      float f2 = this.ade();
      float f3 = this.adl();
      float f4 = this.adm();
      ado(context, f, f2, f3, f4, Color.WHITE);
      adp(f + f3 / 2.0F, f2 + f4 / 2.0F - 2.0F, f + f3 / 2.0F, f2 + f4 / 2.0F + 2.0F, Color.WHITE);
      adp(f + f3 / 2.0F - 2.0F, f2 + f4 / 2.0F, f + f3 / 2.0F + 2.0F, f2 + f4 / 2.0F, Color.WHITE);
   }

   public boolean acZ(double d, double d2) {
      return ada(d, d2, this.add(), this.ade(), this.adl(), this.adm());
   }

   public static boolean ada(double d, double d2, double d3, double d4, double d5, double d6) {
      return d >= d3 && d <= d3 + d5 && d2 >= d4 && d2 <= d4 + d6;
   }

   public abstract void adb();

   public abstract void adc(DrawContext var1);

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public float add() {
      if (!((String)this.textSetting1.atx()).isEmpty()) {
         RenderModule renderModule = Managers.drawMatrixManager.Hy((String)this.textSetting1.atx());
         if (renderModule != null) {
            switch (RenderModuleHelper.a[((LeftMiddleMode)this.leftMiddleMode.atx()).ordinal()]) {
               case 1:
                  return renderModule.add() - this.adl();
               case 2:
                  return renderModule.add() + renderModule.adl();
               case 3:
                  return renderModule.add() + (Float)this.numberSetting1.atx();
               case 4:
                  return renderModule.add() + renderModule.adl() - this.adl() - (Float)this.numberSetting1.atx();
               case 5:
                  return renderModule.add() + renderModule.adl() / 2.0F - this.adl() / 2.0F;
            }
         }
      }

      switch (RenderModuleHelper.a[((LeftMiddleMode)this.leftMiddleMode.atx()).ordinal()]) {
         case 1:
            return (Float)this.numberSetting1.atx();
         case 2:
            return client.getWindow().getScaledWidth() - this.adl() + (Float)this.numberSetting1.atx();
         case 3:
         case 4:
         default:
            return (Float)this.numberSetting1.atx();
         case 5:
            return client.getWindow().getScaledWidth() / 2.0F - this.adl() / 2.0F + (Float)this.numberSetting1.atx();
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public float ade() {
      if (!((String)this.textSetting1.atx()).isEmpty()) {
         RenderModule renderModule = Managers.drawMatrixManager.Hy((String)this.textSetting1.atx());
         if (renderModule != null) {
            switch (RenderModuleHelper.b[((TopMiddleMode)this.topMiddleMode.atx()).ordinal()]) {
               case 1:
                  return renderModule.ade() - this.adm();
               case 2:
                  return renderModule.ade() + renderModule.adm();
               case 3:
                  return renderModule.ade() + (Float)this.numberSetting2.atx();
               case 4:
                  return renderModule.ade() + renderModule.adm() - this.adm() - (Float)this.numberSetting2.atx();
               case 5:
                  return renderModule.ade() + renderModule.adm() / 2.0F - this.adm() / 2.0F;
            }
         }
      }

      switch (RenderModuleHelper.b[((TopMiddleMode)this.topMiddleMode.atx()).ordinal()]) {
         case 1:
            return (Float)this.numberSetting2.atx();
         case 2:
            return (float)(
               client.getWindow().getScaledHeight() - this.adm() + (Float)this.numberSetting2.atx()
                  - ((Float)this.numberSetting2.atx() >= -14.0F ? HUD.zg().zj() : 0.0)
            );
         case 3:
         case 4:
         default:
            return (Float)this.numberSetting2.atx();
         case 5:
            return client.getWindow().getScaledHeight() / 2.0F - this.adm() / 2.0F + (Float)this.numberSetting2.atx();
      }
   }

   public void adf(float f, float f2, LeftMiddleMode leftMiddleMode2, TopMiddleMode topMiddleMode2) {
      this.numberSetting1.aty(f);
      this.numberSetting2.aty(f2);
      this.leftMiddleMode.aty(leftMiddleMode2);
      this.topMiddleMode.aty(topMiddleMode2);
      this.textSetting1.aty("");
   }

   public void adg(float f, TopMiddleMode topMiddleMode2, String str) {
      this.numberSetting2.aty(f);
      this.topMiddleMode.aty(topMiddleMode2);
      this.textSetting1.aty(str);
   }

   public void adh(float f, LeftMiddleMode leftMiddleMode2, String str) {
      this.numberSetting1.aty(f);
      this.leftMiddleMode.aty(leftMiddleMode2);
      this.textSetting1.aty(str);
   }

   public String adi() {
      return (String)this.textSetting1.atx();
   }

   public boolean adj() {
      return (Boolean)this.booleanSetting2_2.atx();
   }

   public boolean adk() {
      return (Boolean)this.booleanSetting1_2.atx();
   }

   public float adl() {
      return (Boolean)this.booleanSetting1_2.atx() ? 4.0F : 0.0F;
   }

   public float adm() {
      return (Boolean)this.booleanSetting1_2.atx() ? 4.0F : 0.0F;
   }

   public void adn(DrawContext context) {
      RenderUtils.acH(context.getMatrices(), this.add(), this.ade(), this.adl(), this.adm(), (Float)this.numberSetting3.atx(), (Color)this.colorSetting1.atx());
   }

   public static void ado(DrawContext context, float f, float f2, float f3, float f4, Color color2) {
      Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.DEBUG_LINE_STRIP;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      float f5 = f2 + f4;
      VertexConsumer vertexConsumer = bufferBuilder.vertex(matrix4f, f, f5, 0.0F);
      int n = color2.getRGB();
      vertexConsumer.color(n);
      float temp2 = f + f3;
      float f6 = f2 + f4;
      float f7 = temp2;
      VertexConsumer vertexConsumer2 = bufferBuilder.vertex(matrix4f, f7, f6, 0.0F);
      int n2 = color2.getRGB();
      vertexConsumer2.color(n2);
      float f8 = f + f3;
      VertexConsumer vertexConsumer3 = bufferBuilder.vertex(matrix4f, f8, f2, 0.0F);
      int n3 = color2.getRGB();
      vertexConsumer3.color(n3);
      VertexConsumer vertexConsumer4 = bufferBuilder.vertex(matrix4f, f, f2, 0.0F);
      int n4 = color2.getRGB();
      vertexConsumer4.color(n4);
      float f9 = f2 + f4;
      VertexConsumer vertexConsumer5 = bufferBuilder.vertex(matrix4f, f, f9, 0.0F);
      int n5 = color2.getRGB();
      vertexConsumer5.color(n5);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   public static void adp(float f, float f2, float f3, float f4, Color color2) {
      DrawMatrixManager.drawTextLayerTypeHelper.xb(f, f2, f3, f4, color2);
   }

   public void adq(DrawContext context, String str, float f, float f2) {
      int n = HUD.zg().zo(f2);
      this.adr(context, str, f, f2 + 1.0F, n);
   }

   public void adr(DrawContext context, String str, float f, float f2, int n) {
      DrawMatrixManager.drawTextLayerTypeHelper.wZ(context, str, f, f2 + 1.0F, n);
   }

   public LeftMiddleMode ads() {
      return (LeftMiddleMode)this.leftMiddleMode.atx();
   }

   public TopMiddleMode adt() {
      return (TopMiddleMode)this.topMiddleMode.atx();
   }

   @Override
   public boolean WS() {
      return true;
   }

   private Boolean isBooleanSetting2Visible() {
      return (Boolean)this.booleanSetting1_2.atx();
   }

   private Boolean isColorSetting1Visible() {
      return (Boolean)this.booleanSetting1_2.atx();
   }

   private Boolean isNumberSetting3Visible() {
      return (Boolean)this.booleanSetting1_2.atx();
   }

   private static Boolean isTextSetting1Visible() {
      return false;
   }

   private static Boolean isTopMiddleModeVisible() {
      return false;
   }

   private static Boolean isLeftMiddleModeVisible() {
      return false;
   }

   private static Boolean isNumberSetting2Visible() {
      return false;
   }

   private static Boolean isNumberSetting1Visible() {
      return false;
   }
}
