package pw.hachimi.client.mixin;

import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VehicleMoveC2SPacket.class)
public interface VehicleMoveC2SPacketMixin {
   @Accessor("y")
   void hookSetY(double var1);
}
