package pw.hachimi.client.model.movement;

public record YawPitch(double x, double y, double z, float yaw, float pitch) {
   public float getYaw() {
      return this.yaw;
   }
}
