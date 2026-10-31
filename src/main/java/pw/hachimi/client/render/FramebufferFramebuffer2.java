package pw.hachimi.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import pw.hachimi.client.Mc;

public class FramebufferFramebuffer2 extends Framebuffer {
   private static final FramebufferFramebuffer2 a_ = new FramebufferFramebuffer2();
   private int b;
   private int c;
   private static final long a = 0L;

   public FramebufferFramebuffer2() {
      super(true);
      this.setClearColor(1.0F, 1.0F, 1.0F, 0.0F);
   }

   public static void afe(Runnable runnable) {
      aff(Mc.client.getFramebuffer(), runnable);
   }

   public static void aff(Framebuffer framebuffer, Runnable runnable) {
      RenderSystem.assertOnRenderThreadOrInit();
      a_.resize(framebuffer.textureWidth, framebuffer.textureHeight, false);
      GlStateManager._glBindFramebuffer(36008, framebuffer.fbo);
      GlStateManager._glBindFramebuffer(36009, a_.fbo);
      GlStateManager._glBlitFrameBuffer(0, 0, a_.textureWidth, a_.textureHeight, 0, 0, a_.textureWidth, a_.textureHeight, 16384, 9729);
      a_.beginWrite(true);
      runnable.run();
      a_.endWrite();
      GlStateManager._glBindFramebuffer(36008, a_.fbo);
      GlStateManager._glBindFramebuffer(36009, framebuffer.fbo);
      GlStateManager._glBlitFrameBuffer(0, 0, a_.textureWidth, a_.textureHeight, 0, 0, a_.textureWidth, a_.textureHeight, 16384, 9729);
      a_.clear(false);
      framebuffer.beginWrite(false);
   }

   public void resize(int width, int height, boolean getError) {
      if (this.textureWidth != width || this.textureHeight != height) {
         super.resize(width, height, false);
      }
   }

   public void initFbo(int width, int height, boolean getError) {
      RenderSystem.assertOnRenderThreadOrInit();
      this.viewportWidth = width;
      this.viewportHeight = height;
      this.textureWidth = width;
      this.textureHeight = height;
      int n = GlStateManager.glGenFramebuffers();
      this.fbo = n;
      GlStateManager._glBindFramebuffer(36160, this.fbo);
      this.b = GlStateManager.glGenRenderbuffers();
      GlStateManager._glBindRenderbuffer(36161, this.b);
      GlStateManager._glRenderbufferStorage(36161, 32856, width, height);
      GlStateManager._glBindRenderbuffer(36161, 0);
      this.c = GlStateManager.glGenRenderbuffers();
      GlStateManager._glBindRenderbuffer(36161, this.c);
      GlStateManager._glRenderbufferStorage(36161, 6402, width, height);
      GlStateManager._glBindRenderbuffer(36161, 0);
      GlStateManager._glFramebufferRenderbuffer(36160, 36064, 36161, this.b);
      GlStateManager._glFramebufferRenderbuffer(36160, 36096, 36161, this.c);
      int n2 = MinecraftClient.getInstance().getFramebuffer().getColorAttachment();
      this.colorAttachment = n2;
      int n3 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachment();
      this.depthAttachment = n3;
      this.checkFramebufferStatus();
      this.clear(getError);
      this.endRead();
   }

   public void delete() {
      RenderSystem.assertOnRenderThreadOrInit();
      this.endRead();
      this.endWrite();
      if (this.fbo > -1) {
         GlStateManager._glBindFramebuffer(36160, 0);
         GlStateManager._glDeleteFramebuffers(this.fbo);
         this.fbo = -1;
      }

      if (this.b > -1) {
         GlStateManager._glDeleteRenderbuffers(this.b);
         this.b = -1;
      }

      if (this.c > -1) {
         GlStateManager._glDeleteRenderbuffers(this.c);
         this.c = -1;
      }

      this.colorAttachment = -1;
      this.depthAttachment = -1;
      this.textureWidth = -1;
      this.textureHeight = -1;
   }
}
