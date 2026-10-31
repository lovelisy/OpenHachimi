package pw.hachimi.client.command;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import pw.hachimi.client.model.WindowsLinuxMode;

public class OpenFolderCommandFileUtils {
   public static boolean Dn(URI uri) {
      return Dq(uri.toString()) ? true : Dr(uri);
   }

   public static boolean Do(File file) {
      return Dq(file.getPath()) ? true : Ds(file);
   }

   public static boolean Dp(long l, int n, File file) {
      label41: {
         boolean temp = false;
         if (n >= 0) {
            if (l <= 0L) {
               return false;
            }

            if (Dq(file.getPath())) {
               if (l >= 0L) {
                  if (l > 0L) {
                     return true;
                  }

                  return false;
               }

               temp = false;
               break label41;
            }

            temp = false;
            if (l >= 0L) {
               temp = false;
               if (l > 0L) {
                  temp = Dt(file);
               }

               return temp;
            }
         }

         if (l >= 0L) {
            return false;
         }
      }

      do {
         if (l < 0L) {
            ;
         }
      } while (l < 0L);

      return false;
   }

   private static boolean Dq(String str) {
      WindowsLinuxMode windowsLinuxMode = Dw();
      if (windowsLinuxMode.fb()) {
         if (Du("kde-open", str)) {
            return true;
         }

         if (Du("gnome-open", str)) {
            return true;
         }

         if (Du("xdg-open", str)) {
            return true;
         }
      }

      if (windowsLinuxMode.fc() && Du("open", str)) {
         return true;
      } else {
         return windowsLinuxMode.fd() ? Du("explorer", str) : false;
      }
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   private static boolean Dr(URI uri) {
      boolean temp;
      try {
         temp = Desktop.isDesktopSupported();
      } catch (Throwable error) {
         return false;
      }

      if (!temp) {
         return false;
      }

      try {
         Desktop desktop = Desktop.getDesktop();
         Action action = Action.BROWSE;
         temp = desktop.isSupported(action);
      } catch (Throwable error2) {
         return false;
      }

      if (!temp) {
         return false;
      }

      try {
         Desktop.getDesktop().browse(uri);
         return true;
      } catch (Throwable error3) {
         return false;
      }
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   private static boolean Ds(File file) {
      boolean temp;
      try {
         temp = Desktop.isDesktopSupported();
      } catch (Throwable error) {
         return false;
      }

      if (!temp) {
         return false;
      }

      try {
         Desktop desktop = Desktop.getDesktop();
         Action action = Action.OPEN;
         temp = desktop.isSupported(action);
      } catch (Throwable error2) {
         return false;
      }

      if (!temp) {
         return false;
      }

      try {
         Desktop.getDesktop().open(file);
         return true;
      } catch (Throwable error3) {
         return false;
      }
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   private static boolean Dt(File file) {
      boolean temp;
      try {
         temp = Desktop.isDesktopSupported();
      } catch (Throwable error) {
         error.printStackTrace();
         return false;
      }

      if (!temp) {
         return false;
      }

      try {
         Desktop desktop = Desktop.getDesktop();
         Action action = Action.EDIT;
         temp = desktop.isSupported(action);
      } catch (Throwable error2) {
         error2.printStackTrace();
         return false;
      }

      if (!temp) {
         return false;
      }

      try {
         Desktop.getDesktop().edit(file);
         return true;
      } catch (Throwable error3) {
         error3.printStackTrace();
         return false;
      }
   }

   private static boolean Du(String str, String str2) {
      String[] strs = Dv(str, str2);

      try {
         Process process = Runtime.getRuntime().exec(strs);
         if (process == null) {
            return false;
         }

         try {
            process.exitValue();
            return false;
         } catch (IllegalThreadStateException exception) {
            return true;
         }
      } catch (IOException exception2) {
         return false;
      }
   }

   private static String[] Dv(String str, String str2) {
      ArrayList arrayList = new ArrayList();
      arrayList.add(str);
      String[] strs = "%s".split(" ");
      int n = strs.length;

      for (int n2 = 0; n2 < n; n2++) {
         String temp = strs[n2];
         Object[] objs = new Object[]{str2};
         String str3 = String.format(temp, objs);
         ArrayList arrayList2 = arrayList;
         String str4 = str3.trim();
         arrayList2.add(str4);
      }

      ArrayList arrayList3 = arrayList;
      String[] strs2 = new String[0];
      return (String[])arrayList3.toArray(strs2);
   }

   public static WindowsLinuxMode Dw() {
      String str = System.getProperty("os.name").toLowerCase();
      if (str.contains("win")) {
         return WindowsLinuxMode.WINDOWS;
      } else if (str.contains("mac")) {
         return WindowsLinuxMode.MAC_OS;
      } else if (str.contains("solaris")) {
         return WindowsLinuxMode.LINUX_SOLARIS;
      } else if (str.contains("sunos")) {
         return WindowsLinuxMode.LINUX_SOLARIS;
      } else if (str.contains("linux")) {
         return WindowsLinuxMode.LINUX;
      } else {
         return str.contains("unix") ? WindowsLinuxMode.LINUX : WindowsLinuxMode.OTHER;
      }
   }
}
