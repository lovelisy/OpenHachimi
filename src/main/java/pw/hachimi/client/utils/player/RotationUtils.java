package pw.hachimi.client.utils.player;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.DisableLimitMode;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.utils.world.PlacementUtils;

public class RotationUtils {
   public static void nH(float f, float f2) {
      Managers.movementTracker.Mj(f, f2);
   }

   public static void nI(Vec3d vec) {
      float[] fs = nM(vec);
      nH(fs[0], fs[1]);
   }

   public static void nJ(BlockPos pos, Direction direction) {
      nI(PlacementUtils.Fn(pos, direction));
   }

   public static void nK() {
      if (HitResultWorldHelper.Sq().SL() == DisableLimitMode.Force) {
         Managers.movementTracker.Mk();
      }
   }

   public static float[] nL(Vec3d vec, Vec3d vec2) {
      double d = vec2.x - vec.x;
      double d2 = vec2.y - vec.y;
      double d3 = vec2.z - vec.z;
      double d4 = Math.sqrt(d * d + d3 * d3);
      float f = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0F;
      float f2 = (float)(-Math.toDegrees(Math.atan2(d2, d4)));
      float f3 = MathHelper.wrapDegrees(f - Mc.client.player.getYaw()) + Mc.client.player.getYaw();
      return new float[]{f3, MathHelper.wrapDegrees(f2)};
   }

   public static float[] nM(Vec3d vec) {
      return nL(Mc.client.player.getEyePos(), vec);
   }

   public static float nN(Vec3d vec) {
      Vec3d vec2 = Mc.client.player.getEyePos();
      double d = vec.x - vec2.x;
      return MathHelper.wrapDegrees((float)Math.toDegrees(Math.atan2(vec.z - vec2.z, d)) - 90.0F);
   }

   public static float nO(Vec3d vec) {
      Vec3d vec2 = Mc.client.player.getEyePos();
      double d = vec.x - vec2.x;
      double d2 = vec.y - vec2.y;
      double d3 = vec.z - vec2.z;
      double d4 = Math.sqrt(d * d + d3 * d3);
      return MathHelper.wrapDegrees((float)(-Math.toDegrees(Math.atan2(d2, d4))));
   }

   public static float[] nP(float[] fs, float[] fs2, float f) {
      float f2 = (1.0F - MathHelper.clamp(f / 100.0F, 0.1F, 0.9F)) * 10.0F;
      float[] fs3 = new float[2];
      fs3[0] = fs2[0] + (float)(-nQ(fs2[0], fs[0]) / f2);
      fs3[1] = fs2[1] + -(fs2[1] - fs[1]) / f2;
      fs3[1] = MathHelper.clamp(fs3[1], -90.0F, 90.0F);
      return fs3;
   }

   public static double nQ(float f, float f2) {
      return ((f - f2) % 360.0 + 540.0) % 360.0 - 180.0;
   }

   public static double nR(float f, float f2) {
      return ((f - f2) % 180.0 + 270.0) % 180.0 - 90.0;
   }

   public static Vec3d nS(float f, float f2) {
      float f3 = f * (float) (Math.PI / 180.0);
      float f4 = -f2 * (float) (Math.PI / 180.0);
      float f5 = MathHelper.cos(f4);
      float f6 = MathHelper.sin(f4);
      float f7 = MathHelper.cos(f3);
      float f8 = MathHelper.sin(f3);
      return new Vec3d(f6 * f7, -f8, f5 * f7);
   }
}
