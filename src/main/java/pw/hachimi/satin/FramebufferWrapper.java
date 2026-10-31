package pw.hachimi.satin;

import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.util.Window;

public final class FramebufferWrapper implements ManagedFramebuffer {
   private final String name;
   private Framebuffer wrapped;

   FramebufferWrapper(String str) {
      this.name = str;
   }

   void findTarget(PostEffectProcessor postEffectProcessor) {
      if (postEffectProcessor == null) {
         this.wrapped = null;
      } else {
         this.wrapped = postEffectProcessor.getSecondaryTarget(this.name);
         if (this.wrapped == null) {
            LogUtils.getLogger().warn("No target framebuffer found with name {} in shader {}", this.name, postEffectProcessor.getName());
         }
      }
   }

   public String getName() {
      return this.name;
   }

   @Override
   public Framebuffer getFramebuffer() {
      return this.wrapped;
   }

   @Override
   public void beginWrite(boolean flag) {
      if (this.wrapped != null) {
         this.wrapped.beginWrite(flag);
      }
   }

   @Override
   public void draw() {
      Window window = MinecraftClient.getInstance().getWindow();
      this.draw(window.getFramebufferWidth(), window.getFramebufferHeight(), true);
   }

   @Override
   public void draw(int n, int n2, boolean flag) {
      if (this.wrapped != null) {
         this.wrapped.draw(n, n2, flag);
      }
   }

   @Override
   public void clear() {
      this.clear(MinecraftClient.IS_SYSTEM_MAC);
   }

   @Override
   public void clear(boolean flag) {
      if (this.wrapped != null) {
         this.wrapped.clear(flag);
      }
   }
}
