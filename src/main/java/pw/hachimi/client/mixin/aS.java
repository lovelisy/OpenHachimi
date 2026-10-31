package pw.hachimi.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pw.hachimi.client.model.render.UnidentifiedInterface16;

@Mixin(targets = "com/mojang/blaze3d/platform/GlStateManager$CapabilityTracker")
public abstract class aS implements UnidentifiedInterface16 {
   @Shadow
   private boolean state;

   @Shadow
   public abstract void setState(boolean var1);

   @Override
   public boolean aaO() {
      return this.state;
   }

   @Override
   public void aaP(boolean flag) {
      this.setState(flag);
   }
}
