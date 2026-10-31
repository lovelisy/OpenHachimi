package pw.hachimi.client.utils.render;

import java.io.InputStream;
import pw.hachimi.client.render.FontRenderer;
import java.io.FileInputStream;
import java.io.IOException;
import pw.hachimi.client.Bootstrap;

public class FontLoader {
   public static final String a = "assets/hachimiclient/font/default.ttf";
   private static final String b = "assets/hachimiclient/font/icon.ttf";
   public static String text = "assets/hachimiclient/font/default.ttf";
   public static FontRenderer fontRenderer;
   public static FontRenderer fontRenderer2;
   public static FontRenderer fontRenderer3;
   public static float g = 9.0F;
   private static boolean h;

   public static void aor() {
      if (!h) {
         Bootstrap.clientPaths.aoh();
         aos();
         h = true;
      }
   }

   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public static void aos() {
       try {
           fontRenderer3 = new FontRenderer(118068599095241L, Bootstrap.Od("assets/hachimiclient/font/icon.ttf"), 9.0f);
           float f = g;
           InputStream inputStream = !text.startsWith("assets") ? new FileInputStream(text) : Bootstrap.Od(text);
           fontRenderer = new FontRenderer(118068599095241L, inputStream, f);
           InputStream inputStream2 = !text.startsWith("assets") ? new FileInputStream(text) : Bootstrap.Od(text);
           fontRenderer2 = new FontRenderer(118068599095241L, inputStream2, 18.0f);
           Bootstrap.NZ("Loaded fonts!");
           return;
       }
       catch (Exception exception) {
           exception.printStackTrace();
           try {
               Bootstrap.Ob("Failed to load fonts!");
               fontRenderer = new FontRenderer(118068599095241L, Bootstrap.Od("assets/hachimiclient/font/default.ttf"), g);
               fontRenderer2 = new FontRenderer(118068599095241L, Bootstrap.Od("assets/hachimiclient/font/default.ttf"), 18.0f);
               Bootstrap.NZ("Loaded default fonts!");
               return;
           }
           catch (Exception exception2) {
               exception2.printStackTrace();
               Bootstrap.Ob("Failed to load default font!");
               return;
           }
       }
   }

   public static void aot() {
      fontRenderer.close();
      fontRenderer2.close();
   }

   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public static void aou(float f) {
       g = f;
       try {
           InputStream inputStream = text.startsWith("assets") ? Bootstrap.Od(text) : new FileInputStream(text);
           fontRenderer = FontLoader.a(inputStream, g);
           return;
       }
       catch (IOException iOException) {
           iOException.printStackTrace();
           return;
       }
   }

   public static boolean aov() {
      return h;
   }

   private static FontRenderer a(InputStream inputStream3, float f2) {
      return new FontRenderer(118068599095241L, inputStream3, f2);
   }
}
