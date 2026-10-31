package pw.hachimi.client.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import pw.hachimi.client.model.render.NearestLinearMode;
import pw.hachimi.client.model.render.RgbRgbaMode;

public class RenderHelper4 {
   public int a;
   public int b;
   private int c;
   private boolean d;

   public RenderHelper4(int n, char c2, int n2, byte[] bs, RgbRgbaMode rgbRgbaMode, NearestLinearMode nearestLinearMode, int n3, int n4, NearestLinearMode nearestLinearMode2) {
      if (RenderSystem.isOnRenderThread()) {
         this.QG(n, n2, bs, rgbRgbaMode, nearestLinearMode, nearestLinearMode2);
      } else {
         RenderSystem.recordRenderCall(() -> this.QM(n, n2, bs, rgbRgbaMode, nearestLinearMode, nearestLinearMode2));
      }
   }

   public RenderHelper4() {
   }

   protected void QG(int n, int n2, byte[] bs, RgbRgbaMode rgbRgbaMode, NearestLinearMode nearestLinearMode, NearestLinearMode nearestLinearMode2) {
      ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bs.length).put(bs);
      this.QH(n, n2, byteBuffer, rgbRgbaMode, nearestLinearMode, nearestLinearMode2);
   }

   public void QH(int n, int n2, ByteBuffer byteBuffer, RgbRgbaMode rgbRgbaMode, NearestLinearMode nearestLinearMode, NearestLinearMode nearestLinearMode2) {
      this.a = n;
      this.b = n2;
      if (!this.d) {
         this.c = RenderUtils2.Is();
         this.d = true;
      }

      this.QK();
      RenderUtils2.Ja();
      RenderUtils2.IY(10242, 10497);
      RenderUtils2.IY(10243, 10497);
      RenderUtils2.IY(10241, nearestLinearMode.wT());
      RenderUtils2.IY(10240, nearestLinearMode2.wT());
      byteBuffer.rewind();
      RenderUtils2.IZ(rgbRgbaMode.ajy(), n, n2, rgbRgbaMode.ajy(), byteBuffer);
      if (nearestLinearMode == NearestLinearMode.LinearMipmapLinear || nearestLinearMode2 == NearestLinearMode.LinearMipmapLinear) {
         RenderUtils2.Jb();
      }
   }

   public boolean QI() {
      return this.d;
   }

   public void QJ() {
      RenderUtils2.Js(this.c);
   }

   public void QK() {
      this.QJ();
   }

   public void QL() {
      RenderUtils2.Ix(this.c);
      this.d = false;
   }

   private void QM(int n, int n2, byte[] bs, RgbRgbaMode rgbRgbaMode, NearestLinearMode nearestLinearMode, NearestLinearMode nearestLinearMode2) {
      this.QG(n, n2, bs, rgbRgbaMode, nearestLinearMode, nearestLinearMode2);
   }
}
