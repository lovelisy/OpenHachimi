package pw.hachimi.client.manager;

import io.netty.util.internal.ConcurrentSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.model.ObsidianObsidianBedrockMode;
import pw.hachimi.client.module.render.HoleESP;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.BlockPosBoxHelper;
import pw.hachimi.client.world.BlockPosManager;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class BlockPosWorldManager {
   private final ExecutorService executorService = Executors.newFixedThreadPool(1);
   private Future future;
   private Set set = new ConcurrentSet();

   public BlockPosWorldManager() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (HoleESP.ec().awg()) {
            BlockPosManager blockPosManager = new BlockPosManager(this, this.Ax(Mc.client.player.getPos()));
            this.future = this.executorService.submit(blockPosManager);
         }
      }
   }

   public List Ax(Vec3d vec) {
      ArrayList arrayList = new ArrayList();
      double d = Math.ceil(Math.max(5.0, HoleESP.ec().ei()));

      for (double d2 = -d; d2 <= d; d2++) {
         for (double d3 = -d; d3 <= d; d3++) {
            for (double d4 = -d; d4 <= d; d4++) {
               Vec3i vec3i = new Vec3i((int)(vec.getX() + d2), (int)(vec.getY() + d3), (int)(vec.getZ() + d4));
               BlockPos pos = new BlockPos(vec3i);
               arrayList.add(pos);
            }
         }
      }

      return arrayList;
   }

   public BlockPosBoxHelper Ay(BlockPos pos) {
      if (pos.getY() == Mc.client.world.getBottomY() && !BlockUtils.TP(pos)) {
         return new BlockPosBoxHelper(pos, ObsidianObsidianBedrockMode.VOID, new BlockPos[0]);
      }

      int n = 0;
      int n2 = 0;
      if (!CollisionUtils.aiw(pos)) {
         return null;
      }

      BlockPos pos2 = pos.add(-1, 0, 0);
      BlockPos pos3 = pos.add(0, 0, -1);
      if (BlockUtils.TR(pos2)) {
         n++;
      } else if (BlockUtils.TP(pos2)) {
         n2++;
      }

      if (BlockUtils.TR(pos3)) {
         n++;
      } else if (BlockUtils.TP(pos3)) {
         n2++;
      }

      if (n + n2 < 2) {
         return null;
      }

      BlockPos pos4 = pos.add(0, 0, 1);
      BlockPos pos5 = pos.add(1, 0, 0);
      boolean flag = Mc.client.world.isAir(pos4);
      boolean flag2 = Mc.client.world.isAir(pos5);
      if (flag && flag2) {
         BlockPos pos6 = pos.add(1, 0, 1);
         if (!Mc.client.world.isAir(pos6)) {
            return null;
         }

         BlockPos[] poss = new BlockPos[]{pos.add(-1, 0, 1), pos.add(0, 0, 2), pos.add(1, 0, 2), pos.add(2, 0, 1), pos.add(2, 0, 0), pos.add(1, 0, -1)};
         for (BlockPos pos7 : poss) {
            if (BlockUtils.TR(pos7)) {
               n++;
            } else if (BlockUtils.TP(pos7)) {
               n2++;
            }
         }

         if (n != 8 && n2 != 8 && n + n2 != 8) {
            return null;
         }

         BlockPosBoxHelper blockPosBoxHelper = new BlockPosBoxHelper(pos, a(n, n2, 8), new BlockPos[]{pos2, pos3, pos4, pos5, pos6});
         blockPosBoxHelper.aei(poss);
         return blockPosBoxHelper;
      }

      if (flag && CollisionUtils.aiw(pos4)) {
         BlockPos[] poss2 = new BlockPos[]{pos.add(-1, 0, 1), pos.add(0, 0, 2), pos.add(1, 0, 1), pos.add(1, 0, 0)};
         for (BlockPos pos8 : poss2) {
            if (BlockUtils.TR(pos8)) {
               n++;
            } else if (BlockUtils.TP(pos8)) {
               n2++;
            }
         }

         if (n != 6 && n2 != 6 && n + n2 != 6) {
            return null;
         }

         BlockPosBoxHelper blockPosBoxHelper2 = new BlockPosBoxHelper(pos, a(n, n2, 6), new BlockPos[]{pos2, pos3, pos4});
         blockPosBoxHelper2.aei(poss2);
         return blockPosBoxHelper2;
      }

      if (flag2 && CollisionUtils.aiw(pos5)) {
         BlockPos[] poss3 = new BlockPos[]{pos.add(0, 0, 1), pos.add(1, 0, 1), pos.add(2, 0, 0), pos.add(1, 0, -1)};
         for (BlockPos pos9 : poss3) {
            if (BlockUtils.TR(pos9)) {
               n++;
            } else if (BlockUtils.TP(pos9)) {
               n2++;
            }
         }

         if (n != 6 && n2 != 6 && n + n2 != 6) {
            return null;
         }

         BlockPosBoxHelper blockPosBoxHelper3 = new BlockPosBoxHelper(pos, a(n, n2, 6), new BlockPos[]{pos2, pos3, pos5});
         blockPosBoxHelper3.aei(poss3);
         return blockPosBoxHelper3;
      }

      if (BlockUtils.TR(pos4)) {
         n++;
      } else if (BlockUtils.TP(pos4)) {
         n2++;
      }

      if (BlockUtils.TR(pos5)) {
         n++;
      } else if (BlockUtils.TP(pos5)) {
         n2++;
      }

      if (n != 4 && n2 != 4 && n + n2 != 4) {
         return null;
      }

      return new BlockPosBoxHelper(pos, a(n, n2, 4), new BlockPos[]{pos2, pos3, pos4, pos5});
   }

   private static ObsidianObsidianBedrockMode a(int n, int n2, int n3) {
      if (n == n3) {
         return ObsidianObsidianBedrockMode.OBSIDIAN;
      }

      return n2 == n3 ? ObsidianObsidianBedrockMode.BEDROCK : ObsidianObsidianBedrockMode.OBSIDIAN_BEDROCK;
   }

   public Set Az() {
      if (this.future != null) {
         BlockPosWorldManager temp = this;
         BlockPosWorldManager temp2 = this;

         try {
            temp.set = (Set)temp2.future.get();
         } catch (ExecutionException | InterruptedException interruptedException) {
         }
      }

      return this.set;
   }
}
