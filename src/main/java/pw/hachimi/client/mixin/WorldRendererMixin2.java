package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEntityEvent;
import pw.hachimi.client.event.render.RenderEvent14;
import pw.hachimi.client.event.render.RenderWorldBorderEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin2 {
   @Inject(method = "renderWorldBorder(Lnet/minecraft/client/render/Camera;)V", at = @At("HEAD"), cancellable = true)
   private void hookRenderWorldBorder(Camera camera, CallbackInfo callbackInfo) {
      RenderWorldBorderEvent renderWorldBorderEvent = new RenderWorldBorderEvent();
      EventBusImpl.INSTANCE.rC(renderWorldBorderEvent);
      if (renderWorldBorderEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @WrapOperation(method = "render(Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;isThirdPerson()Z"))
   public boolean hookRender(Camera camera, Operation operation) {
      RenderEvent14 renderEvent14 = new RenderEvent14(camera);
      EventBusImpl.INSTANCE.rC(renderEvent14);
      return renderEvent14.aik() ? true : (Boolean)operation.call(new Object[]{camera});
   }

   @Inject(method = "renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"), cancellable = true)
   private void hookRenderEntity(
      Entity entity2, double d, double d2, double d3, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, CallbackInfo callbackInfo
   ) {
      RenderEntityEvent renderEntityEvent = new RenderEntityEvent(entity2);
      EventBusImpl.INSTANCE.rC(renderEntityEvent);
      if (renderEntityEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
