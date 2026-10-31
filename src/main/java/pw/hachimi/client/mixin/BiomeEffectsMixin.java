package pw.hachimi.client.mixin;

import java.util.Optional;
import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.render.GetParticleConfigEvent;
import pw.hachimi.client.event.render.GetSkyColorEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(BiomeEffects.class)
public class BiomeEffectsMixin {
   @Inject(method = "getSkyColor()I", at = @At("HEAD"), cancellable = true)
   private void hookGetSkyColor(CallbackInfoReturnable callbackInfoReturnable) {
      GetSkyColorEvent getSkyColorEvent = new GetSkyColorEvent();
      EventBusImpl.INSTANCE.rC(getSkyColorEvent);
      if (getSkyColorEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getSkyColorEvent.arN());
      }
   }

   @Inject(method = "getParticleConfig()Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
   private void hookGetParticleConfig(CallbackInfoReturnable callbackInfoReturnable) {
      GetParticleConfigEvent getParticleConfigEvent = new GetParticleConfigEvent();
      EventBusImpl.INSTANCE.rC(getParticleConfigEvent);
      if (getParticleConfigEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(Optional.ofNullable(getParticleConfigEvent.getBiomeParticleConfig()));
      }
   }
}
