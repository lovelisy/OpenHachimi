package pw.hachimi.client.module.movement;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.mixin.FireworkRocketEntityMixin;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.utils.movement.UnidentifiedHelper20;
import pw.hachimi.eventbus.annotation.EventListener;

public class ElytraAcclerate extends ClientModule {
   private int a;

   public ElytraAcclerate() {
      super("ElytraAcclerate", "Grim exploit", ModuleCategory.MOVEMENT);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         this.a++;
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      Packet packet2 = channelRead0Event.getPacket();
      if (packet2 instanceof EntityVelocityUpdateS2CPacket
         && client.player != null
         && ((EntityVelocityUpdateS2CPacket)packet2).getEntityId() == client.player.getId()
         && !this.Qa()
         && (client.player.isFallFlying() || FakeFly.qr().awg() && !client.player.isOnGround() && !client.player.isTouchingWater())) {
         Vec3d vec = new Vec3d(
            ((EntityVelocityUpdateS2CPacket)packet2).getVelocityX(),
            ((EntityVelocityUpdateS2CPacket)packet2).getVelocityY(),
            ((EntityVelocityUpdateS2CPacket)packet2).getVelocityZ()
         );
         if (!(vec.horizontalLengthSquared() < 1.0E-4)) {
            Vec3d vec2 = client.player.getVelocity();
            double d = Qb(vec2.x, vec2.z, vec.x, vec.z);
            if (vec2.horizontalLengthSquared() > 1.0E-4 && d < 0.0) {
               channelRead0Event.aim();
            }
         } else {
            channelRead0Event.aim();
         }
      }
   }

   @EventListener(priority = -1)
   public void PZ(UnidentifiedHelper20 unidentifiedHelper20) {
      if (client.player.isFallFlying() || FakeFly.qr() != null && FakeFly.qr().awg() && !client.player.isOnGround() && !client.player.isTouchingWater()) {
         unidentifiedHelper20.are();
         unidentifiedHelper20.aqR(unidentifiedHelper20.aqQ() + 2.5 * (this.a % 3 + 1));
      }
   }

   private boolean Qa() {
      Iterator iterator2 = client.world.getEntities().iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2 instanceof FireworkRocketEntity
            && ((FireworkRocketEntityMixin)entity2).hookWasShotByEntity()
            && ((FireworkRocketEntityMixin)entity2).hookGetShooter() == client.player) {
            return true;
         }
      }

      return false;
   }

   public static double Qb(double d, double d2, double d3, double d4) {
      return 0.0 + d * d3 + d2 * d4;
   }
}
