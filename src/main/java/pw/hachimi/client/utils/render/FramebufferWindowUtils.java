package pw.hachimi.client.utils.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gl.Framebuffer;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.satin.ManagedShaderEffect;

public class FramebufferWindowUtils {
   public static void wF(Runnable runnable, float f, float f2) {
      Framebuffer framebuffer = Mc.client.getFramebuffer();
      ManagedShaderEffect managedShaderEffect = Managers.framebufferBuilderHelper.getManagedShaderEffect3();
      ManagedShaderEffect temp = managedShaderEffect;
      Framebuffer temp2 = framebuffer;
      ManagedShaderEffect temp3 = managedShaderEffect;
      final float f3 = f2;
      final float f4 = f;
      final ManagedShaderEffect managedShaderEffect2 = temp3;
      final Framebuffer framebuffer2 = temp2;
      Managers.framebufferBuilderHelper.VY(temp, new Runnable() {
         @Override
         public void run() {
            FramebufferWindowUtils.wG(framebuffer2, managedShaderEffect2, f4, f3);
         }
      }, runnable);
   }

   static void wG(Framebuffer framebuffer, ManagedShaderEffect managedShaderEffect, float f, float f2) {
      GlStateManager._activeTexture(33985);
      GlStateManager._bindTexture(framebuffer.getColorAttachment());
      managedShaderEffect.setUniformValue("radius", f);
      managedShaderEffect.setUniformValue("texelSize", 1.0F / Mc.client.getWindow().getScaledWidth(), 1.0F / Mc.client.getWindow().getScaledHeight());
      managedShaderEffect.setUniformValue("imageTexture", 1);
      managedShaderEffect.render(f2);
   }
}
