package pw.hachimi.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.opengl.GL30;
import java.util.HashMap;

public class FramebufferFramebuffer extends Framebuffer {
   public static final int a_ = GL30.glGetInteger(36183);
   private static final Map INSTANCES = new HashMap();
   private final int b;
   private int c;
   private int d;
   public static int e;
   private static final long a = 0L;

   public FramebufferFramebuffer(int n) {
      super(true);
      this.b = n;
      this.setClearColor(1.0F, 1.0F, 1.0F, 0.0F);
   }

   public static FramebufferFramebuffer adO(int n) {
      Map temp = INSTANCES;
      Integer temp2 = n;
      final int n2 = n;
      Function function2 = new Function() {
         @Override
         public Object apply(Object obj) {
            return FramebufferFramebuffer.adR(n2, (Integer)obj);
         }
      };
      Integer integer = temp2;
      return (FramebufferFramebuffer)temp.computeIfAbsent(integer, function2);
   }

   public static void adP(int n, Runnable runnable) {
      adQ(Math.min(16, a_), MinecraftClient.getInstance().getFramebuffer(), runnable);
   }

   public static void adQ(int n, Framebuffer framebuffer, Runnable runnable) {
      RenderSystem.assertOnRenderThreadOrInit();
      FramebufferFramebuffer framebufferFramebuffer = adO(n);
      framebufferFramebuffer.resize(framebuffer.textureWidth, framebuffer.textureHeight, false);
      GlStateManager._glBindFramebuffer(36008, framebuffer.fbo);
      GlStateManager._glBindFramebuffer(36009, framebufferFramebuffer.fbo);
      e = framebufferFramebuffer.fbo;
      GlStateManager._glBlitFrameBuffer(0, 0, framebufferFramebuffer.textureWidth, framebufferFramebuffer.textureHeight, 0, 0, framebufferFramebuffer.textureWidth, framebufferFramebuffer.textureHeight, 16384, 9729);
      framebufferFramebuffer.beginWrite(true);
      runnable.run();
      framebufferFramebuffer.endWrite();
      GlStateManager._glBindFramebuffer(36008, framebufferFramebuffer.fbo);
      GlStateManager._glBindFramebuffer(36009, framebuffer.fbo);
      GlStateManager._glBlitFrameBuffer(0, 0, framebufferFramebuffer.textureWidth, framebufferFramebuffer.textureHeight, 0, 0, framebufferFramebuffer.textureWidth, framebufferFramebuffer.textureHeight, 16384, 9729);
      framebufferFramebuffer.clear(false);
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
      this.c = GlStateManager.glGenRenderbuffers();
      GlStateManager._glBindRenderbuffer(36161, this.c);
      GL30.glRenderbufferStorageMultisample(36161, this.b, 32856, width, height);
      GlStateManager._glBindRenderbuffer(36161, 0);
      this.d = GlStateManager.glGenRenderbuffers();
      GlStateManager._glBindRenderbuffer(36161, this.d);
      GL30.glRenderbufferStorageMultisample(36161, this.b, 6402, width, height);
      GlStateManager._glBindRenderbuffer(36161, 0);
      GL30.glFramebufferRenderbuffer(36160, 36064, 36161, this.c);
      GL30.glFramebufferRenderbuffer(36160, 36096, 36161, this.d);
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

      if (this.c > -1) {
         GlStateManager._glDeleteRenderbuffers(this.c);
         this.c = -1;
      }

      if (this.d > -1) {
         GlStateManager._glDeleteRenderbuffers(this.d);
         this.d = -1;
      }

      this.colorAttachment = -1;
      this.depthAttachment = -1;
      this.textureWidth = -1;
      this.textureHeight = -1;
   }

   static FramebufferFramebuffer adR(int n, Integer integer) {
      return new FramebufferFramebuffer(n);
   }
}
