package pw.hachimi.client.utils.movement;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;

public class UnidentifiedUtils {
   public static boolean eP() {
      return BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing();
   }

   public static void eQ() {
      BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().forceCancel();
   }

   public static void eR(int n, int n2, int n3) {
      BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(n, n2, n3));
   }

   public static boolean eS() {
      return (Boolean)BaritoneAPI.getSettings().freeLook.value;
   }
}
