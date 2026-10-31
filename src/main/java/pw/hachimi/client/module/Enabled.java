package pw.hachimi.client.module;

import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.setting.BooleanSetting2;
import pw.hachimi.client.setting.BooleanSetting3;
import pw.hachimi.client.setting.BooleanSetting6;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.ValueSetting;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.chat.ChatUtils;

public class Enabled extends ClientModule {
   final Setting settingSetting1_1;
   final Setting settingSetting2_1;
   final Setting settingSetting3;
   final Setting settingSetting4;
   final String[] texts;
   final String[] texts2;
   public static boolean av;

   public Enabled(String str, ModuleCategory moduleCategory, String[] strs, String[] strs2) {
      super(str, "", moduleCategory);
      this.texts = strs;
      this.texts2 = strs2;
      this.settingSetting1_1 = this.registerSetting(new BooleanSetting3("Enabled", "The module enabled state", false));
      this.settingSetting2_1 = this.registerSetting(new BooleanSetting6("Hidden", "The hidden state of the module in the arraylist", false, Enabled::isSettingSetting2Visible));
      this.settingSetting3 = this.registerSetting(new BooleanSetting2("Notify", "Notifies you when the module is toggled in chat", true, Enabled::isSettingSetting3Visible));
      ValueSetting valueSetting = new ValueSetting("Keybind", "The keybind to toggle the module", b(this.Kn(), this::awa));
      this.settingSetting4 = this.registerSetting(valueSetting);
   }

   @Override
   public void awd() {
      String[] strs = this.texts;
      int n = strs.length;

      for (int n2 = 0; n2 < n; n2++) {
         String str = strs[n2];
         if (!str.trim().startsWith("#")) {
            try {
               Managers.commandSourceChatScreenHelper
                  .getCommandDispatcher()
                  .execute(
                     Managers.commandSourceChatScreenHelper
                        .getCommandDispatcher()
                        .parse(str.trim(), Managers.commandSourceChatScreenHelper.getCommandSource())
                  );
            } catch (Exception exception) {
               exception.printStackTrace();
            }
         }
      }
   }

   @Override
   public void awe() {
      String[] strs = this.texts2;
      int n = strs.length;

      for (int n2 = 0; n2 < n; n2++) {
         String str = strs[n2];
         if (!str.trim().startsWith("#")) {
            try {
               Managers.commandSourceChatScreenHelper
                  .getCommandDispatcher()
                  .execute(
                     Managers.commandSourceChatScreenHelper
                        .getCommandDispatcher()
                        .parse(str.trim(), Managers.commandSourceChatScreenHelper.getCommandSource())
                  );
            } catch (Exception exception) {
               exception.printStackTrace();
            }
         }
      }
   }

   @Override
   public String OO() {
      return this.getText2();
   }

   @Override
   public String hw() {
      return "";
   }

   @Override
   public void awb() {
      this.settingSetting1_1.aty(true);
      this.awd();
      if ((Boolean)this.settingSetting3.atx() && client.world != null) {
         ChatUtils.UC("§2[+] §f" + this.OO(), this.hashCode());
      }
   }

   @Override
   public void awc() {
      this.settingSetting1_1.aty(false);
      this.awe();
      if ((Boolean)this.settingSetting3.atx() && client.world != null) {
         ChatUtils.UC("§4[-] §f" + this.OO(), this.hashCode());
      }
   }

   @Override
   public boolean WS() {
      return (Boolean)this.settingSetting2_1.atx();
   }

   @Override
   public void WT(boolean flag) {
      this.settingSetting2_1.aty(flag);
   }

   @Override
   public boolean awg() {
      return (Boolean)this.settingSetting1_1.atx();
   }

   @Override
   public void awf(int n) {
      this.settingSetting4.setConfigContainer(this);
      ((KeybindSetting)this.settingSetting4).aT(n);
   }

   @Override
   public UnidentifiedHelper17 awi() {
      return (UnidentifiedHelper17)this.settingSetting4.atx();
   }

   @Override
   public boolean awj() {
      return (Boolean)this.settingSetting3.atx();
   }

   @Override
   public void awk(boolean flag) {
      this.settingSetting3.aty(flag);
   }

   private static Boolean isSettingSetting3Visible() {
      return false;
   }

   private static Boolean isSettingSetting2Visible() {
      return false;
   }

   private static UnidentifiedHelper17 b(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, -1, runnable);
   }
}
