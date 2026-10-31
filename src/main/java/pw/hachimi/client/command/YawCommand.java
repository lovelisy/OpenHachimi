package pw.hachimi.client.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.utils.chat.ChatUtils;

public class YawCommand extends ClientCommand {
   public static boolean p;

   public YawCommand() {
      super("Yaw", "Sets the player yaw", aep("yaw"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.then(aer("yaw", FloatArgumentType.floatArg()).executes(YawCommand::cQ));
   }

   private static int cQ(CommandContext commandContext) throws CommandSyntaxException {
      float f = FloatArgumentType.getFloat(commandContext, "yaw");
      client.player.setYaw(f);
      client.player.setHeadYaw(f);
      client.player.setBodyYaw(f);
      ChatUtils.UB("Set player yaw to §s" + f);
      return 1;
   }
}
