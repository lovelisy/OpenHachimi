package pw.hachimi.client.utils.config;

import com.google.gson.JsonArray;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.config.FileBase;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.client.UI;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ClientPathsFileHelper2 extends FileBase {
   public ClientPathsFileHelper2(Path path2) {
      super(path2, "clickgui");
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   @Override
   public void aoP() {
      ClientPathsFileHelper2 temp = this;

      Path path2;
      JsonArray jsonArray;
      try {
         path2 = temp.getPath();
         Path path3 = path2;
         LinkOption[] linkOptions = new LinkOption[0];
         if (!Files.exists(path3, linkOptions)) {
            Path path4 = path2;
            FileAttribute[] fileAttributes = new FileAttribute[0];
            Files.createFile(path4, fileAttributes);
         }

         jsonArray = new JsonArray();
         Iterator iterator2 = UI.drawScreenScreen.getList().iterator();

         while (iterator2.hasNext()) {
            CategoryPanel categoryPanel = (CategoryPanel)iterator2.next();
            jsonArray.add(categoryPanel.LT(25681, 44222, '\u218e'));
         }
      } catch (IOException ioException) {
         Bootstrap.Ob("Could not save clickgui file!");
         ioException.printStackTrace();
         return;
      }

      temp = this;
      Path temp2 = path2;

      try {
         temp.aoM(temp2, this.aoI(jsonArray));
      } catch (IOException ioException2) {
         Bootstrap.Ob("Could not save clickgui file!");
         ioException2.printStackTrace();
      }
   }

   @Override
   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public void aoQ() {
       Iterator<JsonElement> iterator;
       try {
           Path path = this.getPath();
           LinkOption[] linkOptionArray = new LinkOption[]{};
           if (!Files.exists(path, linkOptionArray)) {
               return;
           }
           String string = this.aoH(path);
           iterator = this.aoK(string).getAsJsonArray().iterator();
       }
       catch (Exception exception) {
           Bootstrap.Ob("Could not read clickgui file!");
           exception.printStackTrace();
           return;
       }
       while (iterator.hasNext()) {
           JsonObject jsonObject = iterator.next().getAsJsonObject();
           if (!jsonObject.has("category")) continue;
           JsonElement jsonElement = jsonObject.get("category");
           this.jH(ModuleCategory.Wj(jsonElement.getAsString())).bC(jsonObject);
       }
       return;
   }

   public CategoryPanel jH(ModuleCategory moduleCategory) {
      Iterator iterator2 = UI.drawScreenScreen.getList().iterator();

      while (iterator2.hasNext()) {
         CategoryPanel categoryPanel = (CategoryPanel)iterator2.next();
         if (categoryPanel.getModuleCategory().equals(moduleCategory)) {
            return categoryPanel;
         }
      }

      return null;
   }
}
