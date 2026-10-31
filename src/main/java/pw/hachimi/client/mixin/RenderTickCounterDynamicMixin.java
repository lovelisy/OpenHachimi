package pw.hachimi.client.mixin;

import net.minecraft.client.render.RenderTickCounter.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.render.BeginRenderTickEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(Dynamic.class)
public class RenderTickCounterDynamicMixin {
   @Shadow
   private float lastFrameDuration;
   @Shadow
   private float tickDelta;
   @Shadow
   private long prevTimeMillis;
   @Final
   @Shadow
   private float tickTime;

   @Inject(method = "beginRenderTick(J)I", at = @At("HEAD"), cancellable = true)
   private void hookBeginRenderTick(long l, CallbackInfoReturnable callbackInfoReturnable) {
      BeginRenderTickEvent beginRenderTickEvent = new BeginRenderTickEvent();
      EventBusImpl.INSTANCE.rC(beginRenderTickEvent);
      if (beginRenderTickEvent.aik()) {
         this.lastFrameDuration = (float)(l - this.prevTimeMillis) / this.tickTime * beginRenderTickEvent.qS();
         this.prevTimeMillis = l;
         this.tickDelta = this.tickDelta + this.lastFrameDuration;
         int n = (int)this.tickDelta;
         this.tickDelta -= n;
         callbackInfoReturnable.setReturnValue(n);
      }
   }
}
