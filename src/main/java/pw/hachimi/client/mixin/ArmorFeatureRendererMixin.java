package pw.hachimi.client.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderArmorGlintEvent;
import pw.hachimi.client.event.render.RenderArmorPartsEvent;
import pw.hachimi.client.event.render.RenderArmorTrimEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
   @Inject(method = "renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/model/BipedEntityModel;ILnet/minecraft/util/Identifier;)V", at = @At("HEAD"), cancellable = true)
   private void onRenderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, BipedEntityModel bipedEntityModel, int n2, Identifier id, CallbackInfo callbackInfo) {
      RenderArmorPartsEvent renderArmorPartsEvent = new RenderArmorPartsEvent();
      EventBusImpl.INSTANCE.rC(renderArmorPartsEvent);
      if (renderArmorPartsEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "renderTrim(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/trim/ArmorTrim;Lnet/minecraft/client/render/entity/model/BipedEntityModel;Z)V", at = @At("HEAD"), cancellable = true)
   private void onRenderArmorTrim(
      RegistryEntry registryEntry, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, ArmorTrim armorTrim, BipedEntityModel bipedEntityModel, boolean flag, CallbackInfo callbackInfo
   ) {
      RenderArmorTrimEvent renderArmorTrimEvent = new RenderArmorTrimEvent();
      EventBusImpl.INSTANCE.rC(renderArmorTrimEvent);
      if (renderArmorTrimEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "renderGlint(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/model/BipedEntityModel;)V", at = @At("HEAD"), cancellable = true)
   private void onRenderArmorGlint(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, BipedEntityModel bipedEntityModel, CallbackInfo callbackInfo) {
      RenderArmorGlintEvent renderArmorGlintEvent = new RenderArmorGlintEvent();
      EventBusImpl.INSTANCE.rC(renderArmorGlintEvent);
      if (renderArmorGlintEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
