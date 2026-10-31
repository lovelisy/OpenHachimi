package pw.hachimi.client.utils;

import java.lang.reflect.Field;
import org.lwjgl.glfw.GLFW;

public class UnidentifiedUtils5 {
   public static boolean a;

   public static String Xd(int n, int n2) {
      return switch (n) {
         case 32 -> "SPACE";
         case 257 -> "ENTER";
         case 258 -> "TAB";
         case 259 -> "BACKSPACE";
         case 260 -> "INSERT";
         case 261 -> "DELETE";
         case 262 -> "RIGHT";
         case 263 -> "LEFT";
         case 264 -> "DOWN";
         case 265 -> "UP";
         case 266 -> "PAGE_UP";
         case 267 -> "PAGE_DOWN";
         case 268 -> "HOME";
         case 269 -> "END";
         case 280 -> "CAPS_LOCK";
         case 290 -> "F1";
         case 291 -> "F2";
         case 292 -> "F3";
         case 293 -> "F4";
         case 294 -> "F5";
         case 295 -> "F6";
         case 296 -> "F7";
         case 297 -> "F8";
         case 298 -> "F9";
         case 299 -> "F10";
         case 300 -> "F11";
         case 301 -> "F12";
         case 340 -> "LSHIFT";
         case 341 -> "LCONTROL";
         case 342 -> "LALT";
         case 344 -> "RSHIFT";
         case 345 -> "RCONTROL";
         case 346 -> "RALT";
         case 1000 -> "MOUSE0";
         case 1001 -> "MOUSE1";
         case 1002 -> "MOUSE2";
         case 1003 -> "MOUSE3";
         case 1004 -> "MOUSE4";
         case 1005 -> "MOUSE5";
         case 1006 -> "MOUSE6";
         case 1007 -> "MOUSE7";
         default -> GLFW.glfwGetKeyName(n, n2);
      };
   }

   // $VF: Handled exception range with multiple entry points by splitting it
   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   public static int Xf(String str) {
      if (str.equalsIgnoreCase("NONE")) {
         return -1;
      }

      Class<GLFW> temp = GLFW.class;

      Field[] fields;
      int n;
      int n2;
      try {
         fields = temp.getDeclaredFields();
         n = fields.length;
         n2 = 0;
      } catch (Exception exception2) {
         return -1;
      }

      while (true) {
         try {
            if (n2 >= n) {
               break;
            }

            Field field = fields[n2];
            String str2 = field.getName();
            if (field.getName().startsWith("GLFW_KEY_")) {
               String str3 = str2.replaceFirst("GLFW_KEY_", "").toLowerCase();
               if (str3.equalsIgnoreCase(str) || str3.replace("_", "").equalsIgnoreCase(str)) {
                  return (Integer)field.get(null);
               }
            }
         } catch (Exception exception3) {
            break;
         }

         n2++;
      }

      return -1;
   }
}
