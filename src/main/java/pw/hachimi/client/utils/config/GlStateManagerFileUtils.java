package pw.hachimi.client.utils.config;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Base64.Encoder;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class GlStateManagerFileUtils {
   public static boolean a;

   public static void main(String[] strs) throws Exception, IOException {
      Path path = Paths.get("shaders");
      Path path2 = Paths.get("src", "main", "resources", "assets", "hachimiclient", "shaders", "program");

      try (DirectoryStream directoryStream = Files.newDirectoryStream(path, "*.fsh")) {
         Iterator iterator2 = directoryStream.iterator();

         while (iterator2.hasNext()) {
            Path path3 = (Path)iterator2.next();
            String str = path3.getFileName().toString();
            Path path4 = path2.resolve(str);
            String str2 = Files.readString(path3);
            String str3 = avi(str2);
            Files.writeString(path4, "#version 150//HachimiShader" + str3);
            System.out.println("Processed: " + str);
         }
      }
   }

   public static String avi(String str) {
      try {
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         Charset charset2 = StandardCharsets.UTF_8;
         byte[] bs = "liyiwocaonimanimadabirenrenchazuochachayouchachachadenimabikaihuanimadabi".getBytes(charset2);
         bs = Arrays.copyOf(MessageDigest.getInstance("SHA-256").digest(bs), 16);
         SecretKeySpec secretKeySpec = new SecretKeySpec(bs, "AES");
         byte[] bs2 = new byte[16];
         IvParameterSpec ivParameterSpec = new IvParameterSpec(bs2);
         cipher.init(1, secretKeySpec, ivParameterSpec);
         Encoder temp = Base64.getEncoder();
         Charset charset3 = StandardCharsets.UTF_8;
         byte[] bs3 = cipher.doFinal(str.getBytes(charset3));
         return temp.encodeToString(bs3);
      } catch (Exception exception) {
         return null;
      }
   }

   public static void avj(int n, List list, CallbackInfo callbackInfo) {
      StringBuilder stringBuilder = new StringBuilder();
      Iterator iterator2 = list.iterator();

      while (iterator2.hasNext()) {
         stringBuilder.append((String)iterator2.next());
      }

      String str = stringBuilder.toString();
      int n2 = str.indexOf("//HachimiShader");
      if (n2 != -1) {
         callbackInfo.cancel();

         try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            Charset charset2 = StandardCharsets.UTF_8;
            byte[] bs = "liyiwocaonimanimadabirenrenchazuochachayouchachachadenimabikaihuanimadabi".getBytes(charset2);
            bs = Arrays.copyOf(MessageDigest.getInstance("SHA-256").digest(bs), 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bs, "AES");
            byte[] bs2 = new byte[16];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bs2);
            cipher.init(2, secretKeySpec, ivParameterSpec);
            byte[] bs3 = cipher.doFinal(Base64.getDecoder().decode(str.substring(n2 + "//HachimiShader".length())));
            ByteBuffer byteBuffer = MemoryUtil.memAlloc(bs3.length + 1);

            try {
               byteBuffer.put(bs3);
               byteBuffer.put((byte)0);
               byteBuffer.flip();
               MemoryStack memoryStack = MemoryStack.stackPush();

               try {
                  PointerBuffer pointerBuffer = memoryStack.mallocPointer(1);
                  pointerBuffer.put(byteBuffer);
                  GL20C.nglShaderSource(n, 1, pointerBuffer.address0(), 0L);
               } finally {
                  if (memoryStack != null) {
                     memoryStack.close();
                  }
               }
            } finally {
               MemoryUtil.memFree(byteBuffer);
            }
         } catch (Exception exception) {
         }
      }
   }
}
