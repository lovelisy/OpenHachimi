package pw.hachimi.client.module;

import baritone.api.BaritoneAPI;
import java.awt.Color;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.UnknownEvent3;
import pw.hachimi.client.event.movement.MoveToWorldEvent;
import pw.hachimi.client.event.network.AddMessageEvent;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class ModuleManagerColorHelper extends Module3 {
   public ModuleManagerColorHelper() {
      super("Baritone", "Configure baritone", ModuleCategory.CLIENT);
   }

   Setting numberSetting1 = this.registerSetting(new NumberSetting("Range", "Baritone block reach distance", 0.0F, 4.0F, 5.0F));
   Setting booleanSetting1 = this.registerSetting(new BooleanSetting("Place", "Allow baritone to place blocks", true));
   Setting booleanSetting2 = this.registerSetting(new BooleanSetting("Break", "Allow baritone to break blocks", true));
   Setting booleanSetting3 = this.registerSetting(new BooleanSetting("Sprint", "Allow baritone to sprint", true));
   Setting booleanSetting4 = this.registerSetting(new BooleanSetting("UseInventory", "Allow baritone to use player inventory", false));
   Setting booleanSetting5 = this.registerSetting(new BooleanSetting("Vines", "Allow baritone to climb vines", true));
   Setting booleanSetting6 = this.registerSetting(new BooleanSetting("JumpAt256", "Allow baritone to jump at 256 blocks", false));
   Setting booleanSetting7 = this.registerSetting(new BooleanSetting("WaterBucketFall", "Allow baritone to use waterbuckets when falling", false));
   Setting booleanSetting8 = this.registerSetting(new BooleanSetting("Parkour", "Allow baritone to jump between blocks", true));
   Setting booleanSetting9 = this.registerSetting(new BooleanSetting("ParkourPlace", "Allow baritone to jump and place blocks", false));
   Setting booleanSetting10 = this.registerSetting(new BooleanSetting("ParkourAscend", "Allow baritone to jump up blocks", true));
   Setting booleanSetting11 = this.registerSetting(new BooleanSetting("DiagonalAscend", "Allow baritone to jump up blocks diagonally", false));
   Setting booleanSetting12 = this.registerSetting(new BooleanSetting("DiagonalDescend", "Allow baritone to move down blocks diagonally", false));
   Setting booleanSetting13 = this.registerSetting(new BooleanSetting("MineDownward", "Allow baritone to mine down", true));
   Setting booleanSetting14 = this.registerSetting(new BooleanSetting("LegitMine", "Uses baritone legit mine", false));
   Setting booleanSetting15 = this.registerSetting(new BooleanSetting("LogOnArrival", "Logout when you arrive at destination", false));
   Setting booleanSetting16 = this.registerSetting(new BooleanSetting("FreeLook", "Allows you to look around freely while using baritone", true));
   Setting booleanSetting17 = this.registerSetting(new BooleanSetting("AntiCheat", "Uses NCP placements and breaks", false));
   Setting booleanSetting18 = this.registerSetting(new BooleanSetting("Strict-Liquid", "Uses strick liquid checks", false));
   Setting booleanSetting19 = this.registerSetting(new BooleanSetting("CensorCoords", "Censors goal coordinates in chat", false));
   Setting booleanSetting20 = this.registerSetting(new BooleanSetting("CensorCommands", "Censors baritone commands in chat", false));
   Setting booleanSetting21 = this.registerSetting(new BooleanSetting("ChatControl", "Allows you to type baritone commands in chat without prefix", true));
   Setting booleanSetting22 = this.registerSetting(new BooleanSetting("Debug", "Debugs in the chat", false));
   Setting colorSetting1 = this.registerSetting(createColorSetting("GoalColor", "The color of the goal box", Color.GREEN));
   Setting colorSetting2 = this.registerSetting(createColorSetting("CurrentPathColor", "The color of the path", Color.RED));
   Setting colorSetting3 = this.registerSetting(createColorSetting("NextPathColor", "The color of the path", Color.MAGENTA));

   @EventListener
   public void EO(UnknownEvent3 unknownEvent3) {
      if (unknownEvent3.getEventPhase() == EventPhase.POST && unknownEvent3.getSetting().getConfigContainer() == this) {
         BaritoneAPI.getSettings().blockReachDistance.value = (Float)this.numberSetting1.atx();
         BaritoneAPI.getSettings().allowPlace.value = (Boolean)this.booleanSetting1.atx();
         BaritoneAPI.getSettings().allowBreak.value = (Boolean)this.booleanSetting2.atx();
         BaritoneAPI.getSettings().allowSprint.value = (Boolean)this.booleanSetting3.atx();
         BaritoneAPI.getSettings().allowInventory.value = (Boolean)this.booleanSetting4.atx();
         BaritoneAPI.getSettings().allowVines.value = (Boolean)this.booleanSetting5.atx();
         BaritoneAPI.getSettings().allowJumpAt256.value = (Boolean)this.booleanSetting6.atx();
         BaritoneAPI.getSettings().allowWaterBucketFall.value = (Boolean)this.booleanSetting7.atx();
         BaritoneAPI.getSettings().allowParkour.value = (Boolean)this.booleanSetting8.atx();
         BaritoneAPI.getSettings().allowParkourAscend.value = (Boolean)this.booleanSetting10.atx();
         BaritoneAPI.getSettings().allowParkourPlace.value = (Boolean)this.booleanSetting9.atx();
         BaritoneAPI.getSettings().allowDiagonalAscend.value = (Boolean)this.booleanSetting11.atx();
         BaritoneAPI.getSettings().allowDiagonalDescend.value = (Boolean)this.booleanSetting12.atx();
         BaritoneAPI.getSettings().allowDownward.value = (Boolean)this.booleanSetting13.atx();
         BaritoneAPI.getSettings().legitMine.value = (Boolean)this.booleanSetting14.atx();
         BaritoneAPI.getSettings().disconnectOnArrival.value = (Boolean)this.booleanSetting15.atx();
         BaritoneAPI.getSettings().freeLook.value = (Boolean)this.booleanSetting16.atx();
         BaritoneAPI.getSettings().antiCheatCompatibility.value = (Boolean)this.booleanSetting17.atx();
         BaritoneAPI.getSettings().strictLiquidCheck.value = (Boolean)this.booleanSetting18.atx();
         BaritoneAPI.getSettings().censorCoordinates.value = (Boolean)this.booleanSetting19.atx();
         BaritoneAPI.getSettings().censorRanCommands.value = (Boolean)this.booleanSetting20.atx();
         BaritoneAPI.getSettings().chatControl.value = (Boolean)this.booleanSetting21.atx();
         BaritoneAPI.getSettings().chatDebug.value = (Boolean)this.booleanSetting22.atx();
         BaritoneAPI.getSettings().colorGoalBox.value = (Color)this.colorSetting1.atx();
         BaritoneAPI.getSettings().colorCurrentPath.value = (Color)this.colorSetting2.atx();
         BaritoneAPI.getSettings().colorMostRecentConsidered.value = (Color)this.colorSetting2.atx();
         BaritoneAPI.getSettings().colorNextPath.value = (Color)this.colorSetting3.atx();
      }
   }

   @EventListener
   public void EP(AddMessageEvent addMessageEvent) {
      this.ER();
   }

   @EventListener
   public void onMoveToWorld(MoveToWorldEvent moveToWorldEvent) {
      this.ER();
   }

   private void ER() {
      this.numberSetting1.aty((Float)BaritoneAPI.getSettings().blockReachDistance.value);
      this.booleanSetting1.aty((Boolean)BaritoneAPI.getSettings().allowPlace.value);
      this.booleanSetting2.aty((Boolean)BaritoneAPI.getSettings().allowBreak.value);
      this.booleanSetting3.aty((Boolean)BaritoneAPI.getSettings().allowSprint.value);
      this.booleanSetting4.aty((Boolean)BaritoneAPI.getSettings().allowInventory.value);
      this.booleanSetting5.aty((Boolean)BaritoneAPI.getSettings().allowVines.value);
      this.booleanSetting6.aty((Boolean)BaritoneAPI.getSettings().allowJumpAt256.value);
      this.booleanSetting7.aty((Boolean)BaritoneAPI.getSettings().allowWaterBucketFall.value);
      this.booleanSetting8.aty((Boolean)BaritoneAPI.getSettings().allowParkour.value);
      this.booleanSetting10.aty((Boolean)BaritoneAPI.getSettings().allowParkourAscend.value);
      this.booleanSetting9.aty((Boolean)BaritoneAPI.getSettings().allowParkourPlace.value);
      this.booleanSetting11.aty((Boolean)BaritoneAPI.getSettings().allowDiagonalAscend.value);
      this.booleanSetting12.aty((Boolean)BaritoneAPI.getSettings().allowDiagonalDescend.value);
      this.booleanSetting13.aty((Boolean)BaritoneAPI.getSettings().allowDownward.value);
      this.booleanSetting14.aty((Boolean)BaritoneAPI.getSettings().legitMine.value);
      this.booleanSetting15.aty((Boolean)BaritoneAPI.getSettings().disconnectOnArrival.value);
      this.booleanSetting16.aty((Boolean)BaritoneAPI.getSettings().freeLook.value);
      this.booleanSetting17.aty((Boolean)BaritoneAPI.getSettings().antiCheatCompatibility.value);
      this.booleanSetting18.aty((Boolean)BaritoneAPI.getSettings().strictLiquidCheck.value);
      this.booleanSetting19.aty((Boolean)BaritoneAPI.getSettings().censorCoordinates.value);
      this.booleanSetting20.aty((Boolean)BaritoneAPI.getSettings().censorRanCommands.value);
      this.booleanSetting21.aty((Boolean)BaritoneAPI.getSettings().chatControl.value);
      this.booleanSetting22.aty((Boolean)BaritoneAPI.getSettings().chatDebug.value);
      this.colorSetting1.aty((Color)BaritoneAPI.getSettings().colorGoalBox.value);
      this.colorSetting2.aty((Color)BaritoneAPI.getSettings().colorCurrentPath.value);
      this.colorSetting2.aty((Color)BaritoneAPI.getSettings().colorMostRecentConsidered.value);
      this.colorSetting3.aty((Color)BaritoneAPI.getSettings().colorNextPath.value);
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color) {
      return new ColorSetting(str, str2, color, false, false);
   }
}
