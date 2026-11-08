package pw.hachimi.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.TargetESPGhostTapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.combat.Aura;
import pw.hachimi.client.module.combat.AutoAnchor;
import pw.hachimi.client.module.combat.AutoCrystal;
import pw.hachimi.client.module.combat.AutoMine;
import pw.hachimi.client.module.combat.PistonCrystal;
import pw.hachimi.client.module.combat.SpearAura;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.render.Vec3dGameRendererHelper;
import pw.hachimi.client.utils.world.EntityBoxUtils;
import pw.hachimi.eventbus.annotation.EventListener;
import java.util.HashMap;

public class TargetESP extends ClientModule {
   Setting mode = this.registerSetting(new EnumSetting("Mode", "mode", TargetESPGhostTapeMode.Jello, TargetESPGhostTapeMode.values()));
   Setting color = this.registerSetting(createColorSetting("Color", "ESP color", d(), this::isColorVisible));
   Setting shine = this.registerSetting(new BooleanSetting("Shine", "Enables color shine", false, this::isShineVisible));
   private static final Identifier[] d_ = new Identifier[30];
   private final Identifier id = Identifier.of("hachimiclient", "textures/glow.png");
   private final StopwatchImpl stopwatchImpl = new StopwatchImpl();
   int g = 0;
   private static final long a = 0L;
   private static final String[] b = null;
   private static final String[] d = null;
   private static final Map e = new HashMap();

   static {
      for (int n = 1; n <= 30; n++) {
         d_[n - 1] = Identifier.of("hachimiclient", "textures/wtf/frame_" + n + ".png");
      }
   }

   public TargetESP() {
      super("TargetESP", "See targets through walls", ModuleCategory.RENDER);
   }

   @EventListener(priority = -1)
   public void apF(Render3DContext render3DContext) {
      if (client.gameRenderer != null && client.world != null && client.getCameraEntity() != null) {
         Entity entity2 = AutoCrystal.sc().sd();
         if (entity2 == null) {
            entity2 = AutoAnchor.aaj().aak();
         }

         if (entity2 == null) {
            entity2 = PistonCrystal.z().entity;
         }

         if (entity2 == null) {
            entity2 = Aura.aoS().apb();
         }

         if (entity2 == null) {
            entity2 = AutoMine.ars().getEntity();
         }

         if (entity2 == null && SpearAura.avm() != null) {
            entity2 = SpearAura.avm().avq();
         }

         if (entity2 != null) {
            if (!RenderUtils.acy(entity2.getBoundingBox().stretch(1.0, 1.0, 1.0))) {
               return;
            }

            if ((Boolean)this.shine.atx() && (this.mode.atx() == TargetESPGhostTapeMode.Tape || this.mode.atx() == TargetESPGhostTapeMode.Jello)) {
               render3DContext.boxRenderer.YH(render3DContext.matrices);
               render3DContext.boxRenderer.YF();
               Vec3dGameRendererHelper.b = true;
            }

            switch ((TargetESPGhostTapeMode)this.mode.atx()) {
               case Ghost:
                  this.apK(entity2, render3DContext.matrices, render3DContext.f);
                  break;
               case Tape:
                  this.apG(render3DContext, entity2, (Color)this.color.atx(), render3DContext.f);
                  break;
               case Jello:
                  this.apJ(render3DContext, entity2, (Color)this.color.atx(), render3DContext.f);
                  break;
               case PVZ:
                  this.apH(entity2, render3DContext.matrices, render3DContext.f);
            }

            if ((Boolean)this.shine.atx() && (this.mode.atx() == TargetESPGhostTapeMode.Tape || this.mode.atx() == TargetESPGhostTapeMode.Jello)) {
               render3DContext.boxRenderer.YH(render3DContext.matrices);
               Vec3dGameRendererHelper.b = false;
               render3DContext.boxRenderer.YF();
            }
         }
      }
   }

   public void apG(Render3DContext render3DContext, Entity entity2, Color color2, float f2) {
      ArrayList arrayList = new ArrayList();
      ArrayList arrayList2 = new ArrayList();
      ArrayList arrayList3 = new ArrayList();
      double d2 = entity2.prevX + (entity2.getX() - entity2.prevX) * f2;
      double d3 = entity2.prevY + (entity2.getY() - entity2.prevY) * f2;
      double d4 = entity2.prevZ + (entity2.getZ() - entity2.prevZ) * f2;
      double d5 = entity2.getHeight();
      int n = 0;

      do {
         double d6 = Math.sin(Math.toRadians(n));
         Vec3d vec = new Vec3d((float)(Math.cos(Math.toRadians(n)) * 0.5) + d2, d5 + d3, (float)(d6 * 0.5) + d4);
         arrayList.add(vec);
         double d7 = Math.sin(Math.toRadians((n + 120) % 360));
         Vec3d vec2 = new Vec3d((float)(Math.cos(Math.toRadians((n + 120) % 360)) * 0.5) + d2, d5 + d3, (float)(d7 * 0.5) + d4);
         arrayList2.add(vec2);
         double d8 = Math.sin(Math.toRadians((n + 240) % 360));
         Vec3d vec3 = new Vec3d((float)(Math.cos(Math.toRadians((n + 240) % 360)) * 0.5) + d2, d5 + d3, (float)(d8 * 0.5) + d4);
         arrayList3.add(vec3);
         d5 -= 0.004F;
      } while (++n <= 361);

      Vec3dGameRendererHelper vec3dGameRendererHelper = render3DContext.boxRenderer.vec3dGameRendererHelper2;

      for (int n2 = 0; n2 < arrayList.size() - 1; n2++) {
         float f3 = 1.0F - (n2 + (float)this.stopwatchImpl.afs() / 5.0F) % 360.0F / 60.0F;
         Color color3 = ColorUtils.iE(ColorUtils.iC(color2, (int)(n2 / 20.0F)), (int)(f3 * 255.0F));
         int n3 = n2;
         Vec3dGameRendererHelper temp = vec3dGameRendererHelper;
         ArrayList temp2 = arrayList;
         int n4 = n3;
         double d9 = (float)((Vec3d)temp2.get(n4)).x;
         ArrayList temp3 = arrayList;
         int n5 = n2;
         double d10 = (float)((Vec3d)temp3.get(n5)).y;
         ArrayList temp4 = arrayList;
         int n6 = n2;
         int n7 = temp.py(d9, d10, (float)((Vec3d)temp4.get(n6)).z).pA(color3).pB();
         n3 = n2 + 1;
         Vec3dGameRendererHelper vec3dGameRendererHelper3 = vec3dGameRendererHelper;
         double d11 = (float)((Vec3d)arrayList.get(n3)).x;
         temp4 = arrayList;
         int n8 = n2 + 1;
         double d12 = (float)((Vec3d)temp4.get(n8)).y + 0.1F;
         ArrayList temp5 = arrayList;
         int n9 = n2 + 1;
         int n10 = vec3dGameRendererHelper3.py(d11, d12, (float)((Vec3d)temp5.get(n9)).z).pA(color3).pB();
         n3 = n2 + 1;
         Vec3dGameRendererHelper vec3dGameRendererHelper4 = vec3dGameRendererHelper;
         double d13 = (float)((Vec3d)arrayList.get(n3)).x;
         temp5 = arrayList;
         int n11 = n2 + 1;
         double d14 = (float)((Vec3d)temp5.get(n11)).y;
         ArrayList temp6 = arrayList;
         int n12 = n2 + 1;
         n3 = vec3dGameRendererHelper4.py(d13, d14, (float)((Vec3d)temp6.get(n12)).z).pA(color3).pB();
         int n13 = n10;
         int n14 = n7;
         vec3dGameRendererHelper.pE(n14, n13, n3);
         n3 = n2;
         temp = vec3dGameRendererHelper;
         temp2 = arrayList;
         int n15 = n3;
         double d15 = (float)((Vec3d)temp2.get(n15)).x;
         temp3 = arrayList;
         int n16 = n2;
         double d16 = (float)((Vec3d)temp3.get(n16)).y;
         temp4 = arrayList;
         int n17 = n2;
         int n18 = temp.py(d15, d16, (float)((Vec3d)temp4.get(n17)).z).pA(color3).pB();
         n3 = n2;
         Vec3dGameRendererHelper vec3dGameRendererHelper5 = vec3dGameRendererHelper;
         temp3 = arrayList;
         int n19 = n3;
         double d17 = (float)((Vec3d)temp3.get(n19)).x;
         temp4 = arrayList;
         int n20 = n2;
         double d18 = (float)((Vec3d)temp4.get(n20)).y + 0.1F;
         temp5 = arrayList;
         int n21 = n2;
         int n22 = vec3dGameRendererHelper5.py(d17, d18, (float)((Vec3d)temp5.get(n21)).z).pA(color3).pB();
         n3 = n2 + 1;
         Vec3dGameRendererHelper vec3dGameRendererHelper6 = vec3dGameRendererHelper;
         double d19 = (float)((Vec3d)arrayList.get(n3)).x;
         temp5 = arrayList;
         int n23 = n2 + 1;
         double d20 = (float)((Vec3d)temp5.get(n23)).y + 0.1F;
         temp6 = arrayList;
         int n24 = n2 + 1;
         n3 = vec3dGameRendererHelper6.py(d19, d20, (float)((Vec3d)temp6.get(n24)).z).pA(color3).pB();
         n13 = n22;
         n14 = n18;
         vec3dGameRendererHelper.pE(n14, n13, n3);
      }

      for (int n25 = 0; n25 < arrayList2.size() - 1; n25++) {
         float f4 = 1.0F - (n25 + (float)this.stopwatchImpl.afs() / 5.0F) % 360.0F / 60.0F;
         Color color4 = ColorUtils.iE(ColorUtils.iC(color2, (int)(n25 / 20.0F)), (int)(f4 * 255.0F));
         int n26 = n25;
         Vec3dGameRendererHelper vec3dGameRendererHelper7 = vec3dGameRendererHelper;
         ArrayList arrayList4 = arrayList2;
         int n27 = n26;
         double d21 = (float)((Vec3d)arrayList4.get(n27)).x;
         ArrayList arrayList5 = arrayList2;
         int n28 = n25;
         double d22 = (float)((Vec3d)arrayList5.get(n28)).y;
         ArrayList arrayList6 = arrayList2;
         int n29 = n25;
         int n30 = vec3dGameRendererHelper7.py(d21, d22, (float)((Vec3d)arrayList6.get(n29)).z).pA(color4).pB();
         n26 = n25 + 1;
         Vec3dGameRendererHelper vec3dGameRendererHelper8 = vec3dGameRendererHelper;
         double d23 = (float)((Vec3d)arrayList2.get(n26)).x;
         arrayList6 = arrayList2;
         int n31 = n25 + 1;
         double d24 = (float)((Vec3d)arrayList6.get(n31)).y + 0.1F;
         ArrayList arrayList7 = arrayList2;
         int n32 = n25 + 1;
         int n33 = vec3dGameRendererHelper8.py(d23, d24, (float)((Vec3d)arrayList7.get(n32)).z).pA(color4).pB();
         n26 = n25 + 1;
         Vec3dGameRendererHelper vec3dGameRendererHelper9 = vec3dGameRendererHelper;
         double d25 = (float)((Vec3d)arrayList2.get(n26)).x;
         arrayList7 = arrayList2;
         int n34 = n25 + 1;
         double d26 = (float)((Vec3d)arrayList7.get(n34)).y;
         ArrayList arrayList8 = arrayList2;
         int n35 = n25 + 1;
         n26 = vec3dGameRendererHelper9.py(d25, d26, (float)((Vec3d)arrayList8.get(n35)).z).pA(color4).pB();
         int n36 = n33;
         int n37 = n30;
         vec3dGameRendererHelper.pE(n37, n36, n26);
         n26 = n25;
         vec3dGameRendererHelper7 = vec3dGameRendererHelper;
         arrayList4 = arrayList2;
         int n38 = n26;
         double d27 = (float)((Vec3d)arrayList4.get(n38)).x;
         arrayList5 = arrayList2;
         int n39 = n25;
         double d28 = (float)((Vec3d)arrayList5.get(n39)).y;
         arrayList6 = arrayList2;
         int n40 = n25;
         int n41 = vec3dGameRendererHelper7.py(d27, d28, (float)((Vec3d)arrayList6.get(n40)).z).pA(color4).pB();
         n26 = n25;
         Vec3dGameRendererHelper vec3dGameRendererHelper10 = vec3dGameRendererHelper;
         arrayList5 = arrayList2;
         int n42 = n26;
         double d29 = (float)((Vec3d)arrayList5.get(n42)).x;
         arrayList6 = arrayList2;
         int n43 = n25;
         double d30 = (float)((Vec3d)arrayList6.get(n43)).y + 0.1F;
         arrayList7 = arrayList2;
         int n44 = n25;
         int n45 = vec3dGameRendererHelper10.py(d29, d30, (float)((Vec3d)arrayList7.get(n44)).z).pA(color4).pB();
         n26 = n25 + 1;
         Vec3dGameRendererHelper vec3dGameRendererHelper11 = vec3dGameRendererHelper;
         double d31 = (float)((Vec3d)arrayList2.get(n26)).x;
         arrayList7 = arrayList2;
         int n46 = n25 + 1;
         double d32 = (float)((Vec3d)arrayList7.get(n46)).y + 0.1F;
         arrayList8 = arrayList2;
         int n47 = n25 + 1;
         n26 = vec3dGameRendererHelper11.py(d31, d32, (float)((Vec3d)arrayList8.get(n47)).z).pA(color4).pB();
         n36 = n45;
         n37 = n41;
         vec3dGameRendererHelper.pE(n37, n36, n26);
      }

      for (int n48 = 0; n48 < arrayList3.size() - 1; n48++) {
         float f5 = 1.0F - (n48 + (float)this.stopwatchImpl.afs() / 5.0F) % 360.0F / 60.0F;
         Color color5 = ColorUtils.iE(ColorUtils.iC(color2, (int)(n48 / 20.0F)), (int)(f5 * 255.0F));
         int n49 = n48;
         Vec3dGameRendererHelper vec3dGameRendererHelper12 = vec3dGameRendererHelper;
         ArrayList arrayList9 = arrayList3;
         int n50 = n49;
         double d33 = (float)((Vec3d)arrayList9.get(n50)).x;
         ArrayList arrayList10 = arrayList3;
         int n51 = n48;
         double d34 = (float)((Vec3d)arrayList10.get(n51)).y;
         ArrayList arrayList11 = arrayList3;
         int n52 = n48;
         int n53 = vec3dGameRendererHelper12.py(d33, d34, (float)((Vec3d)arrayList11.get(n52)).z).pA(color5).pB();
         n49 = n48 + 1;
         Vec3dGameRendererHelper vec3dGameRendererHelper13 = vec3dGameRendererHelper;
         double d35 = (float)((Vec3d)arrayList3.get(n49)).x;
         arrayList11 = arrayList3;
         int n54 = n48 + 1;
         double d36 = (float)((Vec3d)arrayList11.get(n54)).y + 0.1F;
         ArrayList arrayList12 = arrayList3;
         int n55 = n48 + 1;
         int n56 = vec3dGameRendererHelper13.py(d35, d36, (float)((Vec3d)arrayList12.get(n55)).z).pA(color5).pB();
         n49 = n48 + 1;
         Vec3dGameRendererHelper vec3dGameRendererHelper14 = vec3dGameRendererHelper;
         double d37 = (float)((Vec3d)arrayList3.get(n49)).x;
         arrayList12 = arrayList3;
         int n57 = n48 + 1;
         double d38 = (float)((Vec3d)arrayList12.get(n57)).y;
         ArrayList arrayList13 = arrayList3;
         int n58 = n48 + 1;
         n49 = vec3dGameRendererHelper14.py(d37, d38, (float)((Vec3d)arrayList13.get(n58)).z).pA(color5).pB();
         int n59 = n56;
         int n60 = n53;
         vec3dGameRendererHelper.pE(n60, n59, n49);
         n49 = n48;
         vec3dGameRendererHelper12 = vec3dGameRendererHelper;
         arrayList9 = arrayList3;
         int n61 = n49;
         double d39 = (float)((Vec3d)arrayList9.get(n61)).x;
         arrayList10 = arrayList3;
         int n62 = n48;
         double d40 = (float)((Vec3d)arrayList10.get(n62)).y;
         arrayList11 = arrayList3;
         int n63 = n48;
         int n64 = vec3dGameRendererHelper12.py(d39, d40, (float)((Vec3d)arrayList11.get(n63)).z).pA(color5).pB();
         n49 = n48;
         Vec3dGameRendererHelper vec3dGameRendererHelper15 = vec3dGameRendererHelper;
         arrayList10 = arrayList3;
         int n65 = n49;
         double d41 = (float)((Vec3d)arrayList10.get(n65)).x;
         arrayList11 = arrayList3;
         int n66 = n48;
         double d42 = (float)((Vec3d)arrayList11.get(n66)).y + 0.1F;
         arrayList12 = arrayList3;
         int n67 = n48;
         int n68 = vec3dGameRendererHelper15.py(d41, d42, (float)((Vec3d)arrayList12.get(n67)).z).pA(color5).pB();
         n49 = n48 + 1;
         Vec3dGameRendererHelper vec3dGameRendererHelper16 = vec3dGameRendererHelper;
         double d43 = (float)((Vec3d)arrayList3.get(n49)).x;
         arrayList12 = arrayList3;
         int n69 = n48 + 1;
         double d44 = (float)((Vec3d)arrayList12.get(n69)).y + 0.1F;
         arrayList13 = arrayList3;
         int n70 = n48 + 1;
         n49 = vec3dGameRendererHelper16.py(d43, d44, (float)((Vec3d)arrayList13.get(n70)).z).pA(color5).pB();
         n59 = n68;
         n60 = n64;
         vec3dGameRendererHelper.pE(n60, n59, n49);
      }
   }

   private void apH(Entity entity2, MatrixStack matrices2, float f2) {
      if (this.stopwatchImpl.afq(30)) {
         this.g = (this.g + 1) % 30;
         this.stopwatchImpl.afr(29432, 5265492, (byte)40);
      }

      RenderSystem.disableDepthTest();
      Camera camera = client.gameRenderer.getCamera();
      Vec3d vec = EntityBoxUtils.BA(entity2, f2);
      double d2 = vec.getX();
      double d3 = entity2.getBoundingBox().getLengthY() / 2.0 + vec.getY();
      double d4 = vec.getZ();
      matrices2.push();
      double temp = d2 - camera.getPos().getX();
      double temp2 = d3 - camera.getPos().getY();
      double d5 = d4 - camera.getPos().getZ();
      double d6 = temp2;
      double d7 = temp;
      matrices2.translate(d7, d6, d5);
      RotationAxis rotationAxis = RotationAxis.POSITIVE_Y;
      float f3 = -camera.getYaw();
      Quaternionf quaternionf = rotationAxis.rotationDegrees(f3);
      matrices2.multiply(quaternionf);
      RotationAxis rotationAxis2 = RotationAxis.POSITIVE_X;
      float f4 = camera.getPitch();
      Quaternionf quaternionf2 = rotationAxis2.rotationDegrees(f4);
      matrices2.multiply(quaternionf2);
      matrices2.scale(-0.1F, -0.1F, 1.0F);
      RenderSystem.setShaderTexture(0, d_[this.g]);
      Matrix4f matrix4f = matrices2.peek().getPositionMatrix();
      RenderSystem.setShader(GameRenderer::getPositionTexProgram);
      Tessellator tessellator = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_TEXTURE;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = tessellator.begin(drawMode, vertexFormat);
      bufferBuilder.vertex(matrix4f, -10.0F, -10.0F, 0.0F).texture(0.0F, 0.0F);
      bufferBuilder.vertex(matrix4f, -10.0F, 10.0F, 0.0F).texture(0.0F, 1.0F);
      bufferBuilder.vertex(matrix4f, 10.0F, 10.0F, 0.0F).texture(1.0F, 1.0F);
      bufferBuilder.vertex(matrix4f, 10.0F, -10.0F, 0.0F).texture(1.0F, 0.0F);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.enableDepthTest();
      matrices2.pop();
   }

   private static double apI(double d2) {
      return Math.abs(1.0 + Math.sin(d2)) / 2.0;
   }

   public void apJ(Render3DContext render3DContext, Entity entity2, Color color2, float f2) {
      double d2 = this.stopwatchImpl.afs() / 300.0;
      double d3 = apI(d2 - 0.45F);
      double d4 = apI(d2);
      double d5 = entity2.prevX + (entity2.getX() - entity2.prevX) * f2;
      double d6 = entity2.prevY + (entity2.getY() - entity2.prevY) * f2 + d3 * entity2.getHeight();
      double d7 = entity2.prevZ + (entity2.getZ() - entity2.prevZ) * f2;
      double d8 = entity2.prevY + (entity2.getY() - entity2.prevY) * f2 + d4 * entity2.getHeight();
      Vec3dGameRendererHelper vec3dGameRendererHelper = render3DContext.boxRenderer.vec3dGameRendererHelper2;
      int n = 0;

      do {
         float f3 = (float)(
            d5
               + Math.cos(n * 6.28 / 30.0)
                  * (entity2.getBoundingBox().maxX - entity2.getBoundingBox().minX + (entity2.getBoundingBox().maxZ - entity2.getBoundingBox().minZ))
                  * 0.5
         );
         float f4 = (float)(
            d7
               + Math.sin(n * 6.28 / 30.0)
                  * (entity2.getBoundingBox().maxX - entity2.getBoundingBox().minX + (entity2.getBoundingBox().maxZ - entity2.getBoundingBox().minZ))
                  * 0.5
         );
         float f5 = (float)(
            d5
               + Math.cos((n + 1) * 6.28 / 30.0)
                  * (entity2.getBoundingBox().maxX - entity2.getBoundingBox().minX + (entity2.getBoundingBox().maxZ - entity2.getBoundingBox().minZ))
                  * 0.5
         );
         float f6 = (float)(
            d7
               + Math.sin((n + 1) * 6.28 / 30.0)
                  * (entity2.getBoundingBox().maxX - entity2.getBoundingBox().minX + (entity2.getBoundingBox().maxZ - entity2.getBoundingBox().minZ))
                  * 0.5
         );
         double temp = f3;
         double temp2 = (float)d8;
         double d9 = f4;
         double d10 = temp2;
         double d11 = temp;
         int n2 = vec3dGameRendererHelper.py(d11, d10, d9).pA(color2).pB();
         temp2 = f3;
         double temp3 = (float)d6;
         d9 = f4;
         d10 = temp3;
         d11 = temp2;
         int n3 = vec3dGameRendererHelper.py(d11, d10, d9).pA(ColorUtils.iE(color2, 0)).pB();
         temp3 = f5;
         double temp4 = (float)d8;
         d9 = f6;
         d10 = temp4;
         d11 = temp3;
         int n4 = vec3dGameRendererHelper.py(d11, d10, d9).pA(color2).pB();
         int n5 = n3;
         int n6 = n2;
         vec3dGameRendererHelper.pE(n6, n5, n4);
         temp = f3;
         temp2 = (float)d6;
         d9 = f4;
         d10 = temp2;
         d11 = temp;
         int n7 = vec3dGameRendererHelper.py(d11, d10, d9).pA(ColorUtils.iE(color2, 0)).pB();
         temp2 = f5;
         temp3 = (float)d6;
         d9 = f6;
         d10 = temp3;
         d11 = temp2;
         int n8 = vec3dGameRendererHelper.py(d11, d10, d9).pA(ColorUtils.iE(color2, 0)).pB();
         temp3 = f5;
         temp4 = (float)d8;
         d9 = f6;
         d10 = temp4;
         d11 = temp3;
         n4 = vec3dGameRendererHelper.py(d11, d10, d9).pA(color2).pB();
         n5 = n8;
         n6 = n7;
         vec3dGameRendererHelper.pE(n6, n5, n4);
      } while (++n < 30);
   }

   public void apK(Entity entity2, MatrixStack matrices2, float f2) {
      Camera camera = client.gameRenderer.getCamera();
      matrices2.push();
      double d2 = EntityBoxUtils.BC(entity2.prevX, entity2.getX(), f2) - camera.getPos().getX();
      double d3 = EntityBoxUtils.BC(entity2.prevY, entity2.getY(), f2) - camera.getPos().getY();
      double d4 = EntityBoxUtils.BC(entity2.prevZ, entity2.getZ(), f2) - camera.getPos().getZ();
      float f3 = (float)EntityBoxUtils.BC(entity2.age - 1, entity2.age, f2);
      RenderSystem.enableBlend();
      BufferBuilder bufferBuilder;
      int n;
      int n2;
      if ((Boolean)this.shine.atx()) {
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
         RenderSystem.disableDepthTest();
         RenderSystem.setShaderTexture(0, this.id);
         RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
         Tessellator temp = Tessellator.getInstance();
         VertexFormat vertexFormat = VertexFormats.POSITION_TEXTURE_COLOR;
         DrawMode drawMode = DrawMode.QUADS;
         bufferBuilder = temp.begin(drawMode, vertexFormat);
         n = 0;
         n2 = 0;
         double d5 = Math.toRadians(((0 / 1.5F + f3) * 8.0F + 0 * 120) % 2880.0F);
         double d6 = Math.sin(Math.toRadians(f3 * 2.5F + 0 * (0 + 1)) * 3.0) / 1.8F;
         float f4 = 0 / 14.0F;
         matrices2.push();
         double temp2 = d2 + Math.cos(d5) * entity2.getWidth();
         double temp3 = d3 + 1.0 + d6;
         double d7 = d4 + Math.sin(d5) * entity2.getWidth();
         double d8 = temp3;
         double d9 = temp2;
         matrices2.translate(d9, d8, d7);
         RotationAxis rotationAxis = RotationAxis.POSITIVE_Y;
         float f5 = -camera.getYaw();
         Quaternionf quaternionf = rotationAxis.rotationDegrees(f5);
         matrices2.multiply(quaternionf);
         RotationAxis rotationAxis2 = RotationAxis.POSITIVE_X;
         float f6 = camera.getPitch();
         Quaternionf quaternionf2 = rotationAxis2.rotationDegrees(f6);
         matrices2.multiply(quaternionf2);
         Matrix4f matrix4f = matrices2.peek().getPositionMatrix();
         int n3 = ColorUtils.iE((Color)this.color.atx(), (int)(180.0F * f4)).getRGB();
         float f7 = Math.max(0.24F * f4, 0.2F);
         BufferBuilder bufferBuilder2 = bufferBuilder;
         float f8 = -f7;
         bufferBuilder2.vertex(matrix4f, f8, f7, 0.0F).texture(0.0F, 1.0F).color(n3);
         bufferBuilder.vertex(matrix4f, f7, f7, 0.0F).texture(1.0F, 1.0F).color(n3);
         BufferBuilder bufferBuilder3 = bufferBuilder;
         float f9 = -f7;
         bufferBuilder3.vertex(matrix4f, f7, f9, 0.0F).texture(1.0F, 0.0F).color(n3);
         BufferBuilder bufferBuilder4 = bufferBuilder;
         float f10;
         float f11 = f10 = -f7;
         bufferBuilder4.vertex(matrix4f, f11, f10, 0.0F).texture(0.0F, 0.0F).color(n3);
         matrices2.pop();
         n2++;
      } else {
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableDepthTest();
         RenderSystem.setShaderTexture(0, this.id);
         RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
         Tessellator tessellator = Tessellator.getInstance();
         VertexFormat vertexFormat2 = VertexFormats.POSITION_TEXTURE_COLOR;
         DrawMode drawMode2 = DrawMode.QUADS;
         bufferBuilder = tessellator.begin(drawMode2, vertexFormat2);
         n = 0;
         if (false) {
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            matrices2.pop();
            return;
         }

         n2 = 0;
      }

      while (true) {
         while (n2 > 14) {
            if (++n >= 3) {
               BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
               RenderSystem.enableDepthTest();
               RenderSystem.disableBlend();
               matrices2.pop();
               return;
            }

            n2 = 0;
         }

         double d10 = Math.toRadians(((n2 / 1.5F + f3) * 8.0F + n * 120) % 2880.0F);
         double d11 = Math.sin(Math.toRadians(f3 * 2.5F + n2 * (n + 1)) * 3.0) / 1.8F;
         float f12 = n2 / 14.0F;
         matrices2.push();
         double d12 = d2 + Math.cos(d10) * entity2.getWidth();
         double d13 = d3 + 1.0 + d11;
         double d14 = d4 + Math.sin(d10) * entity2.getWidth();
         double d15 = d13;
         double d16 = d12;
         matrices2.translate(d16, d15, d14);
         RotationAxis rotationAxis3 = RotationAxis.POSITIVE_Y;
         float f13 = -camera.getYaw();
         Quaternionf quaternionf3 = rotationAxis3.rotationDegrees(f13);
         matrices2.multiply(quaternionf3);
         RotationAxis rotationAxis4 = RotationAxis.POSITIVE_X;
         float f14 = camera.getPitch();
         Quaternionf quaternionf4 = rotationAxis4.rotationDegrees(f14);
         matrices2.multiply(quaternionf4);
         Matrix4f matrix4f2 = matrices2.peek().getPositionMatrix();
         int n4 = ColorUtils.iE((Color)this.color.atx(), (int)(180.0F * f12)).getRGB();
         float f15 = Math.max(0.24F * f12, 0.2F);
         BufferBuilder bufferBuilder5 = bufferBuilder;
         float f16 = -f15;
         bufferBuilder5.vertex(matrix4f2, f16, f15, 0.0F).texture(0.0F, 1.0F).color(n4);
         bufferBuilder.vertex(matrix4f2, f15, f15, 0.0F).texture(1.0F, 1.0F).color(n4);
         bufferBuilder5 = bufferBuilder;
         float f17 = -f15;
         bufferBuilder5.vertex(matrix4f2, f15, f17, 0.0F).texture(1.0F, 0.0F).color(n4);
         bufferBuilder5 = bufferBuilder;
         float f18;
         float f19 = f18 = -f15;
         bufferBuilder5.vertex(matrix4f2, f19, f18, 0.0F).texture(0.0F, 0.0F).color(n4);
         matrices2.pop();
         n2++;
      }
   }

   private Boolean isShineVisible() {
      boolean flag;
      if (this.mode.atx() != TargetESPGhostTapeMode.PVZ) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isColorVisible() {
      boolean flag;
      if (this.mode.atx() != TargetESPGhostTapeMode.PVZ) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color2, Supplier supplier) {
      return new ColorSetting(str, str2, color2, true, true, supplier);
   }

   private static Color d() {
      return new Color(0, 0, 0, 255);
   }
}
