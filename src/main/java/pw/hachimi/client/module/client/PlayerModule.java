package pw.hachimi.client.module.client;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.Mc;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.Enabled;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting5;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.config.FileHelper;
import pw.hachimi.client.utils.render.DrawMatrixHelper;
import java.util.ArrayList;

public class PlayerModule extends ModuleBase {
   public static final List a_ = new ArrayList();
   public static final HashMap b_ = new HashMap();
   private static final long a = 0L;
   private static final String[] b = null;
   private static final String[] d = null;
   private static final Map e = new HashMap();

   public PlayerModule() {
      super("Scripts", "Manage your scripts", ModuleCategory.CLIENT);
      this.registerSetting(b("Reload", "Reload scripts from directory", PlayerModule::dh));
   }

   public static void db(short s, int n) {
      if (s >= 0) {
         dd();
         dc();
         if (s >= 0 && n > 0) {
            return;
         }
      }
   }

   public static void dc() {
      try {
         File file = Bootstrap.clientPaths.file2;
         if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles(File::isFile);
            int n = files.length;

            for (int n2 = 0; n2 < n; n2++) {
               File file2 = files[n2];
               if (file2.getName().endsWith(".script")) {
                  try {
                     String str = Files.readString(file2.toPath());
                     String str2 = df(str, "func onEnable {");
                     String str3 = df(str, "func onDisable {");
                     String[] strs = str2.split("\n");
                     String[] strs2 = str3.split("\n");
                     String str4 = file2.getName();
                     Enabled enabled = new Enabled(str4.substring(0, str4.lastIndexOf(".")), ModuleCategory.CLIENT, strs, strs2);
                     Managers.moduleManager.XJ(enabled);
                     Managers.playerScreenManager.akr(enabled.awi());
                     CategoryPanel categoryPanel = de();
                     FileHelper fileHelper = new FileHelper(Bootstrap.clientPaths.path.resolve("Modules"), enabled);
                     Bootstrap.clientPaths.getSet().add(fileHelper);
                     b_.put(enabled, fileHelper);
                     a_.add(enabled);
                     de().getList().add(new DrawMatrixHelper(enabled, categoryPanel, 0.0F, 0.0F));
                  } catch (Exception exception) {
                     Bootstrap.Ob("Failed load script " + file2.getName() + "!");
                     if (Mc.client.player != null) {
                        ChatUtils.UL("Failed load script " + file2.getName() + "!");
                     }

                     exception.printStackTrace();
                  }
               }
            }
         }
      } catch (Exception exception2) {
         Bootstrap.Ob("Failed load scripts!");
         if (Mc.client.player != null) {
            ChatUtils.UL("Failed load scripts!");
         }

         exception2.printStackTrace();
      }
   }

   public static void dd() {
      Iterator iterator2 = a_.iterator();

      while (iterator2.hasNext()) {
         ClientModule clientModule = (ClientModule)iterator2.next();
         Managers.moduleManager.XK(clientModule);
         Managers.playerScreenManager.aks(clientModule.awi());
         List temp = de().getList();
         final ClientModule clientModule2 = clientModule;
         Predicate predicate = new Predicate() {
            @Override
            public boolean test(Object obj) {
               return PlayerModule.dg(clientModule2, (DrawMatrixHelper)obj);
            }
         };
         temp.removeIf(predicate);
         Set set = Bootstrap.clientPaths.getSet();
         Object obj2 = b_.get(clientModule);
         set.remove(obj2);
      }

      a_.clear();
      b_.clear();
   }

   private static CategoryPanel de() {
      return UI.drawScreenScreen == null ? null : (CategoryPanel)UI.drawScreenScreen.getList().getLast();
   }

   public static String df(String str, String str2) {
      int n = str.indexOf(str2);
      if (n == -1) {
         return null;
      }

      n += str2.length();
      int n2 = str.indexOf("}", n);
      return n2 == -1 ? null : str.substring(n, n2).trim();
   }

   static boolean dg(ClientModule clientModule, DrawMatrixHelper drawMatrixHelper) {
      boolean flag;
      if (drawMatrixHelper.getModuleBase() == clientModule) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static void dh() {
      dd();
      dc();
   }

   private static BooleanSetting5 b(String str, String str2, Runnable runnable) {
      return new BooleanSetting5(str, str2, runnable);
   }
}
