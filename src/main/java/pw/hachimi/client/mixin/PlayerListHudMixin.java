package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.player.CollectPlayerEntriesEvent;
import pw.hachimi.client.event.player.GetPlayerNameEvent;
import pw.hachimi.client.event.render.RenderEvent11;
import pw.hachimi.client.event.render.RenderEvent2;
import pw.hachimi.client.event.render.RenderEvent8;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
   @Shadow
   @Final
   private static Comparator ENTRY_ORDERING;
   @Shadow
   @Final
   private MinecraftClient client;

   @Shadow
   protected abstract List collectPlayerEntries();

   @Shadow
   protected abstract Text applyGameModeFormatting(PlayerListEntry var1, MutableText var2);

   @Inject(method = "getPlayerName(Lnet/minecraft/client/network/PlayerListEntry;)Lnet/minecraft/text/Text;", at = @At("HEAD"), cancellable = true)
   private void hookGetPlayerName(PlayerListEntry playerListEntry, CallbackInfoReturnable callbackInfoReturnable) {
      Text text2;
      if (playerListEntry.getDisplayName() != null) {
         text2 = this.applyGameModeFormatting(playerListEntry, playerListEntry.getDisplayName().copy());
      } else {
         text2 = this.applyGameModeFormatting(playerListEntry, Team.decorateName(playerListEntry.getScoreboardTeam(), Text.literal(playerListEntry.getProfile().getName())));
      }

      GetPlayerNameEvent getPlayerNameEvent = new GetPlayerNameEvent(text2, playerListEntry.getProfile().getId());
      EventBusImpl.INSTANCE.rC(getPlayerNameEvent);
      if (getPlayerNameEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getPlayerNameEvent.getText());
      }
   }

   @WrapOperation(method = "render(Lnet/minecraft/client/gui/DrawContext;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Lnet/minecraft/text/StringVisitable;)I"))
   private int hookRender(TextRenderer textRenderer, StringVisitable stringVisitable, Operation operation) {
      RenderEvent8 renderEvent8 = new RenderEvent8(stringVisitable.getString());
      EventBusImpl.INSTANCE.rC(renderEvent8);
      return renderEvent8.aik() ? textRenderer.getWidth(stringVisitable) + 12 : (Integer)operation.call(new Object[]{textRenderer, stringVisitable});
   }

   @WrapOperation(
      method = "render(Lnet/minecraft/client/gui/DrawContext;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I")
   )
   private int hookRender(DrawContext context, TextRenderer textRenderer, Text text2, int n, int n2, int n3, Operation operation) {
      RenderEvent11 renderEvent11 = new RenderEvent11(text2, context, textRenderer, n, n2, n3);
      EventBusImpl.INSTANCE.rC(renderEvent11);
      if (renderEvent11.aik()) {
         int n4 = n + 12;
         context.drawTextWithShadow(textRenderer, text2, n4, n2, n3);
         return n4;
      } else {
         return (Integer)operation.call(new Object[]{context, textRenderer, text2, n, n2, n3});
      }
   }

   @Inject(method = "collectPlayerEntries()Ljava/util/List;", at = @At("HEAD"), cancellable = true)
   private void hookCollectPlayerEntries(CallbackInfoReturnable callbackInfoReturnable) {
      CollectPlayerEntriesEvent collectPlayerEntriesEvent = new CollectPlayerEntriesEvent();
      EventBusImpl.INSTANCE.rC(collectPlayerEntriesEvent);
      if (collectPlayerEntriesEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(this.client.player.networkHandler.getListedPlayerListEntries().stream().sorted(ENTRY_ORDERING).limit(collectPlayerEntriesEvent.Au()).toList());
      }
   }

   @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", shift = Shift.BEFORE))
   private void hookRender(CallbackInfo callbackInfo, @Local(ordinal = 5) LocalIntRef localIntRef, @Local(ordinal = 6) LocalIntRef localIntRef2) {
      int n = 1;
      int n2;
      int n3 = n2 = this.collectPlayerEntries().size();
      RenderEvent2 renderEvent2 = new RenderEvent2();
      EventBusImpl.INSTANCE.rC(renderEvent2);
      if (renderEvent2.aik()) {
         while (n2 > renderEvent2.gI()) {
            n++;
            n2 = (n3 + n - 1) / n;
         }

         localIntRef.set(n2);
         localIntRef2.set(n);
      }
   }
}
