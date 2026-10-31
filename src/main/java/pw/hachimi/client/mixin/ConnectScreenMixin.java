package pw.hachimi.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.CookieStorage;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.network.ConnectEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {
   @Inject(method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;Lnet/minecraft/client/network/CookieStorage;)V", at = @At("HEAD"))
   private void onConnect(MinecraftClient client2, ServerAddress serverAddress, ServerInfo serverInfo, CookieStorage cookieStorage, CallbackInfo callbackInfo) {
      ConnectEvent connectEvent = new ConnectEvent(serverAddress, serverInfo);
      EventBusImpl.INSTANCE.rC(connectEvent);
   }
}
