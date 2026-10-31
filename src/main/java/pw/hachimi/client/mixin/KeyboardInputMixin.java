package pw.hachimi.client.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
   @Inject(method = "tick(ZF)V", at = @At("HEAD"), cancellable = true)
   private void hookTick$Pre(boolean flag, float f, CallbackInfo callbackInfo) {
      Tick_PreEvent tickPreEvent = new Tick_PreEvent((Input)(Object)this);
      tickPreEvent.aqo(EventPhase.PRE);
      EventBusImpl.INSTANCE.rC(tickPreEvent);
      if (tickPreEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "tick(ZF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/input/KeyboardInput;sneaking:Z", shift = Shift.AFTER), cancellable = true)
   private void hookTick$Post(boolean flag, float f, CallbackInfo callbackInfo) {
      Tick_PreEvent tickPreEvent = new Tick_PreEvent((Input)(Object)this);
      tickPreEvent.aqo(EventPhase.POST);
      EventBusImpl.INSTANCE.rC(tickPreEvent);
      if (tickPreEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
