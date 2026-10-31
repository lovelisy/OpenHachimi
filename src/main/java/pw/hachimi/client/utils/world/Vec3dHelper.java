package pw.hachimi.client.utils.world;

import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;

public class Vec3dHelper {
   private Vec3d vec;
   private Vec3dHelper vec3dHelper;
   private ArrayList arrayList;
   private double d;
   private double e;
   private double f;

   public Vec3dHelper(Vec3d vec2, Vec3dHelper vec3dHelper2, ArrayList arrayList2, double d2, double e2, double f2) {
      this.vec = vec2;
      this.vec3dHelper = vec3dHelper2;
      this.arrayList = arrayList2;
      this.d = d2;
      this.e = e2;
      this.f = f2;
   }

   public Vec3d getVec() {
      return this.vec;
   }

   public void setVec(Vec3d vec2) {
      this.vec = vec2;
   }

   public Vec3dHelper getVec3dHelper() {
      return this.vec3dHelper;
   }

   public void setVec3dHelper(Vec3dHelper vec3dHelper2) {
      this.vec3dHelper = vec3dHelper2;
   }

   public ArrayList getArrayList() {
      return this.arrayList;
   }

   public void setArrayList(ArrayList arrayList2) {
      this.arrayList = arrayList2;
   }

   public double He() {
      return this.d;
   }

   public void Hf(double d2) {
      this.d = d2;
   }

   public double Hg() {
      return this.e;
   }

   public void Hh() {
      this.e = 0.0;
   }

   public double Hi() {
      return this.f;
   }

   public void Hj(double d2) {
      this.f = d2;
   }
}
