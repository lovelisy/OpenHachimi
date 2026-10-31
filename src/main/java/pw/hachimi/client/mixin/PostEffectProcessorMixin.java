package pw.hachimi.client.mixin;

import java.util.List;
import net.minecraft.client.gl.PostEffectProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PostEffectProcessor.class)
public interface PostEffectProcessorMixin {
   @Accessor("passes")
   List hookGetPasses();
}
