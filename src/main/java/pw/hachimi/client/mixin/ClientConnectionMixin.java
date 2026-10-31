package pw.hachimi.client.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientConnection.class)
public interface ClientConnectionMixin {
   @Invoker("sendInternal")
   void hookSendInternal(Packet var1, @Nullable PacketCallbacks var2, boolean var3);
}
