package pw.hachimi.client.event.world;

import net.minecraft.entity.LivingEntity;
import pw.hachimi.client.event.Event;

public class UpdateTrackedPositionAndAnglesEvent extends Event {
   private final LivingEntity living;
   private final double b;
   private final double c;
   private final double d;
   private final float e;
   private final float f;
   public static boolean gr;

   public UpdateTrackedPositionAndAnglesEvent(LivingEntity living2, double d2, double d3, double d4, float f2, float f3) {
      this.living = living2;
      this.b = d2;
      this.c = d3;
      this.d = d4;
      this.e = f2;
      this.f = f3;
   }

   public LivingEntity getLiving() {
      return this.living;
   }

   public double aD() {
      return this.b;
   }

   public double aE() {
      return this.c;
   }

   public double aF() {
      return this.d;
   }

   public float aG() {
      return this.e;
   }

   public float aH() {
      return this.f;
   }
}
