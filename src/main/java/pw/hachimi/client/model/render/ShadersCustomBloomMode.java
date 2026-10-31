package pw.hachimi.client.model.render;

import pw.hachimi.client.module.render.Shaders;

public enum ShadersCustomBloomMode {
   Custom {
      @Override
      public boolean gq() {
         return true;
      }

      @Override
      public boolean gr() {
         return (Boolean)Shaders.agJ().fastOutline.atx();
      }

      @Override
      public int gs() {
         return (Integer)Shaders.agJ().quality.atx();
      }

      @Override
      public int gt() {
         return (Integer)Shaders.agJ().steps.atx();
      }

      @Override
      public boolean gu() {
         return (Boolean)Shaders.agJ().glow.atx();
      }

      @Override
      public boolean gv() {
         return (Boolean)Shaders.agJ().inner.atx();
      }

      @Override
      public float gw() {
         return (Float)Shaders.agJ().glowFactor.atx();
      }

      @Override
      public float gx() {
         return (Float)Shaders.agJ().width.atx();
      }
   },
   Bloom {
      @Override
      public boolean gq() {
         return true;
      }

      @Override
      public boolean gr() {
         return true;
      }

      @Override
      public int gs() {
         return 8;
      }

      @Override
      public int gt() {
         return 4;
      }

      @Override
      public boolean gu() {
         return true;
      }

      @Override
      public boolean gv() {
         return true;
      }

      @Override
      public float gw() {
         return 0.35F;
      }

      @Override
      public float gx() {
         return 4.0F;
      }
   },
   Default {
      @Override
      public boolean gq() {
         return true;
      }

      @Override
      public boolean gr() {
         return true;
      }

      @Override
      public int gs() {
         return 10;
      }

      @Override
      public int gt() {
         return 2;
      }

      @Override
      public boolean gu() {
         return false;
      }

      @Override
      public boolean gv() {
         return false;
      }

      @Override
      public float gw() {
         return 0.0F;
      }

      @Override
      public float gx() {
         return 0.8F;
      }
   },
   Off {
      public static boolean bg;

      @Override
      public boolean gq() {
         return false;
      }

      @Override
      public boolean gr() {
         return false;
      }

      @Override
      public int gs() {
         return 0;
      }

      @Override
      public int gt() {
         return 0;
      }

      @Override
      public boolean gu() {
         return false;
      }

      @Override
      public boolean gv() {
         return false;
      }

      @Override
      public float gw() {
         return 0.0F;
      }

      @Override
      public float gx() {
         return 0.0F;
      }
   };

   private static final ShadersCustomBloomMode[] e = gy();

   public abstract boolean gq();

   public abstract boolean gr();

   public abstract int gs();

   public abstract int gt();

   public abstract boolean gu();

   public abstract boolean gv();

   public abstract float gw();

   public abstract float gx();

   private static ShadersCustomBloomMode[] gy() {
      return new ShadersCustomBloomMode[]{Custom, Bloom, Default, Off};
   }
}
