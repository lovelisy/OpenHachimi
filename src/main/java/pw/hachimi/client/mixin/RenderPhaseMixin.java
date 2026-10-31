package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.module.render.Glint;

@Mixin(RenderPhase.class)
public class RenderPhaseMixin {
   @Unique
   private boolean q;
   @Unique
   private boolean l;

   @WrapOperation(method = "setupGlintTexturing(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;"))
   private static Object modifyGlintSpeed(SimpleOption simpleOption, Operation operation) {
      return Glint.OU() ? Glint.OX() : operation.call(new Object[]{simpleOption});
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   private void onInit(String str, Runnable runnable, Runnable runnable2, CallbackInfo callbackInfo) {
      this.q = str.equals("glint_texturing");
      this.l = str.equals("entity_glint_texturing");
   }

   @Inject(method = "startDrawing()V", at = @At("TAIL"))
   private void onStartDrawing(CallbackInfo callbackInfo) {
      if (this.q || this.l) {
         Glint.OV(this.l);
      }
   }

   @Inject(method = "endDrawing()V", at = @At("TAIL"))
   private void onEndDrawing(CallbackInfo callbackInfo) {
      if (this.q || this.l) {
         Glint.OW();
      }
   }
}
