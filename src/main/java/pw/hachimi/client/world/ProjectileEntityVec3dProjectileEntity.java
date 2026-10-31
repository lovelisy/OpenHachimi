package pw.hachimi.client.world;

import java.util.function.Predicate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;

public class ProjectileEntityVec3dProjectileEntity extends ProjectileEntity {
   private final ProjectileEntity projectileEntity;
   public static boolean d;

   public ProjectileEntityVec3dProjectileEntity(ProjectileEntity projectileEntity2) {
      super(EntityType.ENDER_PEARL, MinecraftClient.getInstance().world);
      this.copyPositionAndRotation((Entity)projectileEntity2);
      float f = this.getYaw();
      this.prevYaw = f;
      float f2 = this.getPitch();
      this.prevPitch = f2;
      boolean flag = projectileEntity2.isTouchingWater();
      this.touchingWater = flag;
      Vec3d vec = projectileEntity2.getVelocity();
      this.setVelocity(vec);
      boolean flag2 = projectileEntity2.isOnGround();
      this.setOnGround(flag2);
      boolean flag3 = projectileEntity2.isSneaking();
      this.setSneaking(flag3);
      boolean flag4 = projectileEntity2.isSwimming();
      this.setSwimming(flag4);
      boolean flag5 = projectileEntity2.isSprinting();
      this.setSprinting(flag5);
      EntityPose entityPose = projectileEntity2.getPose();
      this.setPose(entityPose);
      this.setId(1603476961);
      int n = projectileEntity2.age;
      this.age = n;
      this.projectileEntity = projectileEntity2;
   }

   protected void initDataTracker(Builder builder) {
   }

   public HitResult ajv() {
      float f = this.horizontalSpeed;
      this.prevHorizontalSpeed = f;
      this.updateWaterState();
      final ProjectileEntityVec3dProjectileEntity projectileEntityVec3dProjectileEntity = this;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return ProjectileEntityVec3dProjectileEntity.ajw(projectileEntityVec3dProjectileEntity, (Entity)obj);
         }
      };
      HitResult hitResult = ProjectileUtil.getCollision((Entity)this, predicate);
      if (hitResult.getType() != Type.MISS) {
         this.discard();
         return hitResult;
      }

      Vec3d vec = this.getVelocity();
      double d2 = this.getX() + vec.x;
      double d3 = this.getY() + vec.y;
      double d4 = this.getZ() + vec.z;
      this.updateRotation();
      float f2;
      if (this.isTouchingWater()) {
         f2 = 0.8F;
      } else {
         f2 = 0.99F;
      }

      Vec3d temp = vec;
      double d5 = f2;
      Vec3d vec2 = temp.multiply(d5);
      this.setVelocity(vec2);
      this.applyGravity();
      double d6 = this.getX();
      this.prevX = d6;
      double d7 = this.getY();
      this.prevY = d7;
      double d8 = this.getZ();
      this.prevZ = d8;
      this.setPosition(d2, d3, d4);
      return null;
   }

   protected double getGravity() {
      return this.projectileEntity instanceof ArrowEntity ? 0.05 : 0.03;
   }

   static boolean ajw(ProjectileEntityVec3dProjectileEntity projectileEntityVec3dProjectileEntity, Entity entity2) {
      return projectileEntityVec3dProjectileEntity.canHit(entity2);
   }
}
