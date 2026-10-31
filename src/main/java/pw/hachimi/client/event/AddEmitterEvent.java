package pw.hachimi.client.event;

import net.minecraft.particle.ParticleEffect;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.render.AddParticleEvent;

@UnidentifiedInterface2
public class AddEmitterEvent extends AddParticleEvent {
   public static boolean cj;

   public AddEmitterEvent(ParticleEffect particleEffect) {
      super(particleEffect);
   }
}
