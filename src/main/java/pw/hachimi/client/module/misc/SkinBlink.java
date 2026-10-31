package pw.hachimi.client.module.misc;

import java.util.Set;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerModelPart;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.mixin.GameOptionsMixin;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class SkinBlink extends ClientModule {
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting speed = this.registerSetting(new NumberSetting("Speed", "The speed to toggle the player model parts", 0.0F, 0.1F, 20.0F));
   Setting random = this.registerSetting(new BooleanSetting("Random", "Randomizes the toggling of each skin model part", false));
   private Set set;

   public SkinBlink() {
      super("SkinBlink", "Toggles the skin model rendering", ModuleCategory.MISC);
   }

   @Override
   public void awd() {
      if (client.options != null) {
         this.set = ((GameOptionsMixin)client.options).getPlayerModelParts();
      }
   }

   @Override
   public void awe() {
      if (this.set != null && client.options != null) {
         PlayerModelPart[] playerModelParts = PlayerModelPart.values();
         int n = playerModelParts.length;

         for (int n2 = 0; n2 < n; n2++) {
            PlayerModelPart playerModelPart = playerModelParts[n2];
            GameOptions temp = client.options;
            boolean flag = this.set.contains(playerModelPart);
            temp.togglePlayerModelPart(playerModelPart, flag);
         }
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.POST) {
         Setting setting2 = this.speed;
         if (this.stopwatch.afq((Float)setting2.atx() * 1000.0F)) {
            Set set2 = ((GameOptionsMixin)client.options).getPlayerModelParts();
            PlayerModelPart[] playerModelParts = PlayerModelPart.values();
            int n = playerModelParts.length;

            for (int n2 = 0; n2 < n; n2++) {
               PlayerModelPart playerModelPart = playerModelParts[n2];
               GameOptions temp = client.options;
               boolean flag = (Boolean)this.random.atx();
               GameOptions gameOptions = temp;
               if (flag) {
                  temp = gameOptions;
                  Math.random();
                  temp = temp;
                  flag = true;
                  gameOptions = temp;
               } else {
                  temp = gameOptions;
                  flag = set2.contains(playerModelPart);
                  gameOptions = temp;
                  if (!flag) {
                     temp = gameOptions;
                     flag = true;
                     gameOptions = temp;
                  } else {
                     temp = gameOptions;
                     flag = false;
                     gameOptions = temp;
                  }
               }

               temp = gameOptions;
               boolean flag2 = flag;
               temp.togglePlayerModelPart(playerModelPart, flag2);
            }

            this.stopwatch.afr(29432, 5265492, (byte)40);
         }
      }
   }
}
