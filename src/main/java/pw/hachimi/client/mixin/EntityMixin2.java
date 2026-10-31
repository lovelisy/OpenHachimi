package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.Mc;
import pw.hachimi.client.api.UnidentifiedInterface11;
import pw.hachimi.client.event.TickEvent4;
import pw.hachimi.client.event.movement.GetVelocityMultiplierEvent;
import pw.hachimi.client.event.movement.PushAwayFromEvent;
import pw.hachimi.client.event.movement.SlowMovementEvent;
import pw.hachimi.client.event.movement.TravelRotationEvent;
import pw.hachimi.client.event.render.DoesRenderOnFireEvent;
import pw.hachimi.client.event.render.GetTeamColorValueEvent;
import pw.hachimi.client.event.world.CameraPositionVecEvent;
import pw.hachimi.client.event.world.ChangeLookDirectionEvent;
import pw.hachimi.client.event.world.GetCameraPosVecEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(Entity.class)
public abstract class EntityMixin2 implements UnidentifiedInterface11 {
   @Unique
   public boolean d;
   @Shadow
   public boolean velocityDirty;
   @Shadow
   public float fallDistance;
   @Shadow
   protected Vec3d movementMultiplier;
   @Shadow
   private Vec3d velocity;

   @Shadow
   private static Vec3d movementInputToVelocity(Vec3d vec, float f, float f2) {
      return null;
   }

   @Override
   public boolean Ll() {
      return this.d;
   }

   @Override
   public void Lm(boolean flag) {
      this.d = flag;
   }

   @Shadow
   public abstract float getYaw();

   @Shadow
   public abstract float getPitch();

   @Shadow
   public abstract Box getBoundingBox();

   @Shadow
   public abstract Vec3d getVelocity();

   @Shadow
   public abstract void setVelocity(Vec3d var1);

   @Shadow
   public abstract boolean isSprinting();

   @Inject(method = "getPose()Lnet/minecraft/entity/EntityPose;", at = @At("RETURN"), cancellable = true)
   private void getPoseHook(CallbackInfoReturnable callbackInfoReturnable) {
      if ((Object)this == Mc.client.player) {
         if (callbackInfoReturnable.getReturnValue() == EntityPose.FALL_FLYING) {
            TickEvent4 tickEvent4 = new TickEvent4();
            EventBusImpl.INSTANCE.rC(tickEvent4);
            if (tickEvent4.aik()) {
               callbackInfoReturnable.setReturnValue(EntityPose.STANDING);
            }
         }
      }
   }

   @Inject(method = "getRotationVec(F)Lnet/minecraft/util/math/Vec3d;", at = @At("RETURN"), cancellable = true)
   public void hookGetCameraPosVec(float f, CallbackInfoReturnable callbackInfoReturnable) {
      GetCameraPosVecEvent getCameraPosVecEvent = new GetCameraPosVecEvent(f, (Entity)(Object)this, (Vec3d)callbackInfoReturnable.getReturnValue());
      EventBusImpl.INSTANCE.rC(getCameraPosVecEvent);
      callbackInfoReturnable.setReturnValue(getCameraPosVecEvent.getVec());
   }

   @Inject(method = "slowMovement(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
   private void hookSlowMovement(BlockState state, Vec3d vec, CallbackInfo callbackInfo) {
      if ((Object)this == Mc.client.player) {
         SlowMovementEvent slowMovementEvent = new SlowMovementEvent(state);
         EventBusImpl.INSTANCE.rC(slowMovementEvent);
         if (slowMovementEvent.aik()) {
            callbackInfo.cancel();
            this.fallDistance = 0.0F;
            this.movementMultiplier = vec.multiply(slowMovementEvent.WV());
         }
      }
   }

   @WrapOperation(method = "getVelocityMultiplier()F", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
   private Block hookGetVelocityMultiplier(BlockState state, Operation operation) {
      if ((Object)this != Mc.client.player) {
         return (Block)operation.call(new Object[]{state});
      }

      GetVelocityMultiplierEvent getVelocityMultiplierEvent = new GetVelocityMultiplierEvent(state);
      EventBusImpl.INSTANCE.rC(getVelocityMultiplierEvent);
      return getVelocityMultiplierEvent.aik() ? Blocks.DIRT : (Block)operation.call(new Object[]{state});
   }

   @ModifyExpressionValue(method = "updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getYaw()F"))
   public float onTravelRotation(float f) {
      if ((Object)this == Mc.client.player) {
         TravelRotationEvent travelRotationEvent = new TravelRotationEvent(this.getYaw(), this.getPitch());
         EventBusImpl.INSTANCE.rC(travelRotationEvent);
         if (travelRotationEvent.aik()) {
            return travelRotationEvent.a;
         }
      }

      return f;
   }

   @Inject(method = "pushAwayFrom(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
   private void hookPushAwayFrom(Entity entity2, CallbackInfo callbackInfo) {
      PushAwayFromEvent pushAwayFromEvent = new PushAwayFromEvent((Entity)(Object)this, entity2);
      EventBusImpl.INSTANCE.rC(pushAwayFromEvent);
      if (pushAwayFromEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "getTeamColorValue()I", at = @At("HEAD"), cancellable = true)
   private void hookGetTeamColorValue(CallbackInfoReturnable callbackInfoReturnable) {
      GetTeamColorValueEvent getTeamColorValueEvent = new GetTeamColorValueEvent((Entity)(Object)this);
      EventBusImpl.INSTANCE.rC(getTeamColorValueEvent);
      if (getTeamColorValueEvent.aik()) {
         callbackInfoReturnable.setReturnValue(getTeamColorValueEvent.oH());
         callbackInfoReturnable.cancel();
      }
   }

   @Inject(method = "changeLookDirection(DD)V", at = @At("HEAD"), cancellable = true)
   private void hookChangeLookDirection(double d2, double d3, CallbackInfo callbackInfo) {
      if ((Object)this == Mc.client.player) {
         ChangeLookDirectionEvent changeLookDirectionEvent = new ChangeLookDirectionEvent((Entity)(Object)this, d2, d3);
         EventBusImpl.INSTANCE.rC(changeLookDirectionEvent);
         if (changeLookDirectionEvent.aik()) {
            callbackInfo.cancel();
         }
      }
   }

   @Inject(method = "getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;", at = @At("RETURN"), cancellable = true)
   public void hookCameraPositionVec(float f, CallbackInfoReturnable callbackInfoReturnable) {
      CameraPositionVecEvent cameraPositionVecEvent = new CameraPositionVecEvent((Vec3d)callbackInfoReturnable.getReturnValue(), (Entity)(Object)this, f);
      EventBusImpl.INSTANCE.rC(cameraPositionVecEvent);
      callbackInfoReturnable.setReturnValue(cameraPositionVecEvent.getVec());
   }

   @Inject(method = "doesRenderOnFire()Z", at = @At("HEAD"), cancellable = true)
   private void hookDoesRenderOnFire(CallbackInfoReturnable callbackInfoReturnable) {
      DoesRenderOnFireEvent doesRenderOnFireEvent = new DoesRenderOnFireEvent();
      EventBusImpl.INSTANCE.rC(doesRenderOnFireEvent);
      if (doesRenderOnFireEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(false);
      }
   }
}
