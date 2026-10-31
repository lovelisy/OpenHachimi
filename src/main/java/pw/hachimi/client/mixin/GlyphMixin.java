package pw.hachimi.client.mixin;

import net.minecraft.client.font.Glyph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.module.client.Font;

@Mixin(Glyph.class)
public interface GlyphMixin {
   @Inject(method = "getShadowOffset()F", at = @At("HEAD"), cancellable = true)
   private void hookGetShadowOffset(CallbackInfoReturnable callbackInfoReturnable) {
      callbackInfoReturnable.cancel();
      callbackInfoReturnable.setReturnValue(Font.kT().kY());
   }
}
