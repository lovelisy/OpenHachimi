package pw.hachimi.client.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LimbAnimator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Vec3dLivingLivingEntity extends LivingEntity {
   public boolean a = false;
   LivingEntity living;
   public static boolean b;

   public Vec3dLivingLivingEntity(LivingEntity living2) {
      super(EntityType.PLAYER, MinecraftClient.getInstance().world);
      this.copyPositionAndRotation((Entity)living2);
      float f = this.getYaw();
      this.prevYaw = f;
      float f2 = this.getPitch();
      this.prevPitch = f2;
      float f3 = living2.headYaw;
      this.headYaw = f3;
      float f4 = this.headYaw;
      this.prevHeadYaw = f4;
      float f5 = living2.bodyYaw;
      this.bodyYaw = f5;
      float f6 = this.bodyYaw;
      this.prevBodyYaw = f6;
      boolean flag = living2.isTouchingWater();
      this.touchingWater = flag;
      LimbAnimator temp = this.limbAnimator;
      float f7 = living2.limbAnimator.getPos();
      temp.pos = f7;
      temp = this.limbAnimator;
      float f8 = living2.limbAnimator.getSpeed();
      temp.setSpeed(f8);
      float f9 = living2.getMovementSpeed();
      this.setMovementSpeed(f9);
      boolean flag2 = living2.isOnGround();
      this.setOnGround(flag2);
      AttributeContainer attributeContainer = this.getAttributes();
      AttributeContainer attributeContainer2 = living2.getAttributes();
      attributeContainer.setFrom(attributeContainer2);
      boolean flag3 = living2.isSneaking();
      this.setSneaking(flag3);
      boolean flag4 = living2.isSwimming();
      this.setSwimming(flag4);
      boolean flag5 = living2.isSprinting();
      this.setSprinting(flag5);
      EntityPose entityPose = living2.getPose();
      this.setPose(entityPose);
      float f10 = living2.getHealth();
      this.setHealth(f10);
      float f11 = living2.getAbsorptionAmount();
      this.setAbsorptionAmount(f11);
      this.setId(1603476961);
      this.age = 100;
      this.living = living2;
   }

   public void Sl() {
      float f = this.horizontalSpeed;
      this.prevHorizontalSpeed = f;
      float f2 = this.getPitch();
      this.prevPitch = f2;
      float f3 = this.getYaw();
      this.prevYaw = f3;
      this.updateWaterState();
      this.updateSwimming();
      this.bodyTrackingIncrements = 0;
      if (this.headTrackingIncrements > 0) {
         double d = this.serverHeadYaw;
         int n = this.headTrackingIncrements;
         this.lerpHeadYaw(n, d);
         int n2 = this.headTrackingIncrements - 1;
         this.headTrackingIncrements = n2;
      }

      Vec3d vec = this.getVelocity();
      double d2 = vec.x;
      double d3 = vec.y;
      double d4 = vec.z;
      if (Math.abs(vec.x) < 0.003) {
         d2 = 0.0;
      }

      if (Math.abs(vec.y) < 0.003) {
         d3 = 0.0;
      }

      if (Math.abs(vec.z) < 0.003) {
         d4 = 0.0;
      }

      this.setVelocity(d2, d3, d4);
      float f4 = this.sidewaysSpeed * 0.98F;
      this.sidewaysSpeed = f4;
      float f5 = this.forwardSpeed * 0.98F;
      this.forwardSpeed = f5;
      Vec3d vec2 = new Vec3d(this.sidewaysSpeed, this.upwardSpeed, this.forwardSpeed);
      this.Sm(vec2);
   }

   public void Sm(Vec3d vec) {
      label134:
      if (this.living.isSwimming()) {
         double d = this.living.getRotationVector().y;
         double d2;
         if (d < -0.2) {
            d2 = 0.085;
         } else {
            d2 = 0.06;
         }

         double d3 = d2;
         if (!(d <= 0.0) && !this.jumping) {
            World temp = this.getWorld();
            BlockPos pos2 = BlockPos.ofFloored(this.getX(), this.getY() + 1.0 - 0.1, this.getZ());
            if (temp.getBlockState(pos2).getFluidState().isEmpty()) {
               break label134;
            }
         }

         Vec3d vec2 = this.getVelocity();
         double d4 = (d - vec2.y) * d3;
         Vec3d vec3 = vec2.add(0.0, d4, 0.0);
         this.setVelocity(vec3);
      }

      double d5 = this.living.getFinalGravity();
      boolean flag;
      if (this.getVelocity().y <= 0.0) {
         flag = true;
         RegistryEntry registryEntry = StatusEffects.SLOW_FALLING;
         if (this.living.hasStatusEffect(registryEntry)) {
            d5 = Math.min(d5, 0.01);
         }
      } else {
         flag = false;
      }

      BlockPos pos3 = this.getBlockPos();
      FluidState fluidState = this.getWorld().getFluidState(pos3);
      if (this.living.isTouchingWater() && this.shouldSwimInFluids() && !this.living.canWalkOnFluid(fluidState)) {
         double d6 = this.getY();
         float f;
         if (this.living.isSprinting()) {
            f = 0.9F;
         } else {
            f = this.getBaseMovementSpeedMultiplier();
         }

         float f2 = f;
         float f3 = 0.02F;
         RegistryEntry registryEntry2 = EntityAttributes.GENERIC_WATER_MOVEMENT_EFFICIENCY;
         float f4 = (float)this.living.getAttributeValue(registryEntry2);
         if (!this.isOnGround()) {
            f4 *= 0.5F;
         }

         if (f4 > 0.0F) {
            f2 += (0.54600006F - f2) * f4;
            f3 = 0.02F + (this.living.getMovementSpeed() - 0.02F) * f4;
         }

         RegistryEntry registryEntry3 = StatusEffects.DOLPHINS_GRACE;
         if (this.living.hasStatusEffect(registryEntry3)) {
            f2 = 0.96F;
         }

         float f5 = f3;
         this.updateVelocity(f5, vec);
         MovementType movementType = MovementType.SELF;
         Vec3d vec4 = this.getVelocity();
         MovementType movementType2 = movementType;
         this.move(movementType2, vec4);
         Vec3d vec5 = this.getVelocity();
         if (this.horizontalCollision && this.isClimbing()) {
            vec5 = new Vec3d(vec5.x, 0.2, vec5.z);
         }

         Vec3d vec6 = vec5;
         double d7;
         double d8 = d7 = f2;
         Vec3d vec7 = vec6.multiply(d8, 0.8F, d7);
         this.setVelocity(vec7);
         Vec3d vec8 = this.getVelocity();
         boolean flag2 = flag;
         Vec3d vec9 = this.applyFluidMovingSpeed(d5, flag2, vec8);
         this.setVelocity(vec9);
         if (this.horizontalCollision) {
            double d9 = vec9.x;
            double d10 = vec9.y + 0.6F - this.getY() + d6;
            double d11 = vec9.z;
            double d12 = d10;
            double d13 = d9;
            if (this.doesNotCollide(d13, d12, d11)) {
               double d14 = vec9.z;
               double d15 = vec9.x;
               this.setVelocity(d15, 0.3F, d14);
            }
         }
      } else if (this.living.isInLava() && this.shouldSwimInFluids() && !this.living.canWalkOnFluid(fluidState)) {
         double d16 = this.getY();
         this.updateVelocity(0.02F, vec);
         MovementType movementType3 = MovementType.SELF;
         Vec3d vec10 = this.getVelocity();
         MovementType movementType4 = movementType3;
         this.move(movementType4, vec10);
         TagKey tagKey = FluidTags.LAVA;
         if (this.living.getFluidHeight(tagKey) <= this.living.getSwimHeight()) {
            Vec3d vec11 = this.getVelocity().multiply(0.5, 0.8F, 0.5);
            this.setVelocity(vec11);
            Vec3d vec12 = this.getVelocity();
            boolean flag3 = flag;
            Vec3d vec13 = this.applyFluidMovingSpeed(d5, flag3, vec12);
            this.setVelocity(vec13);
         } else {
            Vec3d vec14 = this.getVelocity().multiply(0.5);
            this.setVelocity(vec14);
         }

         if (d5 != 0.0) {
            Vec3d vec15 = this.getVelocity();
            double d17 = -d5 / 4.0;
            Vec3d vec16 = vec15.add(0.0, d17, 0.0);
            this.setVelocity(vec16);
         }

         Vec3d vec17 = this.getVelocity();
         if (this.horizontalCollision) {
            double d18 = vec17.x;
            double d19 = vec17.y + 0.6F - this.getY() + d16;
            double d20 = vec17.z;
            double d21 = d19;
            double d22 = d18;
            if (this.doesNotCollide(d22, d21, d20)) {
               double d23 = vec17.z;
               double d24 = vec17.x;
               this.setVelocity(d24, 0.3F, d23);
            }
         }
      } else if (this.living.isFallFlying()) {
         Vec3d vec18 = this.getVelocity();
         Vec3d vec19 = this.living.getRotationVector();
         float f6 = this.getPitch() * (float) (Math.PI / 180.0);
         double d25 = Math.sqrt(vec19.x * vec19.x + vec19.z * vec19.z);
         double d26 = vec18.horizontalLength();
         double d27 = vec19.length();
         double d28 = Math.cos(f6);
         d28 = d28 * d28 * Math.min(1.0, d27 / 0.4);
         Vec3d vec20 = this.getVelocity();
         double d29 = d5 * (-1.0 + d28 * 0.75);
         vec18 = vec20.add(0.0, d29, 0.0);
         if (vec18.y < 0.0 && d25 > 0.0) {
            double d30 = vec18.y * -0.1 * d28;
            vec20 = vec18;
            double temp2 = vec19.x * d30 / d25;
            double d31 = vec19.z * d30 / d25;
            double d32 = temp2;
            vec18 = vec20.add(d32, d30, d31);
         }

         if (f6 < 0.0F && d25 > 0.0) {
            double d33 = d26 * -MathHelper.sin(f6) * 0.04;
            vec20 = vec18;
            double d34 = -vec19.x * d33 / d25;
            double temp3 = d33 * 3.2;
            double d35 = -vec19.z * d33 / d25;
            double d36 = temp3;
            double d37 = d34;
            vec18 = vec20.add(d37, d36, d35);
         }

         if (d25 > 0.0) {
            vec20 = vec18;
            double d38 = (vec19.x / d25 * d26 - vec18.x) * 0.1;
            double d39 = (vec19.z / d25 * d26 - vec18.z) * 0.1;
            double d40 = d38;
            vec18 = vec20.add(d40, 0.0, d39);
         }

         Vec3d vec21 = vec18.multiply(0.99F, 0.98F, 0.99F);
         this.setVelocity(vec21);
         MovementType movementType5 = MovementType.SELF;
         Vec3d vec22 = this.getVelocity();
         MovementType movementType6 = movementType5;
         this.move(movementType6, vec22);
      } else {
         BlockPos pos4 = this.getVelocityAffectingPos();
         float f7 = this.getWorld().getBlockState(pos4).getBlock().getSlipperiness();
         float f8;
         if (this.isOnGround() && !this.a) {
            f8 = f7 * 0.91F;
         } else {
            f8 = 0.91F;
         }

         float f9 = f8;
         Vec3d vec23 = this.applyMovementInput(vec, f7);
         double d41 = vec23.y;
         RegistryEntry registryEntry4 = StatusEffects.LEVITATION;
         if (this.living.hasStatusEffect(registryEntry4)) {
            RegistryEntry registryEntry5 = StatusEffects.LEVITATION;
            d41 += (0.05 * (this.living.getStatusEffect(registryEntry5).getAmplifier() + 1) - vec23.y) * 0.2;
         } else if (!this.getWorld().isChunkLoaded(pos4)) {
            if (this.getY() > this.getWorld().getBottomY()) {
               d41 = -0.1;
            } else {
               d41 = 0.0;
            }
         } else {
            d41 -= d5;
         }

         if (this.living.hasNoDrag()) {
            double d42 = vec23.z;
            double d43 = vec23.x;
            this.setVelocity(d43, d41, d42);
         } else {
            double d44 = vec23.x * f9;
            boolean flag4 = this instanceof Flutterer;
            double d45 = d44;
            double d46;
            if (flag4) {
               d45 = d41 * f9;
               d46 = d45;
            } else {
               d45 = d41 * 0.98F;
               d46 = d45;
            }

            double d47 = vec23.z * f9;
            this.setVelocity(d46, d45, d47);
         }
      }
   }

   protected float getVelocityMultiplier() {
      BlockPos pos2 = this.getBlockPos();
      BlockState state = this.getWorld().getBlockState(pos2);
      float f = state.getBlock().getVelocityMultiplier();
      BlockState temp = state;
      Block block2 = Blocks.WATER;
      if (!temp.isOf(block2)) {
         temp = state;
         Block block3 = Blocks.BUBBLE_COLUMN;
         if (!temp.isOf(block3)) {
            float f2;
            if (f == 1.0) {
               BlockPos pos3 = this.getVelocityAffectingPos();
               f2 = this.getWorld().getBlockState(pos3).getBlock().getVelocityMultiplier();
            } else {
               f2 = f;
            }

            return f2;
         }
      }

      return f;
   }

   public boolean isDead() {
      return false;
   }

   public Iterable getArmorItems() {
      return null;
   }

   public ItemStack getEquippedStack(EquipmentSlot slot) {
      return null;
   }

   public void equipStack(EquipmentSlot slot, ItemStack stack) {
   }

   public Arm getMainArm() {
      return null;
   }

   public void Sn(Vec2f vec2f) {
      float f = vec2f.x;
      this.forwardSpeed = f;
      float f2 = vec2f.y;
      this.sidewaysSpeed = f2;
   }
}
