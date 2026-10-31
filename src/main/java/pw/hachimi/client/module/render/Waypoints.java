package pw.hachimi.client.module.render;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.config.Vec3dConfigContainer;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.movement.MoveToWorldEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.event.world.AddEntityEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.UnidentifiedHelper19;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.RegistryKeyWorldUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Waypoints extends ClientModule {
   private static Waypoints INSTANCE;
   private final Map map = Maps.newConcurrentMap();
   private final Map map2 = Maps.newConcurrentMap();
   Setting logoutPoints = this.registerSetting(new BooleanSetting("LogoutPoints", "Marks the position of player logouts", false));
   Setting _3c3u = this.registerSetting(new BooleanSetting("3C3U", "If you are playing 3c3u.org, enable this.", false, this::is_3c3uVisible));
   Setting shape = this.registerSetting(new EnumSetting("Shape", "How the shapes are rendered.", ShapeMode.Both, ShapeMode.values(), this::isShapeVisible));
   Setting side = this.registerSetting(new ColorSetting("Side", "Color of side", n(70), this::isSideVisible));
   Setting line = this.registerSetting(new ColorSetting("Line", "Color of line", n(100), this::isLineVisible));
   Setting deathPoints = this.registerSetting(new BooleanSetting("DeathPoints", "Marks the position of player deaths", false));
   Setting coords = this.registerSetting(new BooleanSetting("Coords", "Shows the coordinates of the waypoint", true));
   Setting distance = this.registerSetting(new BooleanSetting("Distance", "Shows the distance to the waypoint", true));
   public static boolean do_;

   public Waypoints() {
      super("Waypoints", "Renders a waypoint at marked locations", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static Waypoints oZ() {
      return INSTANCE;
   }

   @Override
   public void awe() {
      Managers.unidentifiedHelper19.akW();
      this.map.clear();
      this.map2.clear();
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.awe();
   }

   @EventListener
   public void onMoveToWorld(MoveToWorldEvent moveToWorldEvent) {
      this.awe();
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && (Boolean)this.logoutPoints.atx()) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof BundleS2CPacket) {
            Iterator iterator2 = ((BundleS2CPacket)packet2).getPackets().iterator();

            while (iterator2.hasNext()) {
               Packet packet3 = (Packet)iterator2.next();
               this.pd(packet3);
            }
         } else {
            this.pd(channelRead0Event.getPacket());
         }
      }
   }

   public void pd(Packet packet2) {
      if (packet2 instanceof PlayerListS2CPacket && ((PlayerListS2CPacket)packet2).getActions().contains(Action.ADD_PLAYER)) {
         if ((Boolean)this._3c3u.atx()) {
            return;
         }

         Iterator iterator2 = ((PlayerListS2CPacket)packet2).getPlayerAdditionEntries().iterator();

         while (iterator2.hasNext()) {
            Entry entry = (Entry)iterator2.next();
            Iterator iterator3 = this.map2.keySet().iterator();

            while (iterator3.hasNext()) {
               UUID uuid = (UUID)iterator3.next();
               if (uuid.equals(entry.profile().getId())) {
                  this.map2.remove(uuid);
               }
            }
         }

         this.map.clear();
      } else if (packet2 instanceof PlayerRemoveS2CPacket) {
         List list2 = ((PlayerRemoveS2CPacket)packet2).profileIds();
         Iterator iterator4 = list2.iterator();

         while (iterator4.hasNext()) {
            UUID uuid2 = (UUID)iterator4.next();
            Iterator iterator5 = this.map.keySet().iterator();

            while (iterator5.hasNext()) {
               UUID uuid3 = (UUID)iterator5.next();
               if (uuid3.equals(uuid2)) {
                  PlayerEntity player2 = (PlayerEntity)this.map.get(uuid3);
                  if (!this.map2.containsKey(uuid3)) {
                     this.map2.put(uuid3, player2);
                  }
               }
            }
         }

         this.map.clear();
      }
   }

   @EventListener
   public void onAddEntity(AddEntityEvent addEntityEvent) {
      if ((Boolean)this._3c3u.atx()) {
         Entity entity2 = addEntityEvent.getEntity();
         if (entity2 instanceof PlayerEntity) {
            PlayerEntity player2 = (PlayerEntity)entity2;
            Iterator iterator2 = this.map2.keySet().iterator();

            while (iterator2.hasNext()) {
               UUID uuid = (UUID)iterator2.next();
               UUID temp = uuid;
               UUID uuid2 = player2.getGameProfile().getId();
               if (temp.equals(uuid2)) {
                  this.map2.remove(uuid);
                  this.map.clear();
               }
            }
         }
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.POST) {
         Iterator iterator2 = client.world.getPlayers().iterator();

         while (iterator2.hasNext()) {
            PlayerEntity player2 = (PlayerEntity)iterator2.next();
            if (player2 != null) {
               PlayerEntity temp = player2;
               ClientPlayerEntity player3 = client.player;
               if (!temp.equals(player3)) {
                  Map map3 = this.map;
                  UUID uuid = player2.getGameProfile().getId();
                  map3.put(uuid, player2);
               }
            }
         }
      }
   }

   @EventListener
   public void pg(UpdateDeathEvent updateDeathEvent) {
      if (updateDeathEvent.getLiving() instanceof ClientPlayerEntity && (Boolean)this.deathPoints.atx()) {
         String str;
         if (client.isInSingleplayer()) {
            str = "Singleplayer";
         } else {
            str = Managers.serverManager.jb();
         }

         String str2 = str;
         Managers.unidentifiedHelper19.akT();
         UnidentifiedHelper19 temp = Managers.unidentifiedHelper19;
         String temp2 = str2;
         int temp3 = RegistryKeyWorldUtils.OS();
         ClientPlayerEntity player2 = client.player;
         int n2 = temp3;
         String str3 = temp2;
         UnidentifiedHelper19 unidentifiedHelper192 = temp;
         Vec3dConfigContainer vec3dConfigContainer = a(str3, n2, player2.getX(), client.player.getY(), client.player.getZ());
         unidentifiedHelper192.akQ(vec3dConfigContainer);
      }
   }

   @EventListener
   public void ph(Render3DContext render3DContext) {
      if (client.player != null && client.world != null) {
         Iterator iterator2 = Managers.unidentifiedHelper19.getList().iterator();

         while (iterator2.hasNext()) {
            Vec3dConfigContainer vec3dConfigContainer = (Vec3dConfigContainer)iterator2.next();
            String temp = vec3dConfigContainer.getText();
            boolean flag = client.isInSingleplayer();
            String str = temp;
            String str2;
            if (flag) {
               temp = str;
               str = "Singleplayer";
               str2 = temp;
            } else {
               temp = str;
               str = Managers.serverManager.jb();
               str2 = temp;
            }

            temp = str2;
            String str3 = str;
            if (temp.equalsIgnoreCase(str3) && RegistryKeyWorldUtils.OS() == vec3dConfigContainer.xR()) {
               this.pi(render3DContext, vec3dConfigContainer.xQ(), vec3dConfigContainer.getText2());
            }
         }

         if ((Boolean)this.logoutPoints.atx()) {
            iterator2 = this.map2.keySet().iterator();

            while (iterator2.hasNext()) {
               UUID uuid = (UUID)iterator2.next();
               PlayerEntity player2 = (PlayerEntity)this.map2.get(uuid);
               if (player2 != null) {
                  Vec3d vec = player2.getPos();
                  String temp2 = player2.getName().getString() + "'s Logout";
                  boolean flag2 = player2.isCrawling();
                  String str4 = temp2;
                  Vec3d vec2 = vec;
                  this.pj(render3DContext, vec2, str4, flag2);
               }
            }
         }
      }
   }

   private void pi(Render3DContext render3DContext, Vec3d vec, String str) {
      this.pj(render3DContext, vec, str, false);
   }

   private void pj(Render3DContext render3DContext, Vec3d vec, String str, boolean flag) {
      Box box;
      if (flag) {
         box = EntityDimensions.changing(0.6F, 0.6F).withEyeHeight(0.4F).getBoxAt(vec);
      } else {
         box = EntityDimensions.fixed(0.6F, 2.2F).getBoxAt(vec);
      }

      Box box2 = box;
      double d = (box.maxX - box.minX) / 2.0;
      render3DContext.boxRenderer.Zd(box, (Color)this.side.atx(), (Color)this.line.atx(), (ShapeMode)this.shape.atx());
      int n2 = (int)Math.sqrt(client.player.squaredDistanceTo(vec));
      String str2;
      if ((Boolean)this.coords.atx()) {
         Object[] temp = new Object[3];
         DecimalFormat temp2 = bo;
         double d2 = vec.getX();
         temp[0] = temp2.format(d2);
         temp2 = bo;
         double d3 = vec.getY();
         temp[1] = temp2.format(d3);
         temp2 = bo;
         double d4 = vec.getZ();
         temp[2] = temp2.format(d4);
         Object[] objs = temp;
         str2 = String.format(" XYZ %s %s %s", objs);
      } else {
         str2 = "";
      }

      String temp3 = str2;
      boolean flag2 = (Boolean)this.distance.atx();
      String str3 = temp3;
      String str4;
      String str5;
      if (flag2) {
         temp3 = str3;
         Object[] objs2 = new Object[]{n2};
         str5 = String.format(" %sm", objs2);
         str4 = temp3;
      } else {
         temp3 = str3;
         str5 = "";
         str4 = temp3;
      }

      temp3 = str4;
      String str6 = str5;
      String str7 = temp3;
      String str8 = "§7" + str + str7 + str6;
      RenderUtils.acw(render3DContext.matrices, str8, box2.minX + d, box2.maxY + 0.4, box2.minZ + d, -1);
   }

   private Boolean isLineVisible() {
      return (Boolean)this.logoutPoints.atx();
   }

   private Boolean isSideVisible() {
      return (Boolean)this.logoutPoints.atx();
   }

   private Boolean isShapeVisible() {
      return (Boolean)this.logoutPoints.atx();
   }

   private Boolean is_3c3uVisible() {
      return (Boolean)this.logoutPoints.atx();
   }

   private static Vec3dConfigContainer a(String str, int n2, double d, double d2, double d3) {
      return new Vec3dConfigContainer("Last Death", str, n2, d, d2, (short)0, 1882252337, 24401, d3);
   }

   private static Color n(int n2) {
      return new Color(0, 0, 0, n2);
   }
}
