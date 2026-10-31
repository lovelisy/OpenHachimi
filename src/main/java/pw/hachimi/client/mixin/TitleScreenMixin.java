package pw.hachimi.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.gui.TextDrawScreen;
import pw.hachimi.client.module.ColorHelper3;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
   @Shadow
   private long backgroundFadeStart;
   @Shadow
   private boolean doBackgroundFade;

   public TitleScreenMixin(Text text2) {
      super(text2);
   }

   @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At("TAIL"))
   public void hookRender(DrawContext context, int n, int n2, float f, CallbackInfo callbackInfo) {
      float f2 = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
      float f3 = this.doBackgroundFade ? MathHelper.clamp(f2 - 1.0F, 0.0F, 1.0F) : 1.0F;
      int n3 = MathHelper.ceil(f3 * 255.0F) << 24;
      if ((n3 & -67108864) != 0) {
         context.drawTextWithShadow(this.client.textRenderer, String.format("%s %s", "Hachimi", "2.0"), 2, this.height - 9 * 2 - 2, 16777215 | n3);
      }
   }

   @Inject(method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At("HEAD"), cancellable = true)
   public void hookBackground(DrawContext context, int n, int n2, float f, CallbackInfo callbackInfo) {
      if (ColorHelper3.auO().auQ()) {
         callbackInfo.cancel();
         TextDrawScreen.ey(n, n2, this.width, this.height);
      }
   }

   @Inject(method = "renderPanoramaBackground(Lnet/minecraft/client/gui/DrawContext;F)V", at = @At("HEAD"), cancellable = true)
   public void hookBackground(DrawContext context, float f, CallbackInfo callbackInfo) {
      if (ColorHelper3.auO().auQ()) {
         callbackInfo.cancel();
         TextDrawScreen.ez(context, this.width, this.height);
      }
   }
}
