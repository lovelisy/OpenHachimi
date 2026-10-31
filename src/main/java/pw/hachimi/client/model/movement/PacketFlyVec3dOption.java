package pw.hachimi.client.model.movement;

import java.util.Random;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.config.ConfigContainer;

public enum PacketFlyVec3dOption {
   a_ {
      @Override
      public Vec3d ajS(Vec3d vec) {
         Random random = ConfigContainer.random2;
         double d = random.nextDouble(-50.0, 50.0);
         double d2 = random.nextDouble(50.0, 80.0);
         boolean flag = ConfigContainer.random2.nextBoolean();
         double d3 = flag ? 1.0 : -1.0;
         return vec.add(d, d2 * d3, ConfigContainer.random2.nextDouble(-50.0, 50.0));
      }
   },
   b {
      @Override
      public Vec3d ajS(Vec3d vec) {
         double d = ConfigContainer.random2.nextDouble(-100000.0, 100000.0);
         double d2 = ConfigContainer.random2.nextDouble(-100000.0, 100000.0);
         return vec.add(d, 2.0, d2);
      }
   },
   c {
      @Override
      public Vec3d ajS(Vec3d vec) {
         return Vec3d.ZERO;
      }
   },
   d {
      @Override
      public Vec3d ajS(Vec3d vec) {
         Random random = ConfigContainer.random2;
         double d = random.nextDouble(-10.0, 10.0);
         double d2 = random.nextDouble(80.0, 100.0);
         boolean flag = ConfigContainer.random2.nextBoolean();
         double d3 = flag ? 1.0 : -1.0;
         return vec.add(d, d2 * d3, ConfigContainer.random2.nextDouble(-10.0, 10.0));
      }
   },
   e {
      @Override
      public Vec3d ajS(Vec3d vec) {
         double d = vec.y < 127.5 ? 255.0 : 0.0;
         return new Vec3d(vec.x, -vec.y + d, vec.z);
      }
   },
   f {
      @Override
      public Vec3d ajS(Vec3d vec) {
         return vec.subtract(0.0, 1337.0, 0.0);
      }
   },
   g {
      @Override
      public Vec3d ajS(Vec3d vec) {
         return vec.add(0.0, 1337.0, 0.0);
      }
   },
   h {
      @Override
      public Vec3d ajS(Vec3d vec) {
         double d = ConfigContainer.random2.nextDouble(100000.0);
         double d2 = ConfigContainer.random2.nextDouble(100000.0);
         return vec.add(d, 0.0, d2);
      }
   };

   private static final PacketFlyVec3dOption[] i = ajT();
   private static final long a = 0L;

   public abstract Vec3d ajS(Vec3d vec);

   private static PacketFlyVec3dOption[] ajT() {
      return new PacketFlyVec3dOption[]{a_, b, c, d, e, f, g, h};
   }
}
