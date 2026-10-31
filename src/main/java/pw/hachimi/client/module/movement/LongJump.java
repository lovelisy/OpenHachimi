package pw.hachimi.client.module.movement;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.shape.VoxelShape;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.movement.MoveEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.network.SendMovementPacketsTailEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.movement.LongJumpNormalGlideMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.exploit.PacketFly;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class LongJump extends ClientModule {
   private static LongJump INSTANCE;
   Setting mode = this.registerSetting(new EnumSetting("Mode", "The mode for long jump", LongJumpNormalGlideMode.NORMAL, LongJumpNormalGlideMode.values()));
   Setting boost = this.registerSetting(new NumberSetting("Boost", "The jump boost speed", 0.1F, 4.5F, 10.0F, this::isBoostVisible));
   Setting autoDisable = this.registerSetting(new BooleanSetting("AutoDisable", "Automatically disables when rubberband is detected", true));
   private int d;
   private double e;
   private double f;
   private int g;
   private int h;

   public LongJump() {
      super("LongJump", "Allows the player to jump farther", ModuleCategory.MOVEMENT);
      INSTANCE = this;
   }

   public static LongJump rs() {
      return INSTANCE;
   }

   @Override
   public String hy() {
      return DirectionUtils.apW((Enum)this.mode.atx());
   }

   @Override
   public void awd() {
      this.h = 0;
   }

   @Override
   public void awe() {
      this.d = 0;
      this.e = 0.0;
   }

   @EventListener
   public void onMove(MoveEvent moveEvent) {
      if (this.mode.atx() == LongJumpNormalGlideMode.NORMAL) {
         if (client.player == null || client.world == null || Flight.adS().awg() || PacketFly.Gj().awg() || !MovementUtils.awH()) {
            return;
         }

         double d2 = 1.0;
         double d3 = 1.0;
         RegistryEntry registryEntry = StatusEffects.SPEED;
         if (client.player.hasStatusEffect(registryEntry)) {
            RegistryEntry registryEntry2 = StatusEffects.SPEED;
            double d4 = client.player.getStatusEffect(registryEntry2).getAmplifier();
            d2 = 1.0 + 0.2 * (d4 + 1.0);
         }

         RegistryEntry registryEntry3 = StatusEffects.SLOWNESS;
         if (client.player.hasStatusEffect(registryEntry3)) {
            RegistryEntry registryEntry4 = StatusEffects.SLOWNESS;
            double d5 = client.player.getStatusEffect(registryEntry4).getAmplifier();
            d3 = 1.0 + 0.2 * (d5 + 1.0);
         }

         double d6 = 0.2873F * d2 / d3;
         if (this.d == 0) {
            this.d = 1;
            this.f = ((Float)this.boost.atx()).floatValue() * d6 - 0.01;
         } else if (this.d == 1) {
            this.d = 2;
            Managers.playerStateManager.rJ(0.42);
            moveEvent.yo(0.42);
            this.f *= 2.149;
         } else if (this.d == 2) {
            this.d = 3;
            double d7 = 0.66 * (this.e - d6);
            this.f = this.e - d7;
         } else {
            ClientWorld temp = client.world;
            ClientPlayerEntity temp2 = client.player;
            Box temp3 = client.player.getBoundingBox();
            ClientPlayerEntity player2 = client.player;
            Box box = temp3;
            ClientPlayerEntity player3 = temp2;
            ClientWorld world2 = temp;
            double d8 = player2.getVelocity().getY();
            temp = world2;
            Box box2 = box.offset(0.0, d8, 0.0);
            if (!temp.isSpaceEmpty((Entity)player3, box2) || client.player.verticalCollision) {
               this.d = 0;
            }

            this.f = this.e - this.e / 159.0;
         }

         this.f = Math.max(this.f, d6);
         moveEvent.aim();
         Vec2f vec2f = Speed.lq().lu((float)this.f);
         moveEvent.ym(vec2f.x);
         moveEvent.yq(vec2f.y);
      }
   }

   @EventListener
   public void onSendMovementPacketsTail(SendMovementPacketsTailEvent sendMovementPacketsTailEvent) {
      if (sendMovementPacketsTailEvent.getEventPhase() != EventPhase.PRE) {
         return;
      }

      if (this.mode.atx() != LongJumpNormalGlideMode.GLIDE) {
         return;
      }

      if (Flight.adS().awg() || client.player.isFallFlying() || client.player.isHoldingOntoLadder() || client.player.isTouchingWater()) {
         return;
      }

      if (client.player.isOnGround()) {
         this.e = 0.0;
      }

      float f2 = client.player.getYaw();
      f2 += client.player.forwardSpeed < 0.0F ? 180 : 0;
      float f3 = 0.0F;
      if (client.player.sidewaysSpeed > 0.0F) {
         float f4;
         if (client.player.forwardSpeed < 0.0F) {
            f4 = -0.5F;
         } else if (client.player.forwardSpeed > 0.0F) {
            f4 = 0.5F;
         } else {
            f4 = 1.0F;
         }

         f3 = -90.0F * f4;
      }

      f2 += f3;
      float f5 = 0.0F;
      if (client.player.sidewaysSpeed < 0.0F) {
         float f6;
         if (client.player.forwardSpeed < 0.0F) {
            f6 = -0.5F;
         } else if (client.player.forwardSpeed > 0.0F) {
            f6 = 0.5F;
         } else {
            f6 = 1.0F;
         }

         f5 = -90.0F * f6;
      }

      float f7 = f2 - f5;
      float f8 = (float)Math.cos((f7 + 90.0F) * Math.PI / 180.0);
      float f9 = (float)Math.sin((f7 + 90.0F) * Math.PI / 180.0);
      if (!client.player.verticalCollision) {
         this.g++;
         if (client.player.input.sneaking) {
            client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(0.0, 2.147483647E9, 0.0, false));
         }

         this.h = 0;
         if (!client.player.verticalCollision) {
            if (client.player.getVelocity().y == -0.07190068807140403) {
               Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.35F);
            }

            if (client.player.getVelocity().y == -0.10306193759436909) {
               Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.55F);
            }

            if (client.player.getVelocity().y == -0.13395038817442878) {
               Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.67F);
            }

            if (client.player.getVelocity().y == -0.16635183030382) {
               Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.69F);
            }

            if (client.player.getVelocity().y == -0.19088711097794803) {
               Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.71F);
            }

            if (client.player.getVelocity().y == -0.21121925191528862) {
               Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.2F);
            }

            if (client.player.getVelocity().y == -0.11979897632390576) {
               Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.93F);
            }

            if (client.player.getVelocity().y == -0.18758479151225355) {
               Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.72F);
            }

            if (client.player.getVelocity().y == -0.21075983825251726) {
               Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.76F);
            }

            if (this.rw(client.player, 70.0) < 0.5) {
               if (client.player.getVelocity().y == -0.23537393014173347) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.03F);
               }

               if (client.player.getVelocity().y == -0.08531999505205401) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * -0.5);
               }

               if (client.player.getVelocity().y == -0.03659320313669756) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * -0.1F);
               }

               if (client.player.getVelocity().y == -0.07481386749524899) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * -0.07F);
               }

               if (client.player.getVelocity().y == -0.0732677700939672) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * -0.05F);
               }

               if (client.player.getVelocity().y == -0.07480988066790395) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * -0.04F);
               }

               if (client.player.getVelocity().y == -0.0784000015258789) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.1F);
               }

               if (client.player.getVelocity().y == -0.08608320193943977) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.1F);
               }

               if (client.player.getVelocity().y == -0.08683615560584318) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.05F);
               }

               if (client.player.getVelocity().y == -0.08265497329678266) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.05F);
               }

               if (client.player.getVelocity().y == -0.08245009535659828) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.05F);
               }

               if (client.player.getVelocity().y == -0.08244005633718426) {
                  Managers.playerStateManager.rJ(-0.08243956442521608);
               }

               if (client.player.getVelocity().y == -0.08243956442521608) {
                  Managers.playerStateManager.rJ(-0.08244005590677261);
               }

               if (client.player.getVelocity().y > -0.1 && client.player.getVelocity().y < -0.08 && !client.player.isOnGround() && client.player.input.pressingForward) {
                  Managers.playerStateManager.rJ(-1.0E-4F);
               }
            } else {
               if (client.player.getVelocity().y < -0.2 && client.player.getVelocity().y > -0.24) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.7);
               }

               if (client.player.getVelocity().y < -0.25 && client.player.getVelocity().y > -0.32) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.8);
               }

               if (client.player.getVelocity().y < -0.35 && client.player.getVelocity().y > -0.8) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.98);
               }

               if (client.player.getVelocity().y < -0.8 && client.player.getVelocity().y > -1.6) {
                  Managers.playerStateManager.rJ(client.player.getVelocity().y * 0.99);
               }
            }
         }

         Managers.entityManager.rT(0.85F);
         double[] ds = new double[]{
            0.420606, 0.417924, 0.415258, 0.412609, 0.409977, 0.407361, 0.404761, 0.402178, 0.399611, 0.39706,
            0.394525, 0.392, 0.3894, 0.38644, 0.383655, 0.381105, 0.37867, 0.37625, 0.37384, 0.37145,
            0.369, 0.3666, 0.3642, 0.3618, 0.35945, 0.357, 0.354, 0.351, 0.348, 0.345,
            0.342, 0.339, 0.336, 0.333, 0.33, 0.327, 0.324, 0.321, 0.318, 0.315,
            0.312, 0.309, 0.307, 0.305, 0.303, 0.3, 0.297, 0.295, 0.293, 0.291,
            0.289, 0.287, 0.285, 0.283, 0.281, 0.279, 0.277, 0.275, 0.273, 0.271,
            0.269, 0.267, 0.265, 0.263, 0.261, 0.259, 0.257, 0.255, 0.253, 0.251,
            0.249, 0.247, 0.245, 0.243, 0.241, 0.239, 0.237
         };
         if (client.player.input.pressingForward) {
            try {
               Managers.playerStateManager.rK(f8 * ds[this.g - 1] * 3.0, f9 * ds[this.g - 1] * 3.0);
            } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            }
         } else {
            Managers.playerStateManager.rK(0.0, 0.0);
         }
      } else {
         Managers.entityManager.rT(1.0F);
         this.g = 0;
         this.h++;
         Managers.playerStateManager.rK(client.player.getVelocity().x / 13.0, client.player.getVelocity().z / 13.0);
         if (this.h == 1) {
            client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX(), client.player.getY(), client.player.getZ(), client.player.isOnGround()));
            client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX() + 0.0624, client.player.getY(), client.player.getZ(), client.player.isOnGround()));
            client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX(), client.player.getY() + 0.419, client.player.getZ(), client.player.isOnGround()));
            client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX() + 0.0624, client.player.getY(), client.player.getZ(), client.player.isOnGround()));
            client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX(), client.player.getY() + 0.419, client.player.getZ(), client.player.isOnGround()));
         }

         if (this.h > 2) {
            this.h = 0;
            Managers.playerStateManager.rK(f8 * 0.3, f9 * 0.3);
            Managers.playerStateManager.rJ(0.424F);
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null && !(client.currentScreen instanceof DownloadingTerrainScreen)) {
         if (channelRead0Event.getPacket() instanceof PlayerPositionLookS2CPacket && (Boolean)this.autoDisable.atx()) {
            this.awc();
         }
      }
   }

   private double rw(PlayerEntity player2, double d2) {
      ClientWorld temp = client.world;
      Box box = player2.getBoundingBox().expand(0.0, -70.0, 0.0);
      ArrayList arrayList = Lists.newArrayList(temp.getCollisions((Entity)player2, box));
      if (arrayList.isEmpty()) {
         return 0.0;
      }

      d2 = 0.0;
      Iterator iterator2 = arrayList.iterator();

      while (iterator2.hasNext()) {
         Box box2 = ((VoxelShape)iterator2.next()).getBoundingBox();
         if (!(box2.maxY <= d2)) {
            d2 = box2.maxY;
         }
      }

      return player2.getY() - d2;
   }

   private Boolean isBoostVisible() {
      boolean flag;
      if (this.mode.atx() == LongJumpNormalGlideMode.NORMAL) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
