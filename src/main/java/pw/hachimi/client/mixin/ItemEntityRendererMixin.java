package pw.hachimi.client.mixin;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEvent7;
import pw.hachimi.client.model.render.FastHideMode;
import pw.hachimi.client.module.render.NoRender;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer {
   @Final
   @Shadow
   private ItemRenderer itemRenderer;
   @Final
   @Shadow
   private Random random;

   protected ItemEntityRendererMixin(Context context) {
      super(context);
   }

   @Unique
   private static int getRenderedAmount(ItemStack stack) {
      int n = stack.getCount();
      if (n <= 1) {
         return 1;
      } else if (n <= 16) {
         return 2;
      } else if (n <= 32) {
         return 3;
      } else {
         return n <= 48 ? 4 : 5;
      }
   }

   @Inject(method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
   private void hookRender(ItemEntity itemEntity, float f, float f2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, CallbackInfo callbackInfo) {
      RenderEvent7 renderEvent7 = new RenderEvent7(itemEntity);
      EventBusImpl.INSTANCE.rC(renderEvent7);
      if (renderEvent7.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
   public void render(ItemEntity itemEntity, float f, float f2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, CallbackInfo callbackInfo) {
      if (NoRender.ff().awg() && NoRender.ff().items.atx() == FastHideMode.FAST) {
         matrices.push();
         ItemStack stack = itemEntity.getStack();
         long l = stack.isEmpty() ? 187L : Item.getRawId(stack.getItem()) + stack.getDamage();
         this.random.setSeed(l);
         BakedModel bakedModel = this.itemRenderer.getModel(stack, itemEntity.getWorld(), null, itemEntity.getId());
         boolean flag = bakedModel.hasDepth();
         this.shadowRadius = (Boolean)NoRender.ff().castShadow.atx() ? 0.15F : 0.0F;
         float f3 = MathHelper.sin((itemEntity.getItemAge() + f2) / 10.0F + itemEntity.uniqueOffset) * 0.1F + 0.1F;
         float f4 = bakedModel.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
         matrices.translate(0.0F, f3 + 0.25F * f4, 0.0F);
         matrices.multiply(this.dispatcher.getRotation());
         float f5 = bakedModel.getTransformation().ground.scale.x();
         float f6 = bakedModel.getTransformation().ground.scale.y();
         float f7 = bakedModel.getTransformation().ground.scale.z();
         int n2 = getRenderedAmount(stack);
         if (!flag) {
            float f8 = -0.0F * (n2 - 1) * 0.5F * f5;
            float f9 = -0.0F * (n2 - 1) * 0.5F * f6;
            float f10 = -0.09375F * (n2 - 1) * 0.5F * f7;
            matrices.translate(f8, f9, f10);
         }

         for (int n3 = 0; n3 < n2; n3++) {
            matrices.push();
            if (n3 > 0) {
               if (flag) {
                  float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                  float f12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                  float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                  matrices.translate(f11, f12, f13);
               } else {
                  float f14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                  float f15 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                  matrices.translate(f14, f15, 0.0F);
               }
            }

            this.itemRenderer.renderItem(stack, ModelTransformationMode.GROUND, false, matrices, vertexConsumerProvider, n, OverlayTexture.DEFAULT_UV, bakedModel);
            matrices.pop();
            if (!flag) {
               matrices.translate(0.0F * f5, 0.0F * f6, 0.0425F * f7);
            }
         }

         matrices.pop();
         super.render(itemEntity, f, f2, matrices, vertexConsumerProvider, n);
         callbackInfo.cancel();
      }
   }
}
