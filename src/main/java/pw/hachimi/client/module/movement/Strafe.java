package pw.hachimi.client.module.movement;

import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.movement.MoveEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.mixin.EntityVelocityUpdateS2CPacketMixin;
import pw.hachimi.client.mixin.ExplosionS2CPacketMixin;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Strafe extends ClientModule {
   Setting airStatic = this.registerSetting(new BooleanSetting("AirStatic", "Static when you are not moving", true));
   Setting air = this.registerSetting(new BooleanSetting("Air", "Removes acceleration while in the air", true));
   Setting downwards = this.registerSetting(new BooleanSetting("Downwards", "Removes acceleration while descending", true));
   Setting speed = this.registerSetting(new BooleanSetting("Speed", "Apply speed effect", true));
   Setting slow = this.registerSetting(new BooleanSetting("Slow", "Apply slow effect", true));
   Setting motionSync = this.registerSetting(new BooleanSetting("MotionSync", "Apply motion", true));
   Setting velocitySync = this.registerSetting(new BooleanSetting("VelocitySync", "Apply velocity", true));
   Setting velocityBoost = this.registerSetting(new BooleanSetting("VelocityBoost", "Velocity Boost", false));
   Setting amplifier = this.registerSetting(new NumberSetting("Amplifier", "Amplifier for velocity", 0.0F, 1.0F, 5.0F, this.velocityBoost::atx));
   double j;

   public Strafe() {
      super("Strafe", "Removes sprint acceleration", ModuleCategory.MOVEMENT);
   }

   @EventListener
   public void afC(TickEntitiesPreEvent tickEntitiesPreEvent) {
      Vec3d vec = client.player.getVelocity();
      this.j = Math.sqrt(vec.getX() * vec.getX() + vec.getZ() * vec.getZ());
   }

   @EventListener
   public void onMove(MoveEvent moveEvent) {
      if (!Speed.lq().awg() && !Flight.adS().awg() && moveEvent.getMovementType() == MovementType.SELF) {
         if (!MovementUtils.awH()) {
            if ((Boolean)this.airStatic.atx()) {
               moveEvent.aim();
               moveEvent.ym(0.0);
               moveEvent.yq(0.0);
            }
         } else if ((client.player.isOnGround() || (Boolean)this.air.atx()) && (!(client.player.getVelocity().y < 0.0) || (Boolean)this.downwards.atx())) {
            double d = 1.0;
            double d2 = 1.0;
            if ((Boolean)this.speed.atx()) {
               RegistryEntry registryEntry = StatusEffects.SPEED;
               if (client.player.hasStatusEffect(registryEntry)) {
                  RegistryEntry registryEntry2 = StatusEffects.SPEED;
                  double d3 = client.player.getStatusEffect(registryEntry2).getAmplifier();
                  d = 1.0 + 0.2 * (d3 + 1.0);
               }
            }

            if ((Boolean)this.slow.atx()) {
               RegistryEntry registryEntry3 = StatusEffects.SLOWNESS;
               if (client.player.hasStatusEffect(registryEntry3)) {
                  RegistryEntry registryEntry4 = StatusEffects.SLOWNESS;
                  double d4 = client.player.getStatusEffect(registryEntry4).getAmplifier();
                  d2 = 1.0 + 0.2 * (d4 + 1.0);
               }
            }

            double d5 = 0.2873F * d / d2;
            if ((Boolean)this.motionSync.atx()) {
               double d6 = Math.sqrt(moveEvent.yl() * moveEvent.yl() + moveEvent.yp() * moveEvent.yp());
               d5 = Math.max(d5, d6);
            }

            if ((Boolean)this.velocitySync.atx()) {
               d5 = Math.max(d5, this.j);
            }

            Vec2f vec2f = Speed.lq().lv((float)d5);
            moveEvent.aim();
            moveEvent.ym(vec2f.x);
            moveEvent.yq(vec2f.y);
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         if ((Boolean)this.velocityBoost.atx()) {
            Packet packet2 = channelRead0Event.getPacket();
            if (packet2 instanceof EntityVelocityUpdateS2CPacket) {
               if (((EntityVelocityUpdateS2CPacket)packet2).getEntityId() != client.player.getId()) {
                  return;
               }

               double d = Math.sqrt(
                  ((EntityVelocityUpdateS2CPacket)packet2).getVelocityX() * ((EntityVelocityUpdateS2CPacket)packet2).getVelocityX()
                     + ((EntityVelocityUpdateS2CPacket)packet2).getVelocityZ() * ((EntityVelocityUpdateS2CPacket)packet2).getVelocityZ()
               );
               Vec2f vec2f = Speed.lq().lv((float)d);
               ((EntityVelocityUpdateS2CPacketMixin)packet2).setVelocityX((int)(vec2f.x * (Float)this.amplifier.atx() * 8000.0));
               ((EntityVelocityUpdateS2CPacketMixin)packet2).setVelocityZ((int)(vec2f.y * (Float)this.amplifier.atx() * 8000.0));
            } else {
               packet2 = channelRead0Event.getPacket();
               if (packet2 instanceof ExplosionS2CPacket) {
                  double d2 = Math.sqrt(
                     ((ExplosionS2CPacket)packet2).getPlayerVelocityX() * ((ExplosionS2CPacket)packet2).getPlayerVelocityX()
                        + ((ExplosionS2CPacket)packet2).getPlayerVelocityZ() * ((ExplosionS2CPacket)packet2).getPlayerVelocityZ()
                  );
                  Vec2f vec2f2 = Speed.lq().lv((float)d2);
                  ((ExplosionS2CPacketMixin)packet2).setPlayerVelocityX(vec2f2.x * (Float)this.amplifier.atx());
                  ((ExplosionS2CPacketMixin)packet2).setPlayerVelocityZ(vec2f2.y * (Float)this.amplifier.atx());
               }
            }
         }
      }
   }
}
