package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.combat.AttackBlockEvent;
import pw.hachimi.client.event.player.InteractItemEvent;
import pw.hachimi.client.event.player.StackEvent;
import pw.hachimi.client.event.player.SyncSelectedSlotEvent;
import pw.hachimi.client.event.player.UseEvent;
import pw.hachimi.client.event.world.BreakBlockEvent;
import pw.hachimi.client.event.world.CancelBlockBreakingEvent;
import pw.hachimi.client.event.world.InteractBlockEvent;
import pw.hachimi.client.event.world.InteractBlock_2Event;
import pw.hachimi.client.event.world.MiningCooldownFixEvent;
import pw.hachimi.client.event.world.RedirectInteractBlockInternal_shouldCancelInteractionEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
   @Final
   @Shadow
   private MinecraftClient client;
   @Shadow
   private GameMode gameMode;
   @Shadow
   private int blockBreakingCooldown;

   @Shadow
   protected abstract void syncSelectedSlot();

   @Shadow
   protected abstract void sendSequencedPacket(ClientWorld var1, SequencedPacketCreator var2);

   @WrapOperation(
      method = "isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areItemsAndComponentsEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z")
   )
   private boolean stack(ItemStack stack2, ItemStack stack3, Operation operation) {
      StackEvent stackEvent = new StackEvent();
      EventBusImpl.INSTANCE.rC(stackEvent);
      return stackEvent.aik() ? true : (Boolean)operation.call(new Object[]{stack2, stack3});
   }

   @Inject(method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"))
   private void MiningCooldownFix(BlockPos pos, Direction direction, CallbackInfoReturnable callbackInfoReturnable) {
      MiningCooldownFixEvent miningCooldownFixEvent = new MiningCooldownFixEvent();
      EventBusImpl.INSTANCE.rC(miningCooldownFixEvent);
      if (miningCooldownFixEvent.aik()) {
         this.blockBreakingCooldown = 0;
      }
   }

   @Inject(method = "cancelBlockBreaking()V", at = @At("HEAD"), cancellable = true)
   private void hookCancelBlockBreaking(CallbackInfo callbackInfo) {
      CancelBlockBreakingEvent cancelBlockBreakingEvent = new CancelBlockBreakingEvent();
      EventBusImpl.INSTANCE.rC(cancelBlockBreakingEvent);
      if (cancelBlockBreakingEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
   private void hookAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable callbackInfoReturnable) {
      BlockState state = this.client.world.getBlockState(pos);
      AttackBlockEvent attackBlockEvent = new AttackBlockEvent(pos, state, direction);
      EventBusImpl.INSTANCE.rC(attackBlockEvent);
      if (attackBlockEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(false);
      }
   }

   @Inject(method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
   private void hookInteractBlock(ClientPlayerEntity player2, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable callbackInfoReturnable) {
      InteractBlockEvent interactBlockEvent = new InteractBlockEvent(player2, hand, blockHitResult);
      EventBusImpl.INSTANCE.rC(interactBlockEvent);
      if (interactBlockEvent.aik()) {
         callbackInfoReturnable.setReturnValue(ActionResult.SUCCESS);
         callbackInfoReturnable.cancel();
      }
   }

   @Redirect(method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;contains(Lnet/minecraft/util/math/BlockPos;)Z"))
   private boolean hookInteractBlock$2(WorldBorder worldBorder, BlockPos pos) {
      InteractBlock_2Event interactBlock2Event = new InteractBlock_2Event();
      EventBusImpl.INSTANCE.rC(interactBlock2Event);
      return interactBlock2Event.aik() ? true : worldBorder.contains(pos);
   }

   @Inject(method = "breakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
   private void hookBreakBlock(BlockPos pos, CallbackInfoReturnable callbackInfoReturnable) {
      BreakBlockEvent breakBlockEvent = new BreakBlockEvent(pos);
      EventBusImpl.INSTANCE.rC(breakBlockEvent);
      if (breakBlockEvent.aik()) {
         callbackInfoReturnable.setReturnValue(false);
         callbackInfoReturnable.cancel();
      }
   }

   @Inject(method = "interactItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
   public void hookInteractItem(PlayerEntity player2, Hand hand, CallbackInfoReturnable callbackInfoReturnable) {
      if (this.gameMode != GameMode.SPECTATOR) {
         InteractItemEvent interactItemEvent = new InteractItemEvent(hand);
         EventBusImpl.INSTANCE.rC(interactItemEvent);
         if (interactItemEvent.aik()) {
            callbackInfoReturnable.setReturnValue(ActionResult.PASS);
         }
      }
   }

   @Redirect(method = "interactBlockInternal(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;shouldCancelInteraction()Z"))
   private boolean hookRedirectInteractBlockInternal$shouldCancelInteraction(ClientPlayerEntity player2) {
      RedirectInteractBlockInternal_shouldCancelInteractionEvent redirectInteractBlockInternalshouldCancelInteractionEvent = new RedirectInteractBlockInternal_shouldCancelInteractionEvent();
      EventBusImpl.INSTANCE.rC(redirectInteractBlockInternalshouldCancelInteractionEvent);
      return player2.isSneaking() || redirectInteractBlockInternalshouldCancelInteractionEvent.aik();
   }

   @WrapOperation(
      method = "interactBlockInternal(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;")
   )
   private ItemStack hookRedirectInteractBlockInternal$getStackInHand(ClientPlayerEntity player2, Hand hand, Operation operation) {
      if (hand.equals(Hand.OFF_HAND)) {
         return (ItemStack)operation.call(new Object[]{player2, hand});
      }

      UseEvent useEvent = new UseEvent();
      EventBusImpl.INSTANCE.rC(useEvent);
      return useEvent.aik() ? useEvent.getStack() : (ItemStack)operation.call(new Object[]{player2, hand});
   }

   @WrapOperation(method = "interactBlockInternal(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
   private boolean hookRedirectInteractBlockInternal$getMainHandStack(ItemStack stack2, Operation operation) {
      UseEvent useEvent = new UseEvent();
      EventBusImpl.INSTANCE.rC(useEvent);
      return useEvent.aik() ? useEvent.getStack().isEmpty() : (Boolean)operation.call(new Object[]{stack2});
   }

   @Inject(method = "syncSelectedSlot()V", at = @At("HEAD"), cancellable = true)
   private void hookSyncSelectedSlot(CallbackInfo callbackInfo) {
      SyncSelectedSlotEvent syncSelectedSlotEvent = new SyncSelectedSlotEvent();
      EventBusImpl.INSTANCE.rC(syncSelectedSlotEvent);
      if (syncSelectedSlotEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
