package pw.hachimi.client.mixin;

import java.util.List;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityTrackerUpdateS2CPacket.class)
public interface EntityTrackerUpdateS2CPacketMixin {
   @Accessor("trackedValues")
   @Mutable
   void hookSetTrackedValues(List var1);
}
