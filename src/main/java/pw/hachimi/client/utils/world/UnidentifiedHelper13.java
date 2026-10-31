package pw.hachimi.client.utils.world;

import java.util.Comparator;

public class UnidentifiedHelper13 implements Comparator {
   public int UT(Vec3dHelper vec3dHelper, Vec3dHelper vec3dHelper2) {
      return (int)(vec3dHelper.He() + vec3dHelper.Hi() - (vec3dHelper2.He() + vec3dHelper2.Hi()));
   }

   @Override
   public int compare(Object obj, Object obj2) {
      return this.UT((Vec3dHelper)obj, (Vec3dHelper)obj2);
   }
}
