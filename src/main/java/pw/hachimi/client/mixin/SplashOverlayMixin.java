package pw.hachimi.client.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.module.ColorHelper3;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin {
   @Unique
   private static final int w = new Color(0, 0, 0, 255).getRGB();
   @Unique
   private static final Identifier b = Identifier.of("hachimiclient", "textures/logo.png");
   @Final
   @Shadow
   private boolean reloading;
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   private float progress;
   @Shadow
   private long reloadCompleteTime = -1L;
   @Shadow
   private long reloadStartTime = -1L;
   @Final
   @Shadow
   private ResourceReload reload;
   @Final
   @Shadow
   private Consumer exceptionHandler;

   @Shadow
   private static int withAlpha(int n, int n2) {
      return n & 16777215 | n2 << 24;
   }

   @Shadow
   protected abstract void renderProgressBar(DrawContext var1, int var2, int var3, int var4, int var5, float var6);

   @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At("HEAD"), cancellable = true)
   public void render(DrawContext context, int n, int n2, float f, CallbackInfo callbackInfo) {
      callbackInfo.cancel();
      this.renderCustom(context, n, n2, f);
   }

   @Unique
   public void renderCustom(DrawContext context, int n, int n2, float f) {
      int n3 = context.getScaledWindowWidth();
      int n4 = context.getScaledWindowHeight();
      long l = Util.getMeasuringTimeMs();
      if (this.reloading && this.reloadStartTime == -1L) {
         this.reloadStartTime = l;
      }

      float f2 = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
      float f3 = this.reloadStartTime > -1L ? (float)(l - this.reloadStartTime) / 500.0F : -1.0F;
      float f4;
      if (f2 >= 1.0F) {
         if (this.client.currentScreen != null) {
            this.client.currentScreen.render(context, 0, 0, f);
         }

         int n5 = MathHelper.ceil((1.0F - MathHelper.clamp(f2 - 1.0F, 0.0F, 1.0F)) * 255.0F);
         context.fill(RenderLayer.getGuiOverlay(), 0, 0, n3, n4, withAlpha(w, n5));
         f4 = 1.0F - MathHelper.clamp(f2 - 1.0F, 0.0F, 1.0F);
      } else if (this.reloading) {
         if (this.client.currentScreen != null && f3 < 1.0F) {
            this.client.currentScreen.render(context, n, n2, f);
         }

         int n6 = MathHelper.ceil(MathHelper.clamp(f3, 0.15, 1.0) * 255.0);
         context.fill(RenderLayer.getGuiOverlay(), 0, 0, n3, n4, withAlpha(w, n6));
         f4 = MathHelper.clamp(f3, 0.0F, 1.0F);
      } else {
         int n7 = w;
         float f5 = (n7 >> 16 & 0xFF) / 255.0F;
         float f6 = (n7 >> 8 & 0xFF) / 255.0F;
         float f7 = (n7 & 0xFF) / 255.0F;
         GlStateManager._clearColor(f5, f6, f7, 1.0F);
         GlStateManager._clear(16384, MinecraftClient.IS_SYSTEM_MAC);
         f4 = 1.0F;
      }

      int n8 = (int)(context.getScaledWindowWidth() * 0.5);
      int n9 = (int)(context.getScaledWindowHeight() * 0.5);
      double d = Math.min(context.getScaledWindowWidth() * 0.75, context.getScaledWindowHeight()) * 0.25;
      int n10 = (int)(d * 0.5);
      int n11 = (int)(d * 2.5);
      float f8 = this.reload.getProgress();
      this.progress = MathHelper.clamp(this.progress * 0.95F + f8 * 0.050000012F, 0.0F, 1.0F);
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      context.setShaderColor(0.4F, 0.4F, 0.4F, f4);
      context.drawTexture(b, n8 - n11 / 2, n9 - n10, n11, (int)d, 0.0F, 0.0F, 150, 54, 150, 54);
      Color color = ColorHelper3.auO() == null ? Color.WHITE : ColorHelper3.auO().auU();
      context.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, f4);
      context.enableScissor(n8 - n11 / 2, n9 - n10, (int)(n8 - n11 / 2.0F + n11 * this.progress), (int)(n9 - n10 + d));
      context.drawTexture(b, n8 - n11 / 2, n9 - n10, n11, (int)d, 0.0F, 0.0F, 150, 54, 150, 54);
      context.disableScissor();
      context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      if (f2 < 1.0F) {
         int n12 = (int)(context.getScaledWindowHeight() * 0.8325);
         this.renderProgressBar(context, n3 / 2 - n11, n12 - 5, n3 / 2 + n11, n12 + 5, 1.0F - MathHelper.clamp(f2, 0.0F, 1.0F));
      }

      if (f2 >= 2.0F) {
         this.client.setOverlay(null);
      }

      if (this.reloadCompleteTime == -1L && this.reload.isComplete() && (!this.reloading || f3 >= 2.0F)) {
         try {
            this.reload.throwException();
            this.exceptionHandler.accept(Optional.empty());
         } catch (Throwable error) {
            this.exceptionHandler.accept(Optional.of(error));
         }

         this.reloadCompleteTime = Util.getMeasuringTimeMs();
         if (this.client.currentScreen != null) {
            this.client.currentScreen.init(this.client, context.getScaledWindowWidth(), context.getScaledWindowHeight());
         }
      }
   }
}
