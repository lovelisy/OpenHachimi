package pw.hachimi.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import pw.hachimi.client.Bootstrap;
import java.io.BufferedReader;

public abstract class FileBase {
   protected static final Gson b = (new GsonBuilder()).setLenient().setPrettyPrinting().create();
   private final String text;
   private final Path path;

   public FileBase(Path path2, String str) {
      LinkOption[] linkOptions = new LinkOption[0];
      if (!Files.exists(path2, linkOptions)) {
         Path temp = path2;
         byte temp2 = 0;

         try {
            FileAttribute[] fileAttributes = new FileAttribute[temp2];
            Files.createDirectory(temp, fileAttributes);
         } catch (IOException ioException) {
            Object[] objs = new Object[]{path2};
            Bootstrap.Oc("Could not create {} dir", objs);
            ioException.printStackTrace();
         }
      }

      this.text = path2.getFileName().toString();
      String str2 = this.aoR(str);
      this.path = path2.resolve(str2);
   }

   // restored with Procyon (compiles, call/field/constant fingerprint equals the deobf jar)
   public String aoH(final Path path) throws IOException {
       final StringBuilder sb = new StringBuilder();
       final BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
       while (true) {
           final BufferedReader bufferedReader2 = bufferedReader;
           try {
               final String line;
               if ((line = bufferedReader2.readLine()) != null) {
                   sb.append(line).append("\n");
                   continue;
               }
           }
           catch (final Throwable t) {
               Label_0085: {
                   if (bufferedReader == null) {
                       break Label_0085;
                   }
                   final BufferedReader bufferedReader3 = bufferedReader;
                   try {
                       bufferedReader3.close();
                       throw t;
                   }
                   catch (final Throwable t2) {
                       t.addSuppressed(t2);
                   }
               }
           }
           break;
       }
       if (bufferedReader != null) {
           bufferedReader.close();
       }
       return sb.toString();
   }

   public String aoI(Object obj) {
      return b.toJson(obj);
   }

   public JsonObject aoJ(String str) {
      return (JsonObject)this.aoL(str, JsonObject.class);
   }

   public JsonArray aoK(String str) {
      return (JsonArray)this.aoL(str, JsonArray.class);
   }

   protected Object aoL(String str, Class classValue) {
      try {
         return b.fromJson(str, classValue);
      } catch (JsonSyntaxException jsonSyntaxException) {
         Bootstrap.Ob("Invalid json syntax!");
         jsonSyntaxException.printStackTrace();
         return null;
      }
   }

   public void aoM(Path path2, String str) throws IOException {
      OpenOption[] openOptions = new OpenOption[0];
      OutputStream outputStream = Files.newOutputStream(path2, openOptions);
      Charset charset2 = StandardCharsets.UTF_8;
      byte[] bs = str.getBytes(charset2);
      OutputStream temp = outputStream;
      int n2 = bs.length;
      temp.write(bs, 0, n2);
      outputStream.close();
   }

   public String getText() {
      return this.text;
   }

   public Path getPath() {
      return this.path;
   }

   public abstract void aoP();

   public abstract void aoQ();

   private String aoR(String str) {
      Object[] objs = new Object[]{str};
      return String.format("%s.json", objs).toLowerCase();
   }
}
