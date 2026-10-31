package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.utils.chat.ChatUtils;

public class ResetGuiCommand extends ClientCommand {
   public ResetGuiCommand() {
      super("ResetGui", "Resets gui frames", aep("resetgui"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(ResetGuiCommand::Yv);
   }

   private static int Yv(CommandContext commandContext) throws CommandSyntaxException {
      float f = 15.0F;
      Iterator iterator2 = UI.drawScreenScreen.getList().iterator();

      while (iterator2.hasNext()) {
         CategoryPanel categoryPanel = (CategoryPanel)iterator2.next();
         categoryPanel.vE(f, 15.0F);
         f += categoryPanel.vz() + 2.0F;
      }

      ChatUtils.UB("Reset clickgui screen");
      return 1;
   }
}
