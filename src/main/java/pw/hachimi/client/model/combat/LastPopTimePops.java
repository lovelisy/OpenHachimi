package pw.hachimi.client.model.combat;

public record LastPopTimePops(long lastPopTime, int pops) {
   public int getPops() {
      return this.pops;
   }
}
