package pw.hachimi.client.module.misc;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.AntiPortalNetherOverworldMode;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class AntiPortal extends ClientModule {
   Stopwatch stopwatch = new StopwatchImpl();
   private final Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("1.21", "No need toggle this", false));
   Setting netherOverworldMode = this.registerSetting(new EnumSetting("World", "Working in the specified world", AntiPortalNetherOverworldMode.OVERWORLD, AntiPortalNetherOverworldMode.values()));
   Setting numberSetting1 = this.registerSetting(new NumberSetting("Delay", "Delay between placements (ms)", 0, 1000, 5000));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("Rotate", "Look at placement when its being placed", true));
   Setting swapMode = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting swingMode = this.registerSetting(new EnumSetting("Swing", "Swing side for block interaction", SwingMode.Normal, SwingMode.values()));

   public AntiPortal() {
      super("AntiPortal", "Say go fuck your self to portals", ModuleCategory.MISC);
   }

   @EventListener
   public void apL(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (this.stopwatch.afq((Number)this.numberSetting1.atx())) {
         if (client.player.isOnGround()) {
            switch ((AntiPortalNetherOverworldMode)this.netherOverworldMode.atx()) {
               case NETHER:
                  if (client.world.getRegistryKey() != World.NETHER) {
                     return;
                  }
                  break;
               case OVERWORLD:
                  if (client.world.getRegistryKey() != World.OVERWORLD) {
                     return;
                  }
            }

            StackSlotHelper stackSlotHelper = new StackSlotHelper(Items.WATER_BUCKET, (SwapMode)this.swapMode.atx()).LK();
            if (stackSlotHelper.LL()) {
               Iterator iterator2 = BoxUtils.jE(client.player.getBoundingBox()).iterator();

               while (iterator2.hasNext()) {
                  BlockPos pos = (BlockPos)iterator2.next();
                  BlockState temp = client.world.getBlockState(pos);
                  Block block2 = Blocks.NETHER_PORTAL;
                  if (temp.isOf(block2)) {
                     ClientWorld world2 = client.world;
                     BlockPos pos2 = pos.down();
                     temp = world2.getBlockState(pos2);
                     Block block3 = Blocks.OBSIDIAN;
                     if (temp.isOf(block3)) {
                        int n = Managers.inventoryTracker.JS();
                        stackSlotHelper.LO(n);
                        float[] fs = RotationUtils.nM(pos.toBottomCenterPos());
                        Managers.movementTracker.Mi(fs[0], fs[1]);
                        if ((Boolean)this.booleanSetting1_1.atx()) {
                           final float[] fs2 = fs;
                           Managers.serverManager.iQ(new SequencedPacketCreator() {
                              public Packet predict(int tickEntitiesPreEvent) {
                                 return AntiPortal.apN(fs2, tickEntitiesPreEvent);
                              }
                           });
                        } else {
                           if ((Boolean)this.booleanSetting2_1.atx()) {
                              RotationUtils.nH(fs[0], fs[1]);
                           }

                           final float[] fs3 = fs;
                           Managers.serverManager.iQ(new SequencedPacketCreator() {
                              public Packet predict(int tickEntitiesPreEvent) {
                                 return AntiPortal.apM(fs3, tickEntitiesPreEvent);
                              }
                           });
                           if ((Boolean)this.booleanSetting2_1.atx()) {
                              RotationUtils.nK();
                           }
                        }

                        ((SwingMode)this.swingMode.atx()).avX(Hand.MAIN_HAND);
                        client.itemUseCooldown = 4;
                        stackSlotHelper.LP(n);
                        this.stopwatch.afr(29432, 5265492, (byte)40);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   static Packet apM(float[] fs, int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, fs[0], fs[1]);
   }

   static Packet apN(float[] fs, int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, fs[0], fs[1]);
   }
}
