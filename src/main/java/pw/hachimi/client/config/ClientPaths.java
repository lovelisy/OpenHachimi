package pw.hachimi.client.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.config.UnidentifiedOption2;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.config.ClientPathsFileHelper;
import pw.hachimi.client.utils.config.ClientPathsFileHelper2;
import pw.hachimi.client.utils.config.ClientPathsFileHelper3;
import pw.hachimi.client.utils.config.ClientPathsFileHelper4;
import pw.hachimi.client.utils.config.ClientPathsFileHelper5;
import pw.hachimi.client.utils.config.ClientPathsFileHelper6;
import pw.hachimi.client.utils.config.FileHelper;

public class ClientPaths {
   public Path path;
   public ClientPathsFileHelper2 clientPathsFileHelper2;
   public ClientPathsFileHelper5 clientPathsFileHelper5;
   public File file;
   public File file2;
   private final Set set = new HashSet();
   private ClientPathsFileHelper4 clientPathsFileHelper4;
   private ClientPathsFileHelper3 clientPathsFileHelper3;

   public ClientPaths() {
      this.anW();
   }

   public void anW() {
      this.path = Mc.client.runDirectory.toPath().resolve("Hachimi");
      Path path2 = this.path.resolve("Configs");
      Path path3 = this.path.resolve("Macros");
      Path path4 = this.path.resolve("Fonts");
      Path path5 = this.path.resolve("Kits");
      Path path6 = this.path.resolve("Scripts");
      LinkOption[] linkOptions = new LinkOption[0];
      if (!Files.exists(this.path, linkOptions)) {
         try {
            Files.createDirectory(this.path, new FileAttribute[0]);
         } catch (IOException ioException) {
            Bootstrap.Ob("Could not create client dir");
            ioException.printStackTrace();
         }
      }

      if (!Files.exists(path2, linkOptions)) {
         try {
            Files.createDirectory(path2, new FileAttribute[0]);
         } catch (IOException ioException2) {
            Bootstrap.Ob("Could not create config dir");
            ioException2.printStackTrace();
         }
      }

      if (!Files.exists(path3, linkOptions)) {
         try {
            Files.createDirectory(path3, new FileAttribute[0]);
         } catch (IOException ioException3) {
            Bootstrap.Ob("Could not create marco dir");
            ioException3.printStackTrace();
         }
      }

      if (!Files.exists(path4, linkOptions)) {
         try {
            Files.createDirectory(path4, new FileAttribute[0]);
         } catch (IOException ioException4) {
            Bootstrap.Ob("Could not create fonts dir");
            ioException4.printStackTrace();
         }
      }

      if (!Files.exists(path5, linkOptions)) {
         try {
            Files.createDirectory(path5, new FileAttribute[0]);
         } catch (IOException ioException5) {
            Bootstrap.Ob("Could not create kit dir");
            ioException5.printStackTrace();
         }
      }

      if (!Files.exists(path6, linkOptions)) {
         try {
            Files.createDirectory(path6, new FileAttribute[0]);
         } catch (IOException ioException6) {
            Bootstrap.Ob("Could not create script dir");
            ioException6.printStackTrace();
         }
      }

      this.set.add(new ClientPathsFileHelper3(this.path));
      this.set.add(new ClientPathsFileHelper(this.path));
      UnidentifiedOption2[] unidentifiedOption2s = UnidentifiedOption2.values();
      for (int n = 0; n < unidentifiedOption2s.length; n++) {
         this.set.add(new ClientPathsFileHelper6(this.path, unidentifiedOption2s[n]));
      }

      this.file = path5.toFile();
      this.file2 = path6.toFile();
      this.clientPathsFileHelper2 = new ClientPathsFileHelper2(this.path);
      this.clientPathsFileHelper5 = new ClientPathsFileHelper5(this.path);
      Iterator iterator2 = Managers.moduleManager.XM().iterator();

      while (iterator2.hasNext()) {
         ModuleBase moduleBase = (ModuleBase)iterator2.next();
         this.set.add(new FileHelper(this.path.resolve("Modules"), moduleBase));
      }
   }

   public void anX() {
      Iterator iterator2 = this.set.iterator();

      while (iterator2.hasNext()) {
         ((FileBase)iterator2.next()).aoP();
      }
   }

   public void anY() {
      ExecutorService temp = Bootstrap.executorService;
      Runnable runnable = this::aoq;
      temp.submit(runnable);
   }

   public void anZ() {
      ExecutorService temp = Bootstrap.executorService;
      Runnable runnable = this::aop;
      temp.submit(runnable);
   }

   public void aoa(String str, String str2) {
      this.clientPathsFileHelper4 = new ClientPathsFileHelper4(this.path.resolve("Configs"), str, str2);
      this.clientPathsFileHelper4.aoP();
   }

   public boolean aob(String str, String str2) {
      Path path2 = this.path.resolve("Configs");
      this.clientPathsFileHelper4 = new ClientPathsFileHelper4(path2, str, str2);
      Path temp = path2;
      String str3 = str + ".json";
      temp = temp.resolve(str3);
      LinkOption[] linkOptions = new LinkOption[0];
      if (!Files.exists(temp, linkOptions)) {
         ChatUtils.UL("Could not find config file: " + str);
         return false;
      } else {
         ExecutorService executorService2 = Bootstrap.executorService;
         Runnable runnable = this::aoo;
         executorService2.submit(runnable);
         return true;
      }
   }

   public void aoc(String str) {
      ExecutorService temp = Bootstrap.executorService;
      final String str2 = str;
      final ClientPaths clientPaths = this;
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            clientPaths.aon(str2);
         }
      };
      temp.submit(runnable);
   }

   public boolean aod(String str) {
      Path path2 = this.path.resolve("Macros");
      this.clientPathsFileHelper3 = new ClientPathsFileHelper3(path2, str);
      Path temp = path2;
      String str2 = str + ".json";
      temp = temp.resolve(str2);
      LinkOption[] linkOptions = new LinkOption[0];
      if (!Files.exists(temp, linkOptions)) {
         ChatUtils.UL("Could not find config file: " + str);
         return false;
      } else {
         ExecutorService executorService2 = Bootstrap.executorService;
         Runnable runnable = this::aom;
         executorService2.submit(runnable);
         return true;
      }
   }

   public void aoe() {
      this.clientPathsFileHelper2.aoP();
   }

   public void aof() {
      ExecutorService temp = Bootstrap.executorService;
      Runnable runnable = this.clientPathsFileHelper2::aoQ;
      temp.submit(runnable);
   }

   public void aog() {
      this.clientPathsFileHelper5.aoP();
   }

   public void aoh() {
      ExecutorService temp = Bootstrap.executorService;
      Runnable runnable = this.clientPathsFileHelper5::aoQ;
      temp.submit(runnable);
   }

   public Set getSet() {
      return this.set;
   }

   public void aoj(FileBase fileBase) {
      this.set.add(fileBase);
   }

   public void aok(FileBase fileBase) {
      this.set.remove(fileBase);
   }

   public Path getPath() {
      return this.path;
   }

   private void aom() {
      this.clientPathsFileHelper3.aoQ();
   }

   void aon(String str) {
      this.clientPathsFileHelper3 = new ClientPathsFileHelper3(this.path.resolve("Macros"), str);
      this.clientPathsFileHelper3.aoP();
   }

   private void aoo() {
      this.clientPathsFileHelper4.aoQ();
   }

   private void aop() {
      Iterator iterator2 = this.set.iterator();

      while (iterator2.hasNext()) {
         FileBase fileBase = (FileBase)iterator2.next();
         if (fileBase instanceof FileHelper) {
            fileBase.aoQ();
         }
      }
   }

   private void aoq() {
      Iterator iterator2 = this.set.iterator();

      while (iterator2.hasNext()) {
         FileBase fileBase = (FileBase)iterator2.next();
         if (fileBase instanceof FileHelper) {
            fileBase.aoP();
         }
      }
   }
}
