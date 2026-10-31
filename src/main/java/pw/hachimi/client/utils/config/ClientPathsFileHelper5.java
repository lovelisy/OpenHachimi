package pw.hachimi.client.utils.config;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import pw.hachimi.client.config.FileBase;
import pw.hachimi.client.utils.render.FontLoader;

public class ClientPathsFileHelper5 extends FileBase {
   public static boolean b_1;

   public ClientPathsFileHelper5(Path path) {
      super(path, "font");
   }

   @Override
   public void aoP() {
      try {
         Path path = this.getPath();
         if (!Files.exists(path, new LinkOption[0])) {
            Files.createFile(path, new FileAttribute[0]);
         }

         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("font", FontLoader.text);
         this.aoM(path, this.aoI(jsonObject));
      } catch (IOException ioException) {
         ioException.printStackTrace();
      }
   }

   @Override
   public void aoQ() {
      try {
         Path path = this.getPath();
         if (Files.exists(path, new LinkOption[0])) {
            String str = this.aoH(path);
            JsonObject jsonObject = this.aoJ(str);
            FontLoader.text = jsonObject.get("font").getAsString();
         }
      } catch (IOException ioException) {
         ioException.printStackTrace();
      }
   }
}
