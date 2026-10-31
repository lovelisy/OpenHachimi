package pw.hachimi.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import pw.hachimi.client.event.render.RenderEvent3;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class CrystalModel extends ClientModule {
   private static CrystalModel INSTANCE;
   private final Identifier id = Identifier.of("hachimiclient", "textures/glow.png");
   Setting glow = this.registerSetting(new BooleanSetting("Glow", "Draw bloom on crystals", false));
   Setting color = this.registerSetting(new ColorSetting("Color", "Color of glow", r(), this::isColorVisible));
   Setting spin = this.registerSetting(new NumberSetting("Spin", "The spin speed of crystals", 0.0F, 1.0F, 10.0F));
   Setting bounce = this.registerSetting(new BooleanSetting("Bounce", "The crystal bounce", true));
   Setting scale = this.registerSetting(new NumberSetting("Scale", "The scale of crystals", 0.1F, 1.0F, 1.5F));
   Setting offset = this.registerSetting(new NumberSetting("Offset", "The offset of crystals", -1.0F, 0.0F, 1.0F));
   public static boolean ga;

   public CrystalModel() {
      super("CrystalModel", "Renders the crystal model", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static CrystalModel kh() {
      return INSTANCE;
   }

   @EventListener(priority = 1)
   public void onRenderEvent3(RenderEvent3 renderEvent3) {
      renderEvent3.oT((Float)this.spin.atx());
      renderEvent3.oX((Boolean)this.bounce.atx());
      renderEvent3.oV((Float)this.scale.atx());
      renderEvent3.h = (Float)this.offset.atx();
      if ((Boolean)this.glow.atx()) {
         float f;
         if (!renderEvent3.oW()) {
            f = -1.2F;
         } else {
            float f2 = renderEvent3.c;
            f = EndCrystalEntityRenderer.getYOffset(renderEvent3.endCrystalEntity, f2);
         }

         float f3 = f + renderEvent3.h;
         renderEvent3.matrices.push();
         MatrixStack temp = renderEvent3.matrices;
         Quaternionf quaternionf = renderEvent3.getEntityRenderDispatcher().getRotation();
         temp.multiply(quaternionf);
         Camera camera = client.gameRenderer.getCamera();
         Vec3d vec = camera.getPos();
         temp = renderEvent3.matrices;
         RotationAxis temp2 = RotationAxis.POSITIVE_X;
         float f4 = camera.getPitch();
         Quaternionf quaternionf2 = temp2.rotationDegrees(f4);
         temp.multiply(quaternionf2);
         temp = renderEvent3.matrices;
         temp2 = RotationAxis.POSITIVE_Y;
         float f5 = camera.getYaw() + 180.0F;
         Quaternionf quaternionf3 = temp2.rotationDegrees(f5);
         temp.multiply(quaternionf3);
         temp = renderEvent3.matrices;
         double d = renderEvent3.endCrystalEntity.getX() - vec.getX();
         double d2 = renderEvent3.endCrystalEntity.getZ() - vec.getZ();
         double d3 = renderEvent3.endCrystalEntity.getY() - vec.getY();
         double d4 = d;
         temp.translate(d4, d3, d2);
         temp = renderEvent3.matrices;
         float f6;
         float f7 = f6 = renderEvent3.oU();
         float f8 = renderEvent3.oU();
         temp.scale(f8, f7, f6);
         renderEvent3.matrices.scale(2.0F, 2.0F, 2.0F);
         float f9 = 1.5F + f3 / 2.0F;
         renderEvent3.matrices.translate(0.0F, f9, 0.0F);
         temp = renderEvent3.matrices;
         temp2 = RotationAxis.POSITIVE_Y;
         float f10 = -camera.getYaw();
         Quaternionf quaternionf4 = temp2.rotationDegrees(f10);
         temp.multiply(quaternionf4);
         temp = renderEvent3.matrices;
         temp2 = RotationAxis.POSITIVE_X;
         float f11 = camera.getPitch();
         Quaternionf quaternionf5 = temp2.rotationDegrees(f11);
         temp.multiply(quaternionf5);
         temp = renderEvent3.matrices;
         Quaternionf quaternionf6 = RotationAxis.POSITIVE_Z.rotationDegrees(180.0F);
         temp.multiply(quaternionf6);
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.setShaderTexture(0, this.id);
         renderEvent3.matrices.translate(-2.5F, -2.5F, 0.0F);
         RenderSystem.setShaderColor(
            ((Color)this.color.atx()).getRed() / 255.0F,
            ((Color)this.color.atx()).getGreen() / 255.0F,
            ((Color)this.color.atx()).getBlue() / 255.0F,
            ((Color)this.color.atx()).getAlpha() / 255.0F
         );
         this.kj(renderEvent3.matrices);
         RenderSystem.disableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         renderEvent3.matrices.pop();
      }
   }

   public void kj(MatrixStack matrices2) {
      this.kk(matrices2);
   }

   public void kk(MatrixStack matrices2) {
      this.kl(matrices2);
   }

   private void kl(MatrixStack matrices2) {
      this.km(matrices2.peek().getPositionMatrix());
   }

   public void km(Matrix4f matrix4f) {
      RenderSystem.setShader(GameRenderer::getPositionTexProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_TEXTURE;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      bufferBuilder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).texture(0.0F, 0.0F);
      bufferBuilder.vertex(matrix4f, 0.0F, 5.0F, 0.0F).texture(0.0F, 1.0F);
      bufferBuilder.vertex(matrix4f, 5.0F, 5.0F, 0.0F).texture(1.0F, 1.0F);
      bufferBuilder.vertex(matrix4f, 5.0F, 0.0F, 0.0F).texture(1.0F, 0.0F);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
   }

   public boolean kn() {
      return (Boolean)this.bounce.atx();
   }

   public float ko() {
      return (Float)this.spin.atx();
   }

   public float kp() {
      return (Float)this.scale.atx();
   }

   public float kq() {
      return (Float)this.offset.atx();
   }

   private Boolean isColorVisible() {
      return (Boolean)this.glow.atx();
   }

   private static Color r() {
      return new Color(255, 255, 255, 100);
   }
}
