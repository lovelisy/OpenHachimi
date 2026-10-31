package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.UnidentifiedHelper19;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.config.UnidentifiedHelper14;
import pw.hachimi.client.utils.world.RegistryKeyWorldUtils;

public class WaypointCommand extends ClientCommand {
   public static boolean u;

   public WaypointCommand() {
      super("Waypoint", "Adds/Removes a waypoint", aep("waypoint"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      RequiredArgumentBuilder temp = aer("add/del", StringArgumentType.string()).suggests(aes("add", "del", "delete", "remove"));
      RequiredArgumentBuilder requiredArgumentBuilder = aer("waypoint", StringArgumentType.string());
      temp = temp;
      RequiredArgumentBuilder temp2 = requiredArgumentBuilder;
      RequiredArgumentBuilder temp3 = aer("x", DoubleArgumentType.doubleArg());
      RequiredArgumentBuilder temp4 = aer("y", DoubleArgumentType.doubleArg());
      RequiredArgumentBuilder temp5 = aer("z", DoubleArgumentType.doubleArg());
      Command command2 = WaypointCommand::KB;
      requiredArgumentBuilder = temp5;
      RequiredArgumentBuilder requiredArgumentBuilder2 = temp4;
      RequiredArgumentBuilder requiredArgumentBuilder3 = temp3;
      RequiredArgumentBuilder requiredArgumentBuilder4 = temp2;
      RequiredArgumentBuilder requiredArgumentBuilder5 = temp;
      ArgumentBuilder argumentBuilder = requiredArgumentBuilder.executes(command2);
      temp = (RequiredArgumentBuilder)requiredArgumentBuilder4.then(requiredArgumentBuilder3.then(requiredArgumentBuilder2.then(argumentBuilder)));
      command2 = WaypointCommand::KA;
      ArgumentBuilder argumentBuilder2 = temp.executes(command2);
      temp = (RequiredArgumentBuilder)requiredArgumentBuilder5.then(argumentBuilder2);
      command2 = WaypointCommand::Kz;
      ArgumentBuilder argumentBuilder3 = temp.executes(command2);
      LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)literalArgumentBuilder.then(argumentBuilder3);
      command2 = this::Ky;
      literalArgumentBuilder2.executes(command2);
   }

   private int Ky(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Invalid usage! Usage: " + this.aex());
      return 1;
   }

   private static int Kz(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide waypoint name!");
      return 1;
   }

   private static int KA(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "waypoint");
      String str2 = StringArgumentType.getString(commandContext, "add/del");
      if (str2.equalsIgnoreCase("remove") || str2.equalsIgnoreCase("del") || str2.equalsIgnoreCase("delete")) {
         if (!Managers.unidentifiedHelper19.akV(str)) {
            ChatUtils.UL("Waypoint does not exist!");
            return 0;
         }

         ChatUtils.UB("Removed waypoint with name §c" + str);
         Managers.unidentifiedHelper19.akU(str);
      }

      return 1;
   }

   private static int KB(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "waypoint");
      double d = DoubleArgumentType.getDouble(commandContext, "x");
      double d2 = DoubleArgumentType.getDouble(commandContext, "y");
      double d3 = DoubleArgumentType.getDouble(commandContext, "z");
      if (StringArgumentType.getString(commandContext, "add/del").equalsIgnoreCase("add")) {
         if (Managers.unidentifiedHelper19.akV(str)) {
            ChatUtils.UL("Waypoint already exist!");
            return 0;
         }

         ChatUtils.UB("Added waypoint with name §s" + str);
         UnidentifiedHelper19 temp = Managers.unidentifiedHelper19;
         boolean flag = client.isInSingleplayer();
         UnidentifiedHelper19 unidentifiedHelper192 = temp;
         String str2;
         if (flag) {
            str2 = "Singleplayer";
         } else {
            str2 = Managers.serverManager.jb();
         }

         unidentifiedHelper192.akQ(new UnidentifiedHelper14(str, str2, RegistryKeyWorldUtils.OS(), d, d2, d3));
      }

      return 1;
   }
}
