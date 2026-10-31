package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.TickEvent;
import pw.hachimi.client.event.TickEvent3;
import pw.hachimi.client.event.player.GetRotationVectorHookEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin2 {
   @Shadow
   @Nullable
   private LivingEntity shooter;
   @Shadow
   private int life;
   @Shadow
   private int lifeTime;

   @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
   private void hookTick(CallbackInfo callbackInfo) {
      TickEvent3 tickEvent3 = new TickEvent3((FireworkRocketEntity)(Object)this);
      EventBusImpl.INSTANCE.rC(tickEvent3);
      if (tickEvent3.aik()) {
         callbackInfo.cancel();
      }
   }

   @WrapOperation(
      method = "tick()V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 0)
   )
   public Vec3d tick$setVelocity(Vec3d vec, double d, double d2, double d3, Operation operation) {
      if (this.shooter != Mc.client.player) {
         return (Vec3d)operation.call(new Object[]{vec, d, d2, d3});
      }

      TickEvent tickEvent = new TickEvent(d, d2, d3);
      EventBusImpl.INSTANCE.rC(tickEvent);
      return tickEvent.aik() ? vec.add(tickEvent.aqf(), tickEvent.aqe(), tickEvent.aqd()) : (Vec3d)operation.call(new Object[]{vec, d, d2, d3});
   }

   @WrapOperation(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
   public Vec3d getRotationVectorHook(LivingEntity living, Operation operation) {
      GetRotationVectorHookEvent getRotationVectorHookEvent = new GetRotationVectorHookEvent(living, living.getYaw(), living.getPitch());
      EventBusImpl.INSTANCE.rC(getRotationVectorHookEvent);
      return getRotationVectorHookEvent.aik() ? getRotationVectorHookEvent.nF() : (Vec3d)operation.call(new Object[]{living});
   }
}
