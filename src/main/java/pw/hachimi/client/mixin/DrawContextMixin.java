package pw.hachimi.client.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.module.misc.Tooltips;

@Mixin(DrawContext.class)
public class DrawContextMixin {
   @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
   public void onDrawItem(TextRenderer textRenderer, ItemStack stack, int n, int n2, String str, CallbackInfo callbackInfo) {
      Tooltips.aez().aeD(DrawContext.class.cast(this), stack, n, n2);
   }
}
