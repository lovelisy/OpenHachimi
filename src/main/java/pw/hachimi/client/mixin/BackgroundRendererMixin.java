package pw.hachimi.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.render.ApplyFogEvent;
import pw.hachimi.client.event.render.GetFogColorOverrideEvent;
import pw.hachimi.client.event.render.GetFogModifierEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
   @Shadow
   private static float red;
   @Shadow
   private static float green;
   @Shadow
   private static float blue;

   @Inject(method = "applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZF)V", at = @At("TAIL"))
   private static void hookApplyFog(Camera camera, FogType fogType, float f, boolean flag, float f2, CallbackInfo callbackInfo) {
      if (fogType == FogType.FOG_TERRAIN) {
         ApplyFogEvent applyFogEvent = new ApplyFogEvent(f);
         EventBusImpl.INSTANCE.rC(applyFogEvent);
         if (applyFogEvent.aik()) {
            RenderSystem.setShaderFogStart(applyFogEvent.VS());
            RenderSystem.setShaderFogEnd(applyFogEvent.VU());
         }
      }
   }

   @Inject(method = "render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V", at = @At("HEAD"), cancellable = true)
   private static void hookRender(Camera camera, float f, ClientWorld world2, int n, float f2, CallbackInfo callbackInfo) {
      GetFogColorOverrideEvent getFogColorOverrideEvent = new GetFogColorOverrideEvent(f);
      EventBusImpl.INSTANCE.rC(getFogColorOverrideEvent);
      if (getFogColorOverrideEvent.aik()) {
         callbackInfo.cancel();
         Color color = getFogColorOverrideEvent.getColor();
         red = (float)(color.getRed() / 255.0);
         green = (float)(color.getGreen() / 255.0);
         blue = (float)(color.getBlue() / 255.0);
         RenderSystem.clearColor(red, green, blue, (float)(color.getAlpha() / 255.0));
      }
   }

   @Inject(method = "getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;", at = @At("HEAD"), cancellable = true)
   private static void onGetFogModifier(Entity entity2, float f, CallbackInfoReturnable callbackInfoReturnable) {
      GetFogModifierEvent getFogModifierEvent = new GetFogModifierEvent();
      EventBusImpl.INSTANCE.rC(getFogModifierEvent);
      if (getFogModifierEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(null);
      }
   }
}
