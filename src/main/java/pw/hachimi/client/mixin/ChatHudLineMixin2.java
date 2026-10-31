package pw.hachimi.client.mixin;

import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import pw.hachimi.client.api.UnidentifiedInterface3;

@Mixin(ChatHudLine.class)
public abstract class ChatHudLineMixin2 implements UnidentifiedInterface3 {
   @Unique
   private int f;

   @Override
   public int et() {
      return this.f;
   }

   @Override
   public void eu(int n) {
      this.f = n;
   }
}
