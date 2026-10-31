package pw.hachimi.client.utils.config;

import com.google.gson.JsonArray;
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
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.client.Font;
import pw.hachimi.client.module.client.HUD;
import pw.hachimi.client.module.client.UI;
import com.google.gson.JsonElement;

public class ClientPathsFileHelper4 extends FileBase {
   private final String text;

   public ClientPathsFileHelper4(Path path2, String str, String str2) {
      super(path2, str);
      this.text = str2;
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   @Override
   public void aoP() {
      ClientPathsFileHelper4 temp = this;

      Path path2;
      JsonObject jsonObject2;
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

         jsonObject2 = new JsonObject();
         JsonObject jsonObject3 = jsonObject2;
         Object[] objs = new Object[]{"Hachimi", "2.0"};
         jsonObject3.addProperty("version", String.format("%s %s", objs));
         jsonArray = new JsonArray();
         Iterator iterator2 = Managers.moduleManager.XM().iterator();

         while (iterator2.hasNext()) {
            ModuleBase moduleBase2 = (ModuleBase)iterator2.next();
            if (!this.Eb(moduleBase2, this.text)) {
               jsonArray.add(moduleBase2.LT(25681, 44222, '\u218e'));
            }
         }
      } catch (IOException ioException) {
         Object[] objs2 = new Object[]{this.getPath()};
         Bootstrap.Oc("Could not save file for {}!", objs2);
         ioException.printStackTrace();
         return;
      }

      JsonObject jsonObject4 = jsonObject2;

      try {
         jsonObject4.add("configs", jsonArray);
         this.aoM(path2, this.aoI(jsonObject2));
      } catch (IOException ioException2) {
         Object[] objs3 = new Object[]{this.getPath()};
         Bootstrap.Oc("Could not save file for {}!", objs3);
         ioException2.printStackTrace();
      }
   }

   private boolean Eb(ModuleBase moduleBase2, String str) {
      boolean flag;
      if (moduleBase2.getModuleCategory() != ModuleCategory.RENDER
         && moduleBase2.getModuleCategory() != ModuleCategory.HUD
         && !(moduleBase2 instanceof HUD)
         && !(moduleBase2 instanceof UI)
         && !(moduleBase2 instanceof Font)
         && !(moduleBase2 instanceof ColorHelper3)) {
         flag = false;
         if (str.equalsIgnoreCase("visual")) {
            return true;
         }
      } else {
         flag = true;
         if (str.equalsIgnoreCase("visual")) {
            return false;
         }
      }

      return str.equalsIgnoreCase("module") ? flag : false;
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
           JsonObject jsonObject = this.aoJ(string);
           if (!jsonObject.has("configs")) return;
           iterator = jsonObject.getAsJsonArray("configs").asList().iterator();
       }
       catch (IOException iOException) {
           Object[] objectArray = new Object[]{this.getPath()};
           Bootstrap.Oc("Could not read file for {}!", objectArray);
           iOException.printStackTrace();
           return;
       }
       while (iterator.hasNext()) {
           ModuleBase moduleBase;
           JsonObject jsonObject = iterator.next().getAsJsonObject();
           if (!jsonObject.has("id") || (moduleBase = Managers.moduleManager.XL(jsonObject.get("id").getAsString())) == null || this.Eb(moduleBase, this.text)) continue;
           moduleBase.OM(jsonObject);
       }
       return;
   }
}
