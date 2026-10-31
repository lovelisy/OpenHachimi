package pw.hachimi.client.module;

public enum ModuleCategory {
   COMBAT {
      public static boolean y;

      @Override
      public String Wk() {
         return "b";
      }
   },
   MISC {
      public static boolean bh;

      @Override
      public String Wk() {
         return "[";
      }
   },
   RENDER {
      @Override
      public String Wk() {
         return "a";
      }
   },
   MOVEMENT {
      @Override
      public String Wk() {
         return "8";
      }
   },
   PLAYER {
      public static boolean f;

      @Override
      public String Wk() {
         return "5";
      }
   },
   EXPLOIT {
      @Override
      public String Wk() {
         return "6";
      }
   },
   CLIENT {
      public static boolean bd;

      @Override
      public String Wk() {
         return "7";
      }
   },
   HUD {
      @Override
      public String Wk() {
         return "d";
      }
   };

   private static final ModuleCategory[] i = Wl();

   public static ModuleCategory Wj(String str) {
      return Enum.valueOf(ModuleCategory.class, str);
   }

   public abstract String Wk();

   private static ModuleCategory[] Wl() {
      return new ModuleCategory[]{COMBAT, MISC, RENDER, MOVEMENT, PLAYER, EXPLOIT, CLIENT, HUD};
   }
}
