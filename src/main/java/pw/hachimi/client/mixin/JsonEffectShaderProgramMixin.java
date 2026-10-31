package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.ShaderStage.Type;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JsonEffectShaderProgram.class)
public class JsonEffectShaderProgramMixin {
   @WrapOperation(
      method = "loadEffect(Lnet/minecraft/resource/ResourceFactory;Lnet/minecraft/client/gl/ShaderStage$Type;Ljava/lang/String;)Lnet/minecraft/client/gl/EffectShaderStage;",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;")
   )
   private static Identifier ofVanilla1(String str, Operation operation, ResourceFactory resourceFactory, Type type, String str2) {
      if (!str2.contains(":")) {
         return (Identifier)operation.call(new Object[]{str});
      }

      Identifier id = Identifier.of(str2);
      return Identifier.of(id.getNamespace(), "shaders/program/" + id.getPath() + type.getFileExtension());
   }

   @WrapOperation(
      method = "<init>",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;")
   )
   private Identifier ofVanilla0(String str, Operation operation, ResourceFactory resourceFactory, String str2) {
      if (!str2.contains(":")) {
         return (Identifier)operation.call(new Object[]{str});
      }

      Identifier id = Identifier.of(str2);
      return Identifier.of(id.getNamespace(), "shaders/program/" + id.getPath() + ".json");
   }
}
