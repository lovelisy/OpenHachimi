package pw.hachimi.client.module.movement;

import java.text.DecimalFormat;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class EntitySpeed extends ClientModule {
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting speed = this.registerSetting(new NumberSetting("Speed", "The speed of the entity while moving", 0.1F, 0.5F, 4.0F));
   Setting antiStuck = this.registerSetting(new BooleanSetting("AntiStuck", "Prevents entities from getting stuck when moving up", false));
   Setting strict = this.registerSetting(new BooleanSetting("Strict", "The NCP-Updated bypass for speeding up entity movement", false));

   public EntitySpeed() {
      super("EntitySpeed", "Increases riding entity speeds", ModuleCategory.MOVEMENT);
   }

   @Override
   public String hy() {
      DecimalFormat temp = h();
      Object obj = this.speed.atx();
      return temp.format(obj);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (client.player.isRiding() && client.player.getControllingVehicle() != null) {
            double d = Math.cos(Math.toRadians(client.player.getYaw() + 90.0F));
            double d2 = Math.sin(Math.toRadians(client.player.getYaw() + 90.0F));
            BlockPos pos = BlockPos.ofFloored(client.player.getX() + 2.0 * d, client.player.getY() - 1.0, client.player.getZ() + 2.0 * d2);
            BlockPos pos2 = BlockPos.ofFloored(client.player.getX() + 2.0 * d, client.player.getY() - 2.0, client.player.getZ() + 2.0 * d2);
            if ((Boolean)this.antiStuck.atx()
               && !client.player.getControllingVehicle().isOnGround()
               && !client.world.getBlockState(pos).blocksMovement()
               && !client.world.getBlockState(pos2).blocksMovement()) {
               this.stopwatch.afr(29432, 5265492, (byte)40);
               return;
            }

            BlockPos pos3 = BlockPos.ofFloored(client.player.getX() + 2.0 * d, client.player.getY(), client.player.getZ() + 2.0 * d2);
            if ((Boolean)this.antiStuck.atx() && client.world.getBlockState(pos3).blocksMovement()) {
               this.stopwatch.afr(29432, 5265492, (byte)40);
               return;
            }

            BlockPos pos4 = BlockPos.ofFloored(client.player.getX() + d, client.player.getY() + 1.0, client.player.getZ() + d2);
            if ((Boolean)this.antiStuck.atx() && client.world.getBlockState(pos4).blocksMovement()) {
               this.stopwatch.afr(29432, 5265492, (byte)40);
               return;
            }

            if (client.player.input.jumping) {
               this.stopwatch.afr(29432, 5265492, (byte)40);
            }

            if (this.stopwatch.afq(10000) || !(Boolean)this.antiStuck.atx()) {
               if (!client.player.getControllingVehicle().isTouchingWater() || client.player.input.jumping || !this.stopwatch.afq(1000)) {
                  if (client.player.getControllingVehicle().isOnGround()) {
                     Entity temp = client.player.getControllingVehicle();
                     ClientPlayerEntity player2 = client.player;
                     double d3 = client.player.getVelocity().x;
                     Entity entity2 = temp;
                     double d4 = player2.getVelocity().z;
                     entity2.setVelocity(d3, 0.4, d4);
                  }

                  Entity entity3 = client.player.getControllingVehicle();
                  ClientPlayerEntity player3 = client.player;
                  double d5 = client.player.getVelocity().x;
                  Entity entity4 = entity3;
                  double d6 = player3.getVelocity().z;
                  entity4.setVelocity(d5, -0.4, d6);
               }

               if ((Boolean)this.strict.atx()) {
                  ClientPlayerEntity player4 = client.player;
                  ServerManager serverManager2 = Managers.serverManager;
                  Entity temp2 = player4.getControllingVehicle();
                  Hand hand = Hand.MAIN_HAND;
                  Entity entity5 = temp2;
                  serverManager2.iO('\uf560', PlayerInteractEntityC2SPacket.interact(entity5, false, hand));
               }

               this.dY((Float)this.speed.atx(), d, d2);
               this.stopwatch.afr(29432, 5265492, (byte)40);
            }
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.player.isRiding() && !client.options.sneakKey.isPressed() && client.player.getControllingVehicle() != null) {
         if ((Boolean)this.strict.atx()) {
            if (channelRead0Event.getPacket() instanceof EntityPassengersSetS2CPacket) {
               channelRead0Event.aim();
            } else if (channelRead0Event.getPacket() instanceof PlayerPositionLookS2CPacket) {
               channelRead0Event.aim();
            }
         }
      }
   }

   private void dY(float f, double d, double d2) {
      Vec3d vec = client.player.getControllingVehicle().getVelocity();
      float f2 = client.player.input.movementForward;
      float f3 = client.player.input.movementSideways;
      if (f2 == 0.0F && f3 == 0.0F) {
         Entity entity2 = client.player.getControllingVehicle();
         double d3 = vec.y;
         entity2.setVelocity(0.0, d3, 0.0);
      } else {
         Entity temp = client.player.getControllingVehicle();
         double temp2 = f2 * f * d + f3 * f * d2;
         double d4 = f2 * f * d2 - f3 * f * d;
         double d5 = vec.y;
         double d6 = temp2;
         temp.setVelocity(d6, d5, d4);
      }
   }

   private static DecimalFormat h() {
      return new DecimalFormat("0.0");
   }
}
