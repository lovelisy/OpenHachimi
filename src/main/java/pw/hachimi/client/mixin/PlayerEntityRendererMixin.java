package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.TickEvent4;
import pw.hachimi.client.model.render.NormalHeadOnlyMode;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.utils.player.PlayerMathHelperUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.world.EntityBoxUtils;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
   @WrapOperation(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isFallFlying()Z"))
   private boolean hookSetupTransforms(AbstractClientPlayerEntity player2, Operation operation) {
      if (Mc.client.player == player2) {
         TickEvent4 tickEvent4 = new TickEvent4();
         EventBusImpl.INSTANCE.rC(tickEvent4);
         if (tickEvent4.aik()) {
            return false;
         }
      }

      return (Boolean)operation.call(new Object[]{player2});
   }

   @WrapOperation(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getPitch(F)F"))
   private float setupTransforms$Pitch(AbstractClientPlayerEntity player2, float f, Operation operation) {
      return player2 == Mc.client.player && HitResultWorldHelper.Sq().SN() != NormalHeadOnlyMode.None
         ? EntityBoxUtils.BB(PlayerMathHelperUtils.wP(), PlayerMathHelperUtils.wM(), f)
         : (Float)operation.call(new Object[]{player2, f});
   }

   @WrapOperation(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getRotationVec(F)Lnet/minecraft/util/math/Vec3d;"))
   private Vec3d hookSetupTransforms(AbstractClientPlayerEntity player2, float f, Operation operation) {
      return player2 == Mc.client.player && HitResultWorldHelper.Sq().SN() != NormalHeadOnlyMode.None
         ? RotationUtils.nS(
            EntityBoxUtils.BB(PlayerMathHelperUtils.wP(), PlayerMathHelperUtils.wM(), f),
            EntityBoxUtils.BB(PlayerMathHelperUtils.wQ(), PlayerMathHelperUtils.wN(), f)
         )
         : (Vec3d)operation.call(new Object[]{player2, f});
   }
}
