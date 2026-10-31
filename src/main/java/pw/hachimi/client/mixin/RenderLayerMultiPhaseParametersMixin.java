package pw.hachimi.client.mixin;

import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderLayer.OutlineMode;
import net.minecraft.client.render.RenderPhase.TextureBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiPhaseParameters.class)
public interface RenderLayerMultiPhaseParametersMixin {
   @Accessor("outlineMode")
   OutlineMode hookGetOutlineMode();

   @Accessor("texture")
   TextureBase hookGetTexture();
}
