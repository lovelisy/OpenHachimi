package pw.hachimi.client.model.render;

public enum Easing {
   LINEAR {
      @Override
      public double Yy(double var1) {
         return var1;
      }
   },
   SINE_IN {
      public static boolean e;

      @Override
      public double Yy(double var1) {
         return 1.0 - Math.cos(var1 * Math.PI / 2.0);
      }
   },
   SINE_OUT {
      @Override
      public double Yy(double var1) {
         return Math.sin(var1 * Math.PI / 2.0);
      }
   },
   SINE_IN_OUT {
      @Override
      public double Yy(double var1) {
         return -(Math.cos(Math.PI * var1) - 1.0) / 2.0;
      }
   },
   CUBIC_IN {
      @Override
      public double Yy(double var1) {
         return Math.pow(var1, 3.0);
      }
   },
   CUBIC_OUT {
      public static boolean ak;

      @Override
      public double Yy(double var1) {
         return 1.0 - Math.pow(1.0 - var1, 3.0);
      }
   },
   CUBIC_IN_OUT {
      public static boolean bf;

      @Override
      public double Yy(double var1) {
         double var3;
         if (var1 < 0.5) {
            var3 = 4.0 * Math.pow(var1, 3.0);
         } else {
            var3 = 1.0 - Math.pow(-2.0 * var1 + 2.0, 3.0) / 2.0;
         }

         return var3;
      }
   },
   QUAD_IN {
      @Override
      public double Yy(double var1) {
         return Math.pow(var1, 2.0);
      }
   },
   QUAD_OUT {
      public static boolean j;

      @Override
      public double Yy(double var1) {
         return 1.0 - (1.0 - var1) * (1.0 - var1);
      }
   },
   QUAD_IN_OUT {
      public static boolean b;

      @Override
      public double Yy(double var1) {
         double var3;
         if (var1 < 0.5) {
            var3 = 8.0 * Math.pow(var1, 4.0);
         } else {
            var3 = 1.0 - Math.pow(-2.0 * var1 + 2.0, 4.0) / 2.0;
         }

         return var3;
      }
   },
   QUART_IN {
      public static boolean bb;

      @Override
      public double Yy(double var1) {
         return Math.pow(var1, 4.0);
      }
   },
   QUART_OUT {
      public static boolean bl;

      @Override
      public double Yy(double var1) {
         return 1.0 - Math.pow(1.0 - var1, 4.0);
      }
   },
   QUART_IN_OUT {
      public static boolean al;

      @Override
      public double Yy(double var1) {
         double var3;
         if (var1 < 0.5) {
            var3 = 8.0 * Math.pow(var1, 4.0);
         } else {
            var3 = 1.0 - Math.pow(-2.0 * var1 + 2.0, 4.0) / 2.0;
         }

         return var3;
      }
   },
   QUINT_IN {
      @Override
      public double Yy(double var1) {
         return Math.pow(var1, 5.0);
      }
   },
   QUINT_OUT {
      @Override
      public double Yy(double var1) {
         return 1.0 - Math.pow(1.0 - var1, 5.0);
      }
   },
   QUINT_IN_OUT {
      public static boolean bm;

      @Override
      public double Yy(double var1) {
         double var3;
         if (var1 < 0.5) {
            var3 = 16.0 * Math.pow(var1, 5.0);
         } else {
            var3 = 1.0 - Math.pow(-2.0 * var1 + 2.0, 5.0) / 2.0;
         }

         return var3;
      }
   },
   CIRC_IN {
      @Override
      public double Yy(double var1) {
         return 1.0 - Math.sqrt(1.0 - Math.pow(var1, 2.0));
      }
   },
   CIRC_OUT {
      public static boolean z;

      @Override
      public double Yy(double var1) {
         return Math.sqrt(1.0 - Math.pow(var1 - 1.0, 2.0));
      }
   },
   CIRC_IN_OUT {
      public static boolean t;

      @Override
      public double Yy(double var1) {
         double var3;
         if (var1 < 0.5) {
            var3 = (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * var1, 2.0))) / 2.0;
         } else {
            var3 = (Math.sqrt(1.0 - Math.pow(-2.0 * var1 + 2.0, 2.0)) + 1.0) / 2.0;
         }

         return var3;
      }
   },
   EXPO_IN_OUT {
      @Override
      public double Yy(double var1) {
         double var3;
         if (var1 == 0.0) {
            var3 = 0.0;
         } else if (var1 == 1.0) {
            var3 = 1.0;
         } else if (var1 < 0.5) {
            var3 = Math.pow(2.0, 20.0 * var1 - 10.0) / 2.0;
         } else {
            var3 = (2.0 - Math.pow(2.0, -20.0 * var1 + 10.0)) / 2.0;
         }

         return var3;
      }
   },
   ELASTIC_IN {
      public static boolean an;

      @Override
      public double Yy(double var1) {
         double var3;
         if (var1 == 0.0) {
            var3 = 0.0;
         } else if (var1 == 1.0) {
            var3 = 1.0;
         } else {
            var3 = -Math.pow(2.0, 10.0 * var1 - 10.0) * Math.sin((var1 * 10.0 - 10.75) * (Math.PI * 2.0 / 3.0));
         }

         return var3;
      }
   },
   ELASTIC_OUT {
      @Override
      public double Yy(double var1) {
         double var3;
         if (var1 == 0.0) {
            var3 = 0.0;
         } else if (var1 == 1.0) {
            var3 = 1.0;
         } else {
            var3 = Math.pow(2.0, -10.0 * var1) * Math.sin((var1 * 10.0 - 0.75) * (Math.PI * 2.0 / 3.0)) + 1.0;
         }

         return var3;
      }
   },
   ELASTIC_IN_OUT {
      @Override
      public double Yy(double var1) {
         double var3 = Math.sin((20.0 * var1 - 11.125) * (Math.PI * 4.0 / 9.0));
         double var5;
         if (var1 == 0.0) {
            var5 = 0.0;
         } else if (var1 == 1.0) {
            var5 = 1.0;
         } else if (var1 < 0.5) {
            var5 = -(Math.pow(2.0, 20.0 * var1 - 10.0) * var3) / 2.0;
         } else {
            var5 = Math.pow(2.0, -20.0 * var1 + 10.0) * var3 / 2.0 + 1.0;
         }

         return var5;
      }
   },
   BACK_IN {
      @Override
      public double Yy(double var1) {
         return 2.70158 * Math.pow(var1, 3.0) - 1.70158 * var1 * var1;
      }
   },
   BACK_OUT {
      @Override
      public double Yy(double var1) {
         double var5 = 1.70158 + 1.0;
         return 1.0 + var5 * Math.pow(var1 - 1.0, 3.0) + 1.70158 * Math.pow(var1 - 1.0, 2.0);
      }
   },
   BACK_IN_OUT {
      @Override
      public double Yy(double var1) {
         double var3;
         if (var1 < 0.5) {
            var3 = Math.pow(2.0 * var1, 2.0) * (7.189819 * var1 - 2.5949095) / 2.0;
         } else {
            var3 = (Math.pow(2.0 * var1 - 2.0, 2.0) * (3.5949095 * (var1 * 2.0 - 2.0) + 2.5949095) + 2.0) / 2.0;
         }

         return var3;
      }
   },
   BOUNCE_IN {
      @Override
      public double Yy(double var1) {
         return 1.0 - Easing.Yx(1.0 - var1);
      }
   },
   BOUNCE_OUT {
      @Override
      public double Yy(double var1) {
         return Easing.Yx(var1);
      }
   },
   BOUNCE_IN_OUT {
      @Override
      public double Yy(double var1) {
         double var5;
         if (var1 < 0.5) {
            var5 = (1.0 - Easing.Yx(1.0 - 2.0 * var1)) / 2.0;
         } else {
            var5 = (1.0 + Easing.Yx(2.0 * var1 - 1.0)) / 2.0;
         }

         return var5;
      }
   };

   private static final Easing[] D = Yz();

   static double Yx(double d) {
      if (d < 1.0 / 2.75) {
         return 7.5625 * d * d;
      } else if (d < 2.0 / 2.75) {
         double d2;
         return 7.5625 * (d2 = d - 1.5 / 2.75) * d2 + 0.75;
      } else {
         double d3;
         double d4;
         return d < 2.5 / 2.75 ? 7.5625 * (d3 = d - 2.25 / 2.75) * d3 + 0.9375 : 7.5625 * (d4 = d - 2.625 / 2.75) * d4 + 0.984375;
      }
   }

   public abstract double Yy(double var1);

   private static Easing[] Yz() {
      return new Easing[]{
         LINEAR,
         SINE_IN,
         SINE_OUT,
         SINE_IN_OUT,
         CUBIC_IN,
         CUBIC_OUT,
         CUBIC_IN_OUT,
         QUAD_IN,
         QUAD_OUT,
         QUAD_IN_OUT,
         QUART_IN,
         QUART_OUT,
         QUART_IN_OUT,
         QUINT_IN,
         QUINT_OUT,
         QUINT_IN_OUT,
         CIRC_IN,
         CIRC_OUT,
         CIRC_IN_OUT,
         EXPO_IN_OUT,
         ELASTIC_IN,
         ELASTIC_OUT,
         ELASTIC_IN_OUT,
         BACK_IN,
         BACK_OUT,
         BACK_IN_OUT,
         BOUNCE_IN,
         BOUNCE_OUT,
         BOUNCE_IN_OUT
      };
   }
}
