package pw.hachimi.client.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import net.minecraft.network.handler.DecoderHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.network.DecodeEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(DecoderHandler.class)
public class DecoderHandlerMixin {
   @Inject(
      method = "decode",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkPhase;getId()Ljava/lang/String;", shift = Shift.AFTER),
      cancellable = true
   )
   private void hookDecode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list, CallbackInfo callbackInfo) {
      DecodeEvent decodeEvent = new DecodeEvent();
      EventBusImpl.INSTANCE.rC(decodeEvent);
      if (decodeEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
