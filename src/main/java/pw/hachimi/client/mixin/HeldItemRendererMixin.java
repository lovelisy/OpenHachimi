package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.player.ApplyEatOrDrinkTransformationEvent;
import pw.hachimi.client.event.player.EquipProgressMainhandEvent;
import pw.hachimi.client.event.player.UpdateHeldItemsEvent;
import pw.hachimi.client.event.render.RenderArmEvent;
import pw.hachimi.client.event.render.RenderFirstPersonItemEvent;
import pw.hachimi.client.event.render.RenderFirstPersonItem_2Event;
import pw.hachimi.client.module.exploit.SpearFix;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
   @Shadow
   private float equipProgressMainHand;
   @Shadow
   private float equipProgressOffHand;
   @Shadow
   private float prevEquipProgressMainHand;
   @Shadow
   private float prevEquipProgressOffHand;
   @Shadow
   @Final
   private EntityRenderDispatcher entityRenderDispatcher;
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   private ItemStack mainHand;
   @Shadow
   private ItemStack offHand;

   @Inject(method = "renderArmHoldingItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFFLnet/minecraft/util/Arm;)V", at = @At("HEAD"), cancellable = true)
   private void hookRenderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, float f2, float f3, Arm arm, CallbackInfo callbackInfo) {
      PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer)this.entityRenderDispatcher.getRenderer(this.client.player);
      RenderArmEvent renderArmEvent = new RenderArmEvent(matrices, vertexConsumerProvider, n, f2, f3, arm, playerEntityRenderer);
      EventBusImpl.INSTANCE.rC(renderArmEvent);
      if (renderArmEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
   private void hookRenderFirstPersonItem$2(
      AbstractClientPlayerEntity player2,
      float f2,
      float f3,
      Hand hand,
      float f4,
      ItemStack stack,
      float f5,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumerProvider,
      int n,
      CallbackInfo callbackInfo
   ) {
      RenderFirstPersonItem_2Event renderFirstPersonItem2Event = new RenderFirstPersonItem_2Event(hand, stack, f5, matrices);
      EventBusImpl.INSTANCE.rC(renderFirstPersonItem2Event);
      if (renderFirstPersonItem2Event.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(
      method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
      )
   )
   private void hookRenderFirstPersonItem(
      AbstractClientPlayerEntity player2,
      float f2,
      float f3,
      Hand hand,
      float f4,
      ItemStack stack,
      float f5,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumerProvider,
      int n,
      CallbackInfo callbackInfo
   ) {
      RenderFirstPersonItemEvent renderFirstPersonItemEvent = new RenderFirstPersonItemEvent(hand, stack, f5, matrices);
      EventBusImpl.INSTANCE.rC(renderFirstPersonItemEvent);
   }

   @Inject(method = "applyEatOrDrinkTransformation(Lnet/minecraft/client/util/math/MatrixStack;FLnet/minecraft/util/Arm;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"), cancellable = true)
   private void hookApplyEatOrDrinkTransformation(MatrixStack matrices, float f2, Arm arm, ItemStack stack, PlayerEntity player2, CallbackInfo callbackInfo) {
      callbackInfo.cancel();
      float f3 = this.client.player.getItemUseTimeLeft() - f2 + 1.0F;
      float f4 = f3 / stack.getMaxUseTime(Mc.client.player);
      if (f4 < 0.8F) {
         float f5 = MathHelper.abs(MathHelper.cos(f3 / 4.0F * (float) Math.PI) * 0.1F);
         ApplyEatOrDrinkTransformationEvent applyEatOrDrinkTransformationEvent = new ApplyEatOrDrinkTransformationEvent();
         EventBusImpl.INSTANCE.rC(applyEatOrDrinkTransformationEvent);
         matrices.translate(0.0F, applyEatOrDrinkTransformationEvent.aik() ? f5 * applyEatOrDrinkTransformationEvent.Fx() : f5, 0.0F);
      }

      float f6 = 1.0F - (float)Math.pow(f4, 27.0);
      int n = arm == Arm.RIGHT ? 1 : -1;
      matrices.translate(f6 * 0.6F * n, f6 * -0.5F, f6 * 0.0F);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(n * f6 * 90.0F));
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f6 * 10.0F));
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(n * f6 * 30.0F));
   }

   @ModifyArg(method = "updateHeldItems()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal = 2), index = 0)
   private float hookEquipProgressMainhand(float f2) {
      EquipProgressMainhandEvent equipProgressMainhandEvent = new EquipProgressMainhandEvent();
      EventBusImpl.INSTANCE.rC(equipProgressMainhandEvent);
      float f3 = Mc.client.player.getAttackCooldownProgress(1.0F);
      float f4 = equipProgressMainhandEvent.aik() ? 1.0F : f3 * f3 * f3;
      return (ItemStack.areEqual(this.mainHand, Mc.client.player.getMainHandStack()) ? f4 : 0.0F) - this.equipProgressMainHand;
   }

   @Inject(method = "updateHeldItems()V", at = @At("HEAD"), cancellable = true)
   private void hookUpdateHeldItems(CallbackInfo callbackInfo) {
      ItemStack stack = Mc.client.player.getMainHandStack();
      ItemStack stack2 = Mc.client.player.getOffHandStack();
      UpdateHeldItemsEvent updateHeldItemsEvent = new UpdateHeldItemsEvent();
      EventBusImpl.INSTANCE.rC(updateHeldItemsEvent);
      if (updateHeldItemsEvent.aik()) {
         callbackInfo.cancel();
         this.equipProgressMainHand = 1.0F;
         this.equipProgressOffHand = 1.0F;
         this.prevEquipProgressMainHand = 1.0F;
         this.prevEquipProgressOffHand = 1.0F;
         this.mainHand = stack;
         this.offHand = stack2;
      }
   }

   @WrapOperation(method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 12))
   private void suppressNormalSwing(MatrixStack matrices, float f2, float f3, float f4, Operation operation, @Local(argsOnly = true) ItemStack stack) {
      if (!SpearFix.nl().awg() || !SpearFix.nl().nm() || !SpearFix.nt(stack)) {
         operation.call(new Object[]{matrices, f2, f3, f4});
      }
   }

   @WrapOperation(
      method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 8)
   )
   private void suppressSwapAnimation(
      HeldItemRenderer heldItemRenderer,
      MatrixStack matrices,
      Arm arm,
      float f2,
      Operation operation,
      @Local(argsOnly = true) AbstractClientPlayerEntity player2,
      @Local(argsOnly = true) ItemStack stack
   ) {
      if (SpearFix.nl().awg() && SpearFix.nl().nm() && SpearFix.nt(stack)) {
         operation.call(new Object[]{heldItemRenderer, matrices, arm, 0.0F});
      } else {
         operation.call(new Object[]{heldItemRenderer, matrices, arm, f2});
      }
   }

   @WrapOperation(
      method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applySwingOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V")
   )
   private void spearAnimation(HeldItemRenderer heldItemRenderer, MatrixStack matrices, Arm arm, float f2, Operation operation, @Local(argsOnly = true) ItemStack stack) {
      if (SpearFix.nl().awg() && SpearFix.nl().nm() && SpearFix.nt(stack)) {
         if (SpearFix.f > 0.0) {
            float f3 = 1.0F - (float)MathHelper.lerp(Mc.client.getRenderTickCounter().getTickDelta(true), SpearFix.g, SpearFix.f) / 2.0F;
            f2 = f3;
         }

         if (f2 > 0.0F) {
            float f4 = -(MathHelper.cos((float) Math.PI * MathHelper.clamp(MathHelper.getLerpProgress(f2, 0.0F, 0.05F), 0.0F, 1.0F)) - 1.0F) / 2.0F;
            float f5 = MathHelper.clamp(MathHelper.getLerpProgress(f2, 0.05F, 0.2F), 0.0F, 1.0F);
            f5 *= f5;
            float f6 = MathHelper.clamp(MathHelper.getLerpProgress(f2, 0.4F, 1.0F), 0.0F, 1.0F);
            if (f6 < 0.5F) {
               f6 = f6 == 0.0F ? 0.0F : (float)(Math.pow(2.0, 20.0 * f6 - 10.0) / 2.0);
            } else {
               f6 = f6 == 1.0F ? 1.0F : (float)((2.0 - Math.pow(2.0, -20.0 * f6 + 10.0)) / 2.0);
            }

            matrices.translate(f6 * 0.1F * (f4 - f5), -0.075F * (f4 - f6), 0.0F + 0.65F * (f4 - f5));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-70.0F * (f4 - f6)));
            matrices.translate(0.0, 0.0, -0.25 * (f6 - f5));
         }
      } else {
         operation.call(new Object[]{heldItemRenderer, matrices, arm, f2});
      }
   }
}
