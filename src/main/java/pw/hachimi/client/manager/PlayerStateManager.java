package pw.hachimi.client.manager;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.world.RedirectInteractBlockInternal_shouldCancelInteractionEvent;
import pw.hachimi.client.utils.world.EntityVec3dUtils;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class PlayerStateManager {
   private boolean a;

   public PlayerStateManager() {
      EventBusImpl.INSTANCE.rD(this);
   }

   public void rJ(double d) {
      ClientPlayerEntity temp = Mc.client.player;
      double d2 = Mc.client.player.getVelocity().getZ();
      double d3 = Mc.client.player.getVelocity().getX();
      temp.setVelocity(d3, d, d2);
   }

   public void rK(double d, double d2) {
      ClientPlayerEntity temp = Mc.client.player;
      double d3 = Mc.client.player.getVelocity().y;
      temp.setVelocity(d, d3, d2);
   }

   public void rL(double d, double d2, double d3) {
      Mc.client.player.setVelocity(d, d2, d3);
   }

   public void rM(double d) {
      ClientPlayerEntity temp = Mc.client.player;
      double temp2 = Mc.client.player.getVelocity().y;
      double d2 = Mc.client.player.getVelocity().z;
      double d3 = temp2;
      temp.setVelocity(d, d3, d2);
   }

   public void rN(double d) {
      ClientPlayerEntity temp = Mc.client.player;
      double temp2 = Mc.client.player.getVelocity().x;
      double d2 = Mc.client.player.getVelocity().y;
      double d3 = temp2;
      temp.setVelocity(d3, d2, d);
   }

   public void rO(boolean flag) {
      this.a = flag;
      ClientCommandC2SPacket.Mode mode = flag ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
      Managers.serverManager.iO((char)49212, new ClientCommandC2SPacket(Mc.client.player, mode));
   }

   @EventListener
   public void onRedirectInteractBlockInternal_shouldCancelInteraction(RedirectInteractBlockInternal_shouldCancelInteractionEvent redirectInteractBlockInternalshouldCancelInteractionEvent) {
      redirectInteractBlockInternalshouldCancelInteractionEvent.ail(this.a);
   }

   @EventListener(priority = 1000)
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         EntityVec3dUtils.list.clear();
      }
   }
}
