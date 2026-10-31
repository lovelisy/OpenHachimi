package pw.hachimi.client.utils.render;

import com.mojang.datafixers.util.Pair;
import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPart.Cuboid;
import net.minecraft.client.model.ModelPart.Quad;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.model.RabbitEntityModel;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.BoatEntity.Type;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.module.render.CrystalModel;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.utils.player.PlayerMathHelperUtils;

public class MatrixModelPartUtils {
   private static final MatrixStack a_ = new MatrixStack();
   private static final Vector4f b = new Vector4f();
   private static final Vector4f c = new Vector4f();
   private static final Vector4f d = new Vector4f();
   private static final Vector4f e = new Vector4f();
   private static double f;
   private static double g;
   private static double h;
   private static Color color;
   private static Color color2;
   private static ShapeMode shapeMode;
   private static final long a = 0L;

   private MatrixModelPartUtils() {
   }

   public static void hX(Render3DContext render3DContext, Entity entity2, Color color3, Color color4, ShapeMode shapeMode2) {
      hY(render3DContext, render3DContext.f, entity2, 1.0, color3, color4, shapeMode2, 0.0F, false, false);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static void hY(
      Render3DContext render3DContext, float f2, Entity entity2, double d2, Color color3, Color color4, ShapeMode shapeMode2, float f3, boolean flag, boolean flag2
   ) {
      color = color3;
      color2 = color4;
      shapeMode = shapeMode2;
      f = MathHelper.lerp(f2, entity2.lastRenderX, entity2.getX());
      g = MathHelper.lerp(f2, entity2.lastRenderY, entity2.getY());
      h = MathHelper.lerp(f2, entity2.lastRenderZ, entity2.getZ());
      a_.push();
      float f4;
      float f5 = f4 = (float)d2;
      float f6 = (float)d2;
      a_.scale(f6, f5, f4);
      EntityRenderer entityRenderer = Mc.client.getEntityRenderDispatcher().getRenderer(entity2);
      if (entityRenderer instanceof LivingEntityRenderer) {
         EntityPose entityPose = EntityPose.SLEEPING;
         if (((LivingEntity)entity2).isInPose(entityPose)) {
            Direction direction = ((LivingEntity)entity2).getSleepingDirection();
            if (direction != null) {
               EntityPose entityPose2 = EntityPose.STANDING;
               float f7 = ((LivingEntity)entity2).getEyeHeight(entityPose2) - 0.1F;
               MatrixStack temp = a_;
               float f8 = -direction.getOffsetZ() * f7;
               float f9 = -direction.getOffsetX() * f7;
               temp.translate(f9, 0.0F, f8);
            }
         }

         EntityModel entityModel = ((LivingEntityRenderer)entityRenderer).getModel();
         if (entityRenderer instanceof PlayerEntityRenderer) {
            PlayerEntityModel playerEntityModel = (PlayerEntityModel)((PlayerEntityRenderer)entityRenderer).getModel();
            boolean flag3 = entity2.isInSneakingPose() || flag2;
            playerEntityModel.sneaking = flag3;
            if (playerEntityModel.sneaking) {
               a_.translate(0.0, -0.125, 0.0);
            }

            AbstractClientPlayerEntity player2 = (AbstractClientPlayerEntity)entity2;
            Hand hand = Hand.MAIN_HAND;
            ArmPose armPose = PlayerEntityRenderer.getArmPose(player2, hand);
            player2 = (AbstractClientPlayerEntity)entity2;
            Hand hand2 = Hand.OFF_HAND;
            ArmPose armPose2 = PlayerEntityRenderer.getArmPose(player2, hand2);
            if (armPose.isTwoHanded()) {
               armPose2 = ((LivingEntity)entity2).getOffHandStack().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
            }

            if (((LivingEntity)entity2).getMainArm() == Arm.RIGHT) {
               playerEntityModel.rightArmPose = armPose;
               playerEntityModel.leftArmPose = armPose2;
            } else {
               playerEntityModel.rightArmPose = armPose2;
               playerEntityModel.leftArmPose = armPose;
            }
         }

         float f10 = ((LivingEntity)entity2).getHandSwingProgress(f2);
         entityModel.handSwingProgress = f10;
         boolean flag4 = ((LivingEntity)entity2).hasVehicle();
         entityModel.riding = flag4;
         boolean flag5 = ((LivingEntity)entity2).isBaby();
         entityModel.child = flag5;
         float f11;
         float f12;
         float f13;
         if (entity2 == Mc.client.player) {
            switch (UnidentifiedHelper24.a[HitResultWorldHelper.Sq().SN().ordinal()]) {
               case 1:
                  f11 = PlayerMathHelperUtils.wJ(f2) + f3;
                  f12 = PlayerMathHelperUtils.wK(f2) + f3;
                  f13 = PlayerMathHelperUtils.wL(f2);
                  break;
               case 2:
                  f11 = PlayerMathHelperUtils.wK(f2) + f3;
                  f12 = PlayerMathHelperUtils.wK(f2) + f3;
                  f13 = PlayerMathHelperUtils.wL(f2);
                  break;
               case 3:
                  f11 = MathHelper.lerpAngleDegrees(f2, ((LivingEntity)entity2).prevBodyYaw, ((LivingEntity)entity2).bodyYaw) + f3;
                  f12 = PlayerMathHelperUtils.wK(f2) + f3;
                  f13 = PlayerMathHelperUtils.wL(f2);
                  break;
               default:
                  f11 = MathHelper.lerpAngleDegrees(f2, ((LivingEntity)entity2).prevBodyYaw, ((LivingEntity)entity2).bodyYaw) + f3;
                  f12 = MathHelper.lerpAngleDegrees(f2, ((LivingEntity)entity2).prevHeadYaw, ((LivingEntity)entity2).headYaw) + f3;
                  f13 = MathHelper.lerp(f2, ((LivingEntity)entity2).prevPitch, ((LivingEntity)entity2).getPitch());
            }
         } else {
            f11 = MathHelper.lerpAngleDegrees(f2, ((LivingEntity)entity2).prevBodyYaw, ((LivingEntity)entity2).bodyYaw) + f3;
            f12 = MathHelper.lerpAngleDegrees(f2, ((LivingEntity)entity2).prevHeadYaw, ((LivingEntity)entity2).headYaw) + f3;
            f13 = MathHelper.lerp(f2, ((LivingEntity)entity2).prevPitch, ((LivingEntity)entity2).getPitch());
         }

         float f14 = f12 - f11;
         if (((LivingEntity)entity2).hasVehicle()) {
            Entity entity3 = ((LivingEntity)entity2).getVehicle();
            if (entity3 instanceof LivingEntity) {
               f11 = MathHelper.lerpAngleDegrees(f2, ((LivingEntity)entity3).prevBodyYaw, ((LivingEntity)entity3).bodyYaw);
               float f15 = MathHelper.wrapDegrees(f12 - f11);
               if (f15 < -85.0F) {
                  f15 = -85.0F;
               }

               if (f15 >= 85.0F) {
                  f15 = 85.0F;
               }

               f11 = f12 - f15;
               if (f15 * f15 > 2500.0F) {
                  f11 += (float)(f15 * 0.2);
               }

               f14 = f12 - f11;
            }
         }

         float f16 = ((LivingEntityRenderer)entityRenderer).getAnimationProgress((LivingEntity)entity2, f2);
         float f17 = 0.0F;
         float f18 = 0.0F;
         if (!((LivingEntity)entity2).hasVehicle() && ((LivingEntity)entity2).isAlive()) {
            f17 = ((LivingEntity)entity2).limbAnimator.getSpeed(f2);
            f18 = ((LivingEntity)entity2).limbAnimator.getPos(f2);
            if (((LivingEntity)entity2).isBaby()) {
               f18 *= 3.0F;
            }

            if (f17 > 1.0F) {
               f17 = 1.0F;
            }
         }

         float temp2 = flag ? 0.0F : f18;
         float f19 = flag ? 0.0F : f17;
         float f20 = temp2;
         entityModel.animateModel(entity2, f20, f19, f2);
         temp2 = flag ? 0.0F : f18;
         float f21 = flag ? 0.0F : f17;
         float f22 = temp2;
         entityModel.setAngles(entity2, f22, f21, f16, f14, f13);
         MatrixStack temp3 = a_;
         float f23 = ((LivingEntity)entity2).getScale();
         MatrixStack matrices = temp3;
         ((LivingEntityRenderer)entityRenderer).setupTransforms((LivingEntity)entity2, matrices, f16, f11, f2, f23);
         a_.scale(-1.0F, -1.0F, 1.0F);
         MatrixStack matrices2 = a_;
         ((LivingEntityRenderer)entityRenderer).scale((LivingEntity)entity2, matrices2, f2);
         a_.translate(0.0, -1.501F, 0.0);
         if (entityModel instanceof AnimalModel) {
            if (((AnimalModel)entityModel).child) {
               a_.push();
               if (((AnimalModel)entityModel).headScaled) {
                  float f24 = 1.5F / ((AnimalModel)entityModel).invertedChildHeadScale;
                  a_.scale(f24, f24, f24);
               }

               double d3 = ((AnimalModel)entityModel).childHeadYOffset / 16.0F;
               double d4 = ((AnimalModel)entityModel).childHeadZOffset / 16.0F;
               double d5 = d3;
               a_.translate(0.0, d5, d4);
               if (entityModel instanceof BipedEntityModel) {
                  ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).head);
               } else {
                  Iterable iterable = ((AnimalModel)entityModel).getHeadParts();
                  final Render3DContext render3DContext2 = render3DContext;
                  Consumer consumer = new Consumer() {
                     @Override
                     public void accept(Object f2) {
                        MatrixModelPartUtils.ij(render3DContext2, f2);
                     }
                  };
                  iterable.forEach(consumer);
               }

               a_.pop();
               a_.push();
               float f25 = 1.0F / ((AnimalModel)entityModel).invertedChildBodyScale;
               a_.scale(f25, f25, f25);
               double d6 = ((AnimalModel)entityModel).childBodyYOffset / 16.0F;
               a_.translate(0.0, d6, 0.0);
               if (entityModel instanceof BipedEntityModel) {
                  ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).body);
                  ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).leftArm);
                  ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).rightArm);
                  ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).leftLeg);
                  ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).rightLeg);
               } else {
                  Iterable iterable2 = ((AnimalModel)entityModel).getBodyParts();
                  final Render3DContext render3DContext3 = render3DContext;
                  Consumer consumer2 = new Consumer() {
                     @Override
                     public void accept(Object f2) {
                        MatrixModelPartUtils.ii(render3DContext3, f2);
                     }
                  };
                  iterable2.forEach(consumer2);
               }

               a_.pop();
            } else if (entityModel instanceof BipedEntityModel) {
               ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).head);
               ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).body);
               ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).leftArm);
               ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).rightArm);
               ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).leftLeg);
               ia(render3DContext.boxRenderer, ((BipedEntityModel)entityModel).rightLeg);
            } else {
               Iterable iterable3 = ((AnimalModel)entityModel).getHeadParts();
               final Render3DContext render3DContext4 = render3DContext;
               Consumer consumer3 = new Consumer() {
                  @Override
                  public void accept(Object f2) {
                     MatrixModelPartUtils.ih(render3DContext4, f2);
                  }
               };
               iterable3.forEach(consumer3);
               iterable3 = ((AnimalModel)entityModel).getBodyParts();
               final Render3DContext render3DContext5 = render3DContext;
               Consumer consumer4 = new Consumer() {
                  @Override
                  public void accept(Object f2) {
                     MatrixModelPartUtils.ig(render3DContext5, f2);
                  }
               };
               iterable3.forEach(consumer4);
            }
         } else {
            Objects.requireNonNull(entityModel);
            switch (entityModel == null
               ? -1
               : (
                  0 <= 0 && entityModel instanceof SinglePartEntityModel
                     ? 0
                     : (
                        0 <= 1 && entityModel instanceof CompositeEntityModel
                           ? 1
                           : (0 <= 2 && entityModel instanceof LlamaEntityModel ? 2 : (0 <= 3 && entityModel instanceof RabbitEntityModel ? 3 : 4))
                     )
               )) {
               case 0:
                  ia(render3DContext.boxRenderer, ((SinglePartEntityModel)entityModel).getPart());
                  break;
               case 1:
                  Iterable iterable4 = ((CompositeEntityModel)entityModel).getParts();
                  final Render3DContext render3DContext6 = render3DContext;
                  Consumer consumer5 = new Consumer() {
                     @Override
                     public void accept(Object f2) {
                        MatrixModelPartUtils.if_(render3DContext6, f2);
                     }
                  };
                  iterable4.forEach(consumer5);
                  break;
               case 2:
                  if (((LlamaEntityModel)entityModel).child) {
                     a_.push();
                     a_.scale(0.71428573F, 0.64935064F, 0.7936508F);
                     a_.translate(0.0, 1.3125, 0.22F);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).head);
                     a_.pop();
                     a_.push();
                     a_.scale(0.625F, 0.45454544F, 0.45454544F);
                     a_.translate(0.0, 2.0625, 0.0);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).body);
                     a_.pop();
                     a_.push();
                     a_.scale(0.45454544F, 0.41322312F, 0.45454544F);
                     a_.translate(0.0, 2.0625, 0.0);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).rightHindLeg);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).leftHindLeg);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).rightFrontLeg);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).leftFrontLeg);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).rightChest);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).leftChest);
                     a_.pop();
                  } else {
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).head);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).body);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).rightHindLeg);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).leftHindLeg);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).rightFrontLeg);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).leftFrontLeg);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).rightChest);
                     ia(render3DContext.boxRenderer, ((LlamaEntityModel)entityModel).leftChest);
                  }
                  break;
               case 3:
                  if (((RabbitEntityModel)entityModel).child) {
                     a_.push();
                     a_.scale(0.56666666F, 0.56666666F, 0.56666666F);
                     a_.translate(0.0, 1.375, 0.125);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).head);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).leftEar);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).rightEar);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).nose);
                     a_.pop();
                     a_.push();
                     a_.scale(0.4F, 0.4F, 0.4F);
                     a_.translate(0.0, 2.25, 0.0);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).leftHindLeg);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).rightHindLeg);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).leftHaunch);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).rightHaunch);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).body);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).leftFrontLeg);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).rightFrontLeg);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).tail);
                     a_.pop();
                  } else {
                     a_.push();
                     a_.scale(0.6F, 0.6F, 0.6F);
                     a_.translate(0.0, 1.0, 0.0);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).leftHindLeg);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).rightHindLeg);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).leftHaunch);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).rightHaunch);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).body);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).leftFrontLeg);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).rightFrontLeg);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).head);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).rightEar);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).leftEar);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).tail);
                     ia(render3DContext.boxRenderer, ((RabbitEntityModel)entityModel).nose);
                     a_.pop();
                  }
            }
         }

         float f26 = f2;
         float f27 = f13;
         float f28 = f14;
         float f29 = f16;
         float f30 = f17;
         float f31 = f18;
         Entity entity4 = entity2;
         hZ(render3DContext.boxRenderer, (LivingEntityRenderer)entityRenderer, (LivingEntity)entity4, f31, f30, f29, f28, f27, f26);
      }

      if (entityRenderer instanceof EndCrystalEntityRenderer) {
         CrystalModel crystalModel = CrystalModel.kh();
         boolean flag6 = crystalModel.awg();
         a_.push();
         float f32;
         if (flag6 && !crystalModel.kn()) {
            f32 = -1.2F;
         } else {
            f32 = EndCrystalEntityRenderer.getYOffset((EndCrystalEntity)entity2, f2);
         }

         if (flag6) {
            f32 += crystalModel.kq();
         }

         float f33;
         if (flag6) {
            f33 = (((EndCrystalEntity)entity2).endCrystalAge + f2) * crystalModel.ko() * 3.0F;
         } else {
            f33 = (((EndCrystalEntity)entity2).endCrystalAge + f2) * 3.0F;
         }

         a_.push();
         if (flag6) {
            MatrixStack matrices3 = a_;
            float f34;
            float f35 = f34 = 2.0F * crystalModel.kp();
            float f36 = 2.0F * crystalModel.kp();
            matrices3.scale(f36, f35, f34);
         } else {
            a_.scale(2.0F, 2.0F, 2.0F);
         }

         a_.translate(0.0, -0.5, 0.0);
         if (((EndCrystalEntity)entity2).shouldShowBottom()) {
            ia(render3DContext.boxRenderer, ((EndCrystalEntityRenderer)entityRenderer).bottom);
         }

         MatrixStack matrices4 = a_;
         Quaternionf quaternionf = RotationAxis.POSITIVE_Y.rotationDegrees(f33);
         matrices4.multiply(quaternionf);
         double d7 = 1.5F + f32 / 2.0F;
         a_.translate(0.0, d7, 0.0);
         matrices4 = a_;
         Quaternionf quaternionf2 = new Quaternionf()
            .setAngleAxis((float) (Math.PI / 3), EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0F, EndCrystalEntityRenderer.SINE_45_DEGREES);
         matrices4.multiply(quaternionf2);
         ia(render3DContext.boxRenderer, ((EndCrystalEntityRenderer)entityRenderer).frame);
         a_.scale(0.875F, 0.875F, 0.875F);
         matrices4 = a_;
         Quaternionf quaternionf3 = new Quaternionf()
            .setAngleAxis((float) (Math.PI / 3), EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0F, EndCrystalEntityRenderer.SINE_45_DEGREES);
         matrices4.multiply(quaternionf3);
         matrices4 = a_;
         Quaternionf quaternionf4 = RotationAxis.POSITIVE_Y.rotationDegrees(f33);
         matrices4.multiply(quaternionf4);
         ia(render3DContext.boxRenderer, ((EndCrystalEntityRenderer)entityRenderer).frame);
         a_.scale(0.875F, 0.875F, 0.875F);
         matrices4 = a_;
         Quaternionf quaternionf5 = new Quaternionf()
            .setAngleAxis((float) (Math.PI / 3), EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0F, EndCrystalEntityRenderer.SINE_45_DEGREES);
         matrices4.multiply(quaternionf5);
         matrices4 = a_;
         Quaternionf quaternionf6 = RotationAxis.POSITIVE_Y.rotationDegrees(f33);
         matrices4.multiply(quaternionf6);
         ia(render3DContext.boxRenderer, ((EndCrystalEntityRenderer)entityRenderer).core);
         a_.pop();
         a_.pop();
      } else if (entityRenderer instanceof BoatEntityRenderer) {
         a_.push();
         a_.translate(0.0, 0.375, 0.0);
         MatrixStack matrices5 = a_;
         RotationAxis rotationAxis = RotationAxis.POSITIVE_Y;
         float f37 = 180.0F - MathHelper.lerp(f2, entity2.prevYaw, entity2.getYaw());
         Quaternionf quaternionf7 = rotationAxis.rotationDegrees(f37);
         matrices5.multiply(quaternionf7);
         float f38 = ((BoatEntity)entity2).getDamageWobbleTicks() - f2;
         float f39 = ((BoatEntity)entity2).getDamageWobbleStrength() - f2;
         if (f39 < 0.0F) {
            f39 = 0.0F;
         }

         if (f38 > 0.0F) {
            matrices5 = a_;
            rotationAxis = RotationAxis.POSITIVE_X;
            float f40 = MathHelper.sin(f38) * f38 * f39 / 10.0F * ((BoatEntity)entity2).getDamageWobbleSide();
            Quaternionf quaternionf8 = rotationAxis.rotationDegrees(f40);
            matrices5.multiply(quaternionf8);
         }

         if (!MathHelper.approximatelyEquals(((BoatEntity)entity2).interpolateBubbleWobble(f2), 0.0F)) {
            matrices5 = a_;
            Quaternionf quaternionf9 = new Quaternionf().setAngleAxis(((BoatEntity)entity2).interpolateBubbleWobble(f2), 1.0F, 0.0F, 1.0F);
            matrices5.multiply(quaternionf9);
         }

         Map map = ((BoatEntityRenderer)entityRenderer).texturesAndModels;
         Type type = ((BoatEntity)entity2).getVariant();
         CompositeEntityModel compositeEntityModel = (CompositeEntityModel)((Pair)map.get(type)).getSecond();
         a_.scale(-1.0F, -1.0F, 1.0F);
         matrices5 = a_;
         Quaternionf quaternionf10 = RotationAxis.POSITIVE_Y.rotationDegrees(90.0F);
         matrices5.multiply(quaternionf10);
         compositeEntityModel.setAngles(entity2, f2, 0.0F, -0.1F, 0.0F, 0.0F);
         Iterable iterable5 = compositeEntityModel.getParts();
         final Render3DContext render3DContext7 = render3DContext;
         Consumer consumer6 = new Consumer() {
            @Override
            public void accept(Object f2) {
               MatrixModelPartUtils.ie(render3DContext7, (ModelPart)f2);
            }
         };
         iterable5.forEach(consumer6);
         if (!((BoatEntity)entity2).isSubmergedInWater() && compositeEntityModel instanceof ModelWithWaterPatch) {
            ia(render3DContext.boxRenderer, ((ModelWithWaterPatch)compositeEntityModel).getWaterPatch());
         }

         a_.pop();
      } else if (entityRenderer instanceof ItemEntityRenderer) {
         double d8 = (entity2.getX() - entity2.prevX) * f2;
         double d9 = (entity2.getY() - entity2.prevY) * f2;
         double d10 = (entity2.getZ() - entity2.prevZ) * f2;
         Box box = entity2.getBoundingBox();
         render3DContext.boxRenderer
            .YY(
               d8 + box.minX,
               d9 + box.minY,
               d10 + box.minZ,
               d8 + box.maxX,
               d9 + box.maxY,
               d10 + box.maxZ,
               color3,
               color4,
               shapeMode2,
               0
            );
      }

      a_.pop();
   }

   private static void hZ(
      BoxRenderer boxRenderer2, LivingEntityRenderer livingEntityRenderer, LivingEntity living, float f2, float f3, float f4, float f5, float f6, float f7
   ) {
      Iterator iterator2 = livingEntityRenderer.features.iterator();

      while (iterator2.hasNext()) {
         FeatureRenderer featureRenderer = (FeatureRenderer)iterator2.next();
         if (featureRenderer instanceof SheepWoolFeatureRenderer && living instanceof SheepEntity && !((SheepEntity)living).isSheared() && !((SheepEntity)living).isInvisible()
            )
          {
            SheepWoolEntityModel sheepWoolEntityModel = ((SheepWoolFeatureRenderer)featureRenderer).model;
            float f8 = living.getHandSwingProgress(f7);
            sheepWoolEntityModel.handSwingProgress = f8;
            boolean flag = living.hasVehicle();
            sheepWoolEntityModel.riding = flag;
            boolean flag2 = living.isBaby();
            sheepWoolEntityModel.child = flag2;
            ((EntityModel)sheepWoolEntityModel).animateModel((Entity)living, f2, f3, f7);
            ((EntityModel)sheepWoolEntityModel).setAngles((Entity)living, f2, f3, f4, f5, f6);
            Iterable temp = sheepWoolEntityModel.getHeadParts();
            final BoxRenderer boxRenderer3 = boxRenderer2;
            Consumer consumer = new Consumer() {
               @Override
               public void accept(Object livingEntityRenderer) {
                  MatrixModelPartUtils.id(boxRenderer3, (ModelPart)livingEntityRenderer);
               }
            };
            temp.forEach(consumer);
            temp = sheepWoolEntityModel.getBodyParts();
            final BoxRenderer boxRenderer4 = boxRenderer2;
            Consumer consumer2 = new Consumer() {
               @Override
               public void accept(Object livingEntityRenderer) {
                  MatrixModelPartUtils.ic(boxRenderer4, (ModelPart)livingEntityRenderer);
               }
            };
            temp.forEach(consumer2);
         }
      }
   }

   private static void ia(BoxRenderer boxRenderer2, ModelPart modelPart) {
      if (modelPart.visible && (!modelPart.cuboids.isEmpty() || !modelPart.children.isEmpty())) {
         a_.push();
         MatrixStack matrices = a_;
         modelPart.rotate(matrices);
         Iterator iterator2 = modelPart.cuboids.iterator();

         while (iterator2.hasNext()) {
            Cuboid cuboid = (Cuboid)iterator2.next();
            ib(boxRenderer2, cuboid, f, g, h);
         }

         iterator2 = modelPart.children.values().iterator();

         while (iterator2.hasNext()) {
            ModelPart modelPart2 = (ModelPart)iterator2.next();
            ia(boxRenderer2, modelPart2);
         }

         a_.pop();
      }
   }

   private static void ib(BoxRenderer boxRenderer2, Cuboid cuboid, double d2, double d3, double d4) {
      Matrix4f matrix4f = a_.peek().getPositionMatrix();

      for (Quad quad : cuboid.sides) {
         b.set(quad.vertices[0].pos.x / 16.0F, quad.vertices[0].pos.y / 16.0F, quad.vertices[0].pos.z / 16.0F, 1.0F);
         b.mul(matrix4f);
         c.set(quad.vertices[1].pos.x / 16.0F, quad.vertices[1].pos.y / 16.0F, quad.vertices[1].pos.z / 16.0F, 1.0F);
         c.mul(matrix4f);
         d.set(quad.vertices[2].pos.x / 16.0F, quad.vertices[2].pos.y / 16.0F, quad.vertices[2].pos.z / 16.0F, 1.0F);
         d.mul(matrix4f);
         e.set(quad.vertices[3].pos.x / 16.0F, quad.vertices[3].pos.y / 16.0F, quad.vertices[3].pos.z / 16.0F, 1.0F);
         e.mul(matrix4f);
         if (shapeMode.KL()) {
            boxRenderer2.vec3dGameRendererHelper2
               .pD(
                  boxRenderer2.vec3dGameRendererHelper2.py(d2 + b.x, d3 + b.y, d4 + b.z).pA(color).pB(),
                  boxRenderer2.vec3dGameRendererHelper2.py(d2 + c.x, d3 + c.y, d4 + c.z).pA(color).pB(),
                  boxRenderer2.vec3dGameRendererHelper2.py(d2 + d.x, d3 + d.y, d4 + d.z).pA(color).pB(),
                  boxRenderer2.vec3dGameRendererHelper2.py(d2 + e.x, d3 + e.y, d4 + e.z).pA(color).pB()
               );
         }

         if (shapeMode.KK()) {
            boxRenderer2.YJ(d2 + b.x, d3 + b.y, d4 + b.z, d2 + c.x, d3 + c.y, d4 + c.z, color2);
            boxRenderer2.YJ(d2 + c.x, d3 + c.y, d4 + c.z, d2 + d.x, d3 + d.y, d4 + d.z, color2);
            boxRenderer2.YJ(d2 + d.x, d3 + d.y, d4 + d.z, d2 + e.x, d3 + e.y, d4 + e.z, color2);
            boxRenderer2.YJ(d2 + b.x, d3 + b.y, d4 + b.z, d2 + b.x, d3 + b.y, d4 + b.z, color2);
         }
      }
   }

   static void ic(BoxRenderer boxRenderer2, ModelPart modelPart) {
      ia(boxRenderer2, modelPart);
   }

   static void id(BoxRenderer boxRenderer2, ModelPart modelPart) {
      ia(boxRenderer2, modelPart);
   }

   static void ie(Render3DContext render3DContext, ModelPart modelPart) {
      ia(render3DContext.boxRenderer, modelPart);
   }

   static void if_(Render3DContext render3DContext, Object obj) {
      ia(render3DContext.boxRenderer, (ModelPart)obj);
   }

   static void ig(Render3DContext render3DContext, Object obj) {
      ia(render3DContext.boxRenderer, (ModelPart)obj);
   }

   static void ih(Render3DContext render3DContext, Object obj) {
      ia(render3DContext.boxRenderer, (ModelPart)obj);
   }

   static void ii(Render3DContext render3DContext, Object obj) {
      ia(render3DContext.boxRenderer, (ModelPart)obj);
   }

   static void ij(Render3DContext render3DContext, Object obj) {
      ia(render3DContext.boxRenderer, (ModelPart)obj);
   }
}
