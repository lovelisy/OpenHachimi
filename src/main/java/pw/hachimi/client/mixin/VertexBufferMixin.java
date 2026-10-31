package pw.hachimi.client.mixin;

import java.nio.ByteBuffer;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BuiltBuffer.DrawParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.utils.render.RenderUtils2;

@Mixin(VertexBuffer.class)
public abstract class VertexBufferMixin {
   @Shadow
   private int indexBufferId;

   @Inject(method = "uploadIndexBuffer(Lnet/minecraft/client/render/BuiltBuffer$DrawParameters;Ljava/nio/ByteBuffer;)Lcom/mojang/blaze3d/systems/RenderSystem$ShapeIndexBuffer;", at = @At("RETURN"))
   private void onConfigureIndexBuffer(DrawParameters drawParameters, ByteBuffer byteBuffer, CallbackInfoReturnable callbackInfoReturnable) {
      if (callbackInfoReturnable.getReturnValue() == null) {
         RenderUtils2.j = this.indexBufferId;
      } else {
         RenderUtils2.j = ((RenderSystemShapeIndexBufferMixin)callbackInfoReturnable.getReturnValue()).getId();
      }
   }
}
