package pw.hachimi.client.mixin;

import net.minecraft.world.entity.SectionedEntityCache;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleEntityLookup.class)
public interface SimpleEntityLookupMixin {
   @Accessor("cache")
   SectionedEntityCache getCache();
}
