package pw.hachimi.client.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.nio.ByteBuffer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4fStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.render.LinesTrianglesMode;
import pw.hachimi.client.model.render.UnidentifiedOption;
import pw.hachimi.client.render.ColorManager;

public class Vec3dGameRendererHelper {
   public static boolean b;
   public boolean c;
   public double d;
   private final LinesTrianglesMode linesTrianglesMode;
   private final int f;
   private final int g_;
   private final int h_;
   private final int i;
   private ByteBuffer byteBuffer;
   private long k;
   private long l;
   private ByteBuffer byteBuffer2;
   private long n;
   private int o;
   private int p;
   private boolean q;
   private boolean r;
   private double s;
   private double t;
   private boolean u;

   public Vec3dGameRendererHelper(LinesTrianglesMode linesTrianglesMode2, UnidentifiedOption... value) {
      this.c = false;
      this.d = 1.0;
      int n2 = 0;

      for (int n3 = 0; n3 < value.length; n3++) {
         n2 += value[n3].f;
      }

      this.linesTrianglesMode = linesTrianglesMode2;
      this.f = n2 * linesTrianglesMode2.c;
      this.byteBuffer = BufferUtils.createByteBuffer(this.f * 256 * 4);
      this.k = MemoryUtil.memAddress0(this.byteBuffer);
      this.byteBuffer2 = BufferUtils.createByteBuffer(linesTrianglesMode2.c * 512 * 4);
      this.n = MemoryUtil.memAddress0(this.byteBuffer2);
      this.g_ = RenderUtils2.Iq();
      RenderUtils2.IA(this.g_);
      this.h_ = RenderUtils2.Ir();
      RenderUtils2.IB(this.h_);
      this.i = RenderUtils2.Ir();
      RenderUtils2.IC(this.i);
      int n4 = 0;

      for (int n5 = 0; n5 < value.length; n5++) {
         UnidentifiedOption unidentifiedOption = value[n5];
         RenderUtils2.IG(n5);
         RenderUtils2.IH(n5, unidentifiedOption.e, unidentifiedOption.Ew(), unidentifiedOption.g, n2, (long)n4);
         n4 += unidentifiedOption.f;
      }

      RenderUtils2.IA(0);
      RenderUtils2.IB(0);
      RenderUtils2.IC(0);
   }

   public void pw() {
      RenderUtils2.Iu(this.i);
      RenderUtils2.Iu(this.h_);
      RenderUtils2.Iv(this.g_);
   }

   public void px() {
      if (this.q) {
         throw new IllegalStateException("Mesh.begin() called while already building.");
      }

      this.l = this.k;
      this.o = 0;
      this.p = 0;
      this.q = true;
      this.r = UnidentifiedUtils8.b;
      if (this.r) {
         Vec3d vec = Mc.client.gameRenderer.getCamera().getPos();
         this.s = vec.x;
         this.t = vec.z;
      } else {
         this.s = 0.0;
         this.t = 0.0;
      }
   }

   public Vec3dGameRendererHelper py(double d2, double d3, double d4) {
      long l2 = this.l;
      MemoryUtil.memPutFloat(l2, (float)(d2 - this.s));
      MemoryUtil.memPutFloat(l2 + 4L, (float)d3);
      MemoryUtil.memPutFloat(l2 + 8L, (float)(d4 - this.t));
      this.l += 12L;
      return this;
   }

   public Vec3dGameRendererHelper pz(double d2, double d3) {
      long l2 = this.l;
      MemoryUtil.memPutFloat(l2, (float)d2);
      MemoryUtil.memPutFloat(l2 + 4L, (float)d3);
      this.l += 8L;
      return this;
   }

   public Vec3dGameRendererHelper pA(Color color) {
      long l2 = this.l;
      MemoryUtil.memPutByte(l2, (byte)color.getRed());
      MemoryUtil.memPutByte(l2 + 1L, (byte)color.getGreen());
      MemoryUtil.memPutByte(l2 + 2L, (byte)color.getBlue());
      MemoryUtil.memPutByte(l2 + 3L, (byte)(color.getAlpha() * (float)this.d));
      this.l += 4L;
      return this;
   }

   public int pB() {
      return this.o++;
   }

   public void pC(int n2, int n3) {
      long l2 = this.n + (long)this.p * 4L;
      MemoryUtil.memPutInt(l2, n2);
      MemoryUtil.memPutInt(l2 + 4L, n3);
      this.p += 2;
      this.pF();
   }

   public void pD(int n2, int n3, int n4, int n5) {
      long l2 = this.n + (long)this.p * 4L;
      MemoryUtil.memPutInt(l2, n2);
      MemoryUtil.memPutInt(l2 + 4L, n3);
      MemoryUtil.memPutInt(l2 + 8L, n4);
      MemoryUtil.memPutInt(l2 + 12L, n4);
      MemoryUtil.memPutInt(l2 + 16L, n5);
      MemoryUtil.memPutInt(l2 + 20L, n2);
      this.p += 6;
      this.pF();
   }

   public void pE(int n2, int n3, int n4) {
      long l2 = this.n + (long)this.p * 4L;
      MemoryUtil.memPutInt(l2, n2);
      MemoryUtil.memPutInt(l2 + 4L, n3);
      MemoryUtil.memPutInt(l2 + 8L, n4);
      this.p += 3;
      this.pF();
   }

   public void pF() {
      if ((this.o + 1) * this.f >= this.byteBuffer.capacity()) {
         int n2 = this.pM();
         int n3 = this.byteBuffer.capacity() * 2;
         if (n3 % this.f != 0) {
            n3 += n3 % this.f;
         }

         ByteBuffer byteBuffer3 = BufferUtils.createByteBuffer(n3);
         MemoryUtil.memCopy(MemoryUtil.memAddress0(this.byteBuffer), MemoryUtil.memAddress0(byteBuffer3), n2);
         this.byteBuffer = byteBuffer3;
         this.k = MemoryUtil.memAddress0(this.byteBuffer);
         this.l = this.k + n2;
      }

      if (this.p * 4 >= this.byteBuffer2.capacity()) {
         int n4 = this.byteBuffer2.capacity() * 2;
         if (n4 % this.linesTrianglesMode.c != 0) {
            n4 += n4 % (this.linesTrianglesMode.c * 4);
         }

         ByteBuffer byteBuffer4 = BufferUtils.createByteBuffer(n4);
         MemoryUtil.memCopy(MemoryUtil.memAddress0(this.byteBuffer2), MemoryUtil.memAddress0(byteBuffer4), this.p * 4L);
         this.byteBuffer2 = byteBuffer4;
         this.n = MemoryUtil.memAddress0(this.byteBuffer2);
      }
   }

   public void pG() {
      if (!this.q) {
         throw new IllegalStateException("Mesh.end() called while not building.");
      }

      if (this.p > 0) {
         RenderUtils2.IB(this.h_);
         RenderUtils2.IE(34962, this.byteBuffer.limit(this.pM()));
         RenderUtils2.IB(0);
         RenderUtils2.IC(this.i);
         RenderUtils2.IE(34963, this.byteBuffer2.limit(this.p * 4));
         RenderUtils2.IC(0);
      }

      this.q = false;
   }

   public void pH(long l2, int n2, MatrixStack matrices) {
      label30: {
         label29: {
            RenderUtils2.Je();
            if (this.c) {
               RenderUtils2.Jg();
               if (l2 <= 0L) {
                  break label29;
               }
            } else {
               RenderUtils2.Jh();
            }

            if (l2 <= 0L) {
               break label30;
            }
         }

         if (b) {
            RenderUtils2.Jj();
         } else {
            RenderUtils2.Ji();
         }
      }

      RenderUtils2.Jm();
      Vec3dGameRendererHelper temp = this;
      if (n2 < 0) {
         if (this.r) {
            Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
            matrix4fStack.pushMatrix();
            if (n2 < 0 && matrices != null) {
               matrix4fStack.mul(matrices.peek().getPositionMatrix());
            }

            Vec3d vec = Mc.client.gameRenderer.getCamera().getPos();
            matrix4fStack.translate(0.0F, (float)(-vec.y), 0.0F);
         }

         temp = this;
      }

      temp.u = true;
   }

   public void pI(MatrixStack matrices) {
      if (this.q) {
         this.pG();
      }

      if (this.p > 0) {
         boolean flag = this.u;
         if (!flag) {
            this.pH(4604L, -883759551, matrices);
         }

         this.pL();
         ColorManager.colorManager.ajJ();
         RenderUtils2.IA(this.g_);
         RenderUtils2.IF(this.linesTrianglesMode.eY(), this.p);
         RenderUtils2.IA(0);
         if (!flag) {
            this.pJ();
         }
      }
   }

   public void pJ() {
      if (this.r) {
         RenderSystem.getModelViewStack().popMatrix();
      }

      RenderUtils2.Jf();
      this.u = false;
   }

   public boolean pK() {
      return this.q;
   }

   protected void pL() {
   }

   private int pM() {
      return (int)(this.l - this.k);
   }
}
