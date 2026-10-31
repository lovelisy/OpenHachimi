package pw.hachimi.client.model.combat;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.Mc;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.render.WorldUtils;

public enum TargetPriority implements Comparator {
   LowestDistance(Comparator.comparingDouble(new ToDoubleFunction() {
      @Override
      public double applyAsDouble(Object var1) {
         return WorldUtils.Nq((Entity)var1);
      }
   })),
   HighestDistance(new Comparator() {
      @Override
      public int compare(Object var1, Object var2) {
         return TargetPriority.QU((Entity)var1, (Entity)var2);
      }
   }),
   LowestHealth(new Comparator() {
      @Override
      public int compare(Object var1, Object var2) {
         return TargetPriority.QP((Entity)var1, (Entity)var2);
      }
   }),
   HighestHealth(new Comparator() {
      @Override
      public int compare(Object var1, Object var2) {
         return TargetPriority.QT((Entity)var1, (Entity)var2);
      }
   }),
   ClosestAngle(new Comparator() {
      @Override
      public int compare(Object var1, Object var2) {
         return TargetPriority.QQ((Entity)var1, (Entity)var2);
      }
   });

   private final Comparator f;
   private static final TargetPriority[] g = QS();

   TargetPriority(Comparator var3) {
      this.f = var3;
   }

   static int QP(Entity var0, Entity var1) {
      boolean var2 = var0 instanceof LivingEntity;
      boolean var3 = var1 instanceof LivingEntity;
      if (!var2 && !var3) {
         return 0;
      } else if (var2 && !var3) {
         return 1;
      } else {
         return !var2 ? -1 : Float.compare(((LivingEntity)var0).getHealth(), ((LivingEntity)var1).getHealth());
      }
   }

   static int QQ(Entity var0, Entity var1) {
      boolean var6 = var0 instanceof LivingEntity;
      boolean var7 = var1 instanceof LivingEntity;
      if (!var6 && !var7) {
         return 0;
      }

      if (var6 && !var7) {
         return 1;
      }

      if (!var6) {
         return -1;
      }

      double var8 = MathHelper.angleBetween(RotationUtils.nN(var0.getEyePos()), Mc.client.player.getYaw());
      double var10 = MathHelper.angleBetween(RotationUtils.nN(var1.getEyePos()), Mc.client.player.getYaw());
      double var12 = Math.abs(RotationUtils.nO(var0.getEyePos()) - Mc.client.player.getPitch());
      double var15 = Math.abs(RotationUtils.nO(var1.getEyePos()) - Mc.client.player.getPitch());
      return Double.compare(var8 * var8 + var12 * var12, var10 * var10 + var15 * var15);
   }

   public int QR(Entity var1, Entity var2) {
      return this.f.compare(var1, var2);
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.QR((Entity)var1, (Entity)var2);
   }

   private static TargetPriority[] QS() {
      return new TargetPriority[]{LowestDistance, HighestDistance, LowestHealth, HighestHealth, ClosestAngle};
   }

   static int QT(Entity var0, Entity var1) {
      return QP(var1, var0);
   }

   static int QU(Entity var0, Entity var1) {
      return Double.compare(WorldUtils.Nq(var1), WorldUtils.Nq(var0));
   }
}
