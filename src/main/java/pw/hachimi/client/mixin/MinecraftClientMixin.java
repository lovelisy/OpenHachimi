package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.MinecraftClient.LoadingContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.GetFramelimitEvent;
import pw.hachimi.client.event.InputEvents_isPreesedEvent;
import pw.hachimi.client.event.OnInitFinishedEvent;
import pw.hachimi.client.event.OnResolutionChangedEvent;
import pw.hachimi.client.event.ReloadResourcesEvent;
import pw.hachimi.client.event.RunEvent;
import pw.hachimi.client.event.SetScreenEvent;
import pw.hachimi.client.event.TickEvent2;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.combat.AttackEvent;
import pw.hachimi.client.event.combat.DoAttackEvent;
import pw.hachimi.client.event.player.DoItemUseEvent;
import pw.hachimi.client.event.player.IsUsingItemEvent;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.event.render.HasOutlineEvent;
import pw.hachimi.client.model.player.UnidentifiedInterface5;
import pw.hachimi.client.utils.UnidentifiedHelper;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements UnidentifiedInterface5 {
   @Unique
   private static final List i = new ArrayList();
   @Shadow
   public ClientWorld world;
   @Shadow
   public ClientPlayerEntity player;
   @Shadow
   @Nullable
   public ClientPlayerInteractionManager interactionManager;
   @Final
   @Shadow
   public GameOptions options;
   @Shadow
   @Nullable
   public Screen currentScreen;
   @Unique
   private boolean r;
   @Unique
   private boolean v;
   @Unique
   private boolean s;
   @Unique
   private boolean o;
   @Shadow
   @Final
   private Window window;

   @Shadow
   protected abstract void doItemUse();

   @Shadow
   protected abstract boolean doAttack();

   @Shadow
   public abstract void updateWindowTitle();

   @Override
   public void Bu() {
      this.r = true;
   }

   @Override
   public void Bv() {
      this.v = true;
   }

   @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/KeybindTranslations;setFactory(Ljava/util/function/Function;)V"))
   private void hookInit(RunArgs runArgs, CallbackInfo callbackInfo) {
      Bootstrap.NU();
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   private void onInit(CallbackInfo callbackInfo) {
      Bootstrap.NT();
   }

   @Inject(method = "run()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;render(Z)V", shift = Shift.BEFORE))
   private void hookRun(CallbackInfo callbackInfo) {
      RunEvent runEvent = new RunEvent();
      EventBusImpl.INSTANCE.rC(runEvent);
   }

   @Inject(method = "tick()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;overlay:Lnet/minecraft/client/gui/screen/Overlay;", opcode = 180))
   private void hookTick(CallbackInfo callbackInfo) {
      TickEvent2 tickEvent2 = new TickEvent2();
      EventBusImpl.INSTANCE.rC(tickEvent2);
   }

   @Inject(method = "onFinishedLoading(Lnet/minecraft/client/MinecraftClient$LoadingContext;)V", at = @At("HEAD"))
   private void hookOnInitFinished(LoadingContext loadingContext, CallbackInfo callbackInfo) {
      OnInitFinishedEvent onInitFinishedEvent = new OnInitFinishedEvent();
      EventBusImpl.INSTANCE.rC(onInitFinishedEvent);
   }

   @Inject(method = "getFramerateLimit()I", at = @At("HEAD"), cancellable = true)
   private void hookGetFramelimit(CallbackInfoReturnable callbackInfoReturnable) {
      if (this.world == null) {
         callbackInfoReturnable.setReturnValue(120);
      } else {
         GetFramelimitEvent getFramelimitEvent = new GetFramelimitEvent();
         EventBusImpl.INSTANCE.rC(getFramelimitEvent);
         if (getFramelimitEvent.aik()) {
            callbackInfoReturnable.cancel();
            callbackInfoReturnable.setReturnValue(getFramelimitEvent.arH());
         }
      }
   }

   @Inject(method = "tick()V", at = @At("HEAD"))
   private void hookTickPre(CallbackInfo callbackInfo) {
      this.s = false;
      this.o = false;
      if (this.player != null && this.world != null) {
         TickPreEvent tickPreEvent = new TickPreEvent();
         tickPreEvent.aqo(EventPhase.PRE);
         EventBusImpl.INSTANCE.rC(tickPreEvent);
         this.updateDeathEvent();
      }

      if (this.interactionManager != null) {
         if (this.r && !this.s) {
            this.doAttack();
         }

         if (this.v && !this.o) {
            this.doItemUse();
         }

         this.r = false;
         this.v = false;
      }
   }

   @Inject(method = "tick()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;world:Lnet/minecraft/client/world/ClientWorld;", ordinal = 5))
   public void onSelectSlot(CallbackInfo callbackInfo) {
      EventBusImpl.INSTANCE.rB(new UnidentifiedHelper());
   }

   @Inject(method = "tick()V", at = @At("TAIL"))
   private void hookTickPost(CallbackInfo callbackInfo) {
      if (this.player != null && this.world != null) {
         TickPreEvent tickPreEvent = new TickPreEvent();
         tickPreEvent.aqo(EventPhase.POST);
         EventBusImpl.INSTANCE.rC(tickPreEvent);
         this.updateDeathEvent();
      }
   }

   @Unique
   private void updateDeathEvent() {
      for (Entity entity2 : this.world.getEntities()) {
         if (entity2 instanceof LivingEntity) {
            if (((LivingEntity)entity2).isDead() && !i.contains(((LivingEntity)entity2).getId())) {
               UpdateDeathEvent updateDeathEvent2 = new UpdateDeathEvent((LivingEntity)entity2);
               EventBusImpl.INSTANCE.rC(updateDeathEvent2);
               i.add(((LivingEntity)entity2).getId());
            } else if (!((LivingEntity)entity2).isDead()) {
               i.remove(Integer.valueOf(((LivingEntity)entity2).getId()));
            }
         }
      }
   }

   @Inject(method = "setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("TAIL"))
   private void hookSetScreen(Screen screen2, CallbackInfo callbackInfo) {
      SetScreenEvent setScreenEvent = new SetScreenEvent(screen2);
      EventBusImpl.INSTANCE.rC(setScreenEvent);
   }

   @Inject(method = "doItemUse()V", at = @At("HEAD"), cancellable = true)
   private void hookDoItemUse(CallbackInfo callbackInfo) {
      this.o = true;
      DoItemUseEvent doItemUseEvent = new DoItemUseEvent();
      EventBusImpl.INSTANCE.rC(doItemUseEvent);
      if (doItemUseEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @WrapOperation(method = "handleInputEvents()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z", ordinal = 2))
   public boolean handleInputEvents$isPreesed(KeyBinding keyBinding, Operation operation) {
      boolean flag = (Boolean)operation.call(new Object[]{keyBinding});
      if (!flag) {
         InputEvents_isPreesedEvent inputEventsisPreesedEvent = new InputEvents_isPreesedEvent();
         EventBusImpl.INSTANCE.rC(inputEventsisPreesedEvent);
         if (inputEventsisPreesedEvent.aik()) {
            return true;
         }
      }

      return flag;
   }

   @WrapOperation(method = "handleInputEvents()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0))
   public boolean hookAttack(ClientPlayerEntity player2, Operation operation) {
      boolean flag = (Boolean)operation.call(new Object[]{player2});
      if (flag) {
         AttackEvent attackEvent = new AttackEvent();
         EventBusImpl.INSTANCE.rC(attackEvent);
         if (attackEvent.aik()) {
            if (!this.options.useKey.isPressed()) {
               this.interactionManager.stopUsingItem(this.player);
            }

            while (this.options.useKey.wasPressed()) {
            }

            return false;
         }
      }

      return flag;
   }

   @Inject(method = "doAttack()Z", at = @At("HEAD"), cancellable = true)
   private void hookDoAttack(CallbackInfoReturnable callbackInfoReturnable) {
      this.s = true;
      DoAttackEvent doAttackEvent = new DoAttackEvent();
      EventBusImpl.INSTANCE.rC(doAttackEvent);
      if (doAttackEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(false);
      }
   }

   @Redirect(method = "handleBlockBreaking(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
   private boolean hookIsUsingItem(ClientPlayerEntity player2) {
      IsUsingItemEvent isUsingItemEvent = new IsUsingItemEvent();
      EventBusImpl.INSTANCE.rC(isUsingItemEvent);
      return !isUsingItemEvent.aik() && player2.isUsingItem();
   }

   @WrapOperation(method = "doItemUse()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"))
   private boolean hookIsBreakingBlock(ClientPlayerInteractionManager interaction, Operation operation) {
      IsUsingItemEvent isUsingItemEvent = new IsUsingItemEvent();
      EventBusImpl.INSTANCE.rC(isUsingItemEvent);
      return !isUsingItemEvent.aik() && (Boolean)operation.call(new Object[]{interaction});
   }

   @Inject(method = "hasOutline(Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
   private void hookHasOutline(Entity entity2, CallbackInfoReturnable callbackInfoReturnable) {
      HasOutlineEvent hasOutlineEvent = new HasOutlineEvent(entity2);
      EventBusImpl.INSTANCE.rC(hasOutlineEvent);
      if (hasOutlineEvent.aik()) {
         callbackInfoReturnable.setReturnValue(false);
      }
   }

   @Inject(method = "reloadResources(ZLnet/minecraft/client/MinecraftClient$LoadingContext;)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"))
   private void hookReloadResources(boolean flag, LoadingContext loadingContext, CallbackInfoReturnable callbackInfoReturnable) {
      ReloadResourcesEvent reloadResourcesEvent = new ReloadResourcesEvent();
      EventBusImpl.INSTANCE.rC(reloadResourcesEvent);
   }

   @Inject(method = "onResolutionChanged()V", at = @At("TAIL"))
   private void hookOnResolutionChanged(CallbackInfo callbackInfo) {
      OnResolutionChangedEvent onResolutionChangedEvent = new OnResolutionChangedEvent(this.window);
      EventBusImpl.INSTANCE.rC(onResolutionChangedEvent);
   }
}
