package pw.hachimi.client.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.entity.EntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.render.GetCloudsColorEvent;
import pw.hachimi.client.event.render.GetSkyColorEvent;
import pw.hachimi.client.event.world.AddEntityEvent;
import pw.hachimi.client.event.world.AddEntity_2Event;
import pw.hachimi.client.event.world.PlaySoundEvent;
import pw.hachimi.client.event.world.RemoveEntityEvent;
import pw.hachimi.client.event.world.SetBlockStateEvent;
import pw.hachimi.client.event.world.TickEntityEvent;
import pw.hachimi.client.event.world.UnloadBlockEntitiesEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
   @Shadow
   protected abstract EntityLookup getEntityLookup();

   @Inject(method = "tickEntities()V", at = @At("HEAD"))
   private void tickEntities$Head(CallbackInfo callbackInfo) {
      TickEntitiesPreEvent tickEntitiesPreEvent = new TickEntitiesPreEvent();
      EventBusImpl.INSTANCE.rC(tickEntitiesPreEvent);
   }

   @Inject(method = "tickEntity(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
   public void onTickEntity(Entity entity2, CallbackInfo callbackInfo) {
      TickEntityEvent tickEntityEvent = new TickEntityEvent(entity2);
      EventBusImpl.INSTANCE.rC(tickEntityEvent);
      if (tickEntityEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "addEntity(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
   private void hookAddEntity(Entity entity2, CallbackInfo callbackInfo) {
      AddEntityEvent addEntityEvent = new AddEntityEvent(entity2);
      EventBusImpl.INSTANCE.rC(addEntityEvent);
   }

   @Inject(method = "addEntity(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
   private void hookAddEntity$2(Entity entity2, CallbackInfo callbackInfo) {
      AddEntity_2Event addEntity2Event = new AddEntity_2Event(entity2);
      EventBusImpl.INSTANCE.rC(addEntity2Event);
   }

   @Inject(method = "removeEntity(ILnet/minecraft/entity/Entity$RemovalReason;)V", at = @At("HEAD"))
   private void hookRemoveEntity(int n, RemovalReason removalReason, CallbackInfo callbackInfo) {
      Entity entity2 = (Entity)this.getEntityLookup().get(n);
      if (entity2 != null) {
         RemoveEntityEvent removeEntityEvent = new RemoveEntityEvent(entity2, removalReason);
         EventBusImpl.INSTANCE.rC(removeEntityEvent);
      }
   }

   @Inject(method = "getSkyColor(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
   private void hookGetSkyColor(Vec3d vec, float f, CallbackInfoReturnable callbackInfoReturnable) {
      GetSkyColorEvent getSkyColorEvent = new GetSkyColorEvent();
      EventBusImpl.INSTANCE.rC(getSkyColorEvent);
      if (getSkyColorEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getSkyColorEvent.arM());
      }
   }

   @Inject(method = "getCloudsColor(F)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
   private void hookGetCloudsColor(float f, CallbackInfoReturnable callbackInfoReturnable) {
      GetCloudsColorEvent getCloudsColorEvent = new GetCloudsColorEvent();
      EventBusImpl.INSTANCE.rC(getCloudsColorEvent);
      if (getCloudsColorEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getCloudsColorEvent.arM());
      }
   }

   @Inject(method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V", at = @At("HEAD"), cancellable = true)
   private void hookPlaySound(
      double d, double d2, double d3, SoundEvent soundEvent, SoundCategory soundCategory, float f, float f2, boolean flag, long l, CallbackInfo callbackInfo
   ) {
      PlaySoundEvent playSoundEvent = new PlaySoundEvent(new Vec3d(d, d2, d3), soundEvent, soundCategory);
      EventBusImpl.INSTANCE.rC(playSoundEvent);
      if (playSoundEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"), cancellable = true)
   private void hookSetBlockState(BlockPos pos, BlockState state, int n, int n2, CallbackInfoReturnable callbackInfoReturnable) {
      SetBlockStateEvent setBlockStateEvent = new SetBlockStateEvent(n, pos, state);
      EventBusImpl.INSTANCE.rC(setBlockStateEvent);
      if (setBlockStateEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(false);
      }
   }

   @Inject(method = "unloadBlockEntities(Lnet/minecraft/world/chunk/WorldChunk;)V", at = @At("HEAD"))
   private void hookUnloadBlockEntities(WorldChunk worldChunk, CallbackInfo callbackInfo) {
      UnloadBlockEntitiesEvent unloadBlockEntitiesEvent = new UnloadBlockEntitiesEvent(worldChunk);
      EventBusImpl.INSTANCE.rC(unloadBlockEntitiesEvent);
   }
}
