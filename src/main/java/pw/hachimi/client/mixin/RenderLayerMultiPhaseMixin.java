package pw.hachimi.client.mixin;

import net.minecraft.client.render.RenderLayer.MultiPhase;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPhase.class)
public interface RenderLayerMultiPhaseMixin {
   @Invoker("getPhases")
   MultiPhaseParameters hookGetPhases();
}
