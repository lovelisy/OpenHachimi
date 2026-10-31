package pw.hachimi.client.mixin;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatInputSuggestor.SuggestionWindow;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pw.hachimi.client.event.RefreshEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestorMixin {
   @Shadow
   @Final
   TextFieldWidget textField;
   @Shadow
   boolean completingSuggestions;
   @Shadow
   private ParseResults parse;
   @Shadow
   @Nullable
   private SuggestionWindow/* @Nullable */ window;
   @Shadow
   @Nullable
   private CompletableFuture pendingSuggestions;

   @Shadow
   protected abstract void showCommandSuggestions();

   @Inject(
      method = "refresh()V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false),
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void hookRefresh(CallbackInfo callbackInfo, String str, StringReader stringReader) {
      RefreshEvent refreshEvent = new RefreshEvent();
      EventBusImpl.INSTANCE.rC(refreshEvent);
      if (stringReader.getString().startsWith(refreshEvent.getText(), stringReader.getCursor())) {
         stringReader.setCursor(stringReader.getCursor() + 1);
         if (this.parse == null) {
            this.parse = refreshEvent.getCommandDispatcher().parse(stringReader, refreshEvent.getCommandSource());
         }

         int n = this.textField.getCursor();
         if (n >= 1 && (this.window == null || !this.completingSuggestions)) {
            this.pendingSuggestions = refreshEvent.getCommandDispatcher().getCompletionSuggestions(this.parse, n);
            this.pendingSuggestions.thenRun(() -> {
               if (this.pendingSuggestions.isDone()) {
                  this.showCommandSuggestions();
               }
            });
         }

         callbackInfo.cancel();
      }
   }
}
