package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.gui.TextDrawScreen;
import pw.hachimi.client.module.ColorHelper3;

@Mixin(Screen.class)
public abstract class ScreenMixin {
   @Shadow
   public int width;
   @Shadow
   public int height;
   @Shadow
   @Nullable
   protected MinecraftClient client;

   @Shadow
   protected abstract Element addDrawableChild(Element var1);

   @Inject(method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At("HEAD"))
   public void hookBackground(DrawContext context, int n, int n2, float f, CallbackInfo callbackInfo) {
      TextDrawScreen.ey(n, n2, this.width, this.height);
   }

   @WrapOperation(method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderPanoramaBackground(Lnet/minecraft/client/gui/DrawContext;F)V"))
   public void renderPanoramaBackgroundHook(Screen screen2, DrawContext context, float f, Operation operation) {
      if (ColorHelper3.auO().auQ()) {
         TextDrawScreen.ez(context, this.width, this.height);
      } else {
         operation.call(new Object[]{screen2, context, f});
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
