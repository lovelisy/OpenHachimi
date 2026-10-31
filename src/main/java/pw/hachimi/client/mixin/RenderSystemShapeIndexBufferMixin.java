package pw.hachimi.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem.ShapeIndexBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShapeIndexBuffer.class)
public interface RenderSystemShapeIndexBufferMixin {
   @Accessor("id")
   int getId();
}
