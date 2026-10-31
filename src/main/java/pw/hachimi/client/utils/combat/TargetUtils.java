package pw.hachimi.client.utils.combat;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.player.PlayerWorldOtherClientPlayerEntity;
import pw.hachimi.client.utils.render.WorldUtils;
import java.util.ArrayList;

public class TargetUtils {
   private static final List a_ = new ArrayList();
   private static final long a = 0L;

   @Nullable
   public static Entity WX(long l, Predicate predicate, TargetPriority targetPriority) {
      a_.clear();
      WY(a_, predicate, targetPriority, 1);
      return !a_.isEmpty() ? (Entity)a_.getFirst() : null;
   }

   public static void WY(List list, Predicate predicate, TargetPriority targetPriority, int n) {
      list.clear();
      Iterator iterator2 = Mc.client.world.getEntities().iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2 != null && predicate.test(entity2)) {
            list.add(entity2);
         }
      }

      list.sort(targetPriority);
      if (list.size() > n) {
         int n2 = list.size();
         list.subList(n, n2).clear();
      }
   }

   @Nullable
   public static PlayerEntity WZ(long l, double d, TargetPriority targetPriority) {
      if (Mc.client.player != null && Mc.client.world != null) {
         final double d2 = d;
         return (PlayerEntity)WX(12931875445969L, new Predicate() {
            @Override
            public boolean test(Object obj) {
               return TargetUtils.Xc(d2, (Entity)obj);
            }
         }, targetPriority);
      } else {
         return null;
      }
   }

   public static GameMode Xa(PlayerEntity player2) {
      if (player2 == null) {
         return null;
      }

      ClientPlayNetworkHandler temp = Mc.client.getNetworkHandler();
      UUID uuid = player2.getUuid();
      PlayerListEntry playerListEntry = temp.getPlayerListEntry(uuid);
      return (PlayerListEntry)playerListEntry == null ? null : playerListEntry.getGameMode();
   }

   public static boolean Xb(PlayerEntity player2, double d) {
      return player2 == null || !WorldUtils.Nu(player2, d) || !player2.isAlive() || player2.isDead() || player2.getHealth() <= 0.0F;
   }

   static boolean Xc(double d, Entity entity2) {
      if (entity2 instanceof PlayerEntity && entity2 != Mc.client.player) {
         if (((PlayerEntity)entity2).isDead() || ((PlayerEntity)entity2).getHealth() <= 0.0F) {
            return false;
         }

         if (!WorldUtils.Nu(entity2, d)) {
            return false;
         }

         if (Managers.friendManager.awr((PlayerEntity)entity2)) {
            return false;
         }

         boolean flag;
         if (Xa((PlayerEntity)entity2) != GameMode.SURVIVAL && !(entity2 instanceof PlayerWorldOtherClientPlayerEntity)) {
            flag = false;
         } else {
            flag = true;
         }

         return flag;
      } else {
         return false;
      }
   }
}
