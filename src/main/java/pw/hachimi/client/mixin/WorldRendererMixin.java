package pw.hachimi.client.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Set;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface WorldRendererMixin {
   @Accessor("frustum")
   Frustum getFrustum();

   @Accessor("blockBreakingInfos")
   Int2ObjectMap getBlockBreakingProgressions();

   @Accessor("bufferBuilders")
   BufferBuilderStorage hookGetBufferBuilders();

   @Accessor("noCullingBlockEntities")
   Set hookGetNoCullingBlockEntities();
}
