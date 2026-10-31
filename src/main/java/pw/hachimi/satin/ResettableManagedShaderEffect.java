package pw.hachimi.satin;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import pw.hachimi.client.mixin.PostEffectProcessorMixin;
import pw.hachimi.satin.uniform.SamplerUniform;
import pw.hachimi.satin.uniform.SamplerUniformV2;

public final class ResettableManagedShaderEffect extends ResettableManagedShaderBase implements ManagedShaderEffect {
   private final Consumer initCallback;
   private final Map managedTargets;
   private final Map managedSamplers = new HashMap();

   public ResettableManagedShaderEffect(Identifier id, Consumer consumer) {
      super(id);
      this.initCallback = consumer;
      this.managedTargets = new HashMap();
   }

   @Override
   public PostEffectProcessor getShaderEffect() {
      return this.getShaderOrLog();
   }

   protected PostEffectProcessor parseShader(MinecraftClient client2, Identifier id) throws IOException {
      return new PostEffectProcessor(client2.getTextureManager(), client2.getResourceManager(), client2.getFramebuffer(), id);
   }

   @Override
   public void setup(int n, int n2) {
      Preconditions.checkNotNull((PostEffectProcessor)this.shader);
      ((PostEffectProcessor)this.shader).setupDimensions(n, n2);

      for (ManagedUniformBase managedUniformBase : (Iterable<ManagedUniformBase>)this.getManagedUniforms()) {
         this.setupUniform(managedUniformBase, (PostEffectProcessor)this.shader);
      }

      Iterator iterator2 = this.managedTargets.values().iterator();

      while (iterator2.hasNext()) {
         ((FramebufferWrapper)iterator2.next()).findTarget((PostEffectProcessor)this.shader);
      }

      this.initCallback.accept(this);
   }

   @Override
   public void render(float f) {
      PostEffectProcessor postEffectProcessor = this.getShaderEffect();
      if (postEffectProcessor != null) {
         RenderSystem.disableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.resetTextureMatrix();
         postEffectProcessor.render(f);
         MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
         RenderSystem.disableBlend();
         RenderSystem.blendFunc(770, 771);
         RenderSystem.enableDepthTest();
      }
   }

   @Override
   public ManagedFramebuffer getTarget(String str) {
      final ResettableManagedShaderEffect resettableManagedShaderEffect = this;
      return (ManagedFramebuffer)this.managedTargets.computeIfAbsent(str, new Function() {
         @Override
         public Object apply(Object str) {
            return resettableManagedShaderEffect.lambda$getTarget$0((String)str);
         }
      });
   }

   @Override
   public void setUniformValue(String str, int n) {
      this.findUniform1i(str).set(n);
   }

   @Override
   public void setUniformValue(String str, float f) {
      this.findUniform1f(str).set(f);
   }

   @Override
   public void setUniformValue(String str, float f, float f2) {
      this.findUniform2f(str).set(f, f2);
   }

   @Override
   public void setUniformValue(String str, float f, float f2, float f3) {
      this.findUniform3f(str).set(f, f2, f3);
   }

   @Override
   public void setUniformValue(String str, float f, float f2, float f3, float f4) {
      this.findUniform4f(str).set(f, f2, f3, f4);
   }

   @Override
   public SamplerUniformV2 findSampler(String str) {
      return (SamplerUniformV2)this.manageUniform(this.managedSamplers, new Function() {
         @Override
         public Object apply(Object str) {
            return new ManagedSamplerUniformV2((String)str);
         }
      }, str, "sampler");
   }

   protected boolean setupUniform(ManagedUniformBase managedUniformBase, PostEffectProcessor postEffectProcessor) {
      return managedUniformBase.findUniformTargets(((PostEffectProcessorMixin)postEffectProcessor).hookGetPasses());
   }

   @Override
   protected void logInitError(IOException ioException) {
      LogUtils.getLogger().error("Could not create screen shader {}", this.getLocation(), ioException);
   }

   private PostEffectProcessor getShaderOrLog() {
      if (!this.isInitialized() && !this.isErrored()) {
         this.initializeOrLog(MinecraftClient.getInstance().getResourceManager());
      }

      return (PostEffectProcessor)this.shader;
   }

   @Override
   protected boolean setupUniform(ManagedUniformBase managedUniformBase, AutoCloseable autoCloseable) {
      return this.setupUniform(managedUniformBase, (PostEffectProcessor)autoCloseable);
   }

   @Override
   protected AutoCloseable parseShader(ResourceFactory resourceFactory, MinecraftClient client2, Identifier id) throws IOException {
      return this.parseShader(client2, id);
   }

   public SamplerUniform findSampler_1(String str) {
      return this.findSampler(str);
   }

   FramebufferWrapper lambda$getTarget$0(String str) {
      FramebufferWrapper framebufferWrapper = new FramebufferWrapper(str);
      if (this.shader != null) {
         framebufferWrapper.findTarget((PostEffectProcessor)this.shader);
      }

      return framebufferWrapper;
   }
}
