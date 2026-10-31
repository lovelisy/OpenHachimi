package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.chat.ChatUtils;

public class HClipCommand extends ClientCommand {
   public static boolean x;

   public HClipCommand() {
      super("HClip", "Horizontally clips the player", aep("hclip"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      LiteralArgumentBuilder temp = (LiteralArgumentBuilder)literalArgumentBuilder.then(aer("distance", DoubleArgumentType.doubleArg()).executes(HClipCommand::FD));
      Command command2 = HClipCommand::FC;
      temp.executes(command2);
   }

   private static int FC(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide distance!");
      return 1;
   }

   private static int FD(CommandContext commandContext) throws CommandSyntaxException {
      double d = DoubleArgumentType.getDouble(commandContext, "distance");
      double d2 = Math.toRadians(client.player.getYaw() + 90.0F);
      double d3 = Math.cos(d2) * d;
      double d4 = Math.sin(d2) * d;
      Managers.vec3dPlayerHelper.BJ(d3, d4);
      ChatUtils.UB("Horizontally clipped §s" + d + "§f blocks");
      return 1;
   }
}
