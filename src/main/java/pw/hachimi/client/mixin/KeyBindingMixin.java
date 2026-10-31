package pw.hachimi.client.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.movement.UnidentifiedInterface14;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements UnidentifiedInterface14 {
   @Shadow
   private Key boundKey;

   @Shadow
   public abstract void setPressed(boolean var1);

   @Override
   public void WK() {
      this.setPressed(InputUtil.isKeyPressed(Mc.client.getWindow().getHandle(), this.boundKey.getCode()));
   }
}
