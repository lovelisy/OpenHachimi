package pw.hachimi.satin;

import java.util.function.IntSupplier;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.texture.AbstractTexture;
import pw.hachimi.satin.uniform.SamplerUniformV2;

public final class ManagedSamplerUniformV2 extends ManagedSamplerUniformBase implements SamplerUniformV2 {
   public ManagedSamplerUniformV2(String str) {
      super(str);
   }

   @Override
   public void set(AbstractTexture abstractTexture) {
      this.set(abstractTexture::getGlId);
   }

   @Override
   public void set(Framebuffer framebuffer) {
      this.set(framebuffer::getColorAttachment);
   }

   @Override
   public void set(int n) {
      this.set(() -> n);
   }

   @Override
   protected void set(Object obj) {
      this.set((IntSupplier)obj);
   }

   @Override
   public void set(IntSupplier intSupplier) {
      SamplerAccess[] samplerAccesss = this.targets;
      if (samplerAccesss.length > 0 && this.cachedValue != intSupplier) {
         int n = samplerAccesss.length;

         for (int n2 = 0; n2 < n; n2++) {
            ((JsonEffectShaderProgram)samplerAccesss[n2]).bindSampler(this.name, intSupplier);
         }

         this.cachedValue = intSupplier;
      }
   }
}
