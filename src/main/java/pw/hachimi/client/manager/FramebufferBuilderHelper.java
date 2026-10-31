package pw.hachimi.client.manager;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.function.Function;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderLayer.MultiPhase;
import net.minecraft.client.render.RenderLayer.OutlineMode;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters.Builder;
import net.minecraft.client.render.RenderPhase.Cull;
import net.minecraft.client.render.RenderPhase.DepthTest;
import net.minecraft.client.render.RenderPhase.ShaderProgram;
import net.minecraft.client.render.RenderPhase.Target;
import net.minecraft.client.render.RenderPhase.TextureBase;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.util.Identifier;
import pw.hachimi.client.Mc;
import pw.hachimi.client.mixin.RenderLayerMultiPhaseMixin;
import pw.hachimi.client.mixin.RenderLayerMultiPhaseParametersMixin;
import pw.hachimi.client.model.render.RenderHandler2;
import pw.hachimi.client.render.FramebufferFramebuffer3;
import pw.hachimi.client.render.RenderLayerTextureBaseManager;
import pw.hachimi.satin.ManagedShaderEffect;
import pw.hachimi.satin.ShaderEffectManager;

public class FramebufferBuilderHelper {
   private final OutlineVertexConsumerProvider outlineVertexConsumerProvider = new OutlineVertexConsumerProvider(
      VertexConsumerProvider.immediate(new BufferAllocator(256))
   );
   private final Function function;
   private final Target target;
   private final ManagedShaderEffect managedShaderEffect;
   private final ManagedShaderEffect managedShaderEffect2;
   private final ManagedShaderEffect managedShaderEffect3;
   private final FramebufferFramebuffer3 framebufferFramebuffer3 = new FramebufferFramebuffer3();

   public FramebufferBuilderHelper() {
      this.managedShaderEffect2 = ShaderEffectManager.getInstance().manage(Identifier.of("hachimiclient", "shaders/post/menu.json"));
      this.managedShaderEffect = ShaderEffectManager.getInstance().manage(Identifier.of("hachimiclient", "shaders/post/outline.json"));
      this.managedShaderEffect3 = ShaderEffectManager.getInstance().manage(Identifier.of("hachimiclient", "shaders/post/blur.json"));
      this.target = new Target("shader_target", FramebufferBuilderHelper::Wi, FramebufferBuilderHelper::Wh);
      final FramebufferBuilderHelper framebufferBuilderHelper = this;
      this.function = this.Wb(new Function() {
         @Override
         public Object apply(Object obj) {
            return framebufferBuilderHelper.Wg((TextureBase)obj);
         }
      });
   }

   public void VY(ManagedShaderEffect managedShaderEffect4, Runnable runnable, Runnable runnable2) {
      Framebuffer framebuffer = Mc.client.getFramebuffer();
      RenderSystem.assertOnRenderThreadOrInit();
      if (this.framebufferFramebuffer3.textureWidth != framebuffer.textureWidth || this.framebufferFramebuffer3.textureHeight != framebuffer.textureHeight) {
         int n = framebuffer.textureHeight;
         int n2 = framebuffer.textureWidth;
         this.framebufferFramebuffer3.resize(n2, n, false);
      }

      GlStateManager._glBindFramebuffer(36009, this.framebufferFramebuffer3.fbo);
      this.framebufferFramebuffer3.beginWrite(false);
      runnable2.run();
      this.framebufferFramebuffer3.endWrite();
      GlStateManager._glBindFramebuffer(36009, framebuffer.fbo);
      framebuffer.beginWrite(false);
      PostEffectProcessor postEffectProcessor = managedShaderEffect4.getShaderEffect();
      if (postEffectProcessor != null) {
         ((RenderHandler2)postEffectProcessor).ajQ("bufIn", this.framebufferFramebuffer3);
         Framebuffer framebuffer2 = postEffectProcessor.getSecondaryTarget("bufOut");
         runnable.run();
         this.framebufferFramebuffer3.clear(false);
         framebuffer.beginWrite(false);
         RenderSystem.enableBlend();
         RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ZERO, DstFactor.ONE);
         RenderSystem.backupProjectionMatrix();
         int n3 = framebuffer2.textureHeight;
         int n4 = framebuffer2.textureWidth;
         framebuffer2.draw(n4, n3, false);
         RenderSystem.restoreProjectionMatrix();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableBlend();
      }
   }

   public OutlineVertexConsumerProvider getOutlineVertexConsumerProvider() {
      return this.outlineVertexConsumerProvider;
   }

   public VertexConsumerProvider Wa(VertexConsumerProvider vertexConsumerProvider, Color color) {
      final Color color2 = color;
      final VertexConsumerProvider vertexConsumerProvider2 = vertexConsumerProvider;
      final FramebufferBuilderHelper framebufferBuilderHelper = this;
      return new VertexConsumerProvider() {
         public VertexConsumer getBuffer(RenderLayer vertexConsumerProvider) {
            return framebufferBuilderHelper.Wf(vertexConsumerProvider2, color2, vertexConsumerProvider);
         }
      };
   }

   private Function Wb(Function function2) {
      return new RenderLayerTextureBaseManager(function2);
   }

   public ManagedShaderEffect getManagedShaderEffect() {
      return this.managedShaderEffect;
   }

   public ManagedShaderEffect getManagedShaderEffect2() {
      return this.managedShaderEffect2;
   }

   public ManagedShaderEffect getManagedShaderEffect3() {
      return this.managedShaderEffect3;
   }

   VertexConsumer Wf(VertexConsumerProvider vertexConsumerProvider, Color color, RenderLayer renderLayer) {
      VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
      if (renderLayer instanceof MultiPhase
         && ((RenderLayerMultiPhaseParametersMixin)(Object)((RenderLayerMultiPhaseMixin)(Object)renderLayer).hookGetPhases()).hookGetOutlineMode() != OutlineMode.NONE) {
         this.outlineVertexConsumerProvider.setColor(color.getRed(), color.getGreen(), color.getBlue(), 255);
         MultiPhaseParameters multiPhaseParameters = ((RenderLayerMultiPhaseMixin)(Object)renderLayer).hookGetPhases();
         TextureBase textureBase = ((RenderLayerMultiPhaseParametersMixin)(Object)multiPhaseParameters).hookGetTexture();
         RenderLayer renderLayer2 = (RenderLayer)this.function.apply(textureBase);
         return this.outlineVertexConsumerProvider.getBuffer(renderLayer2);
      }

      return vertexConsumer;
   }

   RenderLayer Wg(TextureBase textureBase) {
      VertexFormat temp = VertexFormats.POSITION_TEXTURE_COLOR;
      DrawMode temp2 = DrawMode.QUADS;
      Builder temp3 = MultiPhaseParameters.builder();
      ShaderProgram shaderProgram = RenderPhase.OUTLINE_PROGRAM;
      temp3 = temp3.program(shaderProgram);
      Cull cull2 = RenderPhase.DISABLE_CULLING;
      temp3 = temp3.cull(cull2).texture(textureBase);
      DepthTest depthTest2 = RenderPhase.ALWAYS_DEPTH_TEST;
      temp3 = temp3.depthTest(depthTest2);
      Target target2 = this.target;
      temp3 = temp3.target(target2);
      OutlineMode outlineMode = OutlineMode.IS_OUTLINE;
      MultiPhaseParameters multiPhaseParameters = temp3.build(outlineMode);
      DrawMode drawMode = temp2;
      VertexFormat vertexFormat = temp;
      return RenderLayer.of("hachimiclient_overlay", vertexFormat, drawMode, 1536, multiPhaseParameters);
   }

   private static void Wh() {
   }

   private static void Wi() {
   }
}
