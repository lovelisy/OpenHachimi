package pw.hachimi.client.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.world.entity.SectionedEntityCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SectionedEntityCache.class)
public interface SectionedEntityCacheMixin {
   @Accessor("trackedPositions")
   LongSortedSet getTrackedPositions();

   @Accessor("trackingSections")
   Long2ObjectMap getTrackingSections();
}
