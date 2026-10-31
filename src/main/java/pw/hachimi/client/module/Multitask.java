package pw.hachimi.client.module;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;

public class Multitask extends Module5 {
   public Setting breakSetting = this.registerSetting(new BooleanSetting("Multitask", "Allows actions while using items", false));

   public Multitask(String str, String str2, ModuleCategory moduleCategory) {
      super(str, str2, moduleCategory);
   }

   public Multitask(String str, String str2, ModuleCategory moduleCategory, int n) {
      super(str, str2, moduleCategory, n);
   }

   public PlayerEntity RE(short s, long l, double d) {
      label79: {
         label70:
         if (s >= 0) {
            if (s < 0 || s < 0) {
               break label79;
            }

            Stream temp = client.world.getPlayers().stream();
            Predicate predicate = new Predicate() {
               @Override
               public boolean test(Object s) {
                  return Multitask.RP((AbstractClientPlayerEntity)s);
               }
            };
            temp = temp.filter(predicate);
            final double d2 = d;
            Predicate predicate2 = new Predicate() {
               @Override
               public boolean test(Object s) {
                  return Multitask.RO(d2, (AbstractClientPlayerEntity)s);
               }
            };
            Stream stream2 = temp;
            if (l >= 0L) {
               if (s < 0) {
                  break label70;
               }

               if (s < 0 || s < 0) {
                  break label79;
               }

               temp = stream2.filter(predicate2);
               predicate2 = new Predicate() {
                  @Override
                  public boolean test(Object s) {
                     return Multitask.RN((AbstractClientPlayerEntity)s);
                  }
               };
               stream2 = temp;
               if (s < 0) {
                  break label70;
               }

               if (s < 0 || s < 0) {
                  break label79;
               }

               temp = stream2.filter(predicate2);
               ToDoubleFunction toDoubleFunction = new ToDoubleFunction() {
                  @Override
                  public double applyAsDouble(Object s) {
                     return Multitask.RM((AbstractClientPlayerEntity)s);
                  }
               };
               stream2 = temp;
               if (s < 0) {
                  while (true) {
                     if (s >= 0) {
                        break label79;
                     }
                  }
               }

               if (s < 0) {
                  break label79;
               }

               temp = stream2;
               Comparator comparator = Comparator.comparingDouble(toDoubleFunction);
               PlayerEntity player2 = (PlayerEntity)temp.min(comparator).orElse(null);
               if (s >= 0) {
                  if (s >= 0) {
                     if (s >= 0 && s >= 0) {
                        return player2;
                     }
                     break label79;
                  }
                  break label70;
               }
            }

            if (s >= 0) {
               return null;
            }
         }

         while (s < 0) {
         }

         if (s >= 0) {
         }
      }

      while (s < 0) {
      }

      return null;
   }

   public PlayerEntity RF(Predicate predicate, double d) {
      Stream temp = client.world.getPlayers().stream();
      Predicate predicate2 = new Predicate() {
         @Override
         public boolean test(Object predicate) {
            return Multitask.RL((AbstractClientPlayerEntity)predicate);
         }
      };
      temp = temp.filter(predicate2).filter(predicate);
      final double d2 = d;
      Predicate predicate3 = new Predicate() {
         @Override
         public boolean test(Object predicate) {
            return Multitask.RK(d2, (AbstractClientPlayerEntity)predicate);
         }
      };
      temp = temp.filter(predicate3);
      predicate3 = new Predicate() {
         @Override
         public boolean test(Object predicate) {
            return Multitask.RJ((AbstractClientPlayerEntity)predicate);
         }
      };
      temp = temp.filter(predicate3);
      ToDoubleFunction toDoubleFunction = new ToDoubleFunction() {
         @Override
         public double applyAsDouble(Object predicate) {
            return Multitask.RI((AbstractClientPlayerEntity)predicate);
         }
      };
      temp = temp;
      Comparator comparator = Comparator.comparingDouble(toDoubleFunction);
      return (PlayerEntity)temp.min(comparator).orElse(null);
   }

   public boolean RG() {
      return this.RH(false);
   }

   public boolean RH(boolean flag) {
      return flag && client.player.getActiveHand() != Hand.MAIN_HAND ? false : client.player.isUsingItem();
   }

   static double RI(AbstractClientPlayerEntity player2) {
      return client.player.squaredDistanceTo((Entity)player2);
   }

   static boolean RJ(AbstractClientPlayerEntity player2) {
      boolean flag;
      if (!Managers.friendManager.awq(player2.getName().getString())) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean RK(double d, AbstractClientPlayerEntity player2) {
      boolean flag;
      if (client.player.squaredDistanceTo((Entity)player2) <= d * d) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean RL(AbstractClientPlayerEntity player2) {
      boolean flag;
      if (!(player2 instanceof ClientPlayerEntity) && !player2.isSpectator()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static double RM(AbstractClientPlayerEntity player2) {
      return client.player.squaredDistanceTo((Entity)player2);
   }

   static boolean RN(AbstractClientPlayerEntity player2) {
      boolean flag;
      if (!Managers.friendManager.awq(player2.getName().getString())) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean RO(double d, AbstractClientPlayerEntity player2) {
      boolean flag;
      if (client.player.squaredDistanceTo((Entity)player2) <= d * d) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean RP(AbstractClientPlayerEntity player2) {
      boolean flag;
      if (!(player2 instanceof ClientPlayerEntity) && !player2.isSpectator()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
