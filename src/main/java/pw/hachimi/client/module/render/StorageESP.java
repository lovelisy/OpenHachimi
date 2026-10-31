package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class StorageESP extends ClientModule {
   private static StorageESP INSTANCE;
   Setting range = this.registerSetting(new NumberSetting("Range", "The ESP render range", 10.0F, 50.0F, 200.0F));
   Setting fill = this.registerSetting(new BooleanSetting("Fill", "Fills in the highlight", false));
   Setting chests = this.registerSetting(new BooleanSetting("Chests", "Render players through walls", true));
   Setting chestsColor = this.registerSetting(new ColorSetting("ChestsColor", "The render color for chests", new Color(200, 200, 101), false, false, this::isChestsColorVisible));
   Setting enderChests = this.registerSetting(new BooleanSetting("EnderChests", "Render players through walls", true));
   Setting enderChestsColor = this.registerSetting(new ColorSetting("EnderChestsColor", "The render color for ender chests", new Color(155, 0, 200), false, false, this::isEnderChestsColorVisible));
   Setting shulkers = this.registerSetting(new BooleanSetting("Shulkers", "Render players through walls", true));
   Setting shulkersColor = this.registerSetting(new ColorSetting("ShulkersColor", "The render color for shulkers", new Color(200, 0, 106), false, false, this::isShulkersColorVisible));
   Setting hoppers = this.registerSetting(new BooleanSetting("Hoppers", "Render players through walls", false));
   Setting hoppersColor = this.registerSetting(new ColorSetting("HoppersColor", "The render color for hoppers", new Color(100, 100, 100), false, false, this::isHoppersColorVisible));
   Setting furnaces = this.registerSetting(new BooleanSetting("Furnaces", "Render players through walls", false));
   Setting furnacesColor = this.registerSetting(new ColorSetting("FurnacesColor", "The render color for furnaces", new Color(100, 100, 100), false, false, this::isFurnacesColorVisible));

   public StorageESP() {
      super("StorageESP", "Highlights containers in the world", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static StorageESP Ce() {
      return INSTANCE;
   }

   @EventListener
   public void Cf(Render3DContext render3DContext) {
      Iterator iterator2 = CollisionUtils.aiv().iterator();

      while (iterator2.hasNext()) {
         BlockEntity blockEntity = (BlockEntity)iterator2.next();
         ClientPlayerEntity temp = client.player;
         Vec3d vec = blockEntity.getPos().toCenterPos();
         double d = temp.squaredDistanceTo(vec);
         if (!(d > ((NumberSetting)this.range).abq())) {
            Color color = this.Cg(blockEntity);
            if (color != null) {
               double d2 = MathHelper.clamp(1.0 - d / ((NumberSetting)this.range).abq(), 0.0, 1.0);
               BlockPos pos = blockEntity.getPos();
               if (blockEntity instanceof ChestBlockEntity) {
                  double d3 = pos.getX() + 0.06;
                  double d4 = pos.getY();
                  double d5 = pos.getZ() + 0.06;
                  double d6 = pos.getX() + 0.94;
                  double d7 = pos.getY() + 0.875;
                  double d8 = pos.getZ() + 0.94;
                  BlockState state2 = blockEntity.getCachedState();
                  EnumProperty enumProperty = ChestBlock.CHEST_TYPE;
                  if (state2.contains((Property)enumProperty)) {
                     DirectionProperty directionProperty = ChestBlock.FACING;
                     Direction direction = (Direction)state2.get((Property)directionProperty);
                     EnumProperty enumProperty2 = ChestBlock.CHEST_TYPE;
                     ChestType chestType = (ChestType)state2.get((Property)enumProperty2);
                     if (chestType == ChestType.RIGHT) {
                        direction = direction.rotateYCounterclockwise();
                        if (direction.getOffsetX() < 0) {
                           d3 += direction.getOffsetX();
                        } else {
                           d6 += direction.getOffsetX();
                        }

                        if (direction.getOffsetZ() < 0) {
                           d5 += direction.getOffsetZ();
                        } else {
                           d8 += direction.getOffsetZ();
                        }

                        Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(30.0 * d2));
                        Color color3 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(72.0 * d2));
                        Box box = new Box(d3, d4, d5, d6, d7, d8);
                        BoxRenderer boxRenderer2 = render3DContext.boxRenderer;
                        ShapeMode shapeMode = (Boolean)this.fill.atx() ? ShapeMode.Both : ShapeMode.Lines;
                        boxRenderer2.Za(box, color2, color3, shapeMode, 0);
                     } else if (chestType == ChestType.LEFT) {
                        direction = direction.rotateYClockwise();
                        if (direction.getOffsetX() < 0) {
                           d3 += direction.getOffsetX();
                        } else {
                           d6 += direction.getOffsetX();
                        }

                        if (direction.getOffsetZ() < 0) {
                           d5 += direction.getOffsetZ();
                        } else {
                           d8 += direction.getOffsetZ();
                        }

                        Color color4 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(30.0 * d2));
                        Color color5 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(72.0 * d2));
                        Box box2 = new Box(d3, d4, d5, d6, d7, d8);
                        BoxRenderer boxRenderer3 = render3DContext.boxRenderer;
                        ShapeMode shapeMode2 = (Boolean)this.fill.atx() ? ShapeMode.Both : ShapeMode.Lines;
                        boxRenderer3.Za(box2, color4, color5, shapeMode2, 0);
                     } else if (chestType == ChestType.SINGLE) {
                        Color color6 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(30.0 * d2));
                        Color color7 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(72.0 * d2));
                        Box box3 = new Box(d3, d4, d5, d6, d7, d8);
                        BoxRenderer boxRenderer4 = render3DContext.boxRenderer;
                        ShapeMode shapeMode3 = (Boolean)this.fill.atx() ? ShapeMode.Both : ShapeMode.Lines;
                        boxRenderer4.Za(box3, color6, color7, shapeMode3, 0);
                     }
                  }
               } else if (blockEntity instanceof EnderChestBlockEntity) {
                  double d9 = pos.getX() + 0.06;
                  double d10 = pos.getY();
                  double d11 = pos.getZ() + 0.06;
                  double d12 = pos.getX() + 0.94;
                  double d13 = pos.getY() + 0.875;
                  double d14 = pos.getZ() + 0.94;
                  Color color8 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(30.0 * d2));
                  Color color9 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(72.0 * d2));
                  Box box4 = new Box(d9, d10, d11, d12, d13, d14);
                  BoxRenderer boxRenderer5 = render3DContext.boxRenderer;
                  ShapeMode shapeMode4 = (Boolean)this.fill.atx() ? ShapeMode.Both : ShapeMode.Lines;
                  boxRenderer5.Za(box4, color8, color9, shapeMode4, 0);
               } else {
                  Color color10 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(30.0 * d2));
                  Color color11 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(72.0 * d2));
                  render3DContext.boxRenderer.YZ(pos, color10, color11, (Boolean)this.fill.atx() ? ShapeMode.Both : ShapeMode.Lines);
               }
            }
         }
      }
   }

   public Color Cg(BlockEntity blockEntity) {
      if (blockEntity instanceof ChestBlockEntity) {
         return (Boolean)this.chests.atx() ? (Color)this.chestsColor.atx() : null;
      } else if (blockEntity instanceof EnderChestBlockEntity) {
         return (Boolean)this.enderChests.atx() ? (Color)this.enderChestsColor.atx() : null;
      } else if (blockEntity instanceof ShulkerBoxBlockEntity) {
         return (Boolean)this.shulkers.atx() ? (Color)this.shulkersColor.atx() : null;
      } else if (blockEntity instanceof HopperBlockEntity) {
         return (Boolean)this.hoppers.atx() ? (Color)this.hoppersColor.atx() : null;
      } else if (blockEntity instanceof FurnaceBlockEntity) {
         return (Boolean)this.furnaces.atx() ? (Color)this.furnacesColor.atx() : null;
      } else {
         return null;
      }
   }

   private Boolean isFurnacesColorVisible() {
      return (Boolean)this.furnaces.atx();
   }

   private Boolean isHoppersColorVisible() {
      return (Boolean)this.hoppers.atx();
   }

   private Boolean isShulkersColorVisible() {
      return (Boolean)this.shulkers.atx();
   }

   private Boolean isEnderChestsColorVisible() {
      return (Boolean)this.enderChests.atx();
   }

   private Boolean isChestsColorVisible() {
      return (Boolean)this.chests.atx();
   }
}
