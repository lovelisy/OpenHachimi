package pw.hachimi.client.mixin;

import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
public interface WorldMixin {
   @Invoker("getEntityLookup")
   EntityLookup getEntityLookup();
}
