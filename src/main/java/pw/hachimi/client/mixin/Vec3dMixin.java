package pw.hachimi.client.mixin;

import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Vec3d.class)
public interface Vec3dMixin {
   @Mutable
   @Accessor("x")
   void setX(double var1);

   @Mutable
   @Accessor("y")
   void setY(double var1);

   @Mutable
   @Accessor("z")
   void setZ(double var1);
}
