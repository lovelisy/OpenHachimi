package pw.hachimi.client.module.movement;

import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.mixin.PlayerMoveC2SPacketMixin;
import pw.hachimi.client.model.movement.FlightNormalPacketMode;
import pw.hachimi.client.model.movement.FlightOption;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Flight extends ClientModule {
   private static Flight INSTANCE;
   Setting mode = this.registerSetting(new EnumSetting("Mode", "The mode for vanilla flight", FlightOption.a_, FlightOption.values()));
   Setting speed = this.registerSetting(new NumberSetting("Speed", "The horizontal flight speed", 0.1F, 2.5F, 10.0F));
   Setting verticalSpeed = this.registerSetting(new NumberSetting("VerticalSpeed", "The vertical flight speed", 0.1F, 1.0F, 5.0F));
   Setting antiKick = this.registerSetting(new EnumSetting("AntiKick", "Prevents vanilla flight detection", FlightNormalPacketMode.NORMAL, FlightNormalPacketMode.values()));
   Setting accelerate = this.registerSetting(new BooleanSetting("Accelerate", "Accelerate as you fly", false));
   Setting accelerateSpeed = this.registerSetting(new NumberSetting("AccelerateSpeed", "Speed to accelerate as", 0.01F, 0.2F, 1.0F, this::isAccelerateSpeedVisible));
   Setting maxSpeed = this.registerSetting(new NumberSetting("MaxSpeed", "Max speed to acceleratee to", 1.0F, 5.0F, 10.0F, this::isMaxSpeedVisible));
   private double h;
   private double i;
   private boolean j;
   private int k_;
   private boolean l;
   public static boolean k;

   public Flight() {
      super("Flight", "Allows the player to fly in survival", ModuleCategory.MOVEMENT);
      INSTANCE = this;
   }

   public static Flight adS() {
      return INSTANCE;
   }

   @Override
   public String hy() {
      return DirectionUtils.apW((Enum)this.mode.atx());
   }

   @Override
   public void awd() {
      if (this.mode.atx() == FlightOption.b) {
         this.adX();
      }

      this.h = 0.0;
   }

   @Override
   public void awe() {
      if (this.mode.atx() == FlightOption.b) {
         this.adY();
      }

      this.l = false;
   }

   @EventListener
   public void adT(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if ((Boolean)this.accelerate.atx()) {
         if (!MovementUtils.awH() || client.player.horizontalCollision) {
            this.h = 0.0;
         }

         this.h = this.h + ((Float)this.accelerateSpeed.atx()).floatValue();
         if (this.h > ((Float)this.maxSpeed.atx()).floatValue()) {
            this.h = ((Float)this.maxSpeed.atx()).floatValue();
         }
      } else {
         this.h = ((Float)this.speed.atx()).floatValue();
      }

      FlightOption temp = (FlightOption)this.mode.atx();
      FlightOption flightOption = FlightOption.b;
      if (temp.equals(flightOption)) {
         this.adX();
         PlayerAbilities playerAbilities = client.player.getAbilities();
         float f = (float)(this.h * 0.05F);
         playerAbilities.setFlySpeed(f);
      } else {
         this.adY();
      }

      label56: {
         label69: {
            if (this.j) {
               this.k_++;
               if (this.k_ >= 20) {
                  if (this.antiKick.atx() == FlightNormalPacketMode.PACKET) {
                     this.l = true;
                  } else if (this.antiKick.atx() == FlightNormalPacketMode.NORMAL) {
                     ClientPlayerEntity player2 = client.player;
                     double temp2 = client.player.getX();
                     double temp3 = client.player.getY() - 0.0313;
                     double d = client.player.getZ();
                     double d2 = temp3;
                     double d3 = temp2;
                     player2.setPosition(d3, d2, d);
                     if (this.mode.atx() == FlightOption.b) {
                        this.adY();
                        Managers.playerStateManager.rJ(0.0);
                        this.k_ = 0;
                        this.j = false;
                        if (this.mode.atx() != FlightOption.a_) {
                           return;
                        }

                        Managers.playerStateManager.rJ(0.0);
                        if (client.options.jumpKey.isPressed()) {
                        }
                        break label69;
                     }
                  }

                  this.k_ = 0;
                  this.j = false;
               }
            }

            if (this.mode.atx() != FlightOption.a_) {
               return;
            }

            Managers.playerStateManager.rJ(0.0);
            if (client.options.jumpKey.isPressed()) {
               Managers.playerStateManager.rJ(((Float)this.verticalSpeed.atx()).floatValue());
               break label56;
            }
         }

         if (client.options.sneakKey.isPressed()) {
            Managers.playerStateManager.rJ(-(Float)this.verticalSpeed.atx());
         }
      }

      this.h = Math.max(this.h, 0.2873F);
      float f2 = client.player.input.movementForward;
      float f3 = client.player.input.movementSideways;
      float f4 = client.player.getYaw();
      if (f2 == 0.0F && f3 == 0.0F) {
         Managers.playerStateManager.rK(0.0, 0.0);
      } else {
         double d4 = Math.cos(Math.toRadians(f4 + 90.0F));
         double d5 = Math.sin(Math.toRadians(f4 + 90.0F));
         Managers.playerStateManager.rK(f2 * this.h * d4 + f3 * this.h * d5, f2 * this.h * d5 - f3 * this.h * d4);
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof PlayerMoveC2SPacket && this.antiKick.atx() != FlightNormalPacketMode.OFF) {
            double d = ((PlayerMoveC2SPacket)packet2).getY(Double.NaN);
            if (!Double.isNaN(d)) {
               if (this.l) {
                  ((PlayerMoveC2SPacketMixin)((PlayerMoveC2SPacket)packet2)).hookSetY(this.i - 0.04);
                  this.l = false;
                  return;
               }

               this.j = this.adV((PlayerMoveC2SPacket)packet2);
               this.i = d;
               return;
            }

            if (this.l) {
               Object obj;
               if (((PlayerMoveC2SPacket)packet2).changesLook()) {
                  obj = new Full(
                     client.player.getX(),
                     client.player.getY() - 0.04,
                     client.player.getZ(),
                     client.player.getYaw(),
                     client.player.getPitch(),
                     ((PlayerMoveC2SPacket)packet2).isOnGround()
                  );
               } else {
                  obj = new PositionAndOnGround(
                     client.player.getX(), client.player.getY() - 0.04, client.player.getZ(), ((PlayerMoveC2SPacket)packet2).isOnGround()
                  );
               }

               sendImmediatelyEvent.aim();
               Managers.serverManager.iP((PlayerMoveC2SPacket)obj);
               this.l = false;
            }
         }
      }
   }

   private boolean adV(PlayerMoveC2SPacket playerMoveC2SPacket) {
      double d = client.player.getY();
      boolean flag;
      if (MathHelper.clamp(playerMoveC2SPacket.getY(d), -2.0E7, 2.0E7) - this.i >= -0.03125 && !client.player.groundCollision && this.adW(client.player)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean adW(Entity entity2) {
      Box box = entity2.getBoundingBox().expand(0.0625).stretch(0.0, -0.55, 0.0);
      Stream temp = entity2.getWorld().getStatesInBox(box);
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object entity2) {
            return ((AbstractBlockState)entity2).isAir();
         }
      };
      return temp.allMatch(predicate);
   }

   private void adX() {
      client.player.getAbilities().allowFlying = true;
      client.player.getAbilities().flying = true;
   }

   private void adY() {
      if (!client.player.isCreative()) {
         client.player.getAbilities().allowFlying = false;
      }

      client.player.getAbilities().flying = false;
      client.player.getAbilities().setFlySpeed(0.05F);
   }

   private Boolean isMaxSpeedVisible() {
      return (Boolean)this.accelerate.atx();
   }

   private Boolean isAccelerateSpeedVisible() {
      return (Boolean)this.accelerate.atx();
   }
}
