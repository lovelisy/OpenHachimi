package pw.hachimi.client.module.misc;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.world.PlacementUtils;

public class PenisBuilder extends ClientModule {
   Setting swapMode = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Rotate", "Rotate before placing", true));
   public static boolean ak;

   public PenisBuilder() {
      super("PenisBuilder", "Build a pillar in an instant", ModuleCategory.MISC);
   }

   @Override
   protected void awd() {
      if (client.player != null) {
         StackSlotHelper stackSlotHelper = new StackSlotHelper(Items.OBSIDIAN, (SwapMode)this.swapMode.atx()).LK();
         if (stackSlotHelper.LL()) {
            int n = Managers.inventoryTracker.JS();
            if ((Boolean)this.booleanSetting1_1.atx()) {
               RotationUtils.nH(client.player.getYaw(), 90.0F);
            }

            stackSlotHelper.LO(n);
            int n2 = 0;

            do {
               PositionAndOnGround positionAndOnGround = c(client.player.getX(), client.player.getY() + 0.41999998688698, client.player.getZ());
               Managers.serverManager.iO('\uf560', positionAndOnGround);
               positionAndOnGround = c(client.player.getX(), client.player.getY() + 0.7531999805212, client.player.getZ());
               Managers.serverManager.iO('\uf560', positionAndOnGround);
               positionAndOnGround = c(client.player.getX(), client.player.getY() + 1.0, client.player.getZ());
               Managers.serverManager.iO('\uf560', positionAndOnGround);
               ClientPlayerEntity temp = client.player;
               Vec3d vec = client.player.getPos().add(0.0, 1.0, 0.0);
               temp.setPosition(vec);
               BlockPos pos = client.player.getBlockPos().down();
               ClientPlayNetworkHandler network2 = client.getNetworkHandler();
               Hand hand = Hand.MAIN_HAND;
               ClientPlayNetworkHandler network3 = network2;
               Hand temp2 = hand;
               Vec3d temp3 = pos.toBottomCenterPos();
               Direction direction = Direction.UP;
               Vec3d vec2 = temp3;
               Hand hand2 = temp2;
               ClientPlayNetworkHandler network4 = network3;
               PlayerInteractBlockC2SPacket playerInteractBlockC2SPacket = b(hand2, c(vec2, direction, pos.down()));
               network4.sendPacket((Packet)playerInteractBlockC2SPacket);
               PlacementUtils.swingMode2.avX(Hand.MAIN_HAND);
            } while (++n2 < 5);

            stackSlotHelper.LP(n);
            RotationUtils.nK();
         }
      }

      this.awc();
   }

   private static BlockHitResult c(Vec3d vec, Direction direction, BlockPos pos) {
      return new BlockHitResult(vec, direction, pos, false);
   }

   private static PlayerInteractBlockC2SPacket b(Hand hand, BlockHitResult blockHitResult) {
      return new PlayerInteractBlockC2SPacket(hand, blockHitResult, 0);
   }

   private static PositionAndOnGround c(double d, double d2, double d3) {
      return new PositionAndOnGround(d, d2, d3, true);
   }
}
