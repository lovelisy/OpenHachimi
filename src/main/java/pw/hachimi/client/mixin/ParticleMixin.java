package pw.hachimi.client.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Particle.class)
public abstract class ParticleMixin {
   @Shadow
   public abstract void markDead();

   @Shadow
   public abstract void setColor(float var1, float var2, float var3);
}
