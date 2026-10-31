package pw.hachimi.client.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.event.network.SendChatMessageEvent;
import pw.hachimi.client.event.player.OnEntityEquipmentUpdateEvent;
import pw.hachimi.client.event.player.PlayerPositionLookEvent;
import pw.hachimi.client.event.world.OnChunkDataEvent;
import pw.hachimi.client.handler.PacketHandler;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements PacketHandler {
   @Shadow
   private ClientWorld world;

   @Shadow
   public abstract ClientConnection getConnection();

   @Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
   private void hookSendChatMessage(String str, CallbackInfo callbackInfo) {
      SendChatMessageEvent sendChatMessageEvent = new SendChatMessageEvent(str);
      EventBusImpl.INSTANCE.rC(sendChatMessageEvent);
      if (sendChatMessageEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "onEntityEquipmentUpdate(Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;)V", at = @At("TAIL"))
   private void hookOnEntityEquipmentUpdate(EntityEquipmentUpdateS2CPacket entityEquipmentUpdateS2CPacket, CallbackInfo callbackInfo) {
      OnEntityEquipmentUpdateEvent onEntityEquipmentUpdateEvent = new OnEntityEquipmentUpdateEvent(entityEquipmentUpdateS2CPacket);
      EventBusImpl.INSTANCE.rC(onEntityEquipmentUpdateEvent);
   }

   @Inject(method = "onGameJoin(Lnet/minecraft/network/packet/s2c/play/GameJoinS2CPacket;)V", at = @At("TAIL"))
   private void hookOnGameJoin(GameJoinS2CPacket gameJoinS2CPacket, CallbackInfo callbackInfo) {
      OnGameJoinEvent onGameJoinEvent = new OnGameJoinEvent();
      EventBusImpl.INSTANCE.rC(onGameJoinEvent);
   }

   @Inject(method = "onChunkData(Lnet/minecraft/network/packet/s2c/play/ChunkDataS2CPacket;)V", at = @At("RETURN"))
   private void hookOnChunkData(ChunkDataS2CPacket chunkDataS2CPacket, CallbackInfo callbackInfo) {
      OnChunkDataEvent onChunkDataEvent = new OnChunkDataEvent(chunkDataS2CPacket);
      EventBusImpl.INSTANCE.rC(onChunkDataEvent);
   }

   @Inject(method = "onPlayerPositionLook(Lnet/minecraft/network/packet/s2c/play/PlayerPositionLookS2CPacket;)V", at = @At("HEAD"), cancellable = true)
   private void onPlayerPositionLook(PlayerPositionLookS2CPacket playerPositionLookS2CPacket, CallbackInfo callbackInfo) {
      NetworkThreadUtils.forceMainThread(playerPositionLookS2CPacket, (ClientPlayNetworkHandler)(Object)this, Mc.client);
      PlayerPositionLookEvent playerPositionLookEvent = new PlayerPositionLookEvent(playerPositionLookS2CPacket);
      EventBusImpl.INSTANCE.rC(playerPositionLookEvent);
      if (playerPositionLookEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Override
   public void jG(Packet packet2) {
      ((ClientConnectionMixin)this.getConnection()).hookSendInternal(packet2, null, true);
   }
}
