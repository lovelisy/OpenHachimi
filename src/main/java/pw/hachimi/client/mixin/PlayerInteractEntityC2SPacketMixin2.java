package pw.hachimi.client.mixin;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import pw.hachimi.client.Mc;
import pw.hachimi.client.handler.EntityHandler;
import pw.hachimi.client.model.world.EntityInteractType;

@Mixin(PlayerInteractEntityC2SPacket.class)
public abstract class PlayerInteractEntityC2SPacketMixin2 implements EntityHandler {
   @Unique
   private boolean k;
   @Shadow
   @Final
   private int entityId;

   @Shadow
   protected abstract void write(PacketByteBuf var1);

   @Override
   public Entity Qc() {
      return Mc.client.world == null ? null : Mc.client.world.getEntityById(this.entityId);
   }

   @Override
   public boolean Qe() {
      return this.k;
   }

   @Override
   public void Qf() {
      this.k = true;
   }

   @Override
   public EntityInteractType Qd() {
      PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
      this.write(packetByteBuf);
      packetByteBuf.readVarInt();
      return (EntityInteractType)packetByteBuf.readEnumConstant(EntityInteractType.class);
   }
}
