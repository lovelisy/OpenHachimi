package pw.hachimi.client.module.render;

import java.awt.Color;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class PhaseESP extends ClientModule {
   Setting line = this.registerSetting(new BooleanSetting("Line", "Renders line phase blocks", true));
   Setting width = this.registerSetting(new NumberSetting("Width", "The line width of the render", 1.0F, 1.0F, 5.0F));
   Setting diagonal = this.registerSetting(new BooleanSetting("Diagonal", "Renders safe diagonal phase blocks", true));
   Setting safe = this.registerSetting(new BooleanSetting("Safe", "Highlights safe phase blocks", false));
   Setting unsafeColor = this.registerSetting(new ColorSetting("UnsafeColor", "The color for rendering unsafe phase blocks", new Color(255, 0, 0, 50), true, false));
   Setting obsidianColor = this.registerSetting(new ColorSetting("ObsidianColor", "The color for rendering obsidian phase blocks", new Color(255, 255, 0, 50), true, false, this::isObsidianColorVisible));
   Setting bedrockColor = this.registerSetting(new ColorSetting("BedrockColor", "The color for rendering bedrock phase blocks", new Color(0, 255, 0, 50), true, false, this::isBedrockColorVisible));

   public PhaseESP() {
      super("PhaseESP", "Displays safe phase blocks", ModuleCategory.RENDER);
   }

   @EventListener
   public void rq(Render3DContext render3DContext) {
      if (client.player != null && client.world != null && client.player.isOnGround()) {
         BlockPos pos = client.player.getBlockPos();
         Vec3d vec = client.player.getPos();
         double d = vec.getX() - pos.getX();
         double d2 = vec.getZ() - pos.getZ();

         for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
               BlockPos pos2 = pos.offset(direction);
               if (!client.world.getBlockState(pos2).isReplaceable()) {
                  Color color = this.rr(pos2);
                  if (color != null) {
                     double d3 = pos2.getX();
                     double d4 = pos2.getY();
                     double d5 = pos2.getZ();
                     Box box = new Box(d3, d4, d5, d3 + 1.0, d4 + 0.01, d5 + 1.0);
                     if (direction == Direction.EAST && d >= 0.65) {
                        if (!(Boolean)this.line.atx()) {
                           render3DContext.boxRenderer.Za(box, color, color, ShapeMode.Both, 0);
                        } else {
                           render3DContext.boxRenderer.YL(d3, d4, d5, d3, d4, d5 + 1.0, color, ((Float)this.width.atx()).floatValue());
                        }
                     } else if (direction == Direction.WEST && d <= 0.35) {
                        if (!(Boolean)this.line.atx()) {
                           render3DContext.boxRenderer.Za(box, color, color, ShapeMode.Both, 0);
                        } else {
                           render3DContext.boxRenderer.YL(d3 + 1.0, d4, d5, d3 + 1.0, d4, d5 + 1.0, color, ((Float)this.width.atx()).floatValue());
                        }
                     } else if (direction == Direction.SOUTH && d2 >= 0.65) {
                        if (!(Boolean)this.line.atx()) {
                           render3DContext.boxRenderer.Za(box, color, color, ShapeMode.Both, 0);
                        } else {
                           render3DContext.boxRenderer.YL(d3, d4, d5, d3 + 1.0, d4, d5, color, ((Float)this.width.atx()).floatValue());
                        }
                     } else if (direction == Direction.NORTH && d2 <= 0.35) {
                        if (!(Boolean)this.line.atx()) {
                           render3DContext.boxRenderer.Za(box, color, color, ShapeMode.Both, 0);
                        } else {
                           render3DContext.boxRenderer.YL(d3, d4, d5 + 1.0, d3 + 1.0, d4, d5 + 1.0, color, ((Float)this.width.atx()).floatValue());
                        }
                     }
                  }
               }
            }
         }

         if ((Boolean)this.diagonal.atx()) {
            double d6 = pos.getX();
            double d7 = pos.getY();
            double d8 = pos.getZ();
            Direction direction2 = Direction.WEST;
            BlockPos temp = pos.offset(direction2);
            Direction direction3 = Direction.NORTH;
            BlockPos pos3 = temp.offset(direction3);
            Color color2 = this.rr(pos3);
            if (color2 != null && d <= 0.35 && d2 <= 0.35) {
               if (!(Boolean)this.line.atx()) {
                  Box box2 = new Box(pos3.getX(), pos3.getY(), pos3.getZ(), pos3.getX() + 1, pos3.getY() + 0.01, pos3.getZ() + 1);
                  render3DContext.boxRenderer.Za(box2, color2, color2, ShapeMode.Both, 0);
               } else {
                  render3DContext.boxRenderer.YL(d6, d7, d8, d6, d7 + 1.0, d8, color2, ((Float)this.width.atx()).floatValue());
               }
            }

            Direction direction4 = Direction.WEST;
            temp = pos.offset(direction4);
            Direction direction5 = Direction.SOUTH;
            pos3 = temp.offset(direction5);
            Color color3 = this.rr(pos3);
            if (color3 != null && d <= 0.35 && d2 >= 0.65) {
               if (!(Boolean)this.line.atx()) {
                  Box box3 = new Box(pos3.getX(), pos3.getY(), pos3.getZ(), pos3.getX() + 1, pos3.getY() + 0.01, pos3.getZ() + 1);
                  render3DContext.boxRenderer.Za(box3, color3, color3, ShapeMode.Both, 0);
               } else {
                  render3DContext.boxRenderer.YL(d6, d7, d8 + 1.0, d6, d7 + 1.0, d8 + 1.0, color3, ((Float)this.width.atx()).floatValue());
               }
            }

            Direction direction6 = Direction.EAST;
            temp = pos.offset(direction6);
            Direction direction7 = Direction.NORTH;
            pos3 = temp.offset(direction7);
            Color color4 = this.rr(pos3);
            if (color4 != null && d >= 0.65 && d2 <= 0.35) {
               if (!(Boolean)this.line.atx()) {
                  Box box4 = new Box(pos3.getX(), pos3.getY(), pos3.getZ(), pos3.getX() + 1, pos3.getY() + 0.01, pos3.getZ() + 1);
                  render3DContext.boxRenderer.Za(box4, color4, color4, ShapeMode.Both, 0);
               } else {
                  render3DContext.boxRenderer.YL(d6 + 1.0, d7, d8, d6 + 1.0, d7 + 1.0, d8, color4, ((Float)this.width.atx()).floatValue());
               }
            }

            Direction direction8 = Direction.EAST;
            temp = pos.offset(direction8);
            Direction direction9 = Direction.SOUTH;
            pos3 = temp.offset(direction9);
            Color color5 = this.rr(pos3);
            if (color5 != null && d >= 0.65 && d2 >= 0.65) {
               if (!(Boolean)this.line.atx()) {
                  Box box5 = new Box(pos3.getX(), pos3.getY(), pos3.getZ(), pos3.getX() + 1, pos3.getY() + 0.01, pos3.getZ() + 1);
                  render3DContext.boxRenderer.Za(box5, color5, color5, ShapeMode.Both, 0);
               } else {
                  render3DContext.boxRenderer.YL(d6 + 1.0, d7, d8 + 1.0, d6 + 1.0, d7 + 1.0, d8 + 1.0, color5, ((Float)this.width.atx()).floatValue());
               }
            }
         }
      }
   }

   private Color rr(BlockPos pos) {
      if (client.world.getBlockState(pos).isAir()) {
         return null;
      }

      ClientWorld temp = client.world;
      BlockPos pos2 = pos.down();
      BlockState state = temp.getBlockState(pos2);
      Color color = null;
      if (state.isReplaceable()) {
         color = (Color)this.unsafeColor.atx();
      } else if ((Boolean)this.safe.atx()) {
         if (BlockUtils.TO(state.getBlock())) {
            color = (Color)this.bedrockColor.atx();
         } else {
            color = (Color)this.obsidianColor.atx();
         }
      }

      return color;
   }

   private Boolean isBedrockColorVisible() {
      return (Boolean)this.safe.atx();
   }

   private Boolean isObsidianColorVisible() {
      return (Boolean)this.safe.atx();
   }
}
