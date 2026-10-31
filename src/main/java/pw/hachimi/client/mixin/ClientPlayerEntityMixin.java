package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.CurrentScreenEvent;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickEvent5;
import pw.hachimi.client.event.movement.DismountVehicleEvent;
import pw.hachimi.client.event.movement.GetMountJumpStrengthEvent;
import pw.hachimi.client.event.movement.MoveEvent;
import pw.hachimi.client.event.movement.PushOutOfBlocksEvent;
import pw.hachimi.client.event.movement.SetSprintingEvent;
import pw.hachimi.client.event.movement.TickMovementPostEvent;
import pw.hachimi.client.event.movement.TravelRotationEvent;
import pw.hachimi.client.event.network.SendMovementPacketsEvent;
import pw.hachimi.client.event.network.SendMovementPacketsTailEvent;
import pw.hachimi.client.event.player.SetCurrentHandEvent;
import pw.hachimi.client.event.player.SwingHandEvent;
import pw.hachimi.client.utils.movement.UnidentifiedHelper20;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
   @Shadow
   @Final
   public ClientPlayNetworkHandler networkHandler;
   @Shadow
   public double lastX;
   @Shadow
   public double lastBaseY;
   @Shadow
   public double lastZ;
   @Shadow
   public Input input;
   @Shadow
   public float lastYaw;
   @Shadow
   public float lastPitch;
   @Shadow
   public int ticksSinceLastPositionPacketSent;
   @Shadow
   @Final
   protected MinecraftClient client;
   @Shadow
   private boolean lastSneaking;
   @Shadow
   private boolean lastOnGround;
   @Shadow
   private boolean autoJumpEnabled;
   @Unique
   private boolean c;

   public ClientPlayerEntityMixin() {
      super(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player.getGameProfile());
   }

   @Shadow
   protected abstract void sendSprintingPacket();

   @Shadow
   public abstract boolean isSneaking();

   @Shadow
   protected abstract boolean isCamera();

   @Shadow
   protected abstract void autoJump(float var1, float var2);

   @Shadow
   public abstract void tick();

   @Shadow
   protected abstract void sendMovementPackets();

   @Inject(method = "sendMovementPackets()V", at = @At("TAIL"))
   private void hookSendMovementPacketsTail(CallbackInfo callbackInfo) {
      SendMovementPacketsTailEvent sendMovementPacketsTailEvent = new SendMovementPacketsTailEvent();
      sendMovementPacketsTailEvent.aqo(EventPhase.POST);
      EventBusImpl.INSTANCE.rC(sendMovementPacketsTailEvent);
   }

   @ModifyExpressionValue(method = "hasCollidedSoftly(Lnet/minecraft/util/math/Vec3d;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F"))
   public float hasCollidedSoftly$getYaw(float f) {
      if ((Object)this == Mc.client.player) {
         TravelRotationEvent travelRotationEvent = new TravelRotationEvent(this.getYaw(), this.getPitch());
         EventBusImpl.INSTANCE.rC(travelRotationEvent);
         if (travelRotationEvent.aik()) {
            return travelRotationEvent.a;
         }
      }

      return f;
   }

   @Inject(method = "sendMovementPackets()V", at = @At("HEAD"), cancellable = true)
   private void hookSendMovementPackets(CallbackInfo callbackInfo) {
      SendMovementPacketsTailEvent sendMovementPacketsTailEvent = new SendMovementPacketsTailEvent();
      sendMovementPacketsTailEvent.aqo(EventPhase.PRE);
      EventBusImpl.INSTANCE.rC(sendMovementPacketsTailEvent);
      UnidentifiedHelper20 unidentifiedHelper20 = new UnidentifiedHelper20(
         Mc.client.player.getX(),
         Mc.client.player.getY(),
         Mc.client.player.getZ(),
         Mc.client.player.getYaw(),
         Mc.client.player.getPitch(),
         Mc.client.player.isOnGround()
      );
      EventBusImpl.INSTANCE.rB(unidentifiedHelper20);
      double d = unidentifiedHelper20.aqO();
      double d2 = unidentifiedHelper20.aqQ();
      double d3 = unidentifiedHelper20.aqS();
      float f = unidentifiedHelper20.aqU();
      float f2 = unidentifiedHelper20.aqW();
      boolean flag = unidentifiedHelper20.aqY();
      SendMovementPacketsEvent sendMovementPacketsEvent = new SendMovementPacketsEvent();
      EventBusImpl.INSTANCE.rC(sendMovementPacketsEvent);
      if (sendMovementPacketsEvent.aik()) {
         f += 3.6E7F;
      }

      if (unidentifiedHelper20.arf() || sendMovementPacketsEvent.aik()) {
         callbackInfo.cancel();
         this.sendSprintingPacket();
         boolean flag2 = this.isSneaking();
         if (flag2 != this.lastSneaking) {
            Mode mode = flag2 ? Mode.PRESS_SHIFT_KEY : Mode.RELEASE_SHIFT_KEY;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode));
            this.lastSneaking = flag2;
         }

         if (this.isCamera()) {
            double d4 = d - this.lastX;
            double d5 = d2 - this.lastBaseY;
            double d6 = d3 - this.lastZ;
            double d7 = f - this.lastYaw;
            double d8 = f2 - this.lastPitch;
            this.ticksSinceLastPositionPacketSent++;
            boolean flag3 = MathHelper.squaredMagnitude(d4, d5, d6) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacketSent >= 20 || unidentifiedHelper20.arc();
            boolean flag4 = d7 != 0.0 || d8 != 0.0 || unidentifiedHelper20.ara();
            if (this.hasVehicle()) {
               Vec3d vec = this.getVelocity();
               this.networkHandler.sendPacket(new Full(vec.x, -999.0, vec.z, this.getYaw(), this.getPitch(), flag));
               flag3 = false;
            } else if (flag3 && flag4) {
               this.networkHandler.sendPacket(new Full(d, d2, d3, f, f2, flag));
            } else if (flag3) {
               this.networkHandler.sendPacket(new PositionAndOnGround(d, d2, d3, flag));
            } else if (flag4) {
               this.networkHandler.sendPacket(new LookAndOnGround(f, f2, flag));
            } else if (this.lastOnGround != this.isOnGround()) {
               this.networkHandler.sendPacket(new OnGroundOnly(flag));
            }

            if (flag3) {
               this.lastX = d;
               this.lastBaseY = d2;
               this.lastZ = d3;
               this.ticksSinceLastPositionPacketSent = 0;
            }

            if (flag4) {
               this.lastYaw = f;
               this.lastPitch = f2;
            }

            this.lastOnGround = flag;
            this.autoJumpEnabled = (Boolean)this.client.options.getAutoJump().getValue();
         }

         sendMovementPacketsTailEvent.aqo(EventPhase.POST);
         EventBusImpl.INSTANCE.rC(sendMovementPacketsTailEvent);
      }
   }

   @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V", ordinal = 0, shift = Shift.AFTER))
   private void hookTick(CallbackInfo callbackInfo) {
      if (!this.c) {
         TickEvent5 tickEvent5 = new TickEvent5();
         EventBusImpl.INSTANCE.rC(tickEvent5);
         if (tickEvent5.aik()) {
            for (int n = 0; n < tickEvent5.arE(); n++) {
               this.c = true;
               this.tick();
               this.c = false;
               this.sendMovementPackets();
            }
         }
      }
   }

   @Inject(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;tick(ZF)V", shift = Shift.AFTER))
   private void hookTickMovementPost(CallbackInfo callbackInfo) {
      TickMovementPostEvent tickMovementPostEvent = new TickMovementPostEvent(this.input);
      EventBusImpl.INSTANCE.rC(tickMovementPostEvent);
   }

   @Inject(method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
   private void hookMove(MovementType movementType, Vec3d vec, CallbackInfo callbackInfo) {
      MoveEvent moveEvent = new MoveEvent(movementType, vec);
      EventBusImpl.INSTANCE.rC(moveEvent);
      if (moveEvent.aik()) {
         callbackInfo.cancel();
         double d = this.getX();
         double d2 = this.getZ();
         super.move(movementType, moveEvent.yk());
         this.autoJump((float)(this.getX() - d), (float)(this.getZ() - d2));
      }
   }

   @Inject(method = "pushOutOfBlocks(DD)V", at = @At("HEAD"), cancellable = true)
   private void onPushOutOfBlocks(double d, double d2, CallbackInfo callbackInfo) {
      PushOutOfBlocksEvent pushOutOfBlocksEvent = new PushOutOfBlocksEvent();
      EventBusImpl.INSTANCE.rC(pushOutOfBlocksEvent);
      if (pushOutOfBlocksEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "setCurrentHand(Lnet/minecraft/util/Hand;)V", at = @At("HEAD"))
   private void hookSetCurrentHand(Hand hand, CallbackInfo callbackInfo) {
      SetCurrentHandEvent setCurrentHandEvent = new SetCurrentHandEvent(hand);
      EventBusImpl.INSTANCE.rC(setCurrentHandEvent);
   }

   @WrapOperation(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V", ordinal = 3))
   private void hookSetSprinting(ClientPlayerEntity player2, boolean flag, Operation operation) {
      SetSprintingEvent setSprintingEvent = new SetSprintingEvent();
      EventBusImpl.INSTANCE.rC(setSprintingEvent);
      if (setSprintingEvent.aik()) {
         player2.setSprinting(true);
      } else {
         operation.call(new Object[]{player2, flag});
      }
   }

   @WrapOperation(method = "tickNausea(Z)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"))
   private Screen hookCurrentScreen(MinecraftClient client2, Operation operation) {
      CurrentScreenEvent currentScreenEvent = new CurrentScreenEvent();
      EventBusImpl.INSTANCE.rC(currentScreenEvent);
      return currentScreenEvent.aik() ? null : (Screen)operation.call(new Object[]{client2});
   }

   @Inject(method = "getMountJumpStrength()F", at = @At("HEAD"), cancellable = true)
   private void hookGetMountJumpStrength(CallbackInfoReturnable callbackInfoReturnable) {
      GetMountJumpStrengthEvent getMountJumpStrengthEvent = new GetMountJumpStrengthEvent();
      EventBusImpl.INSTANCE.rC(getMountJumpStrengthEvent);
      if (getMountJumpStrengthEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getMountJumpStrengthEvent.Dc());
      }
   }

   @Inject(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At("RETURN"))
   private void hookSwingHand(Hand hand, CallbackInfo callbackInfo) {
      SwingHandEvent swingHandEvent = new SwingHandEvent(hand);
      EventBusImpl.INSTANCE.rC(swingHandEvent);
   }

   @Inject(method = "dismountVehicle()V", at = @At("HEAD"), cancellable = true)
   private void hookDismountVehicle(CallbackInfo callbackInfo) {
      DismountVehicleEvent dismountVehicleEvent = new DismountVehicleEvent();
      EventBusImpl.INSTANCE.rC(dismountVehicleEvent);
      if (dismountVehicleEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
