package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.render.RenderPreEvent;
import pw.hachimi.client.event.render.Render_isInvisibleToEvent;
import pw.hachimi.client.model.render.NormalHeadOnlyMode;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.utils.player.PlayerMathHelperUtils;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
   @Shadow
   @Final
   public List features;
   @Unique
   LivingEntity u;
   @Shadow
   protected EntityModel model;

   @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
   public void onRenderPre(LivingEntity living, float f, float f2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, CallbackInfo callbackInfo) {
      this.u = living;
      RenderPreEvent renderPreEvent = new RenderPreEvent(
         (LivingEntityRenderer)(Object)this, living, f, f2, matrices, vertexConsumerProvider, n, this.model, this.getRenderLayer(living, false, false, false), this.features
      );
      EventBusImpl.INSTANCE.rC(renderPreEvent);
      if (renderPreEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @WrapOperation(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F", ordinal = 0))
   private float render$bodyYaw(float f, float f2, float f3, Operation operation) {
      if (this.u == Mc.client.player) {
         if (HitResultWorldHelper.Sq().SN() == NormalHeadOnlyMode.BodyFixed) {
            return PlayerMathHelperUtils.wK(f);
         }

         if (HitResultWorldHelper.Sq().SN() == NormalHeadOnlyMode.Normal) {
            return PlayerMathHelperUtils.wJ(f);
         }
      }

      return (Float)operation.call(new Object[]{f, f2, f3});
   }

   @WrapOperation(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F", ordinal = 1))
   private float render$headYaw(float f, float f2, float f3, Operation operation) {
      return this.u == Mc.client.player && HitResultWorldHelper.Sq().SN() != NormalHeadOnlyMode.None
         ? PlayerMathHelperUtils.wK(f)
         : (Float)operation.call(new Object[]{f, f2, f3});
   }

   @WrapOperation(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", ordinal = 0))
   private float render$pitch(float f, float f2, float f3, Operation operation) {
      return this.u == Mc.client.player && HitResultWorldHelper.Sq().SN() != NormalHeadOnlyMode.None
         ? PlayerMathHelperUtils.wL(f)
         : (Float)operation.call(new Object[]{f, f2, f3});
   }

   @Shadow
   protected abstract RenderLayer getRenderLayer(LivingEntity var1, boolean var2, boolean var3, boolean var4);

   @WrapOperation(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInvisibleTo(Lnet/minecraft/entity/player/PlayerEntity;)Z"))
   private boolean redirectRender$isInvisibleTo(LivingEntity living, PlayerEntity player2, Operation operation) {
      Render_isInvisibleToEvent renderisInvisibleToEvent = new Render_isInvisibleToEvent(player2);
      EventBusImpl.INSTANCE.rC(renderisInvisibleToEvent);
      return renderisInvisibleToEvent.aik() ? false : (Boolean)operation.call(new Object[]{living, player2});
   }
}
