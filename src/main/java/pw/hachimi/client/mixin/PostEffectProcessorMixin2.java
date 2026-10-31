package pw.hachimi.client.mixin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.model.render.RenderHandler2;

@Mixin(PostEffectProcessor.class)
public class PostEffectProcessorMixin2 implements RenderHandler2 {
   @Unique
   private static final List m = new CopyOnWriteArrayList();
   @Shadow
   @Final
   private List passes;
   @Shadow
   @Final
   private Map targetsByName;

   @Override
   public void ajQ(String str, Framebuffer framebuffer) {
      Framebuffer framebuffer2 = (Framebuffer)this.targetsByName.get(str);
      if (framebuffer2 != framebuffer) {
         if (framebuffer2 != null) {
            for (PostEffectPass postEffectPass : (Iterable<PostEffectPass>)this.passes) {
               if (postEffectPass.input == framebuffer2) {
                  ((PostEffectPassMixin)postEffectPass).hookSetInput(framebuffer);
               }

               if (postEffectPass.output == framebuffer2) {
                  ((PostEffectPassMixin)postEffectPass).hookSetOutput(framebuffer);
               }
            }

            this.targetsByName.remove(str);
            m.remove(str);
         }

         this.targetsByName.put(str, framebuffer);
         m.add(str);
      }
   }

   @Inject(method = "close", at = @At("HEAD"))
   private void hookClose(CallbackInfo callbackInfo) {
      for (String str : (Iterable<String>)m) {
         this.targetsByName.remove(str);
      }
   }
}
