package pw.hachimi.satin;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import pw.hachimi.satin.uniform.SamplerUniform;

public final class ResettableManagedCoreShader extends ResettableManagedShaderBase implements ManagedCoreShader {
   private final Consumer initCallback;
   private final VertexFormat vertexFormat;
   private final Map managedSamplers = new HashMap();

   public ResettableManagedCoreShader(Identifier id, VertexFormat vertexFormat2, Consumer consumer) {
      super(id);
      this.vertexFormat = vertexFormat2;
      this.initCallback = consumer;
   }

   protected ShaderProgram parseShader(ResourceFactory resourceFactory) throws IOException {
      return new FabricShaderProgram(resourceFactory, this.getLocation(), this.vertexFormat);
   }

   @Override
   public void setup(int n, int n2) {
      Preconditions.checkNotNull((ShaderProgram)this.shader);

      for (ManagedUniformBase managedUniformBase : (Iterable<ManagedUniformBase>)this.getManagedUniforms()) {
         this.setupUniform(managedUniformBase, (ShaderProgram)this.shader);
      }

      this.initCallback.accept(this);
   }

   @Override
   public ShaderProgram getProgram() {
      return (ShaderProgram)this.shader;
   }

   protected boolean setupUniform(ManagedUniformBase managedUniformBase, ShaderProgram shaderProgram) {
      return managedUniformBase.findUniformTarget(shaderProgram);
   }

   @Override
   public SamplerUniform findSampler(String str) {
      return (SamplerUniform)this.manageUniform(this.managedSamplers, new Function() {
         @Override
         public Object apply(Object str) {
            return new ManagedSamplerUniformV1((String)str);
         }
      }, str, "sampler");
   }

   @Override
   protected void logInitError(IOException ioException) {
      LogUtils.getLogger().error("Could not create shader program {}", this.getLocation(), ioException);
   }

   @Override
   protected boolean setupUniform(ManagedUniformBase managedUniformBase, AutoCloseable autoCloseable) {
      return this.setupUniform(managedUniformBase, (ShaderProgram)autoCloseable);
   }

   @Override
   protected AutoCloseable parseShader(ResourceFactory resourceFactory, MinecraftClient client2, Identifier id) throws IOException {
      return this.parseShader(resourceFactory);
   }
}
