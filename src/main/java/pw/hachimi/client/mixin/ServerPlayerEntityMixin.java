package pw.hachimi.client.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.movement.MoveToWorldEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
   @Inject(method = "worldChanged(Lnet/minecraft/server/world/ServerWorld;)V", at = @At("HEAD"))
   private void hookMoveToWorld(ServerWorld serverWorld, CallbackInfo callbackInfo) {
      MoveToWorldEvent moveToWorldEvent = new MoveToWorldEvent();
      EventBusImpl.INSTANCE.rC(moveToWorldEvent);
   }
}
