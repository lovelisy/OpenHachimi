package pw.hachimi.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;

public class FramebufferFramebuffer3 extends Framebuffer {
   public FramebufferFramebuffer3() {
      super(false);
      RenderSystem.assertOnRenderThreadOrInit();
      this.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
   }
}
