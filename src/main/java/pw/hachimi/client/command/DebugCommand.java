package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.setting.Setting;

public class DebugCommand extends ClientCommand {
   public DebugCommand() {
      super("Debug", "Only used for dev", aep("debug"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(DebugCommand::LJ);
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   public static void LI() {
      FileWriter fileWriter;
      try {
         String[] strs = new String[]{"src", "main", "resources", "assets", "hachimiclient", "lang", "en_us.json"};
         Path path = Paths.get("..", strs);
         Path temp = path;
         LinkOption[] linkOptions = new LinkOption[0];
         if (!Files.exists(temp, linkOptions)) {
            return;
         }

         Files.deleteIfExists(path);
         fileWriter = a(path.toFile());
         fileWriter.write("{\n  \"client-name\": \"Hachimi\"");
         Iterator iterator2 = Managers.moduleManager.XM().iterator();

         while (iterator2.hasNext()) {
            ModuleBase moduleBase = (ModuleBase)iterator2.next();
            String str = moduleBase.Kn();
            String str2 = ",\n  \"" + str + "\": \"" + moduleBase.getText2() + "\"";
            fileWriter.write(str2);
            str = moduleBase.Kn();
            str2 = ",\n  \"" + str + ".description\": \"" + moduleBase.getText3() + "\"";
            fileWriter.write(str2);
            Iterator iterator3 = moduleBase.OQ().iterator();

            while (iterator3.hasNext()) {
               Setting setting2 = (Setting)iterator3.next();
               str = setting2.Kn();
               str2 = ",\n  \"" + str + "\": \"" + setting2.getText() + "\"";
               fileWriter.write(str2);
               str = setting2.Kn();
               str2 = ",\n  \"" + str + ".description\": \"" + setting2.getText2() + "\"";
               fileWriter.write(str2);
            }
         }
      } catch (Exception exception2) {
         exception2.printStackTrace();
         return;
      }

      FileWriter fileWriter2 = fileWriter;

      try {
         fileWriter2.write("\n}");
         fileWriter.close();
      } catch (Exception exception3) {
         exception3.printStackTrace();
      }
   }

   private static int LJ(CommandContext commandContext) throws CommandSyntaxException {
      LI();
      return 1;
   }

   private static FileWriter a(File file2) throws IOException {
      return new FileWriter(file2, true);
   }
}
