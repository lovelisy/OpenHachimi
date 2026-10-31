package pw.hachimi.client.mixin;

import java.util.Optional;
import net.minecraft.client.render.RenderPhase.TextureBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextureBase.class)
public interface RenderPhaseTextureBaseMixin {
   @Invoker("getId")
   Optional hookGetId();
}
