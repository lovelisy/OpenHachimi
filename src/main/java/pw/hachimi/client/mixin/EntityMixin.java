package pw.hachimi.client.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityMixin {
   @Invoker("unsetRemoved")
   void hookUnsetRemoved();

   @Invoker("setFlag")
   void hookSetFlag(int var1, boolean var2);
}
