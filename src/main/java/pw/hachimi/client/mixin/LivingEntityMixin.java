package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.AddStatusEffectEvent;
import pw.hachimi.client.event.HasStatusEffectEvent;
import pw.hachimi.client.event.RecastOnLandEvent;
import pw.hachimi.client.event.RemoveStatusEffectEvent;
import pw.hachimi.client.event.TickEvent4;
import pw.hachimi.client.event.UnknownEvent7;
import pw.hachimi.client.event.movement.ClimbingHookEvent;
import pw.hachimi.client.event.movement.ElytraVelocityEvent;
import pw.hachimi.client.event.movement.GetStepHeightEvent;
import pw.hachimi.client.event.movement.Jump_getYawEvent;
import pw.hachimi.client.event.movement.Jump_isSprintingEvent;
import pw.hachimi.client.event.movement.TickMovementEvent;
import pw.hachimi.client.event.movement.TravelPreEvent;
import pw.hachimi.client.event.movement.TravelRotationEvent;
import pw.hachimi.client.event.player.ConsumeItemEvent;
import pw.hachimi.client.event.player.GetHandSwingDurationEvent;
import pw.hachimi.client.event.world.UpdateTrackedPositionAndAnglesEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin2 {
   @Shadow
   protected ItemStack activeItemStack;
   @Shadow
   private int jumpingCooldown;

   @Shadow
   public abstract float getYaw(float var1);

   @Shadow
   public abstract boolean hasStatusEffect(RegistryEntry var1);

   @Inject(method = "isFallFlying()Z", at = @At("RETURN"), cancellable = true)
   public void recastOnLand(CallbackInfoReturnable callbackInfoReturnable) {
      if (LivingEntity.class.cast(this) == Mc.client.player) {
         RecastOnLandEvent recastOnLandEvent = new RecastOnLandEvent((Boolean)callbackInfoReturnable.getReturnValue());
         EventBusImpl.INSTANCE.rC(recastOnLandEvent);
         if (recastOnLandEvent.aik()) {
            callbackInfoReturnable.setReturnValue(recastOnLandEvent.yh());
         }
      }
   }

   @WrapOperation(
      method = "travel(Lnet/minecraft/util/math/Vec3d;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 6)
   )
   public void onElytraVelocity(LivingEntity living, Vec3d vec, Operation operation) {
      if (living == Mc.client.player) {
         ElytraVelocityEvent elytraVelocityEvent = new ElytraVelocityEvent(vec);
         EventBusImpl.INSTANCE.rC(elytraVelocityEvent);
         if (elytraVelocityEvent.aik()) {
            return;
         }
      }

      operation.call(new Object[]{living, vec});
   }

   @ModifyExpressionValue(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
   public Vec3d onTravelRotation(Vec3d vec) {
      if ((Object)this == Mc.client.player) {
         TravelRotationEvent travelRotationEvent = new TravelRotationEvent(this.getYaw(), this.getPitch());
         EventBusImpl.INSTANCE.rC(travelRotationEvent);
         if (travelRotationEvent.aik()) {
            return travelRotationEvent.KW();
         }
      }

      return vec;
   }

   @ModifyExpressionValue(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"))
   @Override
   public float onTravelRotation(float f) {
      if ((Object)this == Mc.client.player) {
         TravelRotationEvent travelRotationEvent = new TravelRotationEvent(this.getYaw(), this.getPitch());
         EventBusImpl.INSTANCE.rC(travelRotationEvent);
         if (travelRotationEvent.aik()) {
            return travelRotationEvent.b;
         }
      }

      return f;
   }

   @Inject(method = "getHandSwingDuration()I", at = @At("HEAD"), cancellable = true)
   private void hookGetHandSwingDuration(CallbackInfoReturnable callbackInfoReturnable) {
      GetHandSwingDurationEvent getHandSwingDurationEvent = new GetHandSwingDurationEvent();
      EventBusImpl.INSTANCE.rC(getHandSwingDurationEvent);
      if (getHandSwingDurationEvent.aik()) {
         if (getHandSwingDurationEvent.Mx() && (Object)this != Mc.client.player) {
            return;
         }

         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getHandSwingDurationEvent.Mv());
      }
   }

   @ModifyExpressionValue(method = "jump()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSprinting()Z"))
   private boolean hookJump$isSprinting(boolean flag) {
      if ((Object)this != Mc.client.player) {
         return flag;
      }

      Jump_isSprintingEvent jumpisSprintingEvent = new Jump_isSprintingEvent(flag);
      EventBusImpl.INSTANCE.rC(jumpisSprintingEvent);
      return jumpisSprintingEvent.aik() ? jumpisSprintingEvent.Ca() : flag;
   }

   @WrapOperation(method = "tickFallFlying()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"))
   private void tickFallFlying$setFallflying(LivingEntity living, int n, boolean flag, Operation operation) {
      UnknownEvent7.abC(living, n, flag, operation);
   }

   @ModifyExpressionValue(method = "jump()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
   private float hookJump$getYaw(float f) {
      if ((Object)this != Mc.client.player) {
         return f;
      }

      Jump_getYawEvent jumpgetYawEvent = new Jump_getYawEvent(f);
      EventBusImpl.INSTANCE.rC(jumpgetYawEvent);
      return jumpgetYawEvent.aik() ? jumpgetYawEvent.On() : f;
   }

   @WrapOperation(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"))
   private boolean hookHasStatusEffect(LivingEntity living, RegistryEntry registryEntry, Operation operation) {
      if (living.equals(Mc.client.player) && registryEntry == StatusEffects.LEVITATION) {
         HasStatusEffectEvent hasStatusEffectEvent = new HasStatusEffectEvent();
         EventBusImpl.INSTANCE.rC(hasStatusEffectEvent);
         return !hasStatusEffectEvent.aik() && (Boolean)operation.call(new Object[]{living, registryEntry});
      } else {
         return (Boolean)operation.call(new Object[]{living, registryEntry});
      }
   }

   @Inject(
      method = "consumeItem()V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/item/ItemStack;finishUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;",
         shift = Shift.AFTER
      )
   )
   private void hookConsumeItem(CallbackInfo callbackInfo) {
      if ((Object)this == Mc.client.player) {
         ConsumeItemEvent consumeItemEvent = new ConsumeItemEvent(this.activeItemStack);
         EventBusImpl.INSTANCE.rC(consumeItemEvent);
      }
   }

   @Inject(method = "tickMovement()V", at = @At("HEAD"))
   private void hookTickMovement(CallbackInfo callbackInfo) {
      if ((Object)this == Mc.client.player) {
         TickMovementEvent tickMovementEvent = new TickMovementEvent();
         EventBusImpl.INSTANCE.rC(tickMovementEvent);
         if (tickMovementEvent.aik()) {
            this.jumpingCooldown = 0;
         }
      }
   }

   @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"))
   private void hookAddStatusEffect(StatusEffectInstance statusEffectInstance, Entity entity2, CallbackInfoReturnable callbackInfoReturnable) {
      if ((Object)this == Mc.client.player) {
         AddStatusEffectEvent addStatusEffectEvent = new AddStatusEffectEvent(statusEffectInstance);
         EventBusImpl.INSTANCE.rC(addStatusEffectEvent);
      }
   }

   @Inject(method = "setStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
   private void hookAddStatusEffect$1(StatusEffectInstance statusEffectInstance, Entity entity2, CallbackInfo callbackInfo) {
      if ((Object)this == Mc.client.player) {
         AddStatusEffectEvent addStatusEffectEvent = new AddStatusEffectEvent(statusEffectInstance);
         EventBusImpl.INSTANCE.rC(addStatusEffectEvent);
      }
   }

   @Inject(method = "removeStatusEffectInternal(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/entity/effect/StatusEffectInstance;", at = @At("HEAD"))
   private void hookRemoveStatusEffect(RegistryEntry registryEntry, CallbackInfoReturnable callbackInfoReturnable) {
      if ((Object)this == Mc.client.player) {
         RemoveStatusEffectEvent removeStatusEffectEvent = new RemoveStatusEffectEvent(registryEntry);
         EventBusImpl.INSTANCE.rC(removeStatusEffectEvent);
      }
   }

   @Inject(method = "updateTrackedPositionAndAngles(DDDFFI)V", at = @At("HEAD"))
   private void hookUpdateTrackedPositionAndAngles(double d, double d2, double d3, float f, float f2, int n, CallbackInfo callbackInfo) {
      UpdateTrackedPositionAndAnglesEvent updateTrackedPositionAndAnglesEvent = new UpdateTrackedPositionAndAnglesEvent((LivingEntity)(Object)this, d, d2, d3, f, f2);
      EventBusImpl.INSTANCE.rC(updateTrackedPositionAndAnglesEvent);
   }

   @Inject(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
   private void hookTravelPre(Vec3d vec, CallbackInfo callbackInfo) {
      TravelPreEvent travelPreEvent = new TravelPreEvent((LivingEntity)(Object)this, true);
      EventBusImpl.INSTANCE.rC(travelPreEvent);
      if (travelPreEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At("RETURN"))
   private void hookTravelPost(Vec3d vec, CallbackInfo callbackInfo) {
      TravelPreEvent travelPreEvent = new TravelPreEvent((LivingEntity)(Object)this, false);
      EventBusImpl.INSTANCE.rC(travelPreEvent);
   }

   @WrapOperation(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"))
   private boolean hookTick(LivingEntity living, Operation operation) {
      if (living == Mc.client.player) {
         TickEvent4 tickEvent4 = new TickEvent4();
         EventBusImpl.INSTANCE.rC(tickEvent4);
         if (tickEvent4.aik()) {
            return false;
         }
      }

      return (Boolean)operation.call(new Object[]{living});
   }

   @Inject(method = "getStepHeight()F", at = @At("HEAD"), cancellable = true)
   private void hookGetStepHeight(CallbackInfoReturnable callbackInfoReturnable) {
      GetStepHeightEvent getStepHeightEvent = new GetStepHeightEvent((LivingEntity)(Object)this, callbackInfoReturnable.getReturnValueF());
      EventBusImpl.INSTANCE.rC(getStepHeightEvent);
      if (getStepHeightEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getStepHeightEvent.Df());
      }
   }

   @ModifyExpressionValue(method = "applyMovementInput(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isClimbing()Z"))
   public boolean climbingHook(boolean flag) {
      if ((Object)this == Mc.client.player) {
         ClimbingHookEvent climbingHookEvent = new ClimbingHookEvent();
         EventBusImpl.INSTANCE.rC(climbingHookEvent);
         if (climbingHookEvent.aik()) {
            return false;
         }
      }

      return flag;
   }
}
