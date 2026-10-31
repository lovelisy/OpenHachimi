package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class TickEvent extends Event {
   private double a;
   private double b;
   private double c;

   public TickEvent(double d, double d2, double d3) {
      this.a = d;
      this.b = d2;
      this.c = d3;
   }

   public void aqa(double d) {
      this.a = d;
   }

   public void aqb(double d) {
      this.b = d;
   }

   public void aqc(double d) {
      this.c = d;
   }

   public double aqd() {
      return this.c;
   }

   public double aqe() {
      return this.b;
   }

   public double aqf() {
      return this.a;
   }

   public void aqg(double d, double d2, double d3) {
      this.a = d;
      this.b = d2;
      this.c = d3;
   }
}
