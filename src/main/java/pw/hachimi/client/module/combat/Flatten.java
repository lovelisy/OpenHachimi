package pw.hachimi.client.module.combat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.movement.Velocity;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.PlayerBlockPosHelper;
import pw.hachimi.eventbus.annotation.EventListener;

public class Flatten extends Multitask {
   private static final Set a = new HashSet();
   Setting blocks = this.registerSetting(new BlockListSetting("Blocks", "What blocks to use for surround", Blocks.OBSIDIAN));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Delay, in ticks, between placements", 0, 2, 20));
   Setting bpt = this.registerSetting(new NumberSetting("BPT", "in places, number of blocks", 1, 1, 8));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting cover = this.registerSetting(new BooleanSetting("Cover", "Only place covered blocks", false));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the obsidian being placed", true));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "The distance of your block reach", 0.0, 4.5, 6.0));
   Setting breakCrystal = this.registerSetting(new BooleanSetting("BreakCrystal", "Break crystals when placing blocks", true));
   Setting breakRange = this.registerSetting(new NumberSetting("BreakRange", "The distance of your entity reach", 0.0, 3.0, 6.0));
   Setting assumeBreak = this.registerSetting(new BooleanSetting("AssumeBreak", "Assuming it has been broken", true));
   Setting breakTime = this.registerSetting(new NumberSetting("BreakTime", "Break time", 0.0, 0.7, 2.0, this::isBreakTimeVisible));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
   Setting multi = this.registerSetting(new BooleanSetting("Multi", "Not only place one block", true));
   Setting groundOnly = this.registerSetting(new BooleanSetting("GroundOnly", "Works only when you are standing on blocks", false));
   Setting phasedOnly = this.registerSetting(new BooleanSetting("PhasedOnly", "Works only when you are phasing", false));
   Setting pauseOnFlying = this.registerSetting(new BooleanSetting("PauseOnFlying", "Pauses the current process while you are flying", false));
   Setting pauseInFluid = this.registerSetting(new BooleanSetting("PauseInFluid", "Pauses the current process while you are swimming", false));
   Setting disableOnJump = this.registerSetting(new BooleanSetting("DisableOnJump", "Automatically disables when you jumping", false));
   Setting disableOnDeath = this.registerSetting(new BooleanSetting("DisableOnDeath", "Toggles off when you die", true));
   private int u;

   public Flatten() {
      super("Flatten", "Flatten your ground", ModuleCategory.COMBAT, 950);
   }

   @Override
   public void awd() {
      this.u = 0;
   }

   @Override
   public void awe() {
      a.clear();
   }

   @EventListener
   private void asX(TickEntitiesPreEvent tickEntitiesPreEvent) {
      a.clear();
      if ((Boolean)this.disableOnJump.atx() && client.player.input.jumping) {
         this.awc();
      } else if (this.u > 0) {
         this.u--;
      } else {
         this.u = (Integer)this.delay.atx();
         if ((Boolean)this.groundOnly.atx() && !client.player.isOnGround()) {
            ClientWorld temp = client.world;
            BlockPos pos = client.player.getBlockPos();
            if (!(temp.getBlockState(pos).getBlock() instanceof LadderBlock)) {
               return;
            }
         }

         if (!(Boolean)this.phasedOnly.atx() || Velocity.amY()) {
            if (!(Boolean)this.pauseOnFlying.atx() || !client.player.isFallFlying()) {
               if (!(Boolean)this.pauseInFluid.atx() || !client.player.isInFluid()) {
                  if ((Boolean)this.breakSetting.atx() || !client.player.isUsingItem()) {
                     if (!Blink.bm()) {
                        if ((Boolean)this.multi.atx()) {
                           List list = WorldUtils.MZ(client.player.getPos());
                           Iterator iterator2 = list.iterator();

                           while (iterator2.hasNext()) {
                              BlockPos pos2 = (BlockPos)iterator2.next();
                              List list2 = list;
                              BlockPos pos3 = pos2.down();
                              if (!list2.contains(pos3) && (!(Boolean)this.cover.atx() || CollisionUtils.aiy(client.player, new Box(pos2)))) {
                                 Set set = a;
                                 BlockPos pos4 = pos2.down();
                                 set.add(pos4);
                              }
                           }
                        } else if (!(Boolean)this.cover.atx() || CollisionUtils.aiy(client.player, new Box(client.player.getBlockPos()))) {
                           Set set2 = a;
                           BlockPos pos5 = client.player.getBlockPos().down();
                           set2.add(pos5);
                        }

                        int n = 0;
                        Iterator iterator3 = a.iterator();

                        while (iterator3.hasNext()) {
                           BlockPos pos6 = (BlockPos)iterator3.next();
                           if (this.asY(pos6)) {
                              n++;
                           }

                           if (n >= (Integer)this.bpt.atx()) {
                              return;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean asY(BlockPos pos) {
      BlockPosBlockItemHelper blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos, (List)this.blocks.atx(), (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx());
      PlayerBlockPosHelper playerBlockPosHelper = Managers.breakManager.Rt(pos);
      boolean flag;
      if (!(Boolean)this.assumeBreak.atx() || playerBlockPosHelper == null || !playerBlockPosHelper.stopwatchImpl.afq((Double)this.breakTime.atx() * 1000.0) && !playerBlockPosHelper.f) {
         flag = blockPosBlockItemHelper.avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
      } else {
         flag = blockPosBlockItemHelper.avO((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
      }

      return flag;
   }

   @EventListener
   private void onChannelRead0(ChannelRead0Event channelRead0Event) {
      Packet packet2 = channelRead0Event.getPacket();
      if (packet2 instanceof DeathMessageS2CPacket) {
         ClientWorld temp = client.world;
         int n = ((DeathMessageS2CPacket)packet2).playerId();
         if (temp.getEntityById(n) == client.player && (Boolean)this.disableOnDeath.atx()) {
            this.awc();
         }
      }
   }

   private Boolean isBreakTimeVisible() {
      return (Boolean)this.assumeBreak.atx();
   }
}
