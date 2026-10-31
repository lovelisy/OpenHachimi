package pw.hachimi.client.mixin;

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.class)
public interface AbstractBlockMixin {
   @Accessor("slipperiness")
   @Mutable
   void setSlipperiness(float var1);
}
