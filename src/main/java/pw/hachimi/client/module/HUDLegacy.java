package pw.hachimi.client.module;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import pw.hachimi.client.event.AddStatusEffectEvent;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.FillEvent;
import pw.hachimi.client.event.RemoveStatusEffectEvent;
import pw.hachimi.client.event.SetScreenEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.event.render.RenderEvent;
import pw.hachimi.client.event.render.RenderHeldItemTooltipEvent;
import pw.hachimi.client.event.render.RenderStatusEffectOverlayEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.HUDLegacyKMHBPSMode;
import pw.hachimi.client.model.render.HUDLegacyMoveHideMode;
import pw.hachimi.client.model.render.HUDLegacyNormalOldMode;
import pw.hachimi.client.model.render.HUDLegacyOption;
import pw.hachimi.client.model.render.HUDLegacyOption2;
import pw.hachimi.client.module.exploit.FastLatency;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.module.misc.Timer;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.combat.UnidentifiedHelper11;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.player.StackPlayerUtils2;
import pw.hachimi.client.utils.render.HUDLegacyRegistryEntryPlayerHelper;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class HUDLegacy extends RenderModule {
   private static final Identifier a = Identifier.of("hachimiclient", "textures/logo.png");
   private static HUDLegacy INSTANCE;
   private final DecimalFormat decimalFormat = new DecimalFormat("0.0#");
   private final UnidentifiedHelper7 unidentifiedHelper7 = new UnidentifiedHelper7(false, '\u0000', 200.0F, 496665058, '楚', Easing.CUBIC_OUT);
   private final UnidentifiedHelper11 unidentifiedHelper11 = new UnidentifiedHelper11();
   private final Map map = new ConcurrentSkipListMap();
   private final Stopwatch stopwatch = new StopwatchImpl();
   private final UnidentifiedHelper7 unidentifiedHelper72 = new UnidentifiedHelper7(false, '\u0000', 300.0F, 496665058, '楚', Easing.LINEAR);
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Watermark", "Displays client name and version watermark", true));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("Logo", "Replace watermark with logo", true, this.booleanSetting1_1::atx));
   Setting numberSetting1_1 = this.registerSetting(new NumberSetting("Scale", "", 0.1F, 1.0F, 3.0F, this::isNumberSetting1Visible));
   Setting serverStatus = this.registerSetting(new BooleanSetting("ServerStatus", "Displays warning for server connection status", true));
   Setting direction = this.registerSetting(new BooleanSetting("Direction", "Displays facing direction", true));
   Setting rotation = this.registerSetting(new BooleanSetting("Rotation", "Displays player yaw and pitch", false, this::isRotationVisible));
   Setting armor = this.registerSetting(new BooleanSetting("Armor", "Displays player equipped armor and durability", true));
   Setting armorDurability = this.registerSetting(new BooleanSetting("ArmorDurability", "Displays player equipped armor durability", false, this::isArmorDurabilityVisible));
   Setting totems = this.registerSetting(new BooleanSetting("Totems", "Displays player totems", false));
   Setting potionHud = this.registerSetting(new EnumSetting("PotionHud", "Renders the Minecraft potion Hud", HUDLegacyMoveHideMode.HIDE, HUDLegacyMoveHideMode.values()));
   Setting itemName = this.registerSetting(new EnumSetting("ItemName", "Renders the Minecraft item name display", HUDLegacyMoveHideMode.HIDE, HUDLegacyMoveHideMode.values()));
   Setting dynamicY = this.registerSetting(new BooleanSetting("DynamicY", "", false, this::isDynamicYVisible));
   Setting yOffset = this.registerSetting(new NumberSetting("YOffset", "", -200, 0, 200, this::isYOffsetVisible));
   Setting xOffset = this.registerSetting(new NumberSetting("XOffset", "", -200, 0, 200, this::isXOffsetVisible));
   Setting potionEffects = this.registerSetting(new BooleanSetting("PotionEffects", "Displays active potion effects", true));
   Setting potionColors = this.registerSetting(new EnumSetting("PotionColors", "Displays active potion colors", HUDLegacyNormalOldMode.NORMAL, HUDLegacyNormalOldMode.values(), this::isPotionColorsVisible));
   Setting durability = this.registerSetting(new BooleanSetting("Durability", "Displays the current held items durability", false));
   Setting coords = this.registerSetting(new BooleanSetting("Coords", "Displays world coordinates", true));
   Setting netherCoords = this.registerSetting(new BooleanSetting("NetherCoords", "Displays nether coordinates", true, this::isNetherCoordsVisible));
   Setting serverBrand = this.registerSetting(new BooleanSetting("ServerBrand", "Displays the current server brand", false));
   Setting booleanSetting3 = this.registerSetting(new BooleanSetting("Chests", "Displays the amount of chests in your render distance", false));
   Setting kMHBPSMode = this.registerSetting(new EnumSetting("Speed", "Displays the current movement speed of the player", HUDLegacyKMHBPSMode.K_M_H, HUDLegacyKMHBPSMode.values()));
   Setting booleanSetting4 = this.registerSetting(new BooleanSetting("Ping", "Display server response time in ms", true));
   Setting booleanSetting5 = this.registerSetting(new BooleanSetting("TPS", "Displays server ticks per second", true));
   Setting booleanSetting6 = this.registerSetting(new BooleanSetting("Packets", "Displays outbound packets per second", false));
   Setting booleanSetting7 = this.registerSetting(new BooleanSetting("FPS", "Displays game FPS", true));
   Setting optionMode = this.registerSetting(new EnumSetting("Rendering", "The rendering mode of the HUD", HUDLegacyOption.a_, HUDLegacyOption.values()));
   Setting rainbow = this.registerSetting(new EnumSetting("Rainbow", "The rendering mode for rainbow", HUDLegacyOption2.a_, HUDLegacyOption2.values()));
   Setting gradientColor = this.registerSetting(new ColorSetting("GradientColor", "The color of the rainbow gradient", Color.WHITE, false, false, this::isGradientColorVisible));
   Setting rainbowSpeed = this.registerSetting(new NumberSetting("Rainbow-Speed", "The speed for the rainbow color cycling", 0.1F, 50.0F, 100.0F, this::isRainbowSpeedVisible));
   Setting rainbowSaturation = this.registerSetting(new NumberSetting("Rainbow-Saturation", "The saturation of rainbow colors", 0, 35, 100, this::isRainbowSaturationVisible));
   Setting rainbowBrightness = this.registerSetting(new NumberSetting("Rainbow-Brightness", "The brightness of rainbow colors", 0, 100, 100, this::isRainbowBrightnessVisible));
   Setting rainbowDifference = this.registerSetting(new NumberSetting("Rainbow-Difference", "The difference offset for rainbow colors", 0.1F, 40.0F, 100.0F, this::isRainbowDifferenceVisible));
   private long S;
   private float T;
   private float U;
   private float V;
   private float W;
   private boolean X;

   public HUDLegacy() {
      super("HUDLegacy", "Displays the HUD (heads up display) screen.");
      INSTANCE = this;
   }

   public static HUDLegacy jI() {
      return INSTANCE;
   }

   @EventListener
   public void jJ(UpdateDeathEvent updateDeathEvent) {
      if (updateDeathEvent.getLiving() == client.player) {
         this.map.clear();
      }
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.map.clear();
   }

   @EventListener
   public void onFill(FillEvent fillEvent) {
      fillEvent.aim();
      fillEvent.y((float)this.unidentifiedHelper7.qW());
   }

   @EventListener
   public void onRender(RenderEvent renderEvent) {
      this.unidentifiedHelper11.Ln();
      if (client.player == null || client.world == null) {
         return;
      }

      Iterator iterator2 = this.map.entrySet().iterator();
      while (iterator2.hasNext()) {
         Map.Entry entry = (Map.Entry)iterator2.next();
         HUDLegacyRegistryEntryPlayerHelper hudLegacyRegistryEntryPlayerHelper = (HUDLegacyRegistryEntryPlayerHelper)entry.getKey();
         UnidentifiedHelper7 unidentifiedHelper73 = (UnidentifiedHelper7)entry.getValue();
         hudLegacyRegistryEntryPlayerHelper.gj();
         RegistryEntry registryEntry = hudLegacyRegistryEntryPlayerHelper.getRegistryEntry();
         if (client.player.getStatusEffect(registryEntry) != null) {
            continue;
         }

         if (unidentifiedHelper73.qW() >= 0.01F) {
            continue;
         }

         this.map.remove(hudLegacyRegistryEntryPlayerHelper);
      }

      if (client.options.hudHidden) {
         return;
      }

      DrawContext context = renderEvent.getContext();
      Window window = client.getWindow();
      if ((Boolean)this.serverStatus.atx()) {
         this.unidentifiedHelper72.qV(this.stopwatch.afq(1000));
         Object[] objs = new Object[]{bo.format((double)this.stopwatch.afs() / 1000.0)};
         String str = String.format("§fServer not responding §7(§r%s.s§7)", objs);
         int n = RenderUtils.acF(str);
         float f2 = MathHelper.clamp(((float)this.stopwatch.afs() - 1000.0F) / 5000.0F, 0.0F, 1.0F);
         Color color = ColorUtils.iA(f2, Color.RED, Color.GREEN);
         int n2 = (int)(255.0 * this.unidentifiedHelper72.qW());
         this.adr(context, str, (float)window.getScaledWidth() / 2.0F - (float)n / 2.0F, 4.0F, ColorUtils.iH(color.getRGB(), (float)n2));
      }

      this.S = 0L;
      this.T = 2.0F;
      this.U = this.T;
      this.V = (float)(window.getScaledHeight() - RenderUtils.acG());
      this.W = this.V;
      this.X = this.optionMode.atx() == HUDLegacyOption.a_;
      this.V = this.V - (float)(14.0 * this.unidentifiedHelper7.qW());
      this.W = this.W - (float)(14.0 * this.unidentifiedHelper7.qW());
      if (this.potionHud.atx() == HUDLegacyMoveHideMode.MOVE && !client.player.getStatusEffects().isEmpty()) {
         this.U += 27.0F;
      }

      long l2 = ((List)Managers.moduleManager.XM()).stream().filter(ClientModule.class::isInstance).map(ClientModule.class::cast).filter(obj -> kb((ClientModule)obj)).count();
      if ((Boolean)this.booleanSetting1_1.atx()) {
         if ((Boolean)this.booleanSetting2_1.atx()) {
            int n3 = this.jU(l2 - this.S);
            RenderSystem.setShaderColor((float)(n3 >> 16 & 0xFF) / 255.0F, (float)(n3 >> 8 & 0xFF) / 255.0F, (float)(n3 & 0xFF) / 255.0F, 1.0F);
            RenderSystem.enableBlend();
            float f3 = (Float)this.numberSetting1_1.atx();
            context.drawTexture(a, 2, (int)this.T, 0.0F, 0.0F, (int)(75.0F * f3), (int)(27.0F * f3), (int)(75.0F * f3), (int)(27.0F * f3));
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         } else {
            Object[] objs2 = new Object[]{"Hachimi", "2.0"};
            this.adr(context, String.format("%s §f%s", objs2), 2.0F, this.T, this.jU(l2 - this.S));
         }

         this.T = this.T + (float)RenderUtils.acG();
      }

      if ((Boolean)this.coords.atx()) {
         double d = client.player.getX();
         double d2 = client.player.getY();
         double d3 = client.player.getZ();
         String str2 = "XYZ §f%s, %s, %s " + ((Boolean)this.netherCoords.atx() ? "§7[§f%s, %s§7]" : "");
         Object[] objs3 = new Object[5];
         objs3[0] = bo.format(d);
         objs3[1] = bo.format(d2);
         objs3[2] = bo.format(d3);
         if (client.world.getRegistryKey() == World.NETHER) {
            objs3[3] = bo.format(d * 8.0);
            objs3[4] = bo.format(d3 * 8.0);
         } else {
            objs3[3] = bo.format(d / 8.0);
            objs3[4] = bo.format(d3 / 8.0);
         }

         this.adr(context, String.format(str2, objs3), 2.0F, this.V, this.jU(this.S));
         this.V = this.V - (float)RenderUtils.acG();
      }

      if ((Boolean)this.direction.atx()) {
         net.minecraft.util.math.Direction direction = client.player.getHorizontalFacing();
         String str3 = DirectionUtils.apY(direction);
         String str4 = DirectionUtils.apZ(direction.getAxis());
         Object[] objs4 = new Object[]{Integer.valueOf((int)MathHelper.wrapDegrees(Managers.movementTracker.Mm()))};
         String str5 = String.format(", %s", objs4);
         Object[] objs5 = new Object[]{str3, str4, direction.getDirection() == net.minecraft.util.math.Direction.AxisDirection.POSITIVE ? "+" : "-", (Boolean)this.rotation.atx() ? str5 : ""};
         this.adr(context, String.format("%s §7[§f%s%s%s§7]", objs5), 2.0F, this.V, this.jU(this.S));
      }

      if ((Boolean)this.potionEffects.atx()) {
         Iterator iterator3 = this.map.entrySet().iterator();
         while (iterator3.hasNext()) {
            Map.Entry entry2 = (Map.Entry)iterator3.next();
            HUDLegacyRegistryEntryPlayerHelper hudLegacyRegistryEntryPlayerHelper2 = (HUDLegacyRegistryEntryPlayerHelper)entry2.getKey();
            UnidentifiedHelper7 unidentifiedHelper74 = (UnidentifiedHelper7)entry2.getValue();
            StatusEffect statusEffect = (StatusEffect)hudLegacyRegistryEntryPlayerHelper2.getRegistryEntry().value();
            if (statusEffect == StatusEffects.NIGHT_VISION.value()) {
               continue;
            }

            String str6;
            String str7;
            if (hudLegacyRegistryEntryPlayerHelper2.gn() == -1) {
               str6 = "Inf";
               str7 = "";
            } else {
               int n4 = MathHelper.floor((float)hudLegacyRegistryEntryPlayerHelper2.gn());
               str6 = StringHelper.formatTicks(n4, client.world.getTickManager().getTickRate());
               str7 = hudLegacyRegistryEntryPlayerHelper2.gm() + 1 > 1 ? hudLegacyRegistryEntryPlayerHelper2.gm() + 1 + " " : "";
            }

            Object[] objs6 = new Object[]{statusEffect.getName().getString(), str7, str6};
            String str8 = String.format("%s %s§f%s", objs6);
            int n5 = RenderUtils.acF(str8);
            float f4 = ((float)n5 + 1.0F) * (float)unidentifiedHelper74.qW();
            int n6;
            switch (((HUDLegacyNormalOldMode)this.potionColors.atx()).ordinal()) {
               case 0:
                  n6 = ColorUtils.iG(statusEffect.getColor(), (int)(255.0 * unidentifiedHelper74.qW()));
                  break;
               case 1:
                  n6 = ColorUtils.iG(this.jX(hudLegacyRegistryEntryPlayerHelper2.getRegistryEntry().getIdAsString(), statusEffect), (int)(255.0 * unidentifiedHelper74.qW()));
                  break;
               case 2:
                  n6 = ColorUtils.iG(this.jU(this.S), (int)(255.0 * unidentifiedHelper74.qW()));
                  break;
               default:
                  throw new MatchException(null, null);
            }

            this.adr(context, str8, (float)window.getScaledWidth() - f4, this.X ? this.W : this.U, ColorUtils.iH(n6, (float)unidentifiedHelper74.qW()));
            if (this.X) {
               this.W = this.W - (float)RenderUtils.acG();
            } else {
               this.U = this.U + (float)RenderUtils.acG();
            }

            this.S++;
         }
      }

      if ((Boolean)this.serverBrand.atx()) {
         String str9 = client.player.networkHandler.getBrand();
         int n7 = RenderUtils.acF(str9);
         this.adr(context, str9, (float)(window.getScaledWidth() - n7) - 1.0F, this.X ? this.W : this.U, this.jU(this.S));
         if (this.X) {
            this.W = this.W - (float)RenderUtils.acG();
         } else {
            this.U = this.U + (float)RenderUtils.acG();
         }

         this.S++;
      }

      if (this.kMHBPSMode.atx() != HUDLegacyKMHBPSMode.OFF) {
         double d4 = client.player.getX() - client.player.prevX;
         double d5 = client.player.getZ() - client.player.prevZ;
         float f5 = Timer.fP().awg() ? Timer.fP().fS() : 1.0F;
         double d6;
         if (this.kMHBPSMode.atx() == HUDLegacyKMHBPSMode.K_M_H) {
            double d7 = Math.sqrt(d4 * d4 + d5 * d5) / 1000.0;
            d6 = d7 / 1.388888888888889E-5 * (double)f5;
         } else {
            d4 *= 20.0;
            d5 *= 20.0;
            double d8 = Math.sqrt(d4 * d4 + d5 * d5);
            d6 = Math.abs(d8) * (double)f5;
         }

         Object[] objs7 = new Object[]{this.decimalFormat.format(d6), this.kMHBPSMode.atx() == HUDLegacyKMHBPSMode.K_M_H ? "km/h" : "b/s"};
         String str10 = String.format("Speed §f%s%s", objs7);
         int n8 = RenderUtils.acF(str10);
         this.adr(context, str10, (float)(window.getScaledWidth() - n8) - 1.0F, this.X ? this.W : this.U, this.jU(this.S));
         if (this.X) {
            this.W = this.W - (float)RenderUtils.acG();
         } else {
            this.U = this.U + (float)RenderUtils.acG();
         }

         this.S++;
      }

      if ((Boolean)this.durability.atx() && client.player.getMainHandStack().isDamageable()) {
         int n9 = client.player.getMainHandStack().getMaxDamage();
         int n10 = client.player.getMainHandStack().getDamage();
         String str11 = String.valueOf(n9 - n10);
         int n11 = RenderUtils.acF("Durability ");
         int n12 = RenderUtils.acF(str11);
         Color color2 = ColorUtils.iz((float)(n9 - n10) / (float)n9 * 120.0F, 100.0F, 50.0F, 1.0F);
         this.adr(context, "Durability ", (float)(window.getScaledWidth() - n11 - n12) - 1.0F, this.X ? this.W : this.U, this.jU(this.S));
         this.adr(context, str11, (float)(window.getScaledWidth() - n12) - 1.0F, this.X ? this.W : this.U, color2.getRGB());
         if (this.X) {
            this.W = this.W - (float)RenderUtils.acG();
         } else {
            this.U = this.U + (float)RenderUtils.acG();
         }

         this.S++;
      }

      if ((Boolean)this.booleanSetting4.atx()) {
         int n13 = FastLatency.Ov().awg() ? (int)FastLatency.Ov().Oz() : Managers.serverManager.iS();
         Object[] objs8 = new Object[]{Integer.valueOf(n13)};
         String str12 = String.format("Ping §f%dms", objs8);
         int n14 = RenderUtils.acF(str12);
         this.adr(context, str12, (float)(window.getScaledWidth() - n14) - 1.0F, this.X ? this.W : this.U, this.jU(this.S));
         if (this.X) {
            this.W = this.W - (float)RenderUtils.acG();
         } else {
            this.U = this.U + (float)RenderUtils.acG();
         }

         this.S++;
      }

      if ((Boolean)this.booleanSetting6.atx()) {
         Object[] objs9 = new Object[]{Integer.valueOf(Managers.serverManager.iZ()), Integer.valueOf(Managers.serverManager.ja())};
         String str13 = String.format("Packets §f%s->%s", objs9);
         int n15 = RenderUtils.acF(str13);
         this.adr(context, str13, (float)(window.getScaledWidth() - n15) - 1.0F, this.X ? this.W : this.U, this.jU(this.S));
         if (this.X) {
            this.W = this.W - (float)RenderUtils.acG();
         } else {
            this.U = this.U + (float)RenderUtils.acG();
         }

         this.S++;
      }

      if ((Boolean)this.booleanSetting5.atx()) {
         float f6 = Managers.entityManager.rX();
         float f7 = Managers.entityManager.rW();
         Object[] objs10 = new Object[]{this.decimalFormat.format((double)f6) + (Managers.entityManager.rZ() ? "" : "*"), this.decimalFormat.format((double)f7)};
         String str14 = String.format("TPS §f%s §7[§f%s§7]", objs10);
         int n16 = RenderUtils.acF(str14);
         this.adr(context, str14, (float)(window.getScaledWidth() - n16) - 1.0F, this.X ? this.W : this.U, this.jU(this.S));
         if (this.X) {
            this.W = this.W - (float)RenderUtils.acG();
         } else {
            this.U = this.U + (float)RenderUtils.acG();
         }

         this.S++;
      }

      if ((Boolean)this.booleanSetting3.atx()) {
         int n17 = 0;
         int n18 = 0;
         Iterator iterator4 = CollisionUtils.aiv().iterator();
         while (iterator4.hasNext()) {
            BlockEntity blockEntity = (BlockEntity)iterator4.next();
            if (!(blockEntity instanceof ChestBlockEntity)) {
               continue;
            }

            BlockState blockState = blockEntity.getCachedState();
            if (!blockState.contains(ChestBlock.CHEST_TYPE)) {
               continue;
            }

            ChestType chestType = (ChestType)blockState.get(ChestBlock.CHEST_TYPE);
            if (chestType == ChestType.SINGLE) {
               n17++;
            } else {
               n18++;
            }
         }

         Object[] objs11 = new Object[]{Integer.valueOf(n17 + n18 / 2)};
         String str15 = String.format("Chests §f%d", objs11);
         int n19 = RenderUtils.acF(str15);
         this.adr(context, str15, (float)(window.getScaledWidth() - n19) - 1.0F, this.X ? this.W : this.U, this.jU(this.S));
         if (this.X) {
            this.W = this.W - (float)RenderUtils.acG();
         } else {
            this.U = this.U + (float)RenderUtils.acG();
         }

         this.S++;
      }

      if ((Boolean)this.booleanSetting7.atx()) {
         Object[] objs12 = new Object[]{Integer.valueOf(this.unidentifiedHelper11.Lo())};
         String str16 = String.format("FPS §f%d", objs12);
         int n20 = RenderUtils.acF(str16);
         this.adr(context, str16, (float)(window.getScaledWidth() - n20) - 1.0F, this.X ? this.W : this.U, this.jU(this.S));
         this.S++;
      }

      if ((Boolean)this.armor.atx()) {
         Entity entity2 = client.player.getVehicle();
         int n21 = window.getScaledWidth() / 2 + 11;
         int n22 = window.getScaledHeight();
         int n23 = client.player.getMaxAir();
         int n24 = Math.min(client.player.getAir(), n23);
         if (!client.player.isSubmergedIn(FluidTags.WATER) && n24 >= n23) {
            if (entity2 instanceof LivingEntity) {
               n22 -= 45 + (int)Math.ceil((((LivingEntity)entity2).getMaxHealth() - 1.0F) / 20.0F) * 10;
            } else if (client.player.isCreative()) {
               n22 += -45;
            } else {
               n22 += -55;
            }
         } else {
            n22 += -65;
         }

         for (int n25 = 3; n25 >= 0; n25--) {
            ItemStack stack = (ItemStack)client.player.getInventory().armor.get(n25);
            int n26 = stack.getMaxDamage();
            int n27 = stack.getDamage();
            context.drawItem(stack, n21, n22);
            context.drawItemInSlot(client.textRenderer, stack, n21, n22);
            if ((Boolean)this.armorDurability.atx() && !stack.isEmpty() && stack.isDamageable()) {
               String str17 = String.valueOf((int)Math.floor((double)((float)(n26 - n27) / (float)n26 * 100.0F)));
               this.adr(context, str17, (float)n21 + 8.0F - (float)RenderUtils.acF(str17) / 2.0F, (float)n22 - 8.0F, ColorUtils.iz((float)(n26 - n27) / (float)n26 * 120.0F, 100.0F, 50.0F, 1.0F).getRGB());
            }

            n21 += 20;
         }
      }

      if ((Boolean)this.totems.atx() && !client.player.isCreative() && !client.player.isSpectator()) {
         int n28 = window.getScaledWidth() / 2 - 8;
         int n29 = window.getScaledHeight() - 55;
         context.drawItem(new ItemStack(Items.TOTEM_OF_UNDYING), n28, n29 - 1);
         String str18 = String.valueOf(StackPlayerUtils2.abK(Items.TOTEM_OF_UNDYING));
         context.getMatrices().translate(0.0F, 0.0F, 200.0F);
         this.adr(context, str18, (float)(n28 + 17 - RenderUtils.acF(str18)), (float)(n29 + 9), -1);
      }
   }

   @EventListener
   public void onAddStatusEffect(AddStatusEffectEvent addStatusEffectEvent) {
      StatusEffectInstance statusEffectInstance = addStatusEffectEvent.getStatusEffectInstance();
      HUDLegacyRegistryEntryPlayerHelper hudLegacyRegistryEntryPlayerHelper = new HUDLegacyRegistryEntryPlayerHelper(statusEffectInstance.getEffectType(), statusEffectInstance.getAmplifier(), statusEffectInstance.getDuration());
      Set temp = this.map.keySet();
      final HUDLegacyRegistryEntryPlayerHelper hudLegacyRegistryEntryPlayerHelper2 = hudLegacyRegistryEntryPlayerHelper;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object addStatusEffectEvent) {
            return HUDLegacy.ka(hudLegacyRegistryEntryPlayerHelper2, (HUDLegacyRegistryEntryPlayerHelper)addStatusEffectEvent);
         }
      };
      if (temp.removeIf(predicate)) {
         Map map2 = this.map;
         UnidentifiedHelper7 unidentifiedHelper73 = new UnidentifiedHelper7(true, '\u0000', 250.0F, 496665058, '楚', Easing.SINE_IN_OUT);
         map2.put(hudLegacyRegistryEntryPlayerHelper, unidentifiedHelper73);
      } else {
         UnidentifiedHelper7 unidentifiedHelper74 = new UnidentifiedHelper7(false, '\u0000', 250.0F, 496665058, '楚', Easing.SINE_IN_OUT);
         unidentifiedHelper74.qV(true);
         this.map.put(hudLegacyRegistryEntryPlayerHelper, unidentifiedHelper74);
      }
   }

   @EventListener
   public void onRemoveStatusEffect(RemoveStatusEffectEvent removeStatusEffectEvent) {
      Iterator iterator2 = this.map.keySet().iterator();

      while (iterator2.hasNext()) {
         HUDLegacyRegistryEntryPlayerHelper hudLegacyRegistryEntryPlayerHelper = (HUDLegacyRegistryEntryPlayerHelper)iterator2.next();
         if (hudLegacyRegistryEntryPlayerHelper.getRegistryEntry() == removeStatusEffectEvent.getRegistryEntry()) {
            ((UnidentifiedHelper7)this.map.get(hudLegacyRegistryEntryPlayerHelper)).qV(false);
            break;
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      this.stopwatch.afr(29432, 5265492, (byte)40);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (client.world == null || client.isPaused()) {
         this.stopwatch.afr(29432, 5265492, (byte)40);
      }
   }

   @EventListener
   public void onSetScreen(SetScreenEvent setScreenEvent) {
      if (setScreenEvent.getScreen() == null && this.unidentifiedHelper7.qU()) {
         this.unidentifiedHelper7.qV(false);
      } else if (setScreenEvent.getScreen() instanceof ChatScreen) {
         this.unidentifiedHelper7.qV(true);
      }
   }

   @EventListener
   public void onRenderStatusEffectOverlay(RenderStatusEffectOverlayEvent renderStatusEffectOverlayEvent) {
      if (this.potionHud.atx() == HUDLegacyMoveHideMode.HIDE) {
         renderStatusEffectOverlayEvent.aim();
      }
   }

   @EventListener
   public void onRenderHeldItemTooltip(RenderHeldItemTooltipEvent renderHeldItemTooltipEvent) {
      if (this.itemName.atx() != HUDLegacyMoveHideMode.KEEP) {
         renderHeldItemTooltipEvent.aim();
      }

      if (this.itemName.atx() == HUDLegacyMoveHideMode.MOVE) {
         Window window = client.getWindow();
         int temp = window.getScaledWidth() / 2;
         TextRenderer temp2 = client.textRenderer;
         String str = renderHeldItemTooltipEvent.getText().getString();
         int n = temp - temp2.getWidth(str) / 2 + (Integer)this.xOffset.atx();
         int n2 = window.getScaledHeight() - 9 - 59 - (Integer)this.yOffset.atx();
         if ((Boolean)this.dynamicY.atx()) {
            if (!client.player.getInventory().armor.isEmpty()) {
               if (client.player.getAbsorptionAmount() > 0.0F) {
                  n2 -= 9;
               }

               n2 -= 9;
            } else if (client.player.getAbsorptionAmount() > 0.0F) {
               n2 -= 9;
            }
         }

         renderHeldItemTooltipEvent.gD(n);
         renderHeldItemTooltipEvent.gF(n2);
      }
   }

   public int jU(long l2) {
      return switch ((HUDLegacyOption2)this.rainbow.atx()) {
         case a_ -> ColorHelper3.auO().auX();
         case b -> {
            float f2 = Math.max(100.0F - (Float)this.rainbowSpeed.atx(), 0.1F);
            float f3 = Math.max(100.0F - (Float)this.rainbowDifference.atx(), 0.1F);
            yield ColorUtils.iA(
                  (float)MathHelper.clamp(Math.abs(Math.sin(Math.toRadians((float)l2 * f3 + (double)System.currentTimeMillis() / f2))), 0.0, 1.0),
                  ColorHelper3.auO().auU(),
                  (Color)this.gradientColor.atx()
               )
               .getRGB();
         }
         case c -> this.jY(l2);
         case d -> this.jY(1L);
      };
   }

   private String jV(ModuleBase moduleBase) {
      return moduleBase.OO() + this.jW(moduleBase.hy());
   }

   private String jW(String str) {
      return !str.isEmpty() ? " §7[§f" + str + "§7]" : "";
   }

   public int jX(String str, StatusEffect statusEffect) {
      String str2 = str.replace("minecraft:", "");
      switch (str2.hashCode()) {
         case -1781004809:
            if (str2.equals("invisibility")) {
               return 8356754;
            }

            return statusEffect.getColor();
         case -1259714865:
            if (str2.equals("absorption")) {
               return 2445989;
            }

            return statusEffect.getColor();
         case -1248513139:
            if (str2.equals("instant_damage")) {
               return 4393481;
            }

            return statusEffect.getColor();
         case -1206104397:
            if (str2.equals("hunger")) {
               return 5797459;
            }

            return statusEffect.getColor();
         case -1130648966:
            if (!str2.equals("instant_health")) {
               return statusEffect.getColor();
            }
            break;
         case -1083012136:
            if (str2.equals("slowness")) {
               return 5926017;
            }

            return statusEffect.getColor();
         case -1052579859:
            if (str2.equals("nausea")) {
               return 5578058;
            }

            return statusEffect.getColor();
         case -982749432:
            if (str2.equals("poison")) {
               return 5149489;
            }

            return statusEffect.getColor();
         case -840436278:
            if (str2.equals("unluck")) {
               return 12624973;
            }

            return statusEffect.getColor();
         case -787569677:
            if (str2.equals("wither")) {
               return 3484199;
            }

            return statusEffect.getColor();
         case -736186929:
            if (str2.equals("weakness")) {
               return 4738376;
            }

            return statusEffect.getColor();
         case -306977811:
            if (str2.equals("levitation")) {
               return 13565951;
            }

            return statusEffect.getColor();
         case -251715502:
            if (str2.equals("jump_boost")) {
               return 2293580;
            }

            return statusEffect.getColor();
         case -230491182:
            if (!str2.equals("saturation")) {
               return statusEffect.getColor();
            }
            break;
         case -84082086:
            if (str2.equals("water_breathing")) {
               return 3035801;
            }

            return statusEffect.getColor();
         case 3333041:
            if (str2.equals("luck")) {
               return 3381504;
            }

            return statusEffect.getColor();
         case 99050123:
            if (str2.equals("haste")) {
               return 14270531;
            }

            return statusEffect.getColor();
         case 109641799:
            if (str2.equals("speed")) {
               return 8171462;
            }

            return statusEffect.getColor();
         case 121707317:
            if (str2.equals("glowing")) {
               return 9740385;
            }

            return statusEffect.getColor();
         case 151619372:
            if (str2.equals("blindness")) {
               return 2039587;
            }

            return statusEffect.getColor();
         case 845042688:
            if (str2.equals("health_boost")) {
               return 16284963;
            }

            return statusEffect.getColor();
         case 1032770443:
            if (str2.equals("regeneration")) {
               return 13458603;
            }

            return statusEffect.getColor();
         case 1254846936:
            if (str2.equals("mining_fatigue")) {
               return 4866583;
            }

            return statusEffect.getColor();
         case 1623775714:
            if (str2.equals("fire_resistance")) {
               return 14981690;
            }

            return statusEffect.getColor();
         case 1749920239:
            if (str2.equals("night_vision")) {
               return 2039713;
            }

            return statusEffect.getColor();
         case 1791316033:
            if (str2.equals("strength")) {
               return 9643043;
            }

            return statusEffect.getColor();
         case 1863800889:
            if (str2.equals("resistance")) {
               return 10044730;
            }

            return statusEffect.getColor();
         default:
            return statusEffect.getColor();
      }

      return 16262179;
   }

   private int jY(long l2) {
      return Color.HSBtoRGB(
         (float)(
            ((double)System.currentTimeMillis() * ((Float)this.rainbowSpeed.atx() / 10.0F) + l2 * 500L)
               % (30000.0F / ((Float)this.rainbowDifference.atx() / 100.0F))
               / (30000.0F / ((Float)this.rainbowDifference.atx() / 20.0F))
         ),
         ((Integer)this.rainbowSaturation.atx()).intValue() / 100.0F,
         ((Integer)this.rainbowBrightness.atx()).intValue() / 100.0F
      );
   }

   public int jZ(long l2) {
      float[] fs = new float[3];
      Color color = ColorHelper3.auO().auU();
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fs);
      float f2 = (float)(
         ((double)System.currentTimeMillis() * ((Float)this.rainbowSpeed.atx() / 10.0F) + l2 * 500L)
            % (30000.0F / ((Float)this.rainbowDifference.atx() / 100.0F))
            / (30000.0F / ((Float)this.rainbowDifference.atx() / 20.0F))
      );
      float f3 = fs[2] * Math.abs(f2 % 1.0F - 0.55F) + 0.4F;
      return Color.HSBtoRGB(fs[0], fs[1], f3);
   }

   @Override
   public void adb() {
   }

   @Override
   public void adc(DrawContext context) {
   }

   @Override
   public float adl() {
      return 0.0F;
   }

   @Override
   public float adm() {
      return 0.0F;
   }

   static boolean ka(HUDLegacyRegistryEntryPlayerHelper hudLegacyRegistryEntryPlayerHelper, HUDLegacyRegistryEntryPlayerHelper hudLegacyRegistryEntryPlayerHelper2) {
      return hudLegacyRegistryEntryPlayerHelper.getRegistryEntry() == hudLegacyRegistryEntryPlayerHelper2.getRegistryEntry();
   }

   static boolean kb(ClientModule clientModule) {
      return !clientModule.WS();
   }

   private Boolean isRainbowDifferenceVisible() {
      return this.rainbow.atx() != HUDLegacyOption2.a_;
   }

   private Boolean isRainbowBrightnessVisible() {
      return this.rainbow.atx() != HUDLegacyOption2.a_ && this.rainbow.atx() != HUDLegacyOption2.b;
   }

   private Boolean isRainbowSaturationVisible() {
      return this.rainbow.atx() != HUDLegacyOption2.a_ && this.rainbow.atx() != HUDLegacyOption2.b;
   }

   private Boolean isRainbowSpeedVisible() {
      return this.rainbow.atx() != HUDLegacyOption2.a_;
   }

   private Boolean isGradientColorVisible() {
      return this.rainbow.atx() == HUDLegacyOption2.b;
   }

   private Boolean isNetherCoordsVisible() {
      return (Boolean)this.coords.atx();
   }

   private Boolean isPotionColorsVisible() {
      return (Boolean)this.potionEffects.atx();
   }

   private Boolean isXOffsetVisible() {
      return this.itemName.atx() == HUDLegacyMoveHideMode.MOVE;
   }

   private Boolean isYOffsetVisible() {
      return this.itemName.atx() == HUDLegacyMoveHideMode.MOVE;
   }

   private Boolean isDynamicYVisible() {
      return this.itemName.atx() == HUDLegacyMoveHideMode.MOVE;
   }

   private Boolean isArmorDurabilityVisible() {
      return (Boolean)this.armor.atx();
   }

   private Boolean isRotationVisible() {
      return (Boolean)this.direction.atx();
   }

   private Boolean isNumberSetting1Visible() {
      return (Boolean)this.booleanSetting1_1.atx() && (Boolean)this.booleanSetting2_1.atx();
   }
}
