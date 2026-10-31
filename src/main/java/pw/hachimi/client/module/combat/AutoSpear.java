package pw.hachimi.client.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.combat.TargetUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoSpear extends Multitask {
   private static AutoSpear INSTANCE;
   private final List list = new ArrayList();
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting sortPriority = this.registerSetting(new EnumSetting("SortPriority", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting range = this.registerSetting(new NumberSetting("Range", "The maximum range the entity can be to attack it", 0.0, 40.0, 300.0));
   Setting predictTicks = this.registerSetting(new NumberSetting("PredictTicks", "Lead target ticks ahead", 0.0, 2.0, 10.0));
   Setting spearRange = this.registerSetting(new NumberSetting("SpearRange", "Spear range", 0.0, 5.0, 10.0));
   Setting minPitch = this.registerSetting(new NumberSetting("MinPitch", "Min pitch", 0.0F, 30.0F, 90.0F));
   Setting pullTime = this.registerSetting(new NumberSetting("PullTime", "Pull time", 0, 400, 2000));
   Setting antiSpear = this.registerSetting(new BooleanSetting("AntiSpear", "I dont know is this useful", false));
   Setting minHeight = this.registerSetting(new NumberSetting("MinHeight", "Min height to avoid", -10.0F, 2.0F, 20.0F));
   Setting avoidPitch = this.registerSetting(new NumberSetting("AvoidPitch", "Avoid pitch", 0.0F, 60.0F, 90.0F));
   Setting ignoreNakeds = this.registerSetting(new BooleanSetting("IgnoreNakeds", "Ignore players with no items", false));
   Vec3d vec;
   Vec3d vec2;

   public AutoSpear() {
      super("AutoSpear", "Follow your enemy", ModuleCategory.COMBAT, 4000);
      INSTANCE = this;
   }

   public static AutoSpear hL() {
      return INSTANCE;
   }

   @EventListener
   public void hM(TickEntitiesPreEvent tickEntitiesPreEvent) {
      this.list.clear();
      final AutoSpear autoSpear = this;
      TargetUtils.WY(this.list, new Predicate() {
         @Override
         public boolean test(Object tickEntitiesPreEvent) {
            return autoSpear.hQ((Entity)tickEntitiesPreEvent);
         }
      }, (TargetPriority)this.sortPriority.atx(), 1);
      if (!this.list.isEmpty()) {
         Iterator iterator2 = this.list.iterator();

         while (true) {
            if (!iterator2.hasNext()) {
               this.list.clear();
               break;
            }

            Object obj = iterator2.next();
            float f = (Float)this.minPitch.atx();
            Vec3d vec3 = new Vec3d(
               (((Entity)obj).getX() - ((Entity)obj).prevX) * (Double)this.predictTicks.atx(),
               (((Entity)obj).getY() - ((Entity)obj).prevY) * (Double)this.predictTicks.atx(),
               (((Entity)obj).getZ() - ((Entity)obj).prevZ) * (Double)this.predictTicks.atx()
            );
            Vec3d vec4 = ((Entity)obj).getEyePos().add(vec3);
            Vec3d vec5 = ((Entity)obj).getPos().add(vec3);
            if (this.stopwatch.afq((Number)this.pullTime.atx())) {
               if ((Boolean)this.antiSpear.atx() && client.player.getY() < vec5.getY() + ((Float)this.minHeight.atx()).floatValue()) {
                  this.vec2 = null;
                  this.stopwatch.afr(29432, 5265492, (byte)40);
                  f = (Float)this.avoidPitch.atx();
               } else {
                  this.vec2 = null;
                  if (!this.hO(vec4, client.player.getEyePos())) {
                     this.vec = vec4;
                     if (this.vec != null) {
                        float[] fs = RotationUtils.nM(this.vec);
                        this.ajM(fs[0], fs[1]);
                        Vec3d vec6 = client.player.getEyePos();
                        if (vec4.distanceTo(vec6) <= (Double)this.spearRange.atx()) {
                           this.stopwatch.afr(29432, 5265492, (byte)40);
                        }
                        break;
                     }
                  }
               }
            }

            if (this.vec2 == null) {
               float f2 = RotationUtils.nM(vec4)[0] - 180.0F;
               Vec3d vec7 = null;

               label82:
               for (float f3 : new float[]{-1.0F, 1.0F}) {
                  for (float f4 = f; f4 <= 90.0F; f4 += 5.0F) {
                     float f5 = 0.0F;

                     do {
                        for (float f6 : new float[]{1.0F, -1.0F}) {
                           float f7 = f2 + f5 * f6;
                           float f8 = f4 * f3;
                           double d = Math.toRadians(f7);
                           double d2 = Math.toRadians(f8);
                           float f9 = (float)(-Math.sin(d) * Math.cos(d2));
                           float f10 = (float)(-Math.sin(d2));
                           float f11 = (float)(-Math.cos(d) * Math.cos(d2));
                           double d3 = vec4.x + f9 * 15.0F;
                           double d4 = vec4.z + f11 * 15.0F;
                           Vec3d vec8 = new Vec3d(d3 + 0.3, vec4.y + f10 * 15.0F + 1.0, d4 + 0.3);
                           Vec3d vec9 = new Vec3d(d3 - 0.3, vec5.y + f10 * 15.0F + 1.0, d4 - 0.3);
                           if (!this.hO(vec8, client.player.getEyePos())
                              && !this.hO(vec9, client.player.getPos())
                              && !this.hO(vec8, vec4)
                              && !this.hO(vec9, vec5)) {
                              vec7 = vec8;
                              break label82;
                           }
                        }

                        f5 += 5.0F;
                     } while (f5 <= 180.0F);
                  }
               }

               this.vec2 = vec7;
            }

            if (this.vec2 != null) {
               float[] fs2 = RotationUtils.nM(this.vec2);
               this.ajM(fs2[0], fs2[1]);
               break;
            }
         }
      }
   }

   @EventListener
   public void hN(Render3DContext render3DContext) {
      if (this.vec != null) {
         render3DContext.boxRenderer
            .Zd(
               new Box(this.vec.x - 0.25, this.vec.y - 0.25, this.vec.z - 0.25, this.vec.x + 0.25, this.vec.y + 0.25, this.vec.z + 0.25),
               g(255, 0),
               Color.red,
               ShapeMode.Sides
            );
      }

      if (this.vec2 != null) {
         render3DContext.boxRenderer
            .Zd(
               new Box(this.vec2.x - 0.25, this.vec2.y - 0.25, this.vec2.z - 0.25, this.vec2.x + 0.25, this.vec2.y + 0.25, this.vec2.z + 0.25),
               g(0, 255),
               Color.red,
               ShapeMode.Sides
            );
      }
   }

   @Override
   protected int ajP() {
      return 114514;
   }

   public boolean hO(Vec3d vec3, Vec3d vec4) {
      ClientWorld temp = client.world;
      RaycastContext raycastContext = new RaycastContext(vec3, vec4, ShapeType.COLLIDER, FluidHandling.NONE, client.player);
      boolean flag;
      if (temp.raycast(raycastContext).getType() == Type.BLOCK) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static boolean hP() {
      boolean flag;
      if (INSTANCE != null && INSTANCE.awg() && !INSTANCE.list.isEmpty()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   boolean hQ(Entity entity2) {
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
               Vec3d vec3 = ((PlayerEntity)entity2).getPos();
               if (temp.distanceTo(vec3) > (Double)this.range.atx()) {
                  return false;
               }

               if ((Boolean)this.ignoreNakeds.atx() && WorldUtils.NO((PlayerEntity)entity2)) {
                  return false;
               }

               boolean flag;
               if (!Managers.friendManager.awr((PlayerEntity)entity2)) {
                  flag = true;
               } else {
                  flag = false;
               }

               return flag;
            }

            return false;
         }
      }

      return false;
   }

   private static Color g(int n, int n2) {
      return new Color(n, n2, 0, 150);
   }
}
