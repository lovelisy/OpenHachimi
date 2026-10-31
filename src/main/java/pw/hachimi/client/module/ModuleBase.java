package pw.hachimi.client.module;

import java.text.DecimalFormat;
import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.manager.TextManager;
import pw.hachimi.client.utils.chat.ChatUtils;

public class ModuleBase extends ConfigContainer {
   public static final DecimalFormat bo = new DecimalFormat("0.0");
   public static final String bp = "%s-module";
   private final String text2_1;
   private final String text3;
   private final ModuleCategory moduleCategory;
   private static String[] texts;

   public ModuleBase(String str, String str2, ModuleCategory moduleCategory2) {
      super(str);
      this.text3 = str2;
      this.moduleCategory = moduleCategory2;
      Object[] objs = new Object[]{str.toLowerCase()};
      this.text2_1 = String.format("%s-module", objs);
   }

   public void hr(String str) {
      ChatUtils.UG("§s[%s]§f %s", this.text2_1, str);
   }

   protected void hs(String str, Object... value) {
      this.hr(String.format(str, value));
   }

   public void ht(String str) {
      ChatUtils.UG("§s[%s]§c %s", this.text2_1, str);
   }

   public void hu(String str, Object... value) {
      this.ht(String.format(str, value));
   }

   @Override
   public String Kn() {
      return this.text2_1;
   }

   public String getText3() {
      return this.text3;
   }

   public String hw() {
      return TextManager.aip(this.Kn() + ".description");
   }

   public ModuleCategory getModuleCategory() {
      return this.moduleCategory;
   }

   public String hy() {
      return "";
   }

   static {
      b(new String[2]);
   }

   public static void b(String[] strs) {
      texts = strs;
   }

   public static String[] A() {
      return texts;
   }
}
