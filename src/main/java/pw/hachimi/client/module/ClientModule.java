package pw.hachimi.client.module;

import net.minecraft.sound.SoundEvent;
import pw.hachimi.client.api.UnidentifiedInterface15;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.BooleanSetting4;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.render.UnidentifiedHelper9;

public class ClientModule extends ModuleBase implements UnidentifiedInterface15 {
   public final UnidentifiedHelper9 unidentifiedHelper9 = new UnidentifiedHelper9();
   public final UnidentifiedHelper9 unidentifiedHelper92 = new UnidentifiedHelper9();
   public String text2_2 = "";
   Setting settingSetting1 = this.registerSetting(new BooleanSetting4("Enabled", "The module enabled state", false));
   Setting booleanSetting1 = this.registerSetting(new BooleanSetting("Hidden", "The hidden state of the module in the arraylist", false, ClientModule::isBooleanSetting1Visible));
   Setting booleanSetting2 = this.registerSetting(new BooleanSetting("Notify", "Notifies you when the module is toggled in chat", true, ClientModule::isBooleanSetting2Visible));
   Setting settingSetting2;
   public static boolean cj;
   private static boolean g;

   public ClientModule(String str, String str2, ModuleCategory moduleCategory) {
      super(str, str2, moduleCategory);
      KeybindSetting keybindSetting = new KeybindSetting("Keybind", "The keybind to toggle the module", e(this.Kn(), this::awa));
      this.settingSetting2 = this.registerSetting(keybindSetting);
   }

   public ClientModule(String str, String str2, ModuleCategory moduleCategory, Integer integer) {
      this(str, str2, moduleCategory);
      this.awf(integer);
   }

   public Setting getBooleanSetting1() {
      return this.booleanSetting1;
   }

   @Override
   public boolean WS() {
      return (Boolean)this.booleanSetting1.atx();
   }

   @Override
   public void WT(boolean flag) {
      this.booleanSetting1.aty(flag);
   }

   public void awa() {
      if (client.world != null && ColorHelper3.auO().auS()) {
         SoundManager temp = Managers.soundManager;
         boolean flag = this.awg();
         SoundManager soundManager2 = temp;
         SoundManager soundManager3;
         SoundEvent soundEvent;
         if (flag) {
            temp = soundManager2;
            soundEvent = SoundManager.h;
            soundManager3 = temp;
         } else {
            temp = soundManager2;
            soundEvent = SoundManager.g;
            soundManager3 = temp;
         }

         soundManager3.jg(soundEvent);
      }

      if (this.awg()) {
         this.awc();
      } else {
         this.awb();
      }
   }

   public void awb() {
      this.settingSetting1.aty(true);
      this.awd();
      if ((Boolean)this.booleanSetting2.atx() && client.world != null) {
         ChatUtils.UC("§2[+] §f" + this.OO(), this.hashCode());
      }
   }

   public void awc() {
      this.settingSetting1.aty(false);
      this.awe();
      if ((Boolean)this.booleanSetting2.atx() && client.world != null) {
         ChatUtils.UC("§4[-] §f" + this.OO(), this.hashCode());
      }
   }

   protected void awd() {
   }

   public void awe() {
   }

   public void awf(int n) {
      this.settingSetting2.setConfigContainer(this);
      ((KeybindSetting)this.settingSetting2).aT(n);
   }

   public boolean awg() {
      return (Boolean)this.settingSetting1.atx();
   }

   public Setting getSettingSetting2() {
      return this.settingSetting2;
   }

   public UnidentifiedHelper17 awi() {
      return (UnidentifiedHelper17)this.settingSetting2.atx();
   }

   public boolean awj() {
      return (Boolean)this.booleanSetting2.atx();
   }

   public void awk(boolean flag) {
      this.booleanSetting2.aty(flag);
   }

   private static Boolean isBooleanSetting2Visible() {
      return false;
   }

   private static Boolean isBooleanSetting1Visible() {
      return false;
   }

   private static UnidentifiedHelper17 e(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, -1, runnable);
   }

   public static void a(boolean flag) {
      g = flag;
   }

   public static boolean D() {
      return false;
   }

   public static boolean E() {
      D();
      return true;
   }
}
