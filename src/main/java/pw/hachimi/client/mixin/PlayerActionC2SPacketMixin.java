package pw.hachimi.client.mixin;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerActionC2SPacket.class)
public interface PlayerActionC2SPacketMixin {
   @Accessor("pos")
   @Mutable
   void hookSetPos(BlockPos var1);
}
