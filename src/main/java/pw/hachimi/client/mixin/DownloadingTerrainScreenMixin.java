package pw.hachimi.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.gui.TextDrawScreen;

@Mixin(DownloadingTerrainScreen.class)
public abstract class DownloadingTerrainScreenMixin extends Screen {
   protected DownloadingTerrainScreenMixin(Text text2) {
      super(text2);
   }

   @Inject(method = "shouldCloseOnEsc()Z", at = @At("HEAD"), cancellable = true)
   public void shouldCloseOnEsc(CallbackInfoReturnable callbackInfoReturnable) {
      callbackInfoReturnable.setReturnValue(true);
   }

   @Inject(method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At("HEAD"))
   public void hookBackground(DrawContext context, int n, int n2, float f, CallbackInfo callbackInfo) {
      TextDrawScreen.ey(n, n2, this.width, this.height);
   }
}
