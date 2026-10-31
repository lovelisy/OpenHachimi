package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.util.math.Box;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.ObsidianObsidianBedrockMode;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.HoleESPHelper;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.BlockPosBoxHelper;
import pw.hachimi.eventbus.annotation.EventListener;

public class HoleESP extends ClientModule {
   private static HoleESP INSTANCE;
   private final Map map = new HashMap();
   Setting range = this.registerSetting(new NumberSetting("Range", "Range to display holes", 3.0F, 5.0F, 25.0F));
   Setting shape = this.registerSetting(new EnumSetting("Shape", "Box rendering mode", ShapeMode.Both, ShapeMode.values()));
   Setting cross = this.registerSetting(new BooleanSetting("Cross", "Renders a cross on the floor of the hole", false));
   Setting size = this.registerSetting(new NumberSetting("Size", "Render height of holes", -1.0F, 1.0F, 2.0F));
   Setting ignoreSelf = this.registerSetting(new BooleanSetting("IgnoreSelf", "Ignores the hole the player is standing in", false));
   Setting obsidian = this.registerSetting(new BooleanSetting("Obsidian", "Displays obsidian holes", true));
   Setting obsidianBedrock = this.registerSetting(new BooleanSetting("Obsidian-Bedrock", "Displays mixed obsidian and bedrock holes", true));
   Setting doubleSetting = this.registerSetting(new BooleanSetting("Double", "Displays double holes where the player can stand in the middle of two blocks to block explosion damage", false));
   Setting quad = this.registerSetting(new BooleanSetting("Quad", "Displays quad holes where the player can stand in the middle of four blocks to block explosion damage", false));
   Setting voidSetting = this.registerSetting(new BooleanSetting("Void", "Displays void holes in the world", false));
   Setting obsidianColor = this.registerSetting(new ColorSetting("ObsidianColor", "The color for rendering obsidian holes", new Color(255, 0, 0, 80), this::isObsidianColorVisible));
   Setting obsidianBedrockColor = this.registerSetting(new ColorSetting("Obsidian-BedrockColor", "The color for rendering mixed holes", new Color(255, 255, 0, 80), this::isObsidianBedrockColorVisible));
   Setting bedrockColor = this.registerSetting(new ColorSetting("BedrockColor", "The color for rendering bedrock holes", new Color(0, 255, 0, 80)));
   Setting voidColor = this.registerSetting(new ColorSetting("VoidColor", "The color for rendering bedrock holes", new Color(255, 0, 0, 140), this::isVoidColorVisible));
   Setting obsidianBottom = this.registerSetting(new ColorSetting("ObsidianBottom", "The color for rendering obsidian holes", new Color(255, 0, 0, 80), this::isObsidianBottomVisible));
   Setting obsidianBedrockBottom = this.registerSetting(new ColorSetting("Obsidian-BedrockBottom", "The color for rendering mixed holes", new Color(255, 255, 0, 80), this::isObsidianBedrockBottomVisible));
   Setting bedrockBottom = this.registerSetting(new ColorSetting("BedrockBottom", "The color for rendering bedrock holes", new Color(0, 255, 0, 80)));
   Setting voidBottom = this.registerSetting(new ColorSetting("VoidBottom", "The color for rendering bedrock holes", new Color(255, 0, 0, 140), this::isVoidBottomVisible));
   Setting fadeTime = this.registerSetting(new NumberSetting("Fade-Time", "Timer for the fade", 0, 300, 1000));

   public HoleESP() {
      super("HoleESP", "Displays nearby blast resistant holes", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static HoleESP ec() {
      return INSTANCE;
   }

   @Override
   public void awe() {
      this.map.clear();
   }

   @EventListener
   public void ed(Render3DContext render3DContext) {
      if (client.player != null) {
         Iterator iterator2 = Managers.blockPosWorldManager.Az().iterator();

         while (iterator2.hasNext()) {
            BlockPosBoxHelper blockPosBoxHelper = (BlockPosBoxHelper)iterator2.next();
            if (!this.ef(this.map.keySet(), blockPosBoxHelper)) {
               UnidentifiedHelper7 unidentifiedHelper7 = new UnidentifiedHelper7(false, ((Integer)this.fadeTime.atx()).intValue());
               this.map.put(blockPosBoxHelper, unidentifiedHelper7);
            }
         }

         iterator2 = this.map.entrySet().iterator();

         while (iterator2.hasNext()) {
            Entry entry = (Entry)iterator2.next();
            BlockPosBoxHelper blockPosBoxHelper2 = (BlockPosBoxHelper)entry.getKey();
            if (blockPosBoxHelper2.adZ(client.player) > ((NumberSetting)this.range).abq()) {
               ((UnidentifiedHelper7)entry.getValue()).qV(false);
            } else {
               label85: {
                  if ((Boolean)this.ignoreSelf.atx()) {
                     Box temp = client.player.getBoundingBox();
                     Box box = blockPosBoxHelper2.aek(0.5);
                     if (temp.intersects(box)) {
                        ((UnidentifiedHelper7)entry.getValue()).qV(false);
                        break label85;
                     }
                  }

                  if ((!blockPosBoxHelper2.aec() && !blockPosBoxHelper2.aed() || (Boolean)this.doubleSetting.atx())
                     && (!blockPosBoxHelper2.aee() || (Boolean)this.quad.atx())
                     && (blockPosBoxHelper2.getObsidianObsidianBedrockMode() != ObsidianObsidianBedrockMode.VOID || (Boolean)this.voidSetting.atx())
                     && (blockPosBoxHelper2.getObsidianObsidianBedrockMode() != ObsidianObsidianBedrockMode.OBSIDIAN || (Boolean)this.obsidian.atx())
                     && (blockPosBoxHelper2.getObsidianObsidianBedrockMode() != ObsidianObsidianBedrockMode.OBSIDIAN_BEDROCK || (Boolean)this.obsidianBedrock.atx())) {
                     ((UnidentifiedHelper7)entry.getValue()).qV(this.ef(Managers.blockPosWorldManager.Az(), blockPosBoxHelper2));
                  } else {
                     ((UnidentifiedHelper7)entry.getValue()).qV(false);
                  }
               }
            }

            if (!(((UnidentifiedHelper7)entry.getValue()).qW() < 0.01F)) {
               Color color = this.eg(blockPosBoxHelper2.getObsidianObsidianBedrockMode());
               Color color2 = this.eh(blockPosBoxHelper2.getObsidianObsidianBedrockMode());
               int n = (int)(color.getAlpha() * ((UnidentifiedHelper7)entry.getValue()).qW());
               int n2 = (int)(color2.getAlpha() * ((UnidentifiedHelper7)entry.getValue()).qW());
               this.ee(render3DContext, blockPosBoxHelper2, ColorUtils.iE(color, n), ColorUtils.iE(color2, n2));
            }
         }
      }
   }

   private void ee(Render3DContext render3DContext, BlockPosBoxHelper blockPosBoxHelper, Color color, Color color2) {
      Box box = blockPosBoxHelper.aek(((Float)this.size.atx()).floatValue());
      render3DContext.boxRenderer.Ze(box, (ShapeMode)this.shape.atx(), color, color2);
      if ((Boolean)this.cross.atx()) {
         render3DContext.boxRenderer.YJ(box.maxX, box.minY, box.minZ, box.minX, box.minY, box.maxZ, color2);
         render3DContext.boxRenderer.YJ(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, color2);
      }
   }

   private boolean ef(Set set, BlockPosBoxHelper blockPosBoxHelper) {
      Stream temp = set.stream();
      final BlockPosBoxHelper blockPosBoxHelper2 = blockPosBoxHelper;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object set) {
            return HoleESP.ej(blockPosBoxHelper2, (BlockPosBoxHelper)set);
         }
      };
      return temp.anyMatch(predicate);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private Color eg(ObsidianObsidianBedrockMode obsidianObsidianBedrockMode) {
      return switch (HoleESPHelper.a[obsidianObsidianBedrockMode.ordinal()]) {
         case 1 -> (Color)this.obsidianColor.atx();
         case 2 -> (Color)this.obsidianBedrockColor.atx();
         case 3 -> (Color)this.bedrockColor.atx();
         case 4 -> (Color)this.voidColor.atx();
         default -> throw new MatchException(null, null);
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private Color eh(ObsidianObsidianBedrockMode obsidianObsidianBedrockMode) {
      return switch (HoleESPHelper.a[obsidianObsidianBedrockMode.ordinal()]) {
         case 1 -> (Color)this.obsidianBottom.atx();
         case 2 -> (Color)this.obsidianBedrockBottom.atx();
         case 3 -> (Color)this.bedrockBottom.atx();
         case 4 -> (Color)this.voidBottom.atx();
         default -> throw new MatchException(null, null);
      };
   }

   public double ei() {
      return ((Float)this.range.atx()).floatValue();
   }

   static boolean ej(BlockPosBoxHelper blockPosBoxHelper, BlockPosBoxHelper blockPosBoxHelper2) {
      if (blockPosBoxHelper2.aec() != blockPosBoxHelper.aec() || blockPosBoxHelper2.aed() != blockPosBoxHelper.aed() || blockPosBoxHelper2.aee() != blockPosBoxHelper.aee() || blockPosBoxHelper2.aea() != blockPosBoxHelper.aea()) {
         return false;
      } else {
         return blockPosBoxHelper.getObsidianObsidianBedrockMode() != blockPosBoxHelper2.getObsidianObsidianBedrockMode() ? false : blockPosBoxHelper.equals(blockPosBoxHelper2);
      }
   }

   private Boolean isVoidBottomVisible() {
      return (Boolean)this.voidSetting.atx();
   }

   private Boolean isObsidianBedrockBottomVisible() {
      return (Boolean)this.obsidianBedrock.atx();
   }

   private Boolean isObsidianBottomVisible() {
      return (Boolean)this.obsidian.atx();
   }

   private Boolean isVoidColorVisible() {
      return (Boolean)this.voidSetting.atx();
   }

   private Boolean isObsidianBedrockColorVisible() {
      return (Boolean)this.obsidianBedrock.atx();
   }

   private Boolean isObsidianColorVisible() {
      return (Boolean)this.obsidian.atx();
   }
}
