package pw.hachimi.client.module.movement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.handler.EntityHandler;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.model.movement.FollowerNormalMaceMode;
import pw.hachimi.client.model.world.EntityInteractType;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.combat.TargetUtils;
import pw.hachimi.client.utils.movement.UnidentifiedUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Follower extends Multitask {
   private static Follower INSTANCE;
   private final List list = new ArrayList();
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting mode = this.registerSetting(new EnumSetting("Mode", "mode", FollowerNormalMaceMode.Normal, FollowerNormalMaceMode.values()));
   Setting sortPriority = this.registerSetting(new EnumSetting("SortPriority", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting numberSetting1 = this.registerSetting(new NumberSetting("Range", "The maximum range the entity can be to attack it", 0.0, 40.0, 300.0));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Friend", "Follow your friend", false));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("OnlyFlying", "Only working when you fly", false));
   Setting fallDistance = this.registerSetting(new NumberSetting("FallDistance", "Rise height", 0.0, 40.0, 300.0, this::isFallDistanceVisible));
   Setting attackDelay = this.registerSetting(new NumberSetting("AttackDelay", "Add delays between attacks", 0.0, 1000.0, 3000.0, this::isAttackDelayVisible));
   Setting predictTicks = this.registerSetting(new NumberSetting("PredictTicks", "Lead target ticks ahead", 0.0, 2.0, 10.0));
   Setting autoSprint = this.registerSetting(new BooleanSetting("AutoSprint", "Sprint while following", true));
   Setting ignoreNakeds = this.registerSetting(new BooleanSetting("IgnoreNakeds", "Ignore players with no items", false));

   public Follower() {
      super("Follower", "Follow your enemy", ModuleCategory.MOVEMENT, 4000);
      INSTANCE = this;
   }

   public static Follower dM() {
      return INSTANCE;
   }

   @EventListener
   public void dN(TickEntitiesPreEvent tickEntitiesPreEvent) {
      this.list.clear();
      if (!(Boolean)this.booleanSetting2_1.atx() || ElytraFly.di().dt()) {
         final Follower follower = this;
         TargetUtils.WY(this.list, new Predicate() {
            @Override
            public boolean test(Object tickEntitiesPreEvent) {
               return follower.dT((Entity)tickEntitiesPreEvent);
            }
         }, (TargetPriority)this.sortPriority.atx(), 1);
         if (!this.list.isEmpty() && HitResultWorldHelper.Sq().SK()) {
            int n;
            if ((n = ((FollowerNormalMaceMode)this.mode.atx()).ordinal()) != 0) {
               if (n == 1) {
                  Iterator iterator2 = this.list.iterator();

                  while (iterator2.hasNext()) {
                     Object obj = iterator2.next();
                     Vec3d temp = ((Entity)obj).getEyePos();
                     double temp2 = (((Entity)obj).getX() - ((Entity)obj).prevX) * (Double)this.predictTicks.atx();
                     double temp3 = (((Entity)obj).getY() - ((Entity)obj).prevY) * (Double)this.predictTicks.atx();
                     double d = (((Entity)obj).getZ() - ((Entity)obj).prevZ) * (Double)this.predictTicks.atx();
                     double d2 = temp3;
                     double d3 = temp2;
                     Vec3d vec = temp.add(d3, d2, d);
                     Vec3d vec2 = vec;
                     if (obj instanceof PlayerEntity && this.dU((PlayerEntity)obj)) {
                        Vec3d vec3 = this.dV((PlayerEntity)obj);
                        vec2 = vec.add(vec3);
                     }

                     Vec3d vec4 = this.dO(vec2);
                     if (vec4 != null) {
                        if (!this.stopwatch.afq((Number)this.attackDelay.atx())) {
                           double d4 = 0.5 + (Double)this.fallDistance.atx();
                           temp = vec4.add(0.0, d4, 0.0);
                        } else {
                           temp = vec4;
                        }

                        float[] fs = RotationUtils.nM(temp);
                        this.ajM(fs[0], fs[1]);
                        return;
                     }
                  }

                  iterator2 = this.list.iterator();

                  while (iterator2.hasNext()) {
                     Object obj2 = iterator2.next();
                     Vec3d vec5 = ((Entity)obj2).getEyePos();
                     double d5 = (((Entity)obj2).getX() - ((Entity)obj2).prevX) * (Double)this.predictTicks.atx();
                     double d6 = (((Entity)obj2).getY() - ((Entity)obj2).prevY) * (Double)this.predictTicks.atx();
                     double d7 = (((Entity)obj2).getZ() - ((Entity)obj2).prevZ) * (Double)this.predictTicks.atx();
                     double d8 = d6;
                     double d9 = d5;
                     Vec3d vec6 = vec5.add(d9, d8, d7);
                     Vec3d vec7 = vec6;
                     if (obj2 instanceof PlayerEntity && this.dU((PlayerEntity)obj2)) {
                        Vec3d vec8 = this.dV((PlayerEntity)obj2);
                        vec7 = vec6.add(vec8);
                     }

                     double d10 = 0.5 + (Double)this.fallDistance.atx();
                     Vec3d vec9 = vec7.add(0.0, d10, 0.0);
                     if (!this.dP(client.player.getPos(), vec9)) {
                        float[] fs2 = RotationUtils.nM(vec9);
                        this.ajM(fs2[0], fs2[1]);
                        return;
                     }
                  }

                  Vec3d vec10 = client.player.getEyePos().add(0.0, 10.0, 0.0);
                  if (!this.dP(client.player.getPos(), vec10)) {
                     float[] fs3 = RotationUtils.nM(vec10);
                     this.ajM(fs3[0], fs3[1]);
                  }
               }
            } else {
               Object obj3 = this.list.getFirst();
               Vec3d vec11 = ((Entity)obj3).getEyePos();
               double d11 = (((Entity)obj3).getX() - ((Entity)obj3).prevX) * (Double)this.predictTicks.atx();
               double d12 = (((Entity)obj3).getY() - ((Entity)obj3).prevY) * (Double)this.predictTicks.atx();
               double d13 = (((Entity)obj3).getZ() - ((Entity)obj3).prevZ) * (Double)this.predictTicks.atx();
               double d14 = d12;
               double d15 = d11;
               float[] fs4 = RotationUtils.nM(vec11.add(d15, d14, d13));
               this.ajM(fs4[0], fs4[1]);
            }
         }
      }
   }

   @Override
   protected int ajP() {
      return 114514;
   }

   public Vec3d dO(Vec3d vec) {
      Vec3d vec2 = client.player.getPos();
      if (!this.dP(vec, vec2)) {
         return vec;
      }

      for (int n = -1; n <= 1; n++) {
         for (int n2 = -1; n2 <= 1; n2++) {
            double temp = n;
            double d = n2;
            double d2 = temp;
            Vec3d vec3 = vec.add(d2, 0.5, d);
            if (!this.dP(vec3, vec2)) {
               return vec3;
            }
         }
      }

      return null;
   }

   public boolean dP(Vec3d vec, Vec3d vec2) {
      ClientWorld temp = client.world;
      RaycastContext raycastContext = new RaycastContext(vec, vec2, ShapeType.COLLIDER, FluidHandling.NONE, client.player);
      boolean flag;
      if (temp.raycast(raycastContext).getType() == Type.BLOCK) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.world != null && client.player != null) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof EntityHandler && ((EntityHandler)packet2).Qd() == EntityInteractType.ATTACK) {
            this.stopwatch.afr(29432, 5265492, (byte)40);
         }
      }
   }

   @EventListener(priority = -99)
   public void onTick_Pre(Tick_PreEvent tickPreEvent) {
      if (!Bootstrap.NV() || !UnidentifiedUtils.eP()) {
         if (HitResultWorldHelper.Sq().SK() && !this.list.isEmpty()) {
            client.player.input.movementForward = 1.0F;
            client.player.input.movementSideways = 0.0F;
            if ((Boolean)this.autoSprint.atx()) {
               client.player.setSprinting(true);
            }
         }
      }
   }

   public boolean dS() {
      boolean flag;
      if (this.awg() && !this.list.isEmpty()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   boolean dT(Entity entity2) {
      ClientPlayerEntity player2 = client.player;
      if (!entity2.equals(player2)) {
         Entity entity3 = client.cameraEntity;
         if (!entity2.equals(entity3)) {
            if (entity2 instanceof PlayerEntity) {
               if (((PlayerEntity)entity2).isDead()) {
                  return false;
               }

               if (!((PlayerEntity)entity2).isAlive()) {
                  return false;
               }

               if (((PlayerEntity)entity2).isCreative()) {
                  return false;
               }

               Vec3d temp = client.player.getEyePos();
               Vec3d vec = ((PlayerEntity)entity2).getPos();
               if (temp.distanceTo(vec) > (Double)this.numberSetting1.atx()) {
                  return false;
               }

               if ((Boolean)this.ignoreNakeds.atx() && WorldUtils.NO((PlayerEntity)entity2)) {
                  return false;
               }

               boolean flag;
               if (!(Boolean)this.booleanSetting1_1.atx() && Managers.friendManager.awr((PlayerEntity)entity2)) {
                  flag = false;
               } else {
                  flag = true;
               }

               return flag;
            }

            return false;
         }
      }

      return false;
   }

   private boolean dU(PlayerEntity player2) {
      ItemStack temp = player2.getMainHandStack();
      Item item2 = Items.SHIELD;
      if (!temp.isOf(item2)) {
         temp = player2.getOffHandStack();
         Item item3 = Items.SHIELD;
         if (!temp.isOf(item3)) {
            return false;
         }
      }

      return true;
   }

   private Vec3d dV(PlayerEntity player2) {
      Vec3d vec = RotationUtils.nS(0.0F, player2.getYaw());
      Vec3d vec2 = t(-vec.x, -vec.z);
      return vec2.lengthSquared() == 0.0 ? Vec3d.ZERO : vec2.normalize().multiply(0.8);
   }

   private Boolean isAttackDelayVisible() {
      boolean flag;
      if (this.mode.atx() == FollowerNormalMaceMode.Mace) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isFallDistanceVisible() {
      boolean flag;
      if (this.mode.atx() == FollowerNormalMaceMode.Mace) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static Vec3d t(double d, double d2) {
      return new Vec3d(d, 0.0, d2);
   }
}
