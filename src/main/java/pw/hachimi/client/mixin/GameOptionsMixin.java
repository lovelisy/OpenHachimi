package pw.hachimi.client.mixin;

import java.util.Set;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameOptions.class)
public interface GameOptionsMixin {
   @Accessor("enabledPlayerModelParts")
   @Mutable
   Set getPlayerModelParts();
}
