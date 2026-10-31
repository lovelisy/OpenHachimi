package pw.hachimi.client.module.misc;

import net.minecraft.sound.SoundEvent;
import pw.hachimi.client.event.network.AddMessageEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.model.world.PMSoundTwitterIosMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class PMSound extends ClientModule {
   private static PMSound INSTANCE;
   Setting sounds;

   public PMSound() {
      super("PMSound", "Plays a sound for private messages", ModuleCategory.MISC);
      PMSoundTwitterIosMode pmSoundTwitterIosMode = PMSoundTwitterIosMode.TWITTER;
      EnumSetting enumSetting = new EnumSetting("Sounds", "The sound to play for PM", pmSoundTwitterIosMode, PMSoundTwitterIosMode.values());
      this.sounds = this.registerSetting(enumSetting);
      INSTANCE = this;
   }

   public static PMSound atn() {
      return INSTANCE;
   }

   @EventListener
   public void ato(AddMessageEvent addMessageEvent) {
      String str = addMessageEvent.getText().getString();
      if (!str.startsWith("<") && (str.contains("whispers:") || str.contains("says:"))) {
         SoundManager temp = Managers.soundManager;
         int n = ((PMSoundTwitterIosMode)this.sounds.atx()).ordinal();
         SoundManager soundManager2 = temp;
         SoundManager soundManager3;
         SoundEvent soundEvent;
         switch (n) {
            case 0:
               temp = soundManager2;
               soundEvent = SoundManager.b;
               soundManager3 = temp;
               break;
            case 1:
               temp = soundManager2;
               soundEvent = SoundManager.c;
               soundManager3 = temp;
               break;
            case 2:
               temp = soundManager2;
               soundEvent = SoundManager.d;
               soundManager3 = temp;
               break;
            case 3:
               temp = soundManager2;
               soundEvent = SoundManager.e;
               soundManager3 = temp;
               break;
            default:
               throw h();
         }

         soundManager3.jg(soundEvent);
      }
   }

   public PMSoundTwitterIosMode atp() {
      return (PMSoundTwitterIosMode)this.sounds.atx();
   }

   private static MatchException h() {
      return new MatchException(null, null);
   }
}
