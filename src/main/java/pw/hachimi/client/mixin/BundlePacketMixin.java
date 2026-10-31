package pw.hachimi.client.mixin;

import net.minecraft.network.packet.BundlePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BundlePacket.class)
public interface BundlePacketMixin {
   @Accessor("packets")
   @Mutable
   void setIterable(Iterable var1);
}
