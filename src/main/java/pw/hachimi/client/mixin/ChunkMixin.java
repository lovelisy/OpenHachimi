package pw.hachimi.client.mixin;

import java.util.Map;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Chunk.class)
public interface ChunkMixin {
   @Accessor("blockEntities")
   Map getBlockEntities();
}
