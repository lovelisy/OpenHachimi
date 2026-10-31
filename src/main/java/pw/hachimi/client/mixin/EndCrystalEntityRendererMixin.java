package pw.hachimi.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEvent3;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(EndCrystalEntityRenderer.class)
public abstract class EndCrystalEntityRendererMixin extends EntityRenderer {
   @Shadow
   @Final
   private static RenderLayer END_CRYSTAL;
   @Shadow
   @Final
   private static float SINE_45_DEGREES;
   @Shadow
   @Final
   public ModelPart core;
   @Shadow
   @Final
   public ModelPart frame;
   @Shadow
   @Final
   public ModelPart bottom;

   protected EndCrystalEntityRendererMixin(Context context) {
      super(context);
   }

   @Inject(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
   private void hookRender(EndCrystalEntity endCrystalEntity, float f, float f2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, CallbackInfo callbackInfo) {
      RenderEvent3 renderEvent3 = new RenderEvent3(endCrystalEntity, f, f2, matrices, n, this.core, this.frame, this.dispatcher);
      EventBusImpl.INSTANCE.rC(renderEvent3);
      callbackInfo.cancel();
      if (!renderEvent3.aik()) {
         if (renderEvent3.l) {
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset(1.0F, -2000000.0F);
         }

         matrices.push();
         float f3 = !renderEvent3.oW() ? -1.2F : EndCrystalEntityRenderer.getYOffset(endCrystalEntity, f2);
         f3 += renderEvent3.h;
         float f4 = (endCrystalEntity.endCrystalAge + f2) * renderEvent3.oS() * 3.0F;
         VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
         matrices.push();
         matrices.scale(renderEvent3.oU(), renderEvent3.oU(), renderEvent3.oU());
         matrices.scale(2.0F, 2.0F, 2.0F);
         matrices.translate(0.0F, -0.5F, 0.0F);
         int n2 = OverlayTexture.DEFAULT_UV;
         if (endCrystalEntity.shouldShowBottom()) {
            this.bottom.render(matrices, vertexConsumer, n, n2);
         }

         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f4));
         matrices.translate(0.0F, 1.5F + f3 / 2.0F, 0.0F);
         matrices.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
         this.frame.render(matrices, vertexConsumer, n, n2);
         matrices.scale(0.875F, 0.875F, 0.875F);
         matrices.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f4));
         this.frame.render(matrices, vertexConsumer, n, n2);
         matrices.scale(0.875F, 0.875F, 0.875F);
         matrices.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f4));
         this.core.render(matrices, vertexConsumer, n, n2);
         matrices.scale(1.0F / renderEvent3.oU(), 1.0F / renderEvent3.oU(), 1.0F / renderEvent3.oU());
         matrices.pop();
         matrices.pop();
         BlockPos pos = endCrystalEntity.getBeamTarget();
         if (pos != null) {
            float f5 = pos.getX() + 0.5F;
            float f6 = pos.getY() + 0.5F;
            float f7 = pos.getZ() + 0.5F;
            float f8 = (float)(f5 - endCrystalEntity.getX());
            float f9 = (float)(f6 - endCrystalEntity.getY());
            float f10 = (float)(f7 - endCrystalEntity.getZ());
            matrices.translate(f8, f9, f10);
            EnderDragonEntityRenderer.renderCrystalBeam(-f8, -f9 + f3, -f10, f2, endCrystalEntity.endCrystalAge, matrices, vertexConsumerProvider, n);
         }

         super.render(endCrystalEntity, f, f2, matrices, vertexConsumerProvider, n);
         RenderSystem.polygonOffset(1.0F, 2000000.0F);
         RenderSystem.disablePolygonOffset();
      }
   }
}
