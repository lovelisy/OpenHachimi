package pw.hachimi.client.mixin;

import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScreenHandler.class)
public interface ScreenHandlerMixin {
   @Accessor("revision")
   void hookSetRevision(int var1);
}
