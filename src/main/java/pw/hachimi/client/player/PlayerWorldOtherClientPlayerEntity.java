package pw.hachimi.client.player;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.player.PlayerEntity;
import pw.hachimi.client.Mc;
import pw.hachimi.client.mixin.LimbAnimatorMixin2;

public class PlayerWorldOtherClientPlayerEntity extends OtherClientPlayerEntity {
   public static final AtomicInteger d_ = new AtomicInteger(1000000);
   private final PlayerEntity player;
   private static final String d = "8667ba71-b85a-4004-af54-457a9734eed7";

   public PlayerWorldOtherClientPlayerEntity(PlayerEntity player2, String str, boolean flag) {
      super(MinecraftClient.getInstance().world, new GameProfile(UUID.fromString(d), str));
      this.player = player2;
      this.copyPositionAndRotation(player2);
      this.prevYaw = this.getYaw();
      this.prevPitch = this.getPitch();
      this.headYaw = player2.headYaw;
      this.prevHeadYaw = this.headYaw;
      this.bodyYaw = player2.bodyYaw;
      this.prevBodyYaw = this.bodyYaw;
      ((LimbAnimatorMixin2)this.limbAnimator).hookSetPos(player2.limbAnimator.getPos());
      this.limbAnimator.setSpeed(player2.limbAnimator.getSpeed());
      Byte temp = (Byte)player2.getDataTracker().get(PlayerEntity.PLAYER_MODEL_PARTS);
      this.getDataTracker().set(PlayerEntity.PLAYER_MODEL_PARTS, temp);
      this.getAttributes().setFrom(player2.getAttributes());
      this.setSneaking(player2.isSneaking());
      this.setSwimming(player2.isSwimming());
      this.setPose(player2.getPose());
      this.setHealth(player2.getHealth());
      this.setAbsorptionAmount(player2.getAbsorptionAmount());
      if (flag) {
         this.getInventory().clone(player2.getInventory());
      }

      this.setId(d_.incrementAndGet());
      this.age = 100;
   }

   public PlayerWorldOtherClientPlayerEntity(PlayerEntity player2) {
      this(player2, player2.getName().getString(), true);
   }

   public void Cp() {
      if (Mc.client.world != null) {
         this.unsetRemoved();
         Mc.client.world.addEntity((Entity)this);
      }
   }

   public void Cq() {
      if (Mc.client.world != null) {
         ClientWorld temp = Mc.client.world;
         int temp2 = this.getId();
         RemovalReason removalReason = RemovalReason.DISCARDED;
         int n = temp2;
         temp.removeEntity(n, removalReason);
         RemovalReason removalReason2 = RemovalReason.DISCARDED;
         this.setRemoved(removalReason2);
      }
   }

   public boolean isDead() {
      return false;
   }

   public PlayerEntity getPlayer() {
      return this.player;
   }
}
