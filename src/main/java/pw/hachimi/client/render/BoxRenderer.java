package pw.hachimi.client.render;

import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.model.render.LinesTrianglesMode;
import pw.hachimi.client.model.render.UnidentifiedOption;
import pw.hachimi.client.utils.render.UnidentifiedHelper16;
import pw.hachimi.client.utils.render.UnidentifiedUtils10;
import pw.hachimi.client.utils.render.Vec3dGameRendererHelper;
import pw.hachimi.client.utils.world.BoxRendererDirectionUtils;

public class BoxRenderer {
   public final Vec3dGameRendererHelper vec3dGameRendererHelper = new UnidentifiedHelper16(
      UnidentifiedUtils10.colorManager, LinesTrianglesMode.Lines, UnidentifiedOption.Vec3, UnidentifiedOption.Color
   );
   public final Vec3dGameRendererHelper vec3dGameRendererHelper2 = new UnidentifiedHelper16(
      UnidentifiedUtils10.colorManager, LinesTrianglesMode.Triangles, UnidentifiedOption.Vec3, UnidentifiedOption.Color
   );

   public void YF() {
      this.vec3dGameRendererHelper.px();
      this.vec3dGameRendererHelper2.px();
   }

   public void YG() {
      this.vec3dGameRendererHelper.pG();
      this.vec3dGameRendererHelper2.pG();
   }

   public void YH(MatrixStack matrices) {
      this.vec3dGameRendererHelper.pI(matrices);
      this.vec3dGameRendererHelper2.pI(matrices);
   }

   public void YI(double d, double d2, double d3, double d4, double d5, double d6, Color color, Color color2) {
      this.vec3dGameRendererHelper
         .pC(this.vec3dGameRendererHelper.py(d, d2, d3).pA(color).pB(), this.vec3dGameRendererHelper.py(d4, d5, d6).pA(color2).pB());
   }

   public void YJ(double d, double d2, double d3, double d4, double d5, double d6, Color color) {
      this.YI(d, d2, d3, d4, d5, d6, color, color);
   }

   public void YK(double d, double d2, double d3, double d4, double d5, double d6, Color color, Color color2, double d7) {
      d7 /= 250.0;
      Vec3d vec = Mc.client.gameRenderer.getCamera().getPos();
      double d8 = d4 - d;
      double d9 = d5 - d2;
      double d10 = d6 - d3;
      double d11 = (d + d4) * 0.5 - vec.x;
      double d12 = (d2 + d5) * 0.5 - vec.y;
      double d13 = (d3 + d6) * 0.5 - vec.z;
      double d14 = d9 * d13 - d10 * d12;
      double d15 = d10 * d11 - d8 * d13;
      double d16 = d8 * d12 - d9 * d11;
      double d17 = Math.sqrt(d14 * d14 + d15 * d15 + d16 * d16);
      if (d17 < 1.0E-10) {
         if (Math.abs(d9) < 0.9) {
            d14 = d10;
            d15 = 0.0;
            d16 = -d8;
         } else {
            d14 = 0.0;
            d15 = -d10;
            d16 = d9;
         }

         d17 = Math.sqrt(d14 * d14 + d15 * d15 + d16 * d16);
         if (d17 < 1.0E-10) {
            return;
         }
      }

      double d18 = d7 * 0.5;
      double d19 = d14 / d17 * d18;
      double d20 = d15 / d17 * d18;
      double d21 = d16 / d17 * d18;
      this.vec3dGameRendererHelper2
         .pD(
            this.vec3dGameRendererHelper2.py(d - d19, d2 - d20, d3 - d21).pA(color).pB(),
            this.vec3dGameRendererHelper2.py(d + d19, d2 + d20, d3 + d21).pA(color).pB(),
            this.vec3dGameRendererHelper2.py(d4 + d19, d5 + d20, d6 + d21).pA(color2).pB(),
            this.vec3dGameRendererHelper2.py(d4 - d19, d5 - d20, d6 - d21).pA(color2).pB()
         );
   }

   public void YL(double d, double d2, double d3, double d4, double d5, double d6, Color color, double d7) {
      this.YK(d, d2, d3, d4, d5, d6, color, color, d7);
   }

   public void YM(short s, double d, double d2, double d3, double d4, double d5, double d6, int n, Color color, int n2) {
      if (color.getAlpha() > 0) {
         int n3 = this.vec3dGameRendererHelper.py(d, d2, d3).pA(color).pB();
         int n4 = this.vec3dGameRendererHelper.py(d, d2, d6).pA(color).pB();
         int n5 = this.vec3dGameRendererHelper.py(d4, d2, d3).pA(color).pB();
         int n6 = this.vec3dGameRendererHelper.py(d4, d2, d6).pA(color).pB();
         int n7 = this.vec3dGameRendererHelper.py(d, d5, d3).pA(color).pB();
         int n8 = this.vec3dGameRendererHelper.py(d, d5, d6).pA(color).pB();
         int n9 = this.vec3dGameRendererHelper.py(d4, d5, d3).pA(color).pB();
         int n10 = this.vec3dGameRendererHelper.py(d4, d5, d6).pA(color).pB();
         if (n2 == 0) {
            this.vec3dGameRendererHelper.pC(n3, n7);
            this.vec3dGameRendererHelper.pC(n4, n8);
            this.vec3dGameRendererHelper.pC(n5, n9);
            this.vec3dGameRendererHelper.pC(n6, n10);
            this.vec3dGameRendererHelper.pC(n3, n4);
            this.vec3dGameRendererHelper.pC(n5, n6);
            this.vec3dGameRendererHelper.pC(n3, n5);
            this.vec3dGameRendererHelper.pC(n4, n6);
            this.vec3dGameRendererHelper.pC(n7, n8);
            this.vec3dGameRendererHelper.pC(n9, n10);
            this.vec3dGameRendererHelper.pC(n7, n9);
            this.vec3dGameRendererHelper.pC(n8, n10);
         } else {
            if (BoxRendererDirectionUtils.Xl(n2, (byte)32) && BoxRendererDirectionUtils.Xl(n2, (byte)8)) {
               this.vec3dGameRendererHelper.pC(n3, n7);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)32) && BoxRendererDirectionUtils.Xl(n2, (byte)16)) {
               this.vec3dGameRendererHelper.pC(n4, n8);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)64) && BoxRendererDirectionUtils.Xl(n2, (byte)8)) {
               this.vec3dGameRendererHelper.pC(n5, n9);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)64) && BoxRendererDirectionUtils.Xl(n2, (byte)16)) {
               this.vec3dGameRendererHelper.pC(n6, n10);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)32) && BoxRendererDirectionUtils.Xl(n2, (byte)4)) {
               this.vec3dGameRendererHelper.pC(n3, n4);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)64) && BoxRendererDirectionUtils.Xl(n2, (byte)4)) {
               this.vec3dGameRendererHelper.pC(n5, n6);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)8) && BoxRendererDirectionUtils.Xl(n2, (byte)4)) {
               this.vec3dGameRendererHelper.pC(n3, n5);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)16) && BoxRendererDirectionUtils.Xl(n2, (byte)4)) {
               this.vec3dGameRendererHelper.pC(n4, n6);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)32) && BoxRendererDirectionUtils.Xl(n2, (byte)2)) {
               this.vec3dGameRendererHelper.pC(n7, n8);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)64) && BoxRendererDirectionUtils.Xl(n2, (byte)2)) {
               this.vec3dGameRendererHelper.pC(n9, n10);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)8) && BoxRendererDirectionUtils.Xl(n2, (byte)2)) {
               this.vec3dGameRendererHelper.pC(n7, n9);
            }

            if (BoxRendererDirectionUtils.Xl(n2, (byte)16) && BoxRendererDirectionUtils.Xl(n2, (byte)2)) {
               this.vec3dGameRendererHelper.pC(n8, n10);
            }
         }

         this.vec3dGameRendererHelper.pF();
      }
   }

   public void YN(int n, int n2, int n3, Color color, long l, int n4) {
      long temp = 45206904086668L ^ l ^ 48059560027898L;
      int temp2 = (int)((45206904086668L ^ l ^ 48059560027898L) >>> 48);
      int n5 = (int)((45206904086668L ^ l ^ 48059560027898L) << 16 >>> 48);
      int n6 = (int)(temp << 32 >>> 32);
      this.YM((short)n5, n, n2, n3, n + 1, n2 + 1, n3 + 1, n6, color, n4);
   }

   public void YO(
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      double d7,
      double d8,
      double d9,
      double d10,
      double d11,
      double d12,
      Color color,
      Color color2,
      Color color3,
      Color color4
   ) {
      this.vec3dGameRendererHelper2
         .pD(
            this.vec3dGameRendererHelper2.py(d, d2, d3).pA(color4).pB(),
            this.vec3dGameRendererHelper2.py(d4, d5, d6).pA(color).pB(),
            this.vec3dGameRendererHelper2.py(d7, d8, d9).pA(color2).pB(),
            this.vec3dGameRendererHelper2.py(d10, d11, d12).pA(color3).pB()
         );
   }

   public void YP(
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      double d7,
      double d8,
      double d9,
      double d10,
      double d11,
      double d12,
      Color color
   ) {
      this.YO(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, color, color, color, color);
   }

   public void YQ(double d, double d2, double d3, double d4, double d5, double d6, Color color) {
      this.YP(d, d2, d3, d, d5, d3, d4, d5, d6, d4, d2, d6, color);
   }

   public void YR(double d, double d2, double d3, double d4, double d5, Color color) {
      this.YP(d, d2, d3, d, d2, d5, d4, d2, d5, d4, d2, d3, color);
   }

   public void YS(double d, double d2, double d3, double d4, double d5, double d6, Color color, Color color2) {
      this.YO(d, d2, d3, d, d5, d3, d4, d5, d6, d4, d2, d6, color, color, color2, color2);
   }

   public void YT(
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      double d7,
      double d8,
      double d9,
      double d10,
      double d11,
      double d12,
      Color color,
      Color color2,
      ShapeMode shapeMode
   ) {
      if (shapeMode.KK()) {
         int n = this.vec3dGameRendererHelper.py(d, d2, d3).pA(color2).pB();
         int n2 = this.vec3dGameRendererHelper.py(d4, d5, d6).pA(color2).pB();
         int n3 = this.vec3dGameRendererHelper.py(d7, d8, d9).pA(color2).pB();
         int n4 = this.vec3dGameRendererHelper.py(d10, d11, d12).pA(color2).pB();
         this.vec3dGameRendererHelper.pC(n, n2);
         this.vec3dGameRendererHelper.pC(n2, n3);
         this.vec3dGameRendererHelper.pC(n3, n4);
         this.vec3dGameRendererHelper.pC(n4, n);
      }

      if (shapeMode.KL()) {
         this.YP(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, color);
      }
   }

   public void YU(double d, double d2, double d3, double d4, double d5, double d6, Color color, Color color2, ShapeMode shapeMode) {
      this.YT(d, d2, d3, d, d5, d3, d4, d5, d6, d4, d2, d6, color, color2, shapeMode);
   }

   public void YV(double d, double d2, double d3, double d4, double d5, Color color, Color color2, ShapeMode shapeMode) {
      this.YT(d, d2, d3, d, d2, d5, d4, d2, d5, d4, d2, d3, color, color2, shapeMode);
   }

   public void YW(double d, double d2, double d3, double d4, double d5, double d6, Color color, int n) {
      if (color.getAlpha() > 0) {
         int n2 = this.vec3dGameRendererHelper2.py(d, d2, d3).pA(color).pB();
         int n3 = this.vec3dGameRendererHelper2.py(d, d2, d6).pA(color).pB();
         int n4 = this.vec3dGameRendererHelper2.py(d4, d2, d3).pA(color).pB();
         int n5 = this.vec3dGameRendererHelper2.py(d4, d2, d6).pA(color).pB();
         int n6 = this.vec3dGameRendererHelper2.py(d, d5, d3).pA(color).pB();
         int n7 = this.vec3dGameRendererHelper2.py(d, d5, d6).pA(color).pB();
         int n8 = this.vec3dGameRendererHelper2.py(d4, d5, d3).pA(color).pB();
         int n9 = this.vec3dGameRendererHelper2.py(d4, d5, d6).pA(color).pB();
         if (n == 0) {
            this.vec3dGameRendererHelper2.pD(n2, n3, n7, n6);
            this.vec3dGameRendererHelper2.pD(n4, n8, n9, n5);
            this.vec3dGameRendererHelper2.pD(n2, n6, n8, n4);
            this.vec3dGameRendererHelper2.pD(n3, n5, n9, n7);
            this.vec3dGameRendererHelper2.pD(n2, n4, n5, n3);
            this.vec3dGameRendererHelper2.pD(n6, n7, n9, n8);
         } else {
            if (BoxRendererDirectionUtils.Xl(n, (byte)32)) {
               this.vec3dGameRendererHelper2.pD(n2, n3, n7, n6);
            }

            if (BoxRendererDirectionUtils.Xl(n, (byte)64)) {
               this.vec3dGameRendererHelper2.pD(n4, n8, n9, n5);
            }

            if (BoxRendererDirectionUtils.Xl(n, (byte)8)) {
               this.vec3dGameRendererHelper2.pD(n2, n6, n8, n4);
            }

            if (BoxRendererDirectionUtils.Xl(n, (byte)16)) {
               this.vec3dGameRendererHelper2.pD(n3, n5, n9, n7);
            }

            if (BoxRendererDirectionUtils.Xl(n, (byte)4)) {
               this.vec3dGameRendererHelper2.pD(n2, n4, n5, n3);
            }

            if (BoxRendererDirectionUtils.Xl(n, (byte)2)) {
               this.vec3dGameRendererHelper2.pD(n6, n7, n9, n8);
            }
         }

         this.vec3dGameRendererHelper2.pF();
      }
   }

   public void YX(int n, int n2, int n3, Color color, int n4) {
      this.YW(n, n2, n3, n + 1, n2 + 1, n3 + 1, color, n4);
   }

   public void YY(double d, double d2, double d3, double d4, double d5, double d6, Color color, Color color2, ShapeMode shapeMode, int n) {
      if (shapeMode.KK()) {
         this.YM((short)15019, d, d2, d3, d4, d5, d6, -109391825, color2, n);
      }

      if (shapeMode.KL()) {
         this.YW(d, d2, d3, d4, d5, d6, color, n);
      }
   }

   public void YZ(BlockPos pos, Color color, Color color2, ShapeMode shapeMode) {
      if (shapeMode.KK()) {
         this.YM((short)15019, pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, -109391825, color2, 0);
      }

      if (shapeMode.KL()) {
         this.YW(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, color, 0);
      }
   }

   public void Za(Box box, Color color, Color color2, ShapeMode shapeMode, int n) {
      if (shapeMode.KK()) {
         this.YM((short)15019, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, -109391825, color2, n);
      }

      if (shapeMode.KL()) {
         this.YW(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color, n);
      }
   }

   public void Zb(double d, double d2, double d3, double d4, double d5, double d6, Color color, Color color2, ShapeMode shapeMode) {
      if (shapeMode.KK()) {
         this.YM((short)15019, d, d2, d3, d4, d5, d6, -109391825, color2, 0);
      }

      if (shapeMode.KL()) {
         this.YW(d, d2, d3, d4, d5, d6, color, 0);
      }
   }

   public void Zc(BlockPos pos, Color color, Color color2, ShapeMode shapeMode) {
      if (shapeMode.KK()) {
         this.YM((short)15019, pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, -109391825, color2, 0);
      }

      if (shapeMode.KL()) {
         this.YW(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, color, 0);
      }
   }

   public void Zd(Box box, Color color, Color color2, ShapeMode shapeMode) {
      if (shapeMode.KK()) {
         this.YM((short)15019, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, -109391825, color2, 0);
      }

      if (shapeMode.KL()) {
         this.YW(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color, 0);
      }
   }

   public void Ze(Box box, ShapeMode shapeMode, Color color, Color color2) {
      double d = box.minX;
      double d2 = box.minY;
      double d3 = box.minZ;
      double d4 = box.maxX;
      double d5 = box.maxY;
      double d6 = box.maxZ;
      if (shapeMode.KK()) {
         this.YI(d, d2, d3, d, d5, d3, color2, color);
         this.YI(d, d2, d6, d, d5, d6, color2, color);
         this.YI(d4, d2, d3, d4, d5, d3, color2, color);
         this.YI(d4, d2, d6, d4, d5, d6, color2, color);
         this.YJ(d, d2, d3, d4, d2, d3, color2);
         this.YJ(d, d5, d3, d4, d5, d3, color);
         this.YJ(d, d2, d6, d4, d2, d6, color2);
         this.YJ(d, d5, d6, d4, d5, d6, color);
         this.YJ(d, d2, d3, d, d2, d6, color2);
         this.YJ(d, d5, d3, d, d5, d6, color);
         this.YJ(d4, d2, d3, d4, d2, d6, color2);
         this.YJ(d4, d5, d3, d4, d5, d6, color);
      }

      if (shapeMode.KL()) {
         this.YP(d, d5, d3, d, d5, d6, d4, d5, d6, d4, d5, d3, color);
         this.YP(d, d2, d3, d, d2, d6, d4, d2, d6, d4, d2, d3, color2);
         this.YS(d, d2, d3, d4, d5, d3, color, color2);
         this.YS(d, d2, d6, d4, d5, d6, color, color2);
         this.YS(d, d2, d3, d, d5, d6, color, color2);
         this.YS(d4, d2, d3, d4, d5, d6, color, color2);
      }
   }
}
