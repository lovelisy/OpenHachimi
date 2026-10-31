package pw.hachimi.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerEntity.class)
public interface ClientPlayerEntityMixin2 {
   @Accessor("lastX")
   double getLastX();

   @Accessor("lastBaseY")
   double getLastBaseY();

   @Accessor("lastZ")
   double getLastZ();

   @Accessor("ticksSinceLastPositionPacketSent")
   void hookSetTicksSinceLastPositionPacketSent(int var1);
}
