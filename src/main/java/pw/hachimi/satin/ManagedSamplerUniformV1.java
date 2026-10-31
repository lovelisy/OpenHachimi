package pw.hachimi.satin;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.texture.AbstractTexture;

public final class ManagedSamplerUniformV1 extends ManagedSamplerUniformBase {
   public ManagedSamplerUniformV1(String str) {
      super(str);
   }

   @Override
   public void set(AbstractTexture abstractTexture) {
      this.set((Object)abstractTexture);
   }

   @Override
   public void set(Framebuffer framebuffer) {
      this.set((Object)framebuffer);
   }

   @Override
   public void set(int n) {
      this.set(Integer.valueOf(n));
   }

   @Override
   protected void set(Object obj) {
      SamplerAccess[] samplerAccesss = this.targets;
      if (samplerAccesss.length > 0 && this.cachedValue != obj) {
         int n = samplerAccesss.length;

         for (int n2 = 0; n2 < n; n2++) {
            ((ShaderProgram)samplerAccesss[n2]).addSampler(this.name, obj);
         }

         this.cachedValue = obj;
      }
   }
}
