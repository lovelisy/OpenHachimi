package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.TracersFeetTorsoMode;
import pw.hachimi.client.model.render.TracersOnScreenOffScreenMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.player.Freecam;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.BoxEntityUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Tracers extends ClientModule {
   Setting mode = this.registerSetting(new EnumSetting("Mode", "Render tracers to entities not visible on the screen", TracersOnScreenOffScreenMode.NORMAL, TracersOnScreenOffScreenMode.values()));
   Setting target = this.registerSetting(new EnumSetting("Target", "The body part of the entity to target", TracersFeetTorsoMode.FEET, TracersFeetTorsoMode.values()));
   Setting width = this.registerSetting(new NumberSetting("Width", "The line width of the tracer", 1.0F, 1.0F, 5.0F));
   Setting players = this.registerSetting(new BooleanSetting("Players", "Render tracers to player", true));
   Setting playersColor = this.registerSetting(new ColorSetting("PlayersColor", "The render color for players", new Color(200, 60, 60), false, this::isPlayersColorVisible));
   Setting friends = this.registerSetting(new BooleanSetting("Friends", "Render tracers to players that are friended", false, this::isFriendsVisible));
   Setting invisibles = this.registerSetting(new BooleanSetting("Invisibles", "Render tracers to invisible entities", false));
   Setting invisiblesColor = this.registerSetting(new ColorSetting("InvisiblesColor", "The render color for invisibles", new Color(200, 100, 0), false, this::isInvisiblesColorVisible));
   Setting monsters = this.registerSetting(new BooleanSetting("Monsters", "Render tracers to monsters", false));
   Setting monstersColor = this.registerSetting(new ColorSetting("MonstersColor", "The render color for monsters", new Color(200, 60, 60), false, this::isMonstersColorVisible));
   Setting animals = this.registerSetting(new BooleanSetting("Animals", "Render tracers to animals", false));
   Setting animalsColor = this.registerSetting(new ColorSetting("AnimalsColor", "The render color for animals", new Color(0, 200, 0), false, this::isAnimalsColorVisible));
   Setting vehicles = this.registerSetting(new BooleanSetting("Vehicles", "Render tracers to vehicles", false));
   Setting vehiclesColor = this.registerSetting(new ColorSetting("VehiclesColor", "The render color for vehicles", new Color(200, 100, 0), false, this::isVehiclesColorVisible));
   Setting items = this.registerSetting(new BooleanSetting("Items", "Render tracers to items", false));
   Setting itemsColor = this.registerSetting(new ColorSetting("ItemsColor", "The render color for items", new Color(255, 255, 255), false, this::isItemsColorVisible));
   private int q;

   public Tracers() {
      super("Tracers", "Draws a tracer to all entities in render distance", ModuleCategory.RENDER);
   }

   @Override
   public String hy() {
      return Integer.toString(this.q);
   }

   @EventListener
   public void amQ(Render3DContext render3DContext) {
      this.q = 0;
      Vector3f vector3f = new Vector3f(0.0F, 0.0F, 1.0F);
      if ((Boolean)client.options.getBobView().getValue() && !Freecam.gP().awg()) {
         MatrixStack matrices = new MatrixStack();
         Entity entity2 = MinecraftClient.getInstance().getCameraEntity();
         if (entity2 instanceof PlayerEntity) {
            float f2 = client.getRenderTickCounter().getTickDelta(true);
            float f3 = ((PlayerEntity)entity2).horizontalSpeed - ((PlayerEntity)entity2).prevHorizontalSpeed;
            float f4 = -(((PlayerEntity)entity2).horizontalSpeed + f3 * f2);
            float f5 = MathHelper.lerp(f2, ((PlayerEntity)entity2).prevStrideDistance, ((PlayerEntity)entity2).strideDistance);
            double temp = -(MathHelper.sin(f4 * (float) Math.PI) * f5 * 0.5);
            double d = Math.abs(MathHelper.cos(f4 * (float) Math.PI) * f5);
            double d2 = temp;
            matrices.translate(d2, d, 0.0);
            RotationAxis rotationAxis = RotationAxis.POSITIVE_Z;
            float f6 = MathHelper.sin(f4 * (float) Math.PI) * f5 * 3.0F;
            Quaternionf quaternionf = rotationAxis.rotationDegrees(f6);
            matrices.multiply(quaternionf);
            RotationAxis rotationAxis2 = RotationAxis.POSITIVE_X;
            float f7 = Math.abs(MathHelper.cos(f4 * (float) Math.PI - 0.2F) * f5) * 5.0F;
            Quaternionf quaternionf2 = rotationAxis2.rotationDegrees(f7);
            matrices.multiply(quaternionf2);
         }

         vector3f.mulPosition(matrices.peek().getPositionMatrix().invert());
      }

      Vec3d temp2 = new Vec3d(vector3f.x, -vector3f.y, vector3f.z);
      float f8 = -((float)Math.toRadians(client.gameRenderer.getCamera().getPitch()));
      temp2 = temp2.rotateX(f8);
      float f9 = -((float)Math.toRadians(client.gameRenderer.getCamera().getYaw()));
      temp2 = temp2.rotateY(f9);
      Vec3d vec = client.gameRenderer.getCamera().getPos();
      Vec3d vec2 = temp2.add(vec);
      Iterator iterator2 = client.world.getEntities().iterator();

      while (iterator2.hasNext()) {
         Entity entity3 = (Entity)iterator2.next();
         switch ((TracersOnScreenOffScreenMode)this.mode.atx()) {
            case ON_SCREEN:
               boolean flag = RenderUtils.acy(entity3.getBoundingBox());
               if (entity3 == null || !entity3.isAlive() || entity3 == client.player || !flag) {
                  continue;
               }
               break;
            case OFF_SCREEN:
               if (RenderUtils.acy(entity3.getBoundingBox())) {
                  if (entity3 != null && entity3.isAlive() && entity3 != client.player) {
                  }
                  continue;
               }
            case NORMAL:
               if (entity3 == null || !entity3.isAlive() || entity3 == client.player) {
                  continue;
               }
               break;
            default:
               throw new MatchException(null, null);
         }

         if (!(entity3 instanceof PlayerEntity) || !Managers.friendManager.awt(entity3.getDisplayName()) || (Boolean)this.friends.atx()) {
            Color color = this.amR(entity3);
            if (color != null) {
               double d3 = entity3.prevX + (entity3.getX() - entity3.prevX) * render3DContext.f;
               double d4 = entity3.prevY + (entity3.getY() - entity3.prevY) * render3DContext.f;
               double d5 = entity3.prevZ + (entity3.getZ() - entity3.prevZ) * render3DContext.f;
               double d6 = entity3.getBoundingBox().maxY - entity3.getBoundingBox().minY;
               if (this.target.atx() == TracersFeetTorsoMode.HEAD) {
                  d4 += d6;
               } else if (this.target.atx() == TracersFeetTorsoMode.TORSO) {
                  d4 += d6 / 2.0;
               }

               render3DContext.boxRenderer.YL(vec2.x, vec2.y, vec2.z, d3, d4, d5, color, (Float)this.width.atx() / 4.0F);
               this.q++;
            }
         }
      }
   }

   private Color amR(Entity entity2) {
      if (entity2.isInvisible() && (Boolean)this.invisibles.atx()) {
         return (Color)this.invisiblesColor.atx();
      } else if (entity2 instanceof PlayerEntity && (Boolean)this.players.atx()) {
         return Managers.friendManager.awt(((PlayerEntity)entity2).getName()) ? ColorHelper.wx().wB() : (Color)this.playersColor.atx();
      } else if (BoxEntityUtils.kF(entity2) && (Boolean)this.monsters.atx()) {
         return (Color)this.monstersColor.atx();
      } else if ((BoxEntityUtils.kI(entity2) || BoxEntityUtils.kH(entity2)) && (Boolean)this.animals.atx()) {
         return (Color)this.animalsColor.atx();
      } else if (BoxEntityUtils.kJ(entity2) && (Boolean)this.vehicles.atx()) {
         return (Color)this.vehiclesColor.atx();
      } else {
         return entity2 instanceof ItemEntity && (Boolean)this.items.atx() ? (Color)this.itemsColor.atx() : null;
      }
   }

   private Boolean isItemsColorVisible() {
      return (Boolean)this.items.atx();
   }

   private Boolean isVehiclesColorVisible() {
      return (Boolean)this.vehicles.atx();
   }

   private Boolean isAnimalsColorVisible() {
      return (Boolean)this.animals.atx();
   }

   private Boolean isMonstersColorVisible() {
      return (Boolean)this.monsters.atx();
   }

   private Boolean isInvisiblesColorVisible() {
      return (Boolean)this.invisibles.atx();
   }

   private Boolean isFriendsVisible() {
      return (Boolean)this.players.atx();
   }

   private Boolean isPlayersColorVisible() {
      return (Boolean)this.players.atx();
   }
}
