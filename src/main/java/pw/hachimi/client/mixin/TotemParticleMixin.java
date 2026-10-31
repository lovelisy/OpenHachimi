package pw.hachimi.client.mixin;

import java.awt.Color;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.InitEvent2;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(TotemParticle.class)
public abstract class TotemParticleMixin extends ParticleMixin {
   @Inject(method = "<init>", at = @At("TAIL"))
   private void hookInit(
      ClientWorld world2, double d, double d2, double d3, double d4, double d5, double d6, SpriteProvider spriteProvider, CallbackInfo callbackInfo
   ) {
      InitEvent2 initEvent2 = new InitEvent2();
      EventBusImpl.INSTANCE.rC(initEvent2);
      if (initEvent2.aik()) {
         Color color = initEvent2.getColor();
         this.setColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
      }
   }
}
