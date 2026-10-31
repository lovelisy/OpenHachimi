package pw.hachimi.client.mixin;

import java.util.List;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
   @Shadow
   protected abstract List getLeftText();

   @Redirect(method = "drawLeftText(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;getLeftText()Ljava/util/List;"))
   private List redirectRightTextEarly(DebugHud debugHud) {
      List list = this.getLeftText();
      String str = String.format("%s %s", "Hachimi", "2.0");
      list.add(1, str);
      return list;
   }
}
