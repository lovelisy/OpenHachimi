package pw.hachimi.client.setting;

import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.eventbus.EventBusImpl;

public class BooleanSetting4 extends BooleanSetting {
   public static boolean fk;

   public BooleanSetting4(String str, String str2, Boolean flag) {
      super(str, str2, flag);
   }

   @Override
   public void Kl(Boolean flag) {
      super.Kl(flag);
      ConfigContainer configContainer = this.getConfigContainer();
      if (configContainer instanceof ClientModule) {
         if (flag) {
            EventBusImpl.INSTANCE.rD((ClientModule)configContainer);
         } else {
            EventBusImpl.INSTANCE.rG((ClientModule)configContainer);
         }
      }
   }

   public void NR() {
      ConfigContainer configContainer = this.getConfigContainer();
      if (configContainer instanceof ClientModule) {
         ((ClientModule)configContainer).awb();
      }
   }

   public void NS() {
      ConfigContainer configContainer = this.getConfigContainer();
      if (configContainer instanceof ClientModule) {
         ((ClientModule)configContainer).awc();
      }
   }

   @Override
   public void aty(Object obj) {
      this.Kl((Boolean)obj);
   }
}
