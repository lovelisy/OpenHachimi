package pw.hachimi.client.utils.render;

import pw.hachimi.client.Mc;

public class ChamsFramebufferHelper {
   private int a;
   public int b;
   public double c = 1.0;
   public int d;
   public int e;

   public ChamsFramebufferHelper(double d2) {
      this.c = d2;
      this.Lp();
   }

   public ChamsFramebufferHelper() {
      this.Lp();
   }

   private void Lp() {
      this.a = RenderUtils2.It();
      this.Lq();
      this.b = RenderUtils2.Is();
      RenderUtils2.Jt(this.b);
      RenderUtils2.Ja();
      RenderUtils2.IY(10242, 33071);
      RenderUtils2.IY(10243, 33071);
      RenderUtils2.IY(10241, 9729);
      RenderUtils2.IY(10240, 9729);
      this.d = (int)(Mc.client.getWindow().getFramebufferWidth() * this.c);
      this.e = (int)(Mc.client.getWindow().getFramebufferHeight() * this.c);
      RenderUtils2.IZ(6407, this.d, this.e, 6407, null);
      RenderUtils2.Jc(this.b);
      this.Ls();
   }

   public void Lq() {
      RenderUtils2.ID(this.a);
   }

   public void Lr() {
      RenderUtils2.IO(this.d, this.e);
   }

   public void Ls() {
      Mc.client.getFramebuffer().beginWrite(false);
   }

   public void Lt() {
      RenderUtils2.Iy(this.a);
      RenderUtils2.Ix(this.b);
      this.Lp();
   }
}
