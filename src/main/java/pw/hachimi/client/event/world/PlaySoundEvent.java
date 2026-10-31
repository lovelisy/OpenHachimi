package pw.hachimi.client.event.world;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class PlaySoundEvent extends Event {
   private final Vec3d vec;
   private final SoundEvent soundEvent;
   private final SoundCategory soundCategory;

   public PlaySoundEvent(Vec3d vec2, SoundEvent soundEvent2, SoundCategory soundCategory2) {
      this.vec = vec2;
      this.soundEvent = soundEvent2;
      this.soundCategory = soundCategory2;
   }

   public Vec3d getVec() {
      return this.vec;
   }

   public SoundEvent getSoundEvent() {
      return this.soundEvent;
   }

   public SoundCategory getSoundCategory() {
      return this.soundCategory;
   }
}
