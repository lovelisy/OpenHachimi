package pw.hachimi.client.module.combat;

import java.time.Instant;
import java.util.BitSet;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.encryption.NetworkEncryptionUtils.SecureRandomUtil;
import net.minecraft.network.message.LastSeenMessageList.Acknowledgment;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.player.PlayerWorldOtherClientPlayerEntity;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.player.StackPlayerUtils2;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoLog extends ClientModule {
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting health = this.registerSetting(new NumberSetting("Health", "Disconnects when player reaches this health", 0.1F, 5.0F, 19.0F));
   Setting healthTotems = this.registerSetting(new BooleanSetting("HealthTotems", "Totem check for health config", true));
   Setting onRender = this.registerSetting(new BooleanSetting("OnRender", "Disconnects when a player enters render distance", false));
   Setting noTotems = this.registerSetting(new BooleanSetting("NoTotems", "Disconnects when player has no totems in the inventory", false));
   Setting totems = this.registerSetting(new NumberSetting("Totems", "The number of totems before disconnecting", 0, 1, 5));
   Setting spawnInvincibility = this.registerSetting(new BooleanSetting("SpawnInvincibility", "Accounts for spawn invincibility for logout", false));
   Setting invincibilityTime = this.registerSetting(new NumberSetting("InvincibilityTime", "The spawn invincibility time ", 0.1F, 2.9F, 5.0F, this::isInvincibilityTimeVisible));
   Setting illegalDisconnect = this.registerSetting(new BooleanSetting("IllegalDisconnect", "Disconnects from the server using invalid packets", false));
   Setting yCheck = this.registerSetting(new BooleanSetting("YCheck", "Disconnects when player current altitude is lower than the set value", false));
   Setting minY = this.registerSetting(new NumberSetting("MinY", "The number of height before disconnecting", -100, 0, 500));
   Setting autoDisable = this.registerSetting(new BooleanSetting("AutoDisable", "Automatically disables", true));

   public AutoLog() {
      super("AutoLog", "Automatically disconnects from server during combat", ModuleCategory.COMBAT);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (!(Boolean)this.spawnInvincibility.atx() || this.stopwatch.afq((Float)this.invincibilityTime.atx() * 1000.0F)) {
            if ((Boolean)this.yCheck.atx() && client.player.getY() < ((Integer)this.minY.atx()).intValue()) {
               this.aa("[AutoLog] Your current altitude is" + client.player.getY());
            } else {
               if ((Boolean)this.onRender.atx()) {
                  Stream temp = client.world.getPlayers().stream();
                  final AutoLog autoLog = this;
                  Predicate predicate = new Predicate() {
                     @Override
                     public boolean test(Object tickPreEvent) {
                        return autoLog.ab((AbstractClientPlayerEntity)tickPreEvent);
                     }
                  };
                  AbstractClientPlayerEntity player2 = (AbstractClientPlayerEntity)temp.filter(predicate).findFirst().orElse(null);
                  if (player2 != null) {
                     Object[] objs = new Object[]{player2.getName().getString()};
                     this.aa("[AutoLog] %s came into render distance.", objs);
                     return;
                  }
               }

               float f;
               label39: {
                  f = client.player.getHealth() + client.player.getAbsorptionAmount();
                  int n = StackPlayerUtils2.abK(Items.TOTEM_OF_UNDYING);
                  if (n <= (Integer)this.totems.atx()) {
                     if (f <= (Float)this.health.atx()) {
                        if ((Boolean)this.healthTotems.atx()) {
                           Object[] objs2 = new Object[]{n, (int)f};
                           this.aa("[AutoLog] logged out with %d totems and %d hearts remaining.", objs2);
                           return;
                        }
                        break label39;
                     }

                     if ((Boolean)this.noTotems.atx()) {
                        Object[] objs3 = new Object[]{n};
                        this.aa("[AutoLog] logged out with %d totems remaining.", objs3);
                     }
                  } else if (f <= (Float)this.health.atx() && !(Boolean)this.healthTotems.atx()) {
                     break label39;
                  }

                  return;
               }

               Object[] objs4 = new Object[]{(int)f};
               this.aa("[AutoLog] logged out with %d hearts remaining.", objs4);
            }
         }
      }
   }

   @EventListener
   public void onOnGameJoin(OnGameJoinEvent onGameJoinEvent) {
      this.stopwatch.afr(29432, 5265492, (byte)40);
   }

   private void aa(String str, Object... value) {
      if ((Boolean)this.illegalDisconnect.atx()) {
         ChatMessageC2SPacket chatMessageC2SPacket = a(Instant.now(), SecureRandomUtil.nextLong(), a(c()));
         Managers.serverManager.iO('\uf560', chatMessageC2SPacket);
         if ((Boolean)this.autoDisable.atx()) {
            this.awc();
         }
      } else if (client.getNetworkHandler() == null) {
         client.world.disconnect();
         if ((Boolean)this.autoDisable.atx()) {
            this.awc();
         }
      } else {
         str = String.format(str, value);
         ClientConnection temp = client.getNetworkHandler().getConnection();
         Text text2 = Text.of(str);
         temp.disconnect(text2);
         if ((Boolean)this.autoDisable.atx()) {
            this.awc();
         }
      }
   }

   boolean ab(AbstractClientPlayerEntity player2) {
      boolean flag;
      if (player2 != client.player && !Managers.friendManager.awt(player2.getName()) && !(player2 instanceof PlayerWorldOtherClientPlayerEntity)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isInvincibilityTimeVisible() {
      return (Boolean)this.spawnInvincibility.atx();
   }

   private static BitSet c() {
      return new BitSet(2);
   }

   private static ChatMessageC2SPacket a(Instant instant, long l, Acknowledgment acknowledgment) {
      return new ChatMessageC2SPacket("§", instant, l, null, acknowledgment);
   }

   private static Acknowledgment a(BitSet bitSet) {
      return new Acknowledgment(1, bitSet);
   }
}
