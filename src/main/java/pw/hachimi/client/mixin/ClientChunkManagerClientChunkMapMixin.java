package pw.hachimi.client.mixin;

import java.util.concurrent.atomic.AtomicReferenceArray;
import net.minecraft.client.world.ClientChunkManager.ClientChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientChunkMap.class)
public interface ClientChunkManagerClientChunkMapMixin {
   @Accessor("chunks")
   AtomicReferenceArray getChunks();
}
