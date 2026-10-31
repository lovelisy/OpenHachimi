package pw.hachimi.client.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.particle.FireworksSparkParticle.FireworkParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.AddExplosionParticleEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(FireworkParticle.class)
public class FireworksSparkParticleFireworkParticleMixin {
   @Inject(method = "addExplosionParticle(DDDDDDLit/unimi/dsi/fastutil/ints/IntList;Lit/unimi/dsi/fastutil/ints/IntList;ZZ)V", at = @At("HEAD"), cancellable = true)
   private void addExplosionParticle(
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      IntList intList,
      IntList intList2,
      boolean flag,
      boolean flag2,
      CallbackInfo callbackInfo
   ) {
      AddExplosionParticleEvent addExplosionParticleEvent = new AddExplosionParticleEvent();
      EventBusImpl.INSTANCE.rC(addExplosionParticleEvent);
      if (addExplosionParticleEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
