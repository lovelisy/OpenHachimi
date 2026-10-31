package pw.hachimi.client.mixin;

import net.minecraft.client.particle.EmitterParticle;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.TickEvent22;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(EmitterParticle.class)
public abstract class EmitterParticleMixin extends ParticleMixin {
   @Shadow
   @Final
   private ParticleEffect parameters;
   @Shadow
   @Final
   private Entity entity;
   @Shadow
   private int emitterAge;
   @Mutable
   @Shadow
   @Final
   private int maxEmitterAge;

   @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
   private void onTick(CallbackInfo callbackInfo) {
      TickEvent22 tickEvent22 = new TickEvent22(this.parameters, 16, 30);
      EventBusImpl.INSTANCE.rC(tickEvent22);
      if (tickEvent22.aik()) {
         callbackInfo.cancel();
         this.maxEmitterAge = tickEvent22.f();

         for (int n = 0; n < tickEvent22.d_1(); n++) {
            double d = Mc.random.nextFloat() * 2.0F - 1.0F;
            double d2;
            double d3;
            if (!(d * d + (d3 = Mc.random.nextFloat() * 2.0F - 1.0F) * d3 + (d2 = Mc.random.nextFloat() * 2.0F - 1.0F) * d2 > 1.0)) {
               double d4 = this.entity.offsetX(d / 4.0);
               double d5 = this.entity.getBodyY(0.5 + d3 / 4.0);
               double d6 = this.entity.offsetZ(d2 / 4.0);
               Mc.client.world.addParticle(this.parameters, false, d4, d5, d6, d, d3 + 0.2, d2);
            }
         }

         this.emitterAge++;
         if (this.emitterAge >= this.maxEmitterAge) {
            this.markDead();
         }
      }
   }
}
