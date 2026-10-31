package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.ClipAtLedgeEvent;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickEvent4;
import pw.hachimi.client.event.combat.AttackEvent2;
import pw.hachimi.client.event.movement.IsPushedByFluidsEvent;
import pw.hachimi.client.event.movement.JumpPreEvent;
import pw.hachimi.client.event.movement.TravelHeadEvent;
import pw.hachimi.client.event.movement.TravelRotationEvent;
import pw.hachimi.client.event.world.GetBlockInteractionRangeEvent;
import pw.hachimi.client.event.world.GetEntityInteractionRangeEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
   protected PlayerEntityMixin(EntityType entityType, World world2) {
      super(entityType, world2);
   }

   @Shadow
   public abstract void travel(Vec3d movementInput);

   @ModifyExpressionValue(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
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

   @Inject(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
   private void hookTravelHead(Vec3d vec, CallbackInfo callbackInfo) {
      TravelHeadEvent travelHeadEvent = new TravelHeadEvent(vec);
      EventBusImpl.INSTANCE.rC(travelHeadEvent);
      if (travelHeadEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "isPushedByFluids()Z", at = @At("HEAD"), cancellable = true)
   private void hookIsPushedByFluids(CallbackInfoReturnable callbackInfoReturnable) {
      if ((Object)this == Mc.client.player) {
         IsPushedByFluidsEvent isPushedByFluidsEvent = new IsPushedByFluidsEvent();
         EventBusImpl.INSTANCE.rC(isPushedByFluidsEvent);
         if (isPushedByFluidsEvent.aik()) {
            callbackInfoReturnable.setReturnValue(false);
            callbackInfoReturnable.cancel();
         }
      }
   }

   @Inject(method = "jump()V", at = @At("HEAD"), cancellable = true)
   private void hookJumpPre(CallbackInfo callbackInfo) {
      if ((Object)this == Mc.client.player) {
         JumpPreEvent jumpPreEvent = new JumpPreEvent();
         jumpPreEvent.aqo(EventPhase.PRE);
         EventBusImpl.INSTANCE.rC(jumpPreEvent);
         if (jumpPreEvent.aik()) {
            callbackInfo.cancel();
         }
      }
   }

   @Inject(method = "jump()V", at = @At("RETURN"))
   private void hookJumpPost(CallbackInfo callbackInfo) {
      if ((Object)this == Mc.client.player) {
         JumpPreEvent jumpPreEvent = new JumpPreEvent();
         jumpPreEvent.aqo(EventPhase.POST);
         EventBusImpl.INSTANCE.rC(jumpPreEvent);
      }
   }

   @Redirect(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
   private void hookAttack(PlayerEntity player2, Vec3d vec) {
      if (player2 instanceof ClientPlayerEntity) {
         AttackEvent2 attackEvent2 = new AttackEvent2();
         EventBusImpl.INSTANCE.rC(attackEvent2);
         if (!attackEvent2.aik()) {
            Mc.client.player.setVelocity(Mc.client.player.getVelocity().multiply(0.6, 1.0, 0.6));
         }
      }
   }

   @Redirect(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V"))
   private void hookAttack$1(PlayerEntity player2, boolean flag) {
      if (player2 instanceof ClientPlayerEntity) {
         AttackEvent2 attackEvent2 = new AttackEvent2();
         EventBusImpl.INSTANCE.rC(attackEvent2);
         if (!attackEvent2.aik()) {
            Mc.client.player.setSprinting(false);
         }
      }
   }

   @Inject(method = "clipAtLedge()Z", at = @At("HEAD"), cancellable = true)
   private void hookClipAtLedge(CallbackInfoReturnable callbackInfoReturnable) {
      ClipAtLedgeEvent clipAtLedgeEvent = new ClipAtLedgeEvent();
      EventBusImpl.INSTANCE.rC(clipAtLedgeEvent);
      if (clipAtLedgeEvent.aik()) {
         callbackInfoReturnable.setReturnValue(clipAtLedgeEvent.pZ());
      }
   }

   @WrapOperation(method = "updatePose()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isFallFlying()Z"))
   private boolean hookUpdatePose(PlayerEntity player2, Operation operation) {
      if (player2 == Mc.client.player) {
         TickEvent4 tickEvent4 = new TickEvent4();
         EventBusImpl.INSTANCE.rC(tickEvent4);
         if (tickEvent4.aik()) {
            return false;
         }
      }

      return (Boolean)operation.call(new Object[]{player2});
   }

   @Inject(method = "getBlockInteractionRange()D", at = @At("HEAD"), cancellable = true)
   private void hookGetBlockInteractionRange(CallbackInfoReturnable callbackInfoReturnable) {
      GetBlockInteractionRangeEvent getBlockInteractionRangeEvent = new GetBlockInteractionRangeEvent();
      EventBusImpl.INSTANCE.rC(getBlockInteractionRangeEvent);
      if (getBlockInteractionRangeEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getBlockInteractionRangeEvent.MD());
      }
   }

   @Inject(method = "getEntityInteractionRange()D", at = @At("HEAD"), cancellable = true)
   private void hookGetEntityInteractionRange(CallbackInfoReturnable callbackInfoReturnable) {
      GetEntityInteractionRangeEvent getEntityInteractionRangeEvent = new GetEntityInteractionRangeEvent();
      EventBusImpl.INSTANCE.rC(getEntityInteractionRangeEvent);
      if (getEntityInteractionRangeEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getEntityInteractionRangeEvent.MD());
      }
   }
}
