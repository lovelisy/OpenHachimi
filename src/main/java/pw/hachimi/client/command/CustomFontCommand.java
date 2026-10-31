package pw.hachimi.client.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.render.FontLoader;

public class CustomFontCommand extends ClientCommand {
   public static boolean ae;

   public CustomFontCommand() {
      super("CustomFont", "Sets the client font", aep("customfont"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.then(aer("font name", StringArgumentType.string()).executes(CustomFontCommand::MI));
   }

   private static int MI(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "font name");
      if (str.equalsIgnoreCase("default")) {
         FontLoader.text = "assets/hachimiclient/font/default.ttf";
         FontLoader.aos();
         Bootstrap.clientPaths.aog();
         ChatUtils.UB("Set client font to " + str + "!");
         return 0;
      }

      String str2;
      if (!str.endsWith(".ttf") && !str.endsWith(".otf")) {
         str2 = str + ".ttf";
      } else {
         str2 = str;
      }

      String str3 = str2;
      Object[] objs = new Object[]{Bootstrap.clientPaths.getPath().toAbsolutePath() + "/Fonts", str3};
      String str4 = String.format("%s/%s", objs);
      String temp = str4;
      String[] strs = new String[0];
      Path path = Path.of(temp, strs);
      LinkOption[] linkOptions = new LinkOption[0];
      if (!Files.exists(path, linkOptions)) {
         ChatUtils.UL("Could not find font file: " + str3);
         return 0;
      } else {
         FontLoader.text = str4;
         FontLoader.aos();
         Bootstrap.clientPaths.aog();
         ChatUtils.UB("Set client font to " + str + "!");
         return 0;
      }
   }
}
