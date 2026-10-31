package pw.hachimi.client.module.combat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.LadderBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.client.utils.world.PlayerBlockPosHelper;
import pw.hachimi.eventbus.annotation.EventListener;

public class Blocker extends Multitask {
   private static final Set a = new HashSet();
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Delay, in ticks, between placements", 0, 0, 20));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the obsidian being placed", true));
   Setting numberSetting1 = this.registerSetting(new NumberSetting("Progress", "Mining progress to active blocker", 0.0, 0.9, 1.0));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("GroundOnly", "Works only when you are standing on blocks", false));
   Setting pauseOnFlying = this.registerSetting(new BooleanSetting("PauseOnFlying", "Pauses the current process while you are flying", false));
   Setting pauseInFluid = this.registerSetting(new BooleanSetting("PauseInFluid", "Pauses the current process while you are swimming", false));
   Setting surroundOnly = this.registerSetting(new BooleanSetting("SurroundOnly", "Only working when surround/selftrap toggled on", true));
   private int j;

   public Blocker() {
      super("Blocker", "Magic to prevents crystals", ModuleCategory.COMBAT);
   }

   @EventListener
   private void WA(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (this.j <= 0) {
         if (!Blink.bm()) {
            if ((Boolean)this.booleanSetting2_1.atx() && !client.player.isOnGround()) {
               ClientWorld temp = client.world;
               BlockPos pos = client.player.getBlockPos();
               if (!(temp.getBlockState(pos).getBlock() instanceof LadderBlock)) {
                  return;
               }
            }

            if ((!(Boolean)this.pauseOnFlying.atx() || !client.player.isFallFlying())
               && (!(Boolean)this.pauseInFluid.atx() || !client.player.isInFluid())
               && ((Boolean)this.breakSetting.atx() || !client.player.isUsingItem())
               && (!(Boolean)this.surroundOnly.atx() || Surround.zD().awg() || SelfTrap.Zf().awg())) {
               StackSlotHelper stackSlotHelper = new StackSlotHelper(Items.FIREWORK_ROCKET, (SwapMode)this.swapAction.atx()).LK();
               if (stackSlotHelper.LL()) {
                  this.WB();
                  int n = Managers.inventoryTracker.JS();
                  Iterator iterator2 = a.iterator();

                  label70:
                  while (iterator2.hasNext()) {
                     Object obj = iterator2.next();
                     if (AutoCrystal.sc().sX(((BlockPos)obj).down())) {
                        PlayerBlockPosHelper playerBlockPosHelper = Managers.breakManager.Rt((BlockPos)obj);
                        if (playerBlockPosHelper != null && (playerBlockPosHelper.MU() >= (Double)this.numberSetting1.atx() || playerBlockPosHelper.f)) {
                           Iterator iterator3 = client.world.getEntities().iterator();

                           while (iterator3.hasNext()) {
                              Object obj2 = iterator3.next();
                              if (obj2 instanceof FireworkRocketEntity) {
                                 Box box = new Box(
                                    ((BlockPos)obj).getX(),
                                    ((BlockPos)obj).getY(),
                                    ((BlockPos)obj).getZ(),
                                    ((BlockPos)obj).getX() + 1,
                                    ((BlockPos)obj).getY() + 2,
                                    ((BlockPos)obj).getZ() + 1
                                 );
                                 Box box2 = ((Entity)obj2).getBoundingBox();
                                 if (box.intersects(box2)) {
                                    continue label70;
                                 }
                              }
                           }

                           this.j = (Integer)this.delay.atx();
                           stackSlotHelper.LO(n);
                           PlacementUtils.Ff(((BlockPos)obj).down(), Direction.UP, (Boolean)this.booleanSetting1_1.atx());
                           stackSlotHelper.LP(n);
                        }
                     }
                  }
               }
            }
         }
      } else {
         this.j--;
      }
   }

   private void WB() {
      a.clear();
      List list = WorldUtils.MZ(client.player.getPos());
      Iterator iterator2 = list.iterator();
      if (iterator2.hasNext()) {
         BlockPos pos = (BlockPos)iterator2.next();
         Direction[] directions = BoxUtils.b;
         int temp = directions.length;
         int n = 0;

         while (true) {
            Direction direction = directions[n];
            BlockPos pos2 = pos.offset(direction);
            if (!list.contains(pos2)) {
               a.add(pos2);
            }

            n++;

            while (n >= 4) {
               if (!iterator2.hasNext()) {
                  return;
               }

               pos = (BlockPos)iterator2.next();
               directions = BoxUtils.b;
               temp = directions.length;
               n = 0;
            }
         }
      }
   }

   @Override
   public void awe() {
      a.clear();
   }
}
