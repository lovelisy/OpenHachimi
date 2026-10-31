package pw.hachimi.client.mixin;

import net.minecraft.component.type.ContainerComponent;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ContainerComponent.class)
public interface ContainerComponentMixin {
   @Accessor("stacks")
   DefaultedList getStacks();
}
