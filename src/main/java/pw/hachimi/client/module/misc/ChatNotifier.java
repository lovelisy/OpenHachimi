package pw.hachimi.client.module.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.network.PlayerListEntry;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.UUID;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import pw.hachimi.client.api.UnidentifiedInterface11;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.player.OnEntityEquipmentUpdateEvent;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.event.world.AddEntityEvent;
import pw.hachimi.client.event.world.RemoveEntityEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.player.PlayerWorldOtherClientPlayerEntity;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public final class ChatNotifier extends ClientModule {
   Setting totemPop = this.registerSetting(new BooleanSetting("TotemPop", "Notifies in chat when a player pops a totem", true));
   Setting visualRange = this.registerSetting(new BooleanSetting("VisualRange", "Notifies in chat when player enters visual range", false));
   Setting sounds = this.registerSetting(new BooleanSetting("Sounds", "Plays sound when player enters visual range", true));
   Setting ignoreNakeds = this.registerSetting(new BooleanSetting("IgnoreNakeds", "Ignore players with no items", false));
   Setting join = this.registerSetting(new BooleanSetting("Join", "Notifies in chat when a player joins", false));
   Setting leave = this.registerSetting(new BooleanSetting("Leave", "Notifies in chat when a player leaves", false));
   Setting friends = this.registerSetting(new BooleanSetting("Friends", "Notifies for friends", false));
   Setting grim = this.registerSetting(new BooleanSetting("Grim", "Notifies you if the server you join is running Grim", false));
   public static boolean at;

   public ChatNotifier() {
      super("ChatNotifier", "Notifies in chat", ModuleCategory.MISC);
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof EntityStatusS2CPacket && ((EntityStatusS2CPacket)packet2).getStatus() == 35 && (Boolean)this.totemPop.atx()) {
            Entity entity2 = ((EntityStatusS2CPacket)packet2).getEntity((World)client.world);
            if (!(entity2 instanceof LivingEntity)) {
               return;
            }

            int n = Managers.entityEntityStatusS2CPacketManager.acp(entity2);
            String str = entity2.getName().getString();
            boolean flag = Managers.friendManager.awq(str);
            if (flag && !(Boolean)this.friends.atx()) {
               return;
            }

            String str2 = entity2 == client.player ? "§fYou" : "§f" + str;
            ChatUtils.UC(str2 + "§s popped §f" + n + "§s totems", entity2.hashCode() + this.hashCode() + 1);
         }

         packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof PlayerListS2CPacket && (Boolean)this.join.atx()) {
            if (!((PlayerListS2CPacket)packet2).getActions().contains(Action.ADD_PLAYER)) {
               return;
            }

            if (client.player.age < 20) {
               return;
            }

            Predicate predicate = obj -> this.akl((Entry)obj);
            Predicate predicate2 = obj -> this.akk((Entry)obj);
            Consumer consumer = obj -> this.akj((Entry)obj);
            ((PlayerListS2CPacket)packet2).getEntries().stream().filter(predicate).filter(predicate2).forEach(consumer);
         }

         packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof PlayerRemoveS2CPacket) {
            List list = ((PlayerRemoveS2CPacket)packet2).profileIds();
            if (!(Boolean)this.leave.atx()) {
               return;
            }

            if (client.getNetworkHandler() == null) {
               return;
            }

            Iterator iterator2 = list.iterator();

            while (iterator2.hasNext()) {
               UUID uuid = (UUID)iterator2.next();
               String str3 = null;
               Iterator iterator3 = new ArrayList(client.getNetworkHandler().getPlayerList()).iterator();

               while (iterator3.hasNext()) {
                  PlayerListEntry playerListEntry = (PlayerListEntry)iterator3.next();
                  if (playerListEntry != null) {
                     GameProfile gameProfile = playerListEntry.getProfile();
                     if (gameProfile.getId().equals(uuid)) {
                        str3 = gameProfile.getName();
                        break;
                     }
                  }
               }

               if (str3 != null) {
                  boolean flag2 = Managers.friendManager.awq(str3);
                  if (flag2 && !(Boolean)this.friends.atx()) {
                     return;
                  }

                  String str4 = flag2 ? "§g" : "§7";
                  ChatUtils.UC(str4 + str3 + "§f has left the server", str3.hashCode() + this.hashCode() + 2);
               }
            }
         }
      }
   }

   @EventListener
   public void onAddEntity(AddEntityEvent addEntityEvent) {
      if ((Boolean)this.visualRange.atx()) {
         Entity entity2 = addEntityEvent.getEntity();
         if (entity2 instanceof PlayerEntity) {
            PlayerEntity player2 = (PlayerEntity)entity2;
            if (!(addEntityEvent.getEntity() instanceof PlayerWorldOtherClientPlayerEntity)) {
               String str = addEntityEvent.getEntity().getName().getString();
               boolean flag = Managers.friendManager.awq(str);
               if ((!flag || (Boolean)this.friends.atx()) && addEntityEvent.getEntity() != client.player) {
                  if ((Boolean)this.ignoreNakeds.atx()) {
                     ((UnidentifiedInterface11)addEntityEvent.getEntity()).Lm(true);
                  } else {
                     MinecraftClient temp = client;
                     String temp2 = str;
                     final PlayerEntity player3 = player2;
                     final String str2 = temp2;
                     final boolean flag2 = flag;
                     final ChatNotifier chatNotifier = this;
                     Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                           chatNotifier.aki(flag2, str2, player3);
                        }
                     };
                     temp.executeSync(runnable);
                     if ((Boolean)this.sounds.atx()) {
                        Managers.soundManager.jg(SoundManager.f);
                     }
                  }

                  return;
               }

               return;
            }
         }
      }
   }

   @EventListener
   public void onOnEntityEquipmentUpdate(OnEntityEquipmentUpdateEvent onEntityEquipmentUpdateEvent) {
      if ((Boolean)this.ignoreNakeds.atx() && (Boolean)this.visualRange.atx()) {
         ClientWorld temp = client.world;
         int n = onEntityEquipmentUpdateEvent.getEntityEquipmentUpdateS2CPacket().getEntityId();
         Entity entity2 = temp.getEntityById(n);
         if (entity2 instanceof PlayerEntity && ((UnidentifiedInterface11)((PlayerEntity)entity2)).Ll() && !WorldUtils.NO((PlayerEntity)entity2)) {
            String str = ((PlayerEntity)entity2).getName().getString();
            boolean flag = Managers.friendManager.awq(str);
            MinecraftClient client2 = client;
            String temp2 = str;
            final PlayerEntity player2 = (PlayerEntity)entity2;
            final String str2 = temp2;
            final boolean flag2 = flag;
            final ChatNotifier chatNotifier = this;
            Runnable runnable = new Runnable() {
               @Override
               public void run() {
                  chatNotifier.akh(flag2, str2, player2);
               }
            };
            client2.executeSync(runnable);
            if ((Boolean)this.sounds.atx()) {
               Managers.soundManager.jg(SoundManager.f);
            }

            ((UnidentifiedInterface11)((PlayerEntity)entity2)).Lm(false);
         }
      }
   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent removeEntityEvent) {
      if ((Boolean)this.visualRange.atx()) {
         Entity entity2 = removeEntityEvent.getEntity();
         if (entity2 instanceof PlayerEntity) {
            PlayerEntity player2 = (PlayerEntity)entity2;
            if (!(removeEntityEvent.getEntity() instanceof PlayerWorldOtherClientPlayerEntity)) {
               String str = removeEntityEvent.getEntity().getName().getString();
               boolean flag = Managers.friendManager.awq(str);
               if ((!flag || (Boolean)this.friends.atx()) && removeEntityEvent.getEntity() != client.player) {
                  MinecraftClient temp = client;
                  String temp2 = str;
                  final PlayerEntity player3 = player2;
                  final String str2 = temp2;
                  final boolean flag2 = flag;
                  final ChatNotifier chatNotifier = this;
                  Runnable runnable = new Runnable() {
                     @Override
                     public void run() {
                        chatNotifier.akg(flag2, str2, player3);
                     }
                  };
                  temp.executeSync(runnable);
                  return;
               }

               return;
            }
         }
      }
   }

   @EventListener
   public void ake(UpdateDeathEvent updateDeathEvent) {
      if ((Boolean)this.totemPop.atx()) {
         int n = Managers.entityEntityStatusS2CPacketManager.acp(updateDeathEvent.getLiving());
         if (n != 0) {
            String str = updateDeathEvent.getLiving().getName().getString();
            boolean flag = Managers.friendManager.awq(str);
            if ((!flag || (Boolean)this.friends.atx()) && updateDeathEvent.getLiving() != client.player) {
               String str2;
               if (flag) {
                  str2 = "§g";
               } else {
                  str2 = "§7";
               }

               ChatUtils.UC(str2 + str + "§f died after popping §s" + n + "§f totems", updateDeathEvent.getLiving().hashCode() + this.hashCode() + 1);
            }
         }
      }
   }

   @EventListener
   public void onOnGameJoin(OnGameJoinEvent onGameJoinEvent) {
      if ((Boolean)this.grim.atx() && !client.isInSingleplayer() && Managers.antiCheatManager.Ug()) {
         ChatUtils.UC("This server is running GrimAC", 102);
      }
   }

   void akg(boolean flag, String str, PlayerEntity player2) {
      String str2;
      if (flag) {
         str2 = "§g";
      } else {
         str2 = "§c";
      }

      String str3 = str2;
      ChatUtils.UF("§s[VisualRange] " + str3 + str + "§f left your visual range", player2.hashCode() + this.hashCode() + 3);
   }

   void akh(boolean flag, String str, PlayerEntity player2) {
      String str2;
      if (flag) {
         str2 = "§g";
      } else {
         str2 = "§7";
      }

      String str3 = str2;
      ChatUtils.UF("§s[VisualRange] " + str3 + str + "§f entered your visual range", player2.hashCode() + this.hashCode() + 3);
   }

   void aki(boolean flag, String str, PlayerEntity player2) {
      String str2;
      if (flag) {
         str2 = "§g";
      } else {
         str2 = "§7";
      }

      String str3 = str2;
      ChatUtils.UF("§s[VisualRange] " + str3 + str + "§f entered your visual range", player2.hashCode() + this.hashCode() + 3);
   }

   void akj(Entry entry) {
      String str = entry.profile().getName();
      if (str != null) {
         boolean flag = Managers.friendManager.awq(str);
         if (!(Boolean)this.friends.atx() && flag) {
            return;
         }

         String str2;
         if (flag) {
            str2 = "§g";
         } else {
            str2 = "§7";
         }

         ChatUtils.UC(str2 + str + "§f has joined the server", str.hashCode() + this.hashCode() + 2);
      }
   }

   static boolean akk(Entry entry) {
      boolean flag;
      if ((entry.profile().getName() == null || entry.profile().getName().isEmpty()) && entry.profile().getId() == null) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   static boolean akl(Entry entry) {
      boolean flag;
      if (entry != null && entry.profile() != null) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
