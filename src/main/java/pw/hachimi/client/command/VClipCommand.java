package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.chat.ChatUtils;

public class VClipCommand extends ClientCommand {
   public VClipCommand() {
      super("VClip", "Vertically clips the player", aep("vclip"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      LiteralArgumentBuilder temp = (LiteralArgumentBuilder)literalArgumentBuilder.then(aer("distance", DoubleArgumentType.doubleArg()).executes(VClipCommand::je));
      Command command2 = VClipCommand::jd;
      temp.executes(command2);
   }

   private static int jd(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide distance!");
      return 1;
   }

   private static int je(CommandContext commandContext) throws CommandSyntaxException {
      double d = DoubleArgumentType.getDouble(commandContext, "distance");
      double d2 = Managers.vec3dPlayerHelper.BS();
      if (Math.abs(d2) != 256.0) {
         Managers.vec3dPlayerHelper.BK(d2 + d);
         ChatUtils.UB("Vertically clipped §s" + d + "§f blocks");
      }

      return 1;
   }
}
