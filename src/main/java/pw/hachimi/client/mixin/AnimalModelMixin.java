package pw.hachimi.client.mixin;

import net.minecraft.client.render.entity.model.AnimalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AnimalModel.class)
public interface AnimalModelMixin {
   @Invoker("getBodyParts")
   Iterable hookGetBodyParts();

   @Invoker("getHeadParts")
   Iterable hookGetHeadParts();

   @Accessor("headScaled")
   boolean hookGetHeadScaled();

   @Accessor("invertedChildHeadScale")
   float hookGetInvertedChildHeadScale();

   @Accessor("invertedChildBodyScale")
   float hookGetInvertedChildBodyScale();

   @Accessor("childHeadYOffset")
   float hookGetChildHeadYOffset();

   @Accessor("childHeadZOffset")
   float hookGetChildHeadZOffset();

   @Accessor("childBodyYOffset")
   float hookGetChildBodyYOffset();
}
