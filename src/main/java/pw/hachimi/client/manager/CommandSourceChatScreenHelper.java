package pw.hachimi.client.manager;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.Mc;
import pw.hachimi.client.command.BindCommand;
import pw.hachimi.client.command.BindsCommand;
import pw.hachimi.client.command.ClientCommand;
import pw.hachimi.client.command.ConfigCommand;
import pw.hachimi.client.command.ConnectCommand;
import pw.hachimi.client.command.CoordsCommand;
import pw.hachimi.client.command.CustomFontCommand;
import pw.hachimi.client.command.DebugCommand;
import pw.hachimi.client.command.DisableAllCommand;
import pw.hachimi.client.command.DisableCommand;
import pw.hachimi.client.command.DrawnCommand;
import pw.hachimi.client.command.EnableCommand;
import pw.hachimi.client.command.FriendCommand;
import pw.hachimi.client.command.HClipCommand;
import pw.hachimi.client.command.HelpCommand;
import pw.hachimi.client.command.HideAllCommand;
import pw.hachimi.client.command.KitCommand;
import pw.hachimi.client.command.LeaveCommand;
import pw.hachimi.client.command.LoadCommand;
import pw.hachimi.client.command.MacroCommand;
import pw.hachimi.client.command.ModulesCommand;
import pw.hachimi.client.command.NbtCommand;
import pw.hachimi.client.command.NotifyCommand;
import pw.hachimi.client.command.OpenFolderCommand;
import pw.hachimi.client.command.PeekCommand;
import pw.hachimi.client.command.PrefixCommand;
import pw.hachimi.client.command.QueueCommand;
import pw.hachimi.client.command.ReloadSoundCommand;
import pw.hachimi.client.command.ResetCommand;
import pw.hachimi.client.command.ResetGuiCommand;
import pw.hachimi.client.command.SaveCommand;
import pw.hachimi.client.command.SettingCommand;
import pw.hachimi.client.command.SkinGrabCommand;
import pw.hachimi.client.command.StatsCommand;
import pw.hachimi.client.command.ToggleCommand;
import pw.hachimi.client.command.VClipCommand;
import pw.hachimi.client.command.VanishCommand;
import pw.hachimi.client.command.WaypointCommand;
import pw.hachimi.client.command.YawCommand;
import pw.hachimi.client.event.OnKeyEvent;
import pw.hachimi.client.event.RefreshEvent;
import pw.hachimi.client.event.SetScreenEvent;
import pw.hachimi.client.event.network.SendMessageEvent;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class CommandSourceChatScreenHelper {
   private final List list = new ArrayList();
   private final CommandDispatcher commandDispatcher = new CommandDispatcher();
   private final CommandSource commandSource = a(Mc.client);
   String text;
   private String text2 = ".";
   private int f = 46;
   private static final String c = "Registered {} commands!";

   public CommandSourceChatScreenHelper() {
      EventBusImpl.INSTANCE.rD(this);
      this.adE(
         new BindCommand(),
         new BindsCommand(),
         new ConfigCommand(),
         new ConnectCommand(),
         new CoordsCommand(),
         new DisableAllCommand(),
         new DisableCommand(),
         new EnableCommand(),
         new DrawnCommand(),
         new DebugCommand(),
         new CustomFontCommand(),
         new FriendCommand(),
         new HClipCommand(),
         new HelpCommand(),
         new HideAllCommand(),
         new KitCommand(),
         new LeaveCommand(),
         new LoadCommand(),
         new MacroCommand(),
         new ModulesCommand(),
         new NbtCommand(),
         new NotifyCommand(),
         new OpenFolderCommand(),
         new PeekCommand(),
         new PrefixCommand(),
         new QueueCommand(),
         new ResetCommand(),
         new ResetGuiCommand(),
         new SaveCommand(),
         new ReloadSoundCommand(),
         new SkinGrabCommand(),
         new StatsCommand(),
         new ToggleCommand(),
         new VanishCommand(),
         new VClipCommand(),
         new WaypointCommand(),
         new YawCommand()
      );
      Iterator iterator2 = Managers.moduleManager.XM().iterator();

      while (iterator2.hasNext()) {
         ModuleBase moduleBase = (ModuleBase)iterator2.next();
         if (!(moduleBase instanceof RenderModule)) {
            this.adF(new SettingCommand(moduleBase));
         }
      }

      Bootstrap.Oa("Registered {} commands!", this.list.size());
      iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         ClientCommand clientCommand = (ClientCommand)iterator2.next();
         Iterator iterator3 = clientCommand.getList().iterator();

         while (iterator3.hasNext()) {
            LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)iterator3.next();
            clientCommand.aet(literalArgumentBuilder);
            this.commandDispatcher.register(literalArgumentBuilder);
         }
      }
   }

   @EventListener(priority = 999)
   public void onSendMessage(SendMessageEvent sendMessageEvent) {
      String str = sendMessageEvent.auZ().trim();
      String temp = str;
      String str2 = this.text2;
      if (temp.startsWith(str2)) {
         String str3 = str.substring(1);
         sendMessageEvent.aim();
         Mc.client.inGameHud.getChatHud().addToMessageHistory(str);
         if (Mc.client.currentScreen instanceof ChatScreen) {
            this.text = str3;
            return;
         }

         CommandSourceChatScreenHelper commandSourceChatScreenHelper = this;

         try {
            commandSourceChatScreenHelper.commandDispatcher.execute(this.commandDispatcher.parse(str3, this.commandSource));
         } catch (Exception exception) {
         }
      }
   }

   @EventListener
   public void onSetScreen(SetScreenEvent setScreenEvent) {
      if (this.text != null) {
         String str = this.text;
         this.text = null;
         CommandSourceChatScreenHelper temp = this;

         try {
            temp.commandDispatcher.execute(this.commandDispatcher.parse(str, this.commandSource));
         } catch (Exception exception) {
         }
      }
   }

   @EventListener
   public void onOnKey(OnKeyEvent onKeyEvent) {
      if (onKeyEvent.KD() == 1 && onKeyEvent.KC() == this.f && Mc.client.currentScreen == null) {
         onKeyEvent.aim();
         MinecraftClient temp = Mc.client;
         ChatScreen chatScreen = a();
         temp.setScreen((Screen)chatScreen);
      }
   }

   @EventListener
   public void onRefresh(RefreshEvent refreshEvent) {
      refreshEvent.setText(this.text2);
      refreshEvent.setCommandDispatcher(this.commandDispatcher);
      refreshEvent.setCommandSource(this.commandSource);
   }

   private void adE(ClientCommand... value) {
      for (ClientCommand clientCommand : value) {
         this.adF(clientCommand);
      }
   }

   private void adF(ClientCommand clientCommand) {
      if (this.adH(clientCommand.getText()) == null) {
         this.list.add(clientCommand);
      }
   }

   public List getList() {
      return this.list;
   }

   public ClientCommand adH(String str) {
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         ClientCommand clientCommand = (ClientCommand)iterator2.next();
         if (clientCommand.getText().equalsIgnoreCase(str)) {
            return clientCommand;
         }
      }

      return null;
   }

   public String getText2() {
      return this.text2;
   }

   public void adJ(String str, int n) {
      this.text2 = str;
      this.f = n;
   }

   public CommandDispatcher getCommandDispatcher() {
      return this.commandDispatcher;
   }

   public CommandSource getCommandSource() {
      return this.commandSource;
   }

   private static ChatScreen a() {
      return new ChatScreen("");
   }

   private static ClientCommandSource a(MinecraftClient client2) {
      return new ClientCommandSource(null, client2);
   }
}
