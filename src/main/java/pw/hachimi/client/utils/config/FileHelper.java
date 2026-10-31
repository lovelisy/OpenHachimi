package pw.hachimi.client.utils.config;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.config.FileBase;
import pw.hachimi.client.module.ModuleBase;

public class FileHelper extends FileBase {
   private final ModuleBase moduleBase;
   public static boolean f;

   public FileHelper(Path path2, ModuleBase moduleBase2) {
      super(path2, moduleBase2.Kn());
      this.moduleBase = moduleBase2;
   }

   @Override
   public void aoP() {
      FileHelper temp = this;

      try {
         Path path2 = temp.getPath();
         Path path3 = path2;
         LinkOption[] linkOptions = new LinkOption[0];
         if (!Files.exists(path3, linkOptions)) {
            Path path4 = path2;
            FileAttribute[] fileAttributes = new FileAttribute[0];
            Files.createFile(path4, fileAttributes);
         }

         JsonObject jsonObject = this.moduleBase.LT(25681, 44222, '\u218e');
         this.aoM(path2, this.aoI(jsonObject));
      } catch (IOException ioException) {
         Object[] objs = new Object[]{this.moduleBase.getText2()};
         Bootstrap.Oc("Could not save file for {}!", objs);
         ioException.printStackTrace();
      }
   }

   @Override
   // restored with Procyon (compiles, call/field/constant fingerprint equals the deobf jar)
   public void aoQ() {
       try {
           final Path path = this.getPath();
           if (Files.exists(path, (LinkOption[])new LinkOption[0])) {
               this.moduleBase.OM(this.aoJ(this.aoH(path)));
           }
       }
       catch (final IOException ex) {
           Bootstrap.Oc("Could not read file for {}!", this.moduleBase.getText2());
           ex.printStackTrace();
       }
   }
}
