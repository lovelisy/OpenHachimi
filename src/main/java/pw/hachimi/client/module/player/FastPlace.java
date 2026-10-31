package pw.hachimi.client.module.player;

import java.util.concurrent.TimeUnit;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.mixin.MinecraftClientMixin2;
import pw.hachimi.client.model.player.FastPlaceWhitelistBlacklistMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.ItemListSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.world.BlockStateUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class FastPlace extends ClientModule {
   private final StopwatchImpl stopwatchImpl = new StopwatchImpl();
   Setting selection = this.registerSetting(new EnumSetting("Selection", "The selection of items to apply fast placements", FastPlaceWhitelistBlacklistMode.WHITELIST, FastPlaceWhitelistBlacklistMode.values()));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Fast place click delay", 0, 1, 4));
   Setting startDelay = this.registerSetting(new NumberSetting("StartDelay", "Fast place start delay", 0.0F, 0.0F, 1.0F));
   Setting ghostFix = this.registerSetting(new BooleanSetting("GhostFix", "Fixes item ghosting issue on some servers", false));
   Setting whitelist = this.registerSetting(new ItemListSetting("Whitelist", "Valid item whitelist", Items.EXPERIENCE_BOTTLE, Items.SNOWBALL, Items.EGG));
   Setting blacklist = this.registerSetting(new ItemListSetting("Blacklist", "Valid item blacklist", Items.ENDER_PEARL, Items.ENDER_EYE));
   public static boolean dx;

   public FastPlace() {
      super("FastPlace", "Place items and blocks faster", ModuleCategory.PLAYER);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (!client.options.useKey.isPressed()) {
            this.stopwatchImpl.afr(29432, 5265492, (byte)40);
         } else if (this.stopwatchImpl.afO((Number)this.startDelay.atx(), TimeUnit.SECONDS)
            && ((MinecraftClientMixin2)client).hookGetItemUseCooldown() > (Integer)this.delay.atx()) {
            ClientPlayerEntity player2 = client.player;
            if (this.abw(player2.getMainHandStack())) {
               if ((Boolean)this.ghostFix.atx()) {
                  Managers.serverManager.iQ(FastPlace::abx);
               }

               ((MinecraftClientMixin2)client).hookSetItemUseCooldown((Integer)this.delay.atx());
            }
         }
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null && client.world != null) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof PlayerInteractBlockC2SPacket && (Boolean)this.ghostFix.atx() && !sendImmediatelyEvent.apr()) {
            ClientPlayerEntity player2 = client.player;
            ClientPlayerEntity temp = player2;
            Hand hand = ((PlayerInteractBlockC2SPacket)packet2).getHand();
            if (this.abw(temp.getStackInHand(hand))) {
               ClientWorld temp2 = client.world;
               BlockPos pos = ((PlayerInteractBlockC2SPacket)packet2).getBlockHitResult().getBlockPos();
               if (!BlockStateUtils.Ww(temp2.getBlockState(pos))) {
                  sendImmediatelyEvent.aim();
               }
            }
         }
      }
   }

   private boolean abw(ItemStack stack) {
      return switch ((FastPlaceWhitelistBlacklistMode)this.selection.atx()) {
         case WHITELIST -> ((ItemListSetting)this.whitelist).al(stack.getItem());
         case BLACKLIST -> {
            if (!((ItemListSetting)this.blacklist).al(stack.getItem())) {
               yield true;
            } else {
               yield false;
            }
         }
         case ALL -> true;
      };
   }

   private static Packet abx(int n) {
      return new PlayerInteractItemC2SPacket(client.player.getActiveHand(), n, client.player.getYaw(), client.player.getPitch());
   }

   private static MatchException k() {
      return new MatchException(null, null);
   }
}
