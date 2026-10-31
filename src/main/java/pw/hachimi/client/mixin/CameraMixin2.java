package pw.hachimi.client.mixin;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.ClipToSpaceEvent;
import pw.hachimi.client.event.player.UpdateRotationEvent;
import pw.hachimi.client.event.render.RenderUnderwaterOverlayEvent;
import pw.hachimi.client.event.world.UpdatePositionEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(Camera.class)
public abstract class CameraMixin2 {
   @Shadow
   private float lastTickDelta;

   @Shadow
   protected abstract void setPos(double var1, double var3, double var5);

   @Shadow
   protected abstract void setRotation(float var1, float var2);

   @Inject(method = "getSubmersionType()Lnet/minecraft/block/enums/CameraSubmersionType;", at = @At("HEAD"), cancellable = true)
   private void hookGetSubmersionType(CallbackInfoReturnable callbackInfoReturnable) {
      RenderUnderwaterOverlayEvent renderUnderwaterOverlayEvent = new RenderUnderwaterOverlayEvent(null);
      EventBusImpl.INSTANCE.rC(renderUnderwaterOverlayEvent);
      if (renderUnderwaterOverlayEvent.aik()) {
         callbackInfoReturnable.setReturnValue(CameraSubmersionType.NONE);
         callbackInfoReturnable.cancel();
      }
   }

   @Inject(method = "clipToSpace(F)F", at = @At("HEAD"), cancellable = true)
   private void hookClipToSpace(float f, CallbackInfoReturnable callbackInfoReturnable) {
      ClipToSpaceEvent clipToSpaceEvent = new ClipToSpaceEvent(f);
      EventBusImpl.INSTANCE.rC(clipToSpaceEvent);
      if (clipToSpaceEvent.aik()) {
         callbackInfoReturnable.setReturnValue(clipToSpaceEvent.TW());
         callbackInfoReturnable.cancel();
      }
   }

   @Redirect(method = "update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
   private void hookUpdatePosition(Camera camera, double d, double d2, double d3) {
      UpdatePositionEvent updatePositionEvent = new UpdatePositionEvent(d, d2, d3, this.lastTickDelta);
      EventBusImpl.INSTANCE.rC(updatePositionEvent);
      this.setPos(updatePositionEvent.Dx(), updatePositionEvent.Dz(), updatePositionEvent.DB());
   }

   @Redirect(method = "update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
   private void hookUpdateRotation(Camera camera, float f, float f2) {
      UpdateRotationEvent updateRotationEvent = new UpdateRotationEvent(f, f2, this.lastTickDelta);
      EventBusImpl.INSTANCE.rC(updateRotationEvent);
      this.setRotation(updateRotationEvent.ET(), updateRotationEvent.EV());
   }
}
