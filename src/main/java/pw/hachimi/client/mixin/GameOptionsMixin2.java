package pw.hachimi.client.mixin;

import com.mojang.serialization.Codec;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.option.SimpleOption.ValidatingIntSliderCallbacks;
import net.minecraft.client.option.SimpleOption.ValueTextGetter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.SetPerspectiveEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(GameOptions.class)
public class GameOptionsMixin2 {
   @Mutable
   @Shadow
   @Final
   private SimpleOption fov;

   @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;load()V", shift = Shift.BEFORE))
   private void hookInit(MinecraftClient client2, File file, CallbackInfo callbackInfo) {
      this.fov = new SimpleOption("options.fov", SimpleOption.emptyTooltip(), (text2, value) -> {
         Integer integer = (Integer)value;
         return switch (integer) {
            case 70 -> GameOptions.getGenericValueText(text2, Text.translatable("options.fov.min"));
            case 110 -> GameOptions.getGenericValueText(text2, Text.translatable("options.fov.max"));
            default -> GameOptions.getGenericValueText(text2, integer);
         };
      }, new ValidatingIntSliderCallbacks(30, 175), Codec.DOUBLE.xmap(
         (Function)(value -> (int)((Double)value * 40.0 + 70.0)),
         (Function)(value -> ((Integer)value - 70.0) / 40.0)
      ), 70, (Consumer)(value -> MinecraftClient.getInstance().worldRenderer.scheduleTerrainUpdate()));
   }

   @Inject(method = "setPerspective(Lnet/minecraft/client/option/Perspective;)V", at = @At("HEAD"))
   private void hookSetPerspective(Perspective perspective, CallbackInfo callbackInfo) {
      SetPerspectiveEvent setPerspectiveEvent = new SetPerspectiveEvent(perspective);
      EventBusImpl.INSTANCE.rC(setPerspectiveEvent);
   }

}
