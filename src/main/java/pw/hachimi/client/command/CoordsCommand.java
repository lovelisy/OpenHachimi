package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.player.PlayerListEntryPlayerHelper;

public class CoordsCommand extends ClientCommand {
   public static boolean ai;

   public CoordsCommand() {
      super("Coords", "Sends player coordinates", aep("coords"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      LiteralArgumentBuilder temp = (LiteralArgumentBuilder)literalArgumentBuilder.then(aer("player", PlayerListEntryPlayerHelper.aic()).executes(this::agj));
      Command command2 = this::agi;
      temp.executes(command2);
   }

   public String agh() {
      DecimalFormat decimalFormat = d();
      double d2 = client.player.getX();
      double d3 = client.player.getY();
      double d4 = client.player.getZ();
      return decimalFormat.format(d2) + ", " + decimalFormat.format(d3) + ", " + decimalFormat.format(d4);
   }

   private int agi(CommandContext commandContext) throws CommandSyntaxException {
      Clipboard clipboard2 = Toolkit.getDefaultToolkit().getSystemClipboard();
      StringSelection stringSelection = new StringSelection(this.agh());
      clipboard2.setContents(stringSelection, (ClipboardOwner)null);
      ChatUtils.UB("Copied player coordinates to clipboard!");
      return 1;
   }

   private int agj(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UI("w " + PlayerListEntryPlayerHelper.aid(commandContext, "player") + " " + this.agh());
      return 1;
   }

   private static DecimalFormat d() {
      return new DecimalFormat("0.0");
   }
}
