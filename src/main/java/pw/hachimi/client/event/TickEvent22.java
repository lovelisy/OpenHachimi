package pw.hachimi.client.event;

import net.minecraft.particle.ParticleEffect;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class TickEvent22 extends Event {
   private final ParticleEffect particleEffect;
   private int b;
   private int c;

   public TickEvent22(ParticleEffect particleEffect2, int n, int n2) {
      this.particleEffect = particleEffect2;
      this.b = n;
      this.c = n2;
   }

   public ParticleEffect getParticleEffect() {
      return this.particleEffect;
   }

   public int d_1() {
      return this.b;
   }

   public void e(int n) {
      this.b = n;
   }

   public int f() {
      return this.c;
   }

   public void g(int n) {
      this.c = n;
   }
}
