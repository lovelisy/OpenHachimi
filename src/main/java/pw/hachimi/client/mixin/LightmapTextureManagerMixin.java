package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.UpdateEvent2;
import pw.hachimi.client.event.UpdateEvent3;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
   @Final
   @Shadow
   private NativeImageBackedTexture texture;
   @Final
   @Shadow
   private NativeImage image;
   @Shadow
   private boolean dirty;
   @Final
   @Shadow
   private MinecraftClient client;

   @WrapOperation(method = "update(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"))
   private void hookUpdate(NativeImage nativeImage, int n, int n2, int n3, Operation operation) {
      UpdateEvent3 updateEvent3 = new UpdateEvent3(n3);
      EventBusImpl.INSTANCE.rC(updateEvent3);
      if (updateEvent3.aik()) {
         nativeImage.setColor(n, n2, updateEvent3.aiX());
      } else {
         operation.call(new Object[]{nativeImage, n, n2, n3});
      }
   }

   @Inject(method = "update(F)V", at = @At("HEAD"), cancellable = true)
   private void hookUpdate(float f, CallbackInfo callbackInfo) {
      UpdateEvent2 updateEvent2 = new UpdateEvent2();
      EventBusImpl.INSTANCE.rC(updateEvent2);
      Color color = updateEvent2.getColor();
      if (updateEvent2.aik()) {
         if (this.dirty) {
            this.dirty = false;
            this.client.getProfiler().push("lightTex");
            ClientWorld world2 = this.client.world;
            if (world2 != null) {
               for (int n = 0; n < 16; n++) {
                  for (int n2 = 0; n2 < 16; n2++) {
                     if (n2 < 15) {
                        this.image.setColor(n2, n, color.getRGB());
                     } else {
                        this.image.setColor(n2, n, -1);
                     }
                  }
               }

               this.texture.upload();
               this.client.getProfiler().pop();
            }
         }

         callbackInfo.cancel();
      }
   }
}
