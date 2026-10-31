package pw.hachimi.client.mixin;

import net.minecraft.world.chunk.light.ChunkSkyLightProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.RecalculateLevelEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ChunkSkyLightProvider.class)
public class ChunkSkyLightProviderMixin {
   @Inject(method = "method_51531", at = @At("HEAD"), cancellable = true)
   private void hookRecalculateLevel(long l, long l2, int n, CallbackInfo callbackInfo) {
      RecalculateLevelEvent recalculateLevelEvent = new RecalculateLevelEvent();
      EventBusImpl.INSTANCE.rC(recalculateLevelEvent);
      if (recalculateLevelEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
