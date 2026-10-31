package pw.hachimi.client.mixin;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.model.render.FastHideMode;
import pw.hachimi.client.module.exploit.SpearFix;
import pw.hachimi.client.module.render.NoRender;
import pw.hachimi.client.utils.render.ItemRendererBakedModelDirectionHelper;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin2 {
   @Unique
   private static final ItemRendererBakedModelDirectionHelper p = new ItemRendererBakedModelDirectionHelper();
   @Unique
   private ModelTransformationMode a;

   @Shadow
   public abstract void renderItem(
      ItemStack var1, ModelTransformationMode var2, boolean var3, MatrixStack var4, VertexConsumerProvider var5, int var6, int var7, BakedModel var8
   );

   @Inject(method = "renderItem*", at = @At("HEAD"))
   private void getRenderType(
      ItemStack stack,
      ModelTransformationMode modelTransformationMode,
      boolean flag,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumerProvider,
      int n,
      int n2,
      BakedModel bakedModel,
      CallbackInfo callbackInfo
   ) {
      this.a = modelTransformationMode;
   }

   @ModifyVariable(method = "renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V", at = @At("HEAD"), index = 1, argsOnly = true, require = 0)
   private BakedModel replaceItemModelClass(BakedModel bakedModel, BakedModel bakedModel2, ItemStack stack, int n, int n2, MatrixStack matrices, VertexConsumer vertexConsumer) {
      if (NoRender.ff().awg()
         && NoRender.ff().items.atx() == FastHideMode.FAST
         && !(Boolean)NoRender.ff().sidesOfItems.atx()
         && !stack.isEmpty()
         && !bakedModel.hasDepth()
         && this.a == ModelTransformationMode.GROUND) {
         p.setBakedModel(bakedModel);
         return p;
      } else {
         return bakedModel;
      }
   }

   @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At("HEAD"), cancellable = true)
   private void renderItem$HEAD(
      LivingEntity living,
      ItemStack stack,
      ModelTransformationMode modelTransformationMode,
      boolean flag,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumerProvider,
      World world2,
      int n,
      int n2,
      int n3,
      CallbackInfo callbackInfo
   ) {
      if (modelTransformationMode != ModelTransformationMode.GUI) {
         SpearFix spearFix = SpearFix.nl();
         if (spearFix.awg() && spearFix.nm() && SpearFix.nt(stack)) {
            callbackInfo.cancel();
            BakedModel bakedModel = spearFix.nn(stack, true);
            this.renderItem(stack, modelTransformationMode, flag, matrices, vertexConsumerProvider, n, n2, bakedModel);
         }
      }
   }

   @Inject(method = "getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;", at = @At("HEAD"), cancellable = true)
   private void getModel$HEAD(ItemStack stack, World world2, LivingEntity living, int n, CallbackInfoReturnable callbackInfoReturnable) {
      SpearFix spearFix = SpearFix.nl();
      if (spearFix.awg() && spearFix.nm() && SpearFix.nt(stack)) {
         callbackInfoReturnable.setReturnValue(spearFix.nn(stack, false));
      }
   }
}
