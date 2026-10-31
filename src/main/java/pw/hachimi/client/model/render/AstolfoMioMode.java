package pw.hachimi.client.model.render;

import java.awt.Color;
import pw.hachimi.client.model.UnidentifiedOption4;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.utils.render.ColorUtils;

public enum AstolfoMioMode {
   Astolfo {
      public static boolean v;

      @Override
      public boolean Qv() {
         return true;
      }

      @Override
      public boolean Qw() {
         return true;
      }

      @Override
      public boolean Qx() {
         return true;
      }

      @Override
      public UnidentifiedOption3 Qy() {
         return UnidentifiedOption3.a_;
      }

      @Override
      public boolean Qz() {
         return false;
      }

      @Override
      public Color QA() {
         return UI.G;
      }

      @Override
      public Color QB() {
         return UI.H;
      }

      @Override
      public boolean QC() {
         return false;
      }
   },
   Mio {
      @Override
      public boolean Qv() {
         return false;
      }

      @Override
      public boolean Qw() {
         return false;
      }

      @Override
      public boolean Qx() {
         return false;
      }

      @Override
      public UnidentifiedOption3 Qy() {
         return UnidentifiedOption3.a_;
      }

      @Override
      public boolean Qz() {
         return false;
      }

      @Override
      public Color QA() {
         return a(ColorUtils.iG(UI.INSTANCE.atW(0.0), 60));
      }

      @Override
      public Color QB() {
         return a(ColorUtils.iG(UI.INSTANCE.atW(0.0), 180));
      }

      @Override
      public boolean QC() {
         return false;
      }

      private static Color a(int var0) {
         return new Color(var0, true);
      }
   },
   Custom {
      @Override
      public boolean Qv() {
         return (Boolean)UI.INSTANCE.outline.atx();
      }

      @Override
      public boolean Qw() {
         return (Boolean)UI.INSTANCE.icon.atx();
      }

      @Override
      public boolean Qx() {
         return (Boolean)UI.INSTANCE.moduleButton.atx();
      }

      @Override
      public UnidentifiedOption3 Qy() {
         return (UnidentifiedOption3)UI.INSTANCE.fadeButton.atx();
      }

      @Override
      public boolean Qz() {
         return (Boolean)UI.INSTANCE.interval.atx();
      }

      @Override
      public Color QA() {
         return (Color)UI.INSTANCE.backGround.atx();
      }

      @Override
      public Color QB() {
         return (Color)UI.INSTANCE.frame.atx();
      }

      @Override
      public boolean QC() {
         boolean var1;
         if (UI.INSTANCE.frameStyle.atx() == UnidentifiedOption4.b) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   };

   private static final AstolfoMioMode[] d = QD();

   public abstract boolean Qv();

   public abstract boolean Qw();

   public abstract boolean Qx();

   public abstract UnidentifiedOption3 Qy();

   public abstract boolean Qz();

   public abstract Color QA();

   public abstract Color QB();

   public abstract boolean QC();

   private static AstolfoMioMode[] QD() {
      return new AstolfoMioMode[]{Astolfo, Mio, Custom};
   }
}
