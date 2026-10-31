package pw.hachimi.client.module.movement;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickEvent5;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.client.event.movement.GetStepHeightEvent;
import pw.hachimi.client.event.movement.MoveEvent;
import pw.hachimi.client.event.network.PacketEvent;
import pw.hachimi.client.event.network.SendMovementPacketsTailEvent;
import pw.hachimi.client.manager.EntityManager;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.movement.StepVanillaNormalMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Step extends ClientModule {
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting mode = this.registerSetting(new EnumSetting("Mode", "Step mode", StepVanillaNormalMode.NORMAL, StepVanillaNormalMode.values()));
   Setting height = this.registerSetting(new NumberSetting("Height", "The maximum height for stepping up blocks", 1.0F, 2.5F, 10.0F, this::isHeightVisible));
   Setting strict = this.registerSetting(new BooleanSetting("Strict", "Confirms the step height for NCP servers", false, this::isStrictVisible));
   Setting useTimer = this.registerSetting(new BooleanSetting("UseTimer", "Slows down packets by applying timer when stepping", true, this::isUseTimerVisible));
   Setting entityStep = this.registerSetting(new BooleanSetting("EntityStep", "Allows entities to step up blocks", false, this::isEntityStepVisible));
   private boolean g;
   private boolean h;
   private float i;

   public Step() {
      super("Step", "Allows the player to step up blocks", ModuleCategory.MOVEMENT);
   }

   @Override
   public String hy() {
      return DirectionUtils.apW((Enum)this.mode.atx());
   }

   @Override
   public void awd() {
      if (client.player != null) {
         float f2;
         if (this.na(client.player.getVehicle())) {
            f2 = 1.0F;
         } else {
            f2 = 0.6F;
         }

         this.i = f2;
         Managers.entityManager.rT(1.0F);
      }
   }

   @Override
   public void awe() {
      Managers.entityManager.rT(1.0F);
   }

   @EventListener
   public void onSendMovementPacketsTail(SendMovementPacketsTailEvent sendMovementPacketsTailEvent) {
      if (sendMovementPacketsTailEvent.getEventPhase() == EventPhase.PRE) {
         if (this.mode.atx() == StepVanillaNormalMode.NORMAL) {
            double d = client.player.getY() - client.player.prevY;
            if (d <= 0.5 || d > ((Float)this.height.atx()).floatValue()) {
               return;
            }

            double[] ds = this.mZ(d);
            if ((Boolean)this.useTimer.atx()) {
               double d2;
               int n = (d2 = d - 1.0) == 0.0 ? 0 : (d2 < 0.0 ? -1 : 1);
               EntityManager entityManager2 = Managers.entityManager;
               float f2;
               if (n > 0) {
                  EntityManager temp = entityManager2;
                  f2 = 0.15F;
                  entityManager2 = temp;
               } else {
                  EntityManager entityManager3 = entityManager2;
                  f2 = 0.35F;
                  entityManager2 = entityManager3;
               }

               entityManager2.rT(f2);
               this.g = true;
            }

            int n2 = ds.length;

            for (int n3 = 0; n3 < n2; n3++) {
               double d3 = ds[n3];
               Managers.serverManager.iO('\uf560', f(client.player.prevX, client.player.prevY + d3, client.player.prevZ));
            }

            this.stopwatch.afr(29432, 5265492, (byte)40);
         }
      }
   }

   @EventListener(priority = -100)
   public void onTick_Pre(Tick_PreEvent tickPreEvent) {
      if (this.mode.atx() == StepVanillaNormalMode.INSTANT && tickPreEvent.getEventPhase() == EventPhase.POST && this.h) {
         client.player.input.jumping = true;
      }
   }

   @EventListener
   public void onTickEvent5(TickEvent5 tickEvent5) {
      if (this.mode.atx() == StepVanillaNormalMode.INSTANT && this.h && client.player.input.jumping) {
         tickEvent5.aim();
         tickEvent5.arF(4);
         this.h = false;
      }
   }

   @EventListener(priority = -9999)
   public void onMove(MoveEvent moveEvent) {
      if (this.mode.atx() == StepVanillaNormalMode.INSTANT) {
         this.mV(moveEvent.yl(), moveEvent.yp());
      }
   }

   private boolean mU() {
      boolean flag;
      if (client.player.isOnGround() && !client.player.isSneaking() && !client.player.hasVehicle() && MovementUtils.awI()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   protected void mV(double d, double d2) {
      if (this.mU()) {
         Vec3d vec = client.player.getPos();
         Vec3d vec2 = vec.add(d, 0.0, d2);
         Vec3d vec3 = new Vec3d(d, 0.0, d2);
         float f2 = client.player.getMovementSpeed();
         float f3 = (float)vec3.lengthSquared();
         if (f3 <= 0.001F) {
            Vec2f vec2f = client.player.input.getMovementInput();
            float f4 = f2 * vec2f.x;
            float f5 = f2 * vec2f.y;
            float f6 = MathHelper.sin(client.player.getYaw() * (float) (Math.PI / 180.0));
            float f7 = MathHelper.cos(client.player.getYaw() * (float) (Math.PI / 180.0));
            vec3 = new Vec3d(f4 * f7 - f5 * f6, vec3.y, f5 * f7 + f4 * f6);
            f3 = (float)vec3.lengthSquared();
            if (f3 <= 0.001F) {
               return;
            }
         }

         float f8 = MathHelper.inverseSqrt(f3);
         Vec3d temp = vec3;
         double d3 = f8;
         Vec3d vec4 = temp.multiply(d3);
         Vec3d vec5 = client.player.getRotationVecClient();
         if (!((float)(vec5.x * vec4.x + vec5.z * vec4.z) < -0.15F)) {
            ShapeContext shapeContext = ShapeContext.of((Entity)client.player);
            BlockPos pos = BlockPos.ofFloored(client.player.getX(), client.player.getBoundingBox().maxY, client.player.getZ());
            BlockState state = client.player.getWorld().getBlockState(pos);
            World world2 = client.player.getWorld();
            if (state.getCollisionShape((BlockView)world2, pos, shapeContext).isEmpty()) {
               pos = pos.up();
               BlockState state2 = client.player.getWorld().getBlockState(pos);
               World world3 = client.player.getWorld();
               if (state2.getCollisionShape((BlockView)world3, pos, shapeContext).isEmpty()) {
                  float f9 = 1.2F;
                  RegistryEntry registryEntry = StatusEffects.JUMP_BOOST;
                  if (client.player.hasStatusEffect(registryEntry)) {
                     RegistryEntry registryEntry2 = StatusEffects.JUMP_BOOST;
                     f9 = 1.2F + (client.player.getStatusEffect(registryEntry2).getAmplifier() + 1) * 0.75F;
                  }

                  float f10 = Math.max(f2 * 7.0F, 1.0F / f8);
                  temp = vec2;
                  Vec3d temp2 = vec4;
                  double d4 = f10;
                  Vec3d vec6 = temp2.multiply(d4);
                  Vec3d vec7 = temp.add(vec6);
                  float f11 = client.player.getWidth();
                  float f12 = client.player.getHeight();
                  Vec3d temp3 = vec;
                  Vec3d temp4 = vec7;
                  double d5 = f12;
                  Box box = new Box(temp3, temp4.add(0.0, d5, 0.0));
                  double d6;
                  double d7 = d6 = f11;
                  Box box2 = box.expand(d7, 0.0, d6);
                  Vec3d vec8 = vec.add(0.0, 0.51F, 0.0);
                  vec7 = vec7.add(0.0, 0.51F, 0.0);
                  temp = vec4;
                  Vec3d vec9 = new Vec3d(0.0, 1.0, 0.0);
                  temp = temp.crossProduct(vec9);
                  double d8 = f11 * 0.5F;
                  Vec3d vec10 = temp.multiply(d8);
                  Vec3d vec11 = vec8.subtract(vec10);
                  Vec3d vec12 = vec7.subtract(vec10);
                  Vec3d vec13 = vec8.add(vec10);
                  Vec3d vec14 = vec7.add(vec10);
                  World world4 = client.player.getWorld();
                  ClientPlayerEntity player2 = client.player;
                  Stream stream2 = StreamSupport.stream(world4.getCollisions((Entity)player2, box2).spliterator(), false);
                  Function function2 = new Function() {
                     @Override
                     public Object apply(Object d) {
                        return Step.nb((VoxelShape)d);
                     }
                  };
                  Iterator iterator2 = stream2.flatMap(function2).iterator();
                  float f13 = Float.MIN_VALUE;

                  while (iterator2.hasNext()) {
                     Box box3 = (Box)iterator2.next();
                     if (box3.intersects(vec11, vec12) || box3.intersects(vec13, vec14)) {
                        f13 = (float)box3.maxY;
                        BlockPos pos2 = BlockPos.ofFloored(box3.getCenter());

                        for (int n = 1; n < f9; n++) {
                           BlockPos pos3 = pos2;
                           int n2 = n;
                           BlockPos pos4 = pos3.up(n2);
                           BlockState state3 = client.player.getWorld().getBlockState(pos4);
                           World world5 = client.player.getWorld();
                           VoxelShape voxelShape;
                           if (!(voxelShape = state3.getCollisionShape((BlockView)world5, pos4, shapeContext)).isEmpty()) {
                              VoxelShape voxelShape2 = voxelShape;
                              Axis axis = Axis.Y;
                              f13 = (float)voxelShape2.getMax(axis) + pos4.getY();
                              if (f13 - client.player.getY() > f9) {
                                 return;
                              }
                           }

                           if (n > 1) {
                              pos = pos.up();
                              BlockState state4 = client.player.getWorld().getBlockState(pos);
                              World world6 = client.player.getWorld();
                              if (!state4.getCollisionShape((BlockView)world6, pos, shapeContext).isEmpty()) {
                                 return;
                              }
                           }
                        }
                        break;
                     }
                  }

                  if (f13 != Float.MIN_VALUE) {
                     float f14 = (float)(f13 - client.player.getY());
                     if (!(f14 <= 0.5F) && !(f14 > f9)) {
                        this.h = true;
                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (!client.player.isTouchingWater() && !client.player.isInLava() && !client.player.isFallFlying()) {
            if (this.g && client.player.isOnGround()) {
               Managers.entityManager.rT(1.0F);
               this.g = false;
            }

            if (client.player.isOnGround() && this.stopwatch.afq(200)) {
               this.i = (Float)this.height.atx();
            } else {
               float f2;
               if (this.na(client.player.getVehicle())) {
                  f2 = 1.0F;
               } else {
                  f2 = 0.6F;
               }

               this.i = f2;
            }
         } else {
            Managers.entityManager.rT(1.0F);
            float f3;
            if (this.na(client.player.getVehicle())) {
               f3 = 1.0F;
            } else {
               f3 = 0.6F;
            }

            this.i = f3;
         }
      }
   }

   @EventListener
   public void onGetStepHeight(GetStepHeightEvent getStepHeightEvent) {
      if (this.mode.atx() != StepVanillaNormalMode.INSTANT) {
         if (client.player != null
            && (
               getStepHeightEvent.getLiving() == client.player
                  || (Boolean)this.entityStep.atx() && client.player.getVehicle() != null && getStepHeightEvent.getLiving() == client.player.getVehicle()
            )) {
            getStepHeightEvent.aim();
            getStepHeightEvent.Dg(this.i);
         }
      }
   }

   @EventListener
   public void mY(PacketEvent packetEvent) {
      if (client.player != null) {
         if (packetEvent.getPacket() instanceof PlayerPositionLookS2CPacket) {
            this.awc();
         }
      }
   }

   private double[] mZ(double d) {
      double[] ds = new double[0];
      if ((Boolean)this.strict.atx()) {
         if (d > 1.1661) {
            ds = new double[]{0.42, 0.7532, 1.001, 1.1661, d};
         } else if (d > 1.015) {
            ds = new double[]{0.42, 0.7532, 1.001, d};
         } else if (d > 0.6) {
            ds = new double[]{0.42 * d, 0.7532 * d, d};
         }

         return ds;
      } else {
         if (d > 2.019) {
            ds = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.919};
         } else if (d > 1.5) {
            ds = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
         } else if (d > 1.015) {
            ds = new double[]{0.42, 0.7532, 1.01, 1.093, 1.015};
         } else if (d > 0.6) {
            ds = new double[]{0.42 * d, 0.7532 * d};
         }

         return ds;
      }
   }

   private boolean na(Entity entity2) {
      boolean flag;
      if (!(entity2 instanceof HorseEntity) && !(entity2 instanceof LlamaEntity) && !(entity2 instanceof MuleEntity)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   static Stream nb(VoxelShape voxelShape) {
      return voxelShape.getBoundingBoxes().stream();
   }

   private Boolean isEntityStepVisible() {
      boolean flag;
      if (this.mode.atx() != StepVanillaNormalMode.INSTANT) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isUseTimerVisible() {
      boolean flag;
      if (this.mode.atx() == StepVanillaNormalMode.NORMAL) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isStrictVisible() {
      boolean flag;
      if (this.mode.atx() == StepVanillaNormalMode.NORMAL && (Float)this.height.atx() <= 2.5F) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isHeightVisible() {
      boolean flag;
      if (this.mode.atx() != StepVanillaNormalMode.INSTANT) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static PositionAndOnGround f(double d, double d2, double d3) {
      return new PositionAndOnGround(d, d2, d3, false);
   }
}
