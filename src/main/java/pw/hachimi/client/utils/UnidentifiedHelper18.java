package pw.hachimi.client.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.setting.Setting;

public class UnidentifiedHelper18 implements ArgumentType {
   private final ModuleBase moduleBase;

   private UnidentifiedHelper18(ModuleBase moduleBase2) {
      this.moduleBase = moduleBase2;
   }

   public static UnidentifiedHelper18 akN(ModuleBase moduleBase2) {
      return new UnidentifiedHelper18(moduleBase2);
   }

   public static Setting akO(CommandContext commandContext) {
      return (Setting)commandContext.getArgument("setting", Setting.class);
   }

   public Setting akP(long l, StringReader stringReader) throws CommandSyntaxException {
      String str = stringReader.readString();
      Iterator iterator2 = this.moduleBase.OQ().iterator();

      while (iterator2.hasNext()) {
         Setting setting2 = (Setting)iterator2.next();
         if (!setting2.getText().equalsIgnoreCase("Enabled")
            && !setting2.getText().equalsIgnoreCase("Keybind")
            && !setting2.getText().equalsIgnoreCase("Hidden")
            && !setting2.getText().equalsIgnoreCase("Notify")
            && setting2.getText().equalsIgnoreCase(str)) {
            if (setting2 != null) {
               return setting2;
            }
            break;
         }
      }

      throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(stringReader, null);
   }

   public CompletableFuture listSuggestions(CommandContext commandContext, SuggestionsBuilder suggestionsBuilder) {
      Iterator iterator2 = this.moduleBase.OQ().iterator();

      while (iterator2.hasNext()) {
         Setting setting2 = (Setting)iterator2.next();
         if (!setting2.getText().equalsIgnoreCase("Enabled") && !setting2.getText().equalsIgnoreCase("Keybind") && !setting2.getText().equalsIgnoreCase("Hidden")) {
            suggestionsBuilder.suggest(setting2.getText().toLowerCase());
         }
      }

      return suggestionsBuilder.buildFuture();
   }

   public Object parse(StringReader stringReader) throws CommandSyntaxException {
      return this.akP(27655861953593L, stringReader);
   }
}
