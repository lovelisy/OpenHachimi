package pw.hachimi.client.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.AddEmitterEvent;
import pw.hachimi.client.event.render.AddParticleEvent;
import pw.hachimi.client.event.world.AddBlockBreakParticlesEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
   @Shadow
   @Final
   private Object2IntOpenHashMap groupCounts;

   @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
   private void hookAddParticle(
      ParticleEffect particleEffect, double d, double d2, double d3, double d4, double d5, double d6, CallbackInfoReturnable callbackInfoReturnable
   ) {
      AddParticleEvent addParticleEvent = new AddParticleEvent(particleEffect);
      EventBusImpl.INSTANCE.rC(addParticleEvent);
      if (addParticleEvent.aik()) {
         callbackInfoReturnable.setReturnValue(null);
         callbackInfoReturnable.cancel();
      }
   }

   @Inject(method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V", at = @At("HEAD"), cancellable = true)
   private void hookAddEmitter(Entity entity2, ParticleEffect particleEffect, int n, CallbackInfo callbackInfo) {
      AddEmitterEvent addEmitterEvent = new AddEmitterEvent(particleEffect);
      EventBusImpl.INSTANCE.rC(addEmitterEvent);
      if (addEmitterEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "addBlockBreakParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"), cancellable = true)
   private void onAddBlockBreakParticles(BlockPos pos, BlockState state, CallbackInfo callbackInfo) {
      AddBlockBreakParticlesEvent addBlockBreakParticlesEvent = new AddBlockBreakParticlesEvent();
      EventBusImpl.INSTANCE.rC(addBlockBreakParticlesEvent);
      if (addBlockBreakParticlesEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "addBlockBreakingParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)V", at = @At("HEAD"), cancellable = true)
   private void onAddBlockBreakingParticles(BlockPos pos, Direction direction, CallbackInfo callbackInfo) {
      AddBlockBreakParticlesEvent addBlockBreakParticlesEvent = new AddBlockBreakParticlesEvent();
      EventBusImpl.INSTANCE.rC(addBlockBreakParticlesEvent);
      if (addBlockBreakParticlesEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
