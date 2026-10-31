package pw.hachimi.client.utils.render;

import com.mojang.blaze3d.platform.GlStateManager;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import pw.hachimi.client.Mc;
import java.nio.FloatBuffer;
import java.util.List;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL32C;
import pw.hachimi.client.mixin.BufferRendererMixin;
import pw.hachimi.client.model.render.UnidentifiedInterface16;

public class RenderUtils2 {
   private static final FloatBuffer a = BufferUtils.createFloatBuffer(16);
   private static final UnidentifiedInterface16 b = Jv("DEPTH");
   private static final UnidentifiedInterface16 c = Jv("BLEND");
   private static final UnidentifiedInterface16 d = Jv("CULL");
   private static final UnidentifiedInterface16 e = Jv("SCISSOR");
   private static boolean f;
   private static boolean g;
   private static boolean h;
   private static boolean i;
   public static int j;
   private static int k;

   private RenderUtils2() {
   }

   public static int Iq() {
      return GlStateManager._glGenVertexArrays();
   }

   public static int Ir() {
      return GlStateManager._glGenBuffers();
   }

   public static int Is() {
      return GlStateManager._genTexture();
   }

   public static int It() {
      return GlStateManager.glGenFramebuffers();
   }

   public static void Iu(int n) {
      GlStateManager._glDeleteBuffers(n);
   }

   public static void Iv(int n) {
      GlStateManager._glDeleteVertexArrays(n);
   }

   public static void Iw(int n) {
      GlStateManager.glDeleteShader(n);
   }

   public static void Ix(int n) {
      GlStateManager._deleteTexture(n);
   }

   public static void Iy(int n) {
      GlStateManager._glDeleteFramebuffers(n);
   }

   public static void Iz(int n) {
      GlStateManager.glDeleteProgram(n);
   }

   public static void IA(int n) {
      GlStateManager._glBindVertexArray(n);
      BufferRendererMixin.setCurrentVertexBuffer(null);
   }

   public static void IB(int n) {
      GlStateManager._glBindBuffer(34962, n);
   }

   public static void IC(int n) {
      if (n != 0) {
         k = j;
      }

      GlStateManager._glBindBuffer(34963, n != 0 ? n : k);
   }

   public static void ID(int n) {
      GlStateManager._glBindFramebuffer(36160, n);
   }

   public static void IE(int n, ByteBuffer byteBuffer) {
      GlStateManager._glBufferData(n, byteBuffer, 35048);
   }

   public static void IF(int n, int n2) {
      GlStateManager._drawElements(n, n2, 5125, 0L);
   }

   public static void IG(int n) {
      GlStateManager._enableVertexAttribArray(n);
   }

   public static void IH(int n, int n2, int n3, boolean flag, int n4, long l) {
      GlStateManager._vertexAttribPointer(n, n2, n3, flag, n4, l);
   }

   public static int II(int n) {
      return GlStateManager.glCreateShader(n);
   }

   public static void IJ(int n, String str) {
      GlStateManager.glShaderSource(n, List.of(str));
   }

   public static String IK(int n) {
      GlStateManager.glCompileShader(n);
      return GlStateManager.glGetShaderi(n, 35713) == 0 ? GlStateManager.glGetShaderInfoLog(n, 512) : null;
   }

   public static int IL() {
      return GlStateManager.glCreateProgram();
   }

   public static String IM(int n, int n2, int n3) {
      GlStateManager.glAttachShader(n, n2);
      GlStateManager.glAttachShader(n, n3);
      GlStateManager.glLinkProgram(n);
      return GlStateManager.glGetProgrami(n, 35714) == 0 ? GlStateManager.glGetProgramInfoLog(n, 512) : null;
   }

   public static void IN(int n) {
      GlStateManager._glUseProgram(n);
   }

   public static void IO(int n, int n2) {
      GlStateManager._viewport(0, 0, n, n2);
   }

   public static int IP(int n, String str) {
      return GlStateManager._glGetUniformLocation(n, str);
   }

   public static void IQ(int n, int n2) {
      GlStateManager._glUniform1i(n, n2);
   }

   public static void IR(int n, float f2) {
      GL32C.glUniform1f(n, f2);
   }

   public static void IS(int n, float f2, float f3) {
      GL32C.glUniform2f(n, f2, f3);
   }

   public static void IT(int n, float f2, float f3, float f4) {
      GL32C.glUniform3f(n, f2, f3, f4);
   }

   public static void IU(int n, float f2, float f3, float f4, float f5) {
      GL32C.glUniform4f(n, f2, f3, f4, f5);
   }

   public static void IV(int n, float[] fs) {
      GL32C.glUniform3fv(n, fs);
   }

   public static void IW(int n, Matrix4f matrix4f) {
      matrix4f.get(a);
      GlStateManager._glUniformMatrix4(n, false, a);
   }

   public static void IX(int n, int n2) {
      GlStateManager._pixelStore(n, n2);
   }

   public static void IY(int n, int n2) {
      GlStateManager._texParameter(3553, n, n2);
   }

   public static void IZ(int n, int n2, int n3, int n4, ByteBuffer byteBuffer) {
      GL32C.glTexImage2D(3553, 0, n, n2, n3, 0, n4, 5121, byteBuffer);
   }

   public static void Ja() {
      IX(3312, 0);
      IX(3313, 0);
      IX(3314, 0);
      IX(32878, 0);
      IX(3315, 0);
      IX(3316, 0);
      IX(32877, 0);
      IX(3317, 4);
   }

   public static void Jb() {
      GL32C.glGenerateMipmap(3553);
   }

   public static void Jc(int n) {
      GlStateManager._glFramebufferTexture2D(36160, 36064, 3553, n, 0);
   }

   public static void Jd(int n) {
      GlStateManager._clearColor(0.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager._clear(n, false);
   }

   public static void Je() {
      f = b.aaO();
      g = c.aaO();
      h = d.aaO();
      i = e.aaO();
   }

   public static void Jf() {
      b.aaP(f);
      c.aaP(g);
      d.aaP(h);
      e.aaP(i);
      Jq();
   }

   public static void Jg() {
      GlStateManager._enableDepthTest();
   }

   public static void Jh() {
      GlStateManager._disableDepthTest();
   }

   public static void Ji() {
      GlStateManager._enableBlend();
      GlStateManager._blendFunc(770, 771);
   }

   public static void Jj() {
      GlStateManager._enableBlend();
      GlStateManager._blendFunc(770, 32772);
   }

   public static void Jk() {
      GlStateManager._disableBlend();
   }

   public static void Jl() {
      GlStateManager._enableCull();
   }

   public static void Jm() {
      GlStateManager._disableCull();
   }

   public static void Jn() {
      GlStateManager._enableScissorTest();
   }

   public static void Jo() {
      GlStateManager._disableScissorTest();
   }

   public static void Jp() {
      GL32C.glEnable(2848);
      GL32C.glLineWidth(1.0F);
   }

   public static void Jq() {
      GL32C.glDisable(2848);
   }

   public static void Jr(Identifier id) {
      GlStateManager._activeTexture(33984);
      Mc.client.getTextureManager().bindTexture(id);
   }

   public static void Js(int n) {
      GlStateManager._activeTexture(33984);
      GlStateManager._bindTexture(n);
   }

   public static void Jt(int n) {
      Js(n);
   }

   public static void Ju() {
      GlStateManager._activeTexture(33984);
   }

   private static UnidentifiedInterface16 Jv(String str) {
      try {
         Field field = GlStateManager.class.getDeclaredField(str);
         field.setAccessible(true);
         Object obj = field.get(null);
         String str2 = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "com.mojang.blaze3d.platform.GlStateManager$class_1018");
         Field field2 = null;
         Field[] fields = obj.getClass().getDeclaredFields();

         for (int n = 0; n < fields.length; n++) {
            Field field3 = fields[n];
            if (field3.getType().getName().equals(str2)) {
               field2 = field3;
               break;
            }
         }

         field2.setAccessible(true);
         return (UnidentifiedInterface16)field2.get(obj);
      } catch (Exception exception) {
         throw new IllegalStateException("Could not find GL state tracker '" + str + "'", exception);
      }
   }
}
