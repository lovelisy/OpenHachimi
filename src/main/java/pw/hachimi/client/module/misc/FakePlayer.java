package pw.hachimi.client.module.misc;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.movement.PushAwayFromEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.model.movement.YawPitch;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.player.PlayerWorldOtherClientPlayerEntity;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.client.utils.world.Vec3dEntityUtils;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class FakePlayer extends ClientModule {
   Setting name = this.registerSetting(createStringSetting("Name", "Fake player's name"));
   Setting damage = this.registerSetting(new BooleanSetting("Damage", "Allow you damage fakeplayer", true));
   Setting assumeBestArmor = this.registerSetting(new BooleanSetting("AssumeBestArmor", "Assumes Prot 0 armor is max armor", false));
   Setting recordSetting = this.registerSetting(new BooleanSetting("Record", "Record your movement", false));
   Setting play = this.registerSetting(new BooleanSetting("Play", "Play the saved movement", false));
   final List list = new ArrayList();
   int g;
   boolean h = false;
   private PlayerWorldOtherClientPlayerEntity playerWorldOtherClientPlayerEntity;
   public static boolean be;

   public FakePlayer() {
      super("FakePlayer", "Spawns an indestructible client-side player", ModuleCategory.MISC);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if ((Boolean)this.recordSetting.atx() != this.h && (Boolean)this.recordSetting.atx()) {
            this.list.clear();
         }

         this.h = (Boolean)this.recordSetting.atx();
         if ((Boolean)this.recordSetting.atx()) {
            List temp = this.list;
            YawPitch yawPitch = new YawPitch(client.player.getX(), client.player.getY(), client.player.getZ(), client.player.getYaw(), client.player.getPitch());
            temp.add(yawPitch);
         }

         if ((Boolean)this.play.atx() && !this.list.isEmpty()) {
            this.g++;
            if (this.g >= this.list.size()) {
               this.g = 0;
            }

            int n = this.g;
            YawPitch yawPitch2 = (YawPitch)this.list.get(n);
            PlayerWorldOtherClientPlayerEntity playerWorldOtherClientPlayerEntity2 = this.playerWorldOtherClientPlayerEntity;
            float f = yawPitch2.getYaw();
            playerWorldOtherClientPlayerEntity2.setYaw(f);
            playerWorldOtherClientPlayerEntity2 = this.playerWorldOtherClientPlayerEntity;
            float f2 = yawPitch2.pitch();
            playerWorldOtherClientPlayerEntity2.setPitch(f2);
            playerWorldOtherClientPlayerEntity2 = this.playerWorldOtherClientPlayerEntity;
            float f3 = yawPitch2.getYaw();
            playerWorldOtherClientPlayerEntity2.setHeadYaw(f3);
            playerWorldOtherClientPlayerEntity2 = this.playerWorldOtherClientPlayerEntity;
            double temp2 = yawPitch2.x();
            double d = yawPitch2.z();
            double d2 = yawPitch2.y();
            double d3 = temp2;
            playerWorldOtherClientPlayerEntity2.updateTrackedPosition(d3, d2, d);
            playerWorldOtherClientPlayerEntity2 = this.playerWorldOtherClientPlayerEntity;
            temp2 = yawPitch2.x();
            double temp3 = yawPitch2.y();
            double temp4 = yawPitch2.z();
            float f4 = yawPitch2.pitch();
            float f5 = yawPitch2.getYaw();
            double d4 = temp4;
            double d5 = temp3;
            double d6 = temp2;
            playerWorldOtherClientPlayerEntity2.updateTrackedPositionAndAngles(d6, d5, d4, f5, f4, 3);
         }
      }
   }

   @Override
   public void awd() {
      if (client.player != null && client.world != null) {
         this.playerWorldOtherClientPlayerEntity = a(client.player, (String)this.name.atx());
         this.playerWorldOtherClientPlayerEntity.Cp();
      }
   }

   @Override
   public void awe() {
      if (this.playerWorldOtherClientPlayerEntity != null) {
         this.playerWorldOtherClientPlayerEntity.Cq();
         this.playerWorldOtherClientPlayerEntity = null;
      }
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.playerWorldOtherClientPlayerEntity = null;
      this.awc();
   }

   @EventListener
   public void onPushAwayFrom(PushAwayFromEvent pushAwayFromEvent) {
      Entity temp = pushAwayFromEvent.getEntity();
      ClientPlayerEntity player2 = client.player;
      if (temp.equals(player2)) {
         temp = pushAwayFromEvent.getEntity2();
         PlayerWorldOtherClientPlayerEntity playerWorldOtherClientPlayerEntity2 = this.playerWorldOtherClientPlayerEntity;
         if (temp.equals(playerWorldOtherClientPlayerEntity2)) {
            pushAwayFromEvent.ail(true);
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if ((Boolean)this.damage.atx() && this.playerWorldOtherClientPlayerEntity != null && this.playerWorldOtherClientPlayerEntity.hurtTime <= 0) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof ExplosionS2CPacket) {
            Vec3d temp = new Vec3d(((ExplosionS2CPacket)packet2).getX(), ((ExplosionS2CPacket)packet2).getY(), ((ExplosionS2CPacket)packet2).getZ());
            Vec3d vec = this.playerWorldOtherClientPlayerEntity.getPos();
            if (Math.sqrt(temp.squaredDistanceTo(vec)) > 10.0) {
               return;
            }

            ClientWorld world2 = client.world;
            Mutable mutable = new Mutable(((ExplosionS2CPacket)packet2).getX(), ((ExplosionS2CPacket)packet2).getY(), ((ExplosionS2CPacket)packet2).getZ());
            double d;
            if (world2.getBlockState((BlockPos)mutable).getBlock() == Blocks.RESPAWN_ANCHOR) {
               d = Vec3dEntityUtils.vX(
                  this.playerWorldOtherClientPlayerEntity,
                  new Vec3d(((ExplosionS2CPacket)packet2).getX(), ((ExplosionS2CPacket)packet2).getY(), ((ExplosionS2CPacket)packet2).getZ()),
                  false,
                  10.0F,
                  0,
                  (Boolean)this.assumeBestArmor.atx()
               );
            } else {
               d = Vec3dEntityUtils.vK(
                  this.playerWorldOtherClientPlayerEntity,
                  new Vec3d(((ExplosionS2CPacket)packet2).getX(), ((ExplosionS2CPacket)packet2).getY(), ((ExplosionS2CPacket)packet2).getZ()),
                  (Boolean)this.assumeBestArmor.atx()
               );
            }

            MinecraftClient client2 = client;
            final double d2 = d;
            final FakePlayer fakePlayer = this;
            Runnable runnable = new Runnable() {
               @Override
               public void run() {
                  fakePlayer.GE(d2);
               }
            };
            client2.execute(runnable);
         }
      }
   }

   void GE(double d) {
      PlayerWorldOtherClientPlayerEntity temp = this.playerWorldOtherClientPlayerEntity;
      DamageSource damageSource = client.world.getDamageSources().generic();
      temp.onDamaged(damageSource);
      if (this.playerWorldOtherClientPlayerEntity.getHealth() > d) {
         temp = this.playerWorldOtherClientPlayerEntity;
         float f = (float)(this.playerWorldOtherClientPlayerEntity.getHealth() - d);
         temp.setHealth(f);
      } else {
         EntityStatusS2CPacket entityStatusS2CPacket = a(this.playerWorldOtherClientPlayerEntity);
         ChannelRead0Event channelRead0Event = new ChannelRead0Event(client.getNetworkHandler(), entityStatusS2CPacket);
         EventBusImpl.INSTANCE.rC(channelRead0Event);
         if (!channelRead0Event.aik()) {
            EntityStatusS2CPacket entityStatusS2CPacket2 = entityStatusS2CPacket;
            ClientPlayNetworkHandler network2 = client.getNetworkHandler();
            entityStatusS2CPacket2.apply((ClientPlayPacketListener)network2);
         }
      }
   }

   private static PlayerWorldOtherClientPlayerEntity a(PlayerEntity player2, String str) {
      return new PlayerWorldOtherClientPlayerEntity(player2, str, true);
   }

   private static StringSetting createStringSetting(String str, String str2) {
      return new StringSetting(str, str2, "FakePlayer");
   }

   private static EntityStatusS2CPacket a(Entity entity2) {
      return new EntityStatusS2CPacket(entity2, (byte)35);
   }
}
