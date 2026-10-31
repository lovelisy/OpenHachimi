package pw.hachimi.client.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.module.client.Font;

@Mixin(targets = "net/minecraft/client/font/TextRenderer$Drawer")
public class bj {
   @Final
   @Mutable
   @Shadow
   private float brightnessMultiplier;
   @Final
   @Mutable
   @Shadow
   private float red;
   @Final
   @Mutable
   @Shadow
   private float green;
   @Final
   @Mutable
   @Shadow
   private float blue;

   @Inject(method = "<init>", at = @At("TAIL"))
   private void onNew(
      TextRenderer textRenderer,
      VertexConsumerProvider vertexConsumerProvider,
      float f,
      float f2,
      int n,
      boolean flag,
      Matrix4f matrix4f,
      TextLayerType textLayerType,
      int n2,
      CallbackInfo callbackInfo
   ) {
      if (flag && Font.kT() != null) {
         this.brightnessMultiplier = Font.kT().la();
         this.red = (n >> 16 & 0xFF) / 255.0F * this.brightnessMultiplier;
         this.green = (n >> 8 & 0xFF) / 255.0F * this.brightnessMultiplier;
         this.blue = (n & 0xFF) / 255.0F * this.brightnessMultiplier;
      }
   }
}
