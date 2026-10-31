package pw.hachimi.client.utils.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.config.FileBase;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.UnidentifiedHelper17;

public class ClientPathsFileHelper3 extends FileBase {
   public static boolean e;

   public ClientPathsFileHelper3(Path path) {
      super(path, "macros");
   }

   public ClientPathsFileHelper3(Path path, String str) {
      super(path, str);
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   @Override
   public void aoP() {
      ClientPathsFileHelper3 temp = this;

      Path path;
      JsonArray jsonArray;
      try {
         path = temp.getPath();
         Path path2 = path;
         LinkOption[] linkOptions = new LinkOption[0];
         if (!Files.exists(path2, linkOptions)) {
            Path path3 = path;
            FileAttribute[] fileAttributes = new FileAttribute[0];
            Files.createFile(path3, fileAttributes);
         }

         jsonArray = new JsonArray();
         Iterator iterator2 = Managers.playerScreenManager.getSet().iterator();

         while (iterator2.hasNext()) {
            UnidentifiedHelper17 unidentifiedHelper17 = (UnidentifiedHelper17)iterator2.next();
            jsonArray.add(unidentifiedHelper17.LT(25681, 44222, '\u218e'));
         }
      } catch (IOException ioException) {
         Bootstrap.Ob("Could not save macro file!");
         ioException.printStackTrace();
         return;
      }

      temp = this;
      Path temp2 = path;

      try {
         temp.aoM(temp2, this.aoI(jsonArray));
      } catch (IOException ioException2) {
         Bootstrap.Ob("Could not save macro file!");
         ioException2.printStackTrace();
      }
   }

   @Override
   public void aoQ() {
      try {
         Path path = this.getPath();
         if (!Files.exists(path, new LinkOption[0])) {
            return;
         }

         String str = this.aoH(path);
         JsonArray jsonArray = this.aoK(str);
         Iterator iterator2 = jsonArray.getAsJsonArray().iterator();

         while (iterator2.hasNext()) {
            JsonObject jsonObject = ((JsonElement)iterator2.next()).getAsJsonObject();
            if (jsonObject.has("id")) {
               String str2 = jsonObject.get("id").getAsString();
               UnidentifiedHelper17 unidentifiedHelper17 = Managers.playerScreenManager.akt(helper -> ys(str2, (UnidentifiedHelper17)helper));
               if (unidentifiedHelper17 != null) {
                  if (jsonObject.has("runOnRelease")) {
                     unidentifiedHelper17.d = jsonObject.get("runOnRelease").getAsBoolean();
                  }

                  if (jsonObject.has("value")) {
                     unidentifiedHelper17.agu(jsonObject.get("value").getAsInt());
                  }
               }
            }
         }
      } catch (IOException ioException) {
         Bootstrap.Ob("Could not read macro file!");
         ioException.printStackTrace();
      }
   }

   static boolean ys(String str, UnidentifiedHelper17 unidentifiedHelper17) {
      return unidentifiedHelper17.Kn().equals(str);
   }
}
