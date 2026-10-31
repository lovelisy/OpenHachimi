package pw.hachimi.client.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;
import pw.hachimi.client.config.Vec3dConfigContainer;
import pw.hachimi.client.utils.config.UnidentifiedHelper14;

public class UnidentifiedHelper19 {
   private final List list = new CopyOnWriteArrayList();
   public static boolean a;

   public void akQ(Vec3dConfigContainer vec3dConfigContainer) {
      this.list.add(vec3dConfigContainer);
   }

   public void akR(Vec3dConfigContainer... value) {
      for (int n = 0; n < value.length; n++) {
         this.akQ(value[n]);
      }
   }

   public boolean akS(Vec3dConfigContainer vec3dConfigContainer) {
      return this.list.remove(vec3dConfigContainer);
   }

   public boolean akT() {
      List temp = this.list;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return UnidentifiedHelper19.alc((Vec3dConfigContainer)obj);
         }
      };
      return temp.removeIf(predicate);
   }

   public boolean akU(String str) {
      List temp = this.list;
      final String str2 = str;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object str) {
            return UnidentifiedHelper19.alb(str2, (Vec3dConfigContainer)str);
         }
      };
      return temp.removeIf(predicate);
   }

   public boolean akV(String str) {
      Stream temp = this.list.stream();
      final String str2 = str;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object str) {
            return UnidentifiedHelper19.ala(str2, (Vec3dConfigContainer)str);
         }
      };
      return temp.anyMatch(predicate);
   }

   public void akW() {
      List temp = this.list;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return UnidentifiedHelper19.akZ((Vec3dConfigContainer)obj);
         }
      };
      temp.removeIf(predicate);
   }

   public List getList() {
      return this.list;
   }

   public List akY() {
      ArrayList arrayList = new ArrayList();
      Iterator iterator2 = this.getList().iterator();

      while (iterator2.hasNext()) {
         Vec3dConfigContainer vec3dConfigContainer = (Vec3dConfigContainer)iterator2.next();
         ArrayList temp = arrayList;
         String str = vec3dConfigContainer.getText();
         temp.add(str);
      }

      return arrayList;
   }

   static boolean akZ(Vec3dConfigContainer vec3dConfigContainer) {
      boolean flag;
      if (!(vec3dConfigContainer instanceof UnidentifiedHelper14)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean ala(String str, Vec3dConfigContainer vec3dConfigContainer) {
      return vec3dConfigContainer.getText2().contains(str);
   }

   static boolean alb(String str, Vec3dConfigContainer vec3dConfigContainer) {
      return vec3dConfigContainer.getText2().equalsIgnoreCase(str);
   }

   static boolean alc(Vec3dConfigContainer vec3dConfigContainer) {
      return vec3dConfigContainer.getText2().contains("Last Death");
   }
}
