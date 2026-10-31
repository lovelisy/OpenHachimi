package pw.hachimi.client.utils.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.config.FileBase;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.config.UnidentifiedOption2;

public class ClientPathsFileHelper6 extends FileBase {
   private final UnidentifiedOption2 unidentifiedOption2;
   public static boolean a;

   public ClientPathsFileHelper6(Path path, UnidentifiedOption2 unidentifiedOption22) {
      super(path, unidentifiedOption22.name());
      this.unidentifiedOption2 = unidentifiedOption22;
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   @Override
   public void aoP() {
      ClientPathsFileHelper6 temp = this;

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
         Iterator iterator2 = Managers.friendManager.awz(this.unidentifiedOption2).iterator();

         while (iterator2.hasNext()) {
            String str = (String)iterator2.next();
            jsonArray.add(new JsonPrimitive(str));
         }
      } catch (IOException ioException) {
         Object[] objs = new Object[1];
         UnidentifiedOption2 unidentifiedOption22 = this.unidentifiedOption2;
         Object[] objs2 = objs;
         Object[] objs3 = objs;
         objs2[0] = unidentifiedOption22.name().toLowerCase();
         Bootstrap.Oc("Could not save file for {}.json!", objs3);
         ioException.printStackTrace();
         return;
      }

      temp = this;
      Path temp2 = path;

      try {
         temp.aoM(temp2, this.aoI(jsonArray));
      } catch (IOException ioException2) {
         Object[] objs4 = new Object[1];
         UnidentifiedOption2 unidentifiedOption23 = this.unidentifiedOption2;
         Object[] objs5 = objs4;
         Object[] objs6 = objs4;
         objs5[0] = unidentifiedOption23.name().toLowerCase();
         Bootstrap.Oc("Could not save file for {}.json!", objs6);
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
               JsonElement jsonElement = (JsonElement)iterator2.next();
               Managers.friendManager.awu(jsonElement.getAsString(), this.unidentifiedOption2);
            }
         }
      } catch (IOException ioException) {
         Object[] objs = new Object[]{this.unidentifiedOption2.name().toLowerCase()};
         Bootstrap.Oc("Could not read file for {}.json!", objs);
         ioException.printStackTrace();
      }
   }
}
