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
import pw.hachimi.client.config.Vec3dConfigContainer;
import pw.hachimi.client.manager.Managers;

public class ClientPathsFileHelper extends FileBase {
   public static boolean g;

   public ClientPathsFileHelper(Path path) {
      super(path, "waypoints");
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   @Override
   public void aoP() {
      ClientPathsFileHelper temp = this;

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
         Iterator iterator2 = Managers.unidentifiedHelper19.getList().iterator();

         while (iterator2.hasNext()) {
            Vec3dConfigContainer vec3dConfigContainer = (Vec3dConfigContainer)iterator2.next();
            if (vec3dConfigContainer instanceof UnidentifiedHelper14) {
               jsonArray.add(vec3dConfigContainer.LT(25681, 44222, '\u218e'));
            }
         }
      } catch (IOException ioException) {
         Bootstrap.Ob("Could not save file for waypoints.json!");
         ioException.printStackTrace();
         return;
      }

      temp = this;
      Path temp2 = path;

      try {
         temp.aoM(temp2, this.aoI(jsonArray));
      } catch (IOException ioException2) {
         Bootstrap.Ob("Could not save file for waypoints.json!");
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
         if (jsonArray != null) {
            Iterator iterator2 = jsonArray.asList().iterator();

            while (iterator2.hasNext()) {
               JsonObject jsonObject = ((JsonElement)iterator2.next()).getAsJsonObject();
               if (jsonObject.has("tag") && jsonObject.has("dimension") && jsonObject.has("ip")) {
                  Managers.unidentifiedHelper19.akQ(
                     new UnidentifiedHelper14(
                        jsonObject.get("tag").getAsString(),
                        jsonObject.get("ip").getAsString(),
                        jsonObject.get("dimension").getAsInt(),
                        jsonObject.get("x").getAsDouble(),
                        jsonObject.get("y").getAsDouble(),
                        jsonObject.get("z").getAsDouble()
                     )
                  );
               }
            }
         }
      } catch (IOException ioException) {
         Bootstrap.Ob("Could not read file for waypoints.json!");
         ioException.printStackTrace();
      }
   }
}
