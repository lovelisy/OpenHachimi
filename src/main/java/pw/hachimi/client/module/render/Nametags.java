package pw.hachimi.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.ColorHelper.Argb;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import pw.hachimi.client.event.render.RenderLabelIfPresentEvent;
import pw.hachimi.client.event.world.PlaySoundEvent;
import pw.hachimi.client.event.world.PosSoundEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.mixin.TextRendererMixin;
import pw.hachimi.client.model.render.NametagsNoneDistanceMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.client.Font;
import pw.hachimi.client.module.misc.PotionTracker;
import pw.hachimi.client.module.player.Freecam;
import pw.hachimi.client.player.PlayerWorldOtherClientPlayerEntity;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.FontLoader;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.EntityBoxUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Nametags extends ClientModule {
   private static Nametags INSTANCE;
   private final Map map = new HashMap();
   private final List list = e();
   Setting armor = this.registerSetting(new BooleanSetting("Armor", "Displays the player's armor", true));
   Setting items = this.registerSetting(new BooleanSetting("Items", "Displays the player's held items", true));
   Setting enchantments = this.registerSetting(new BooleanSetting("Enchantments", "Displays a list of the item's enchantments", true));
   Setting durability = this.registerSetting(new BooleanSetting("Durability", "Displays item durability", true));
   Setting itemName = this.registerSetting(new BooleanSetting("ItemName", "Displays the player's current held item name", false));
   Setting entityId = this.registerSetting(new BooleanSetting("EntityId", "Displays the player's entity id", false));
   Setting gamemode = this.registerSetting(new BooleanSetting("Gamemode", "Displays the player's gamemode", false));
   Setting ping = this.registerSetting(new BooleanSetting("Ping", "Displays the player's server connection ping", true));
   Setting health = this.registerSetting(new BooleanSetting("Health", "Displays the player's current health", true));
   Setting totems = this.registerSetting(new BooleanSetting("Totems", "Displays the player's popped totem count", false));
   Setting invisibles = this.registerSetting(new BooleanSetting("Invisibles", "Renders nametags on invisible players", true));
   Setting background = this.registerSetting(new BooleanSetting("Background", "Renders a background behind the nametag", true));
   Setting border = this.registerSetting(new BooleanSetting("Border", "Renders a border around the nametag", false));
   Setting pearls = this.registerSetting(new BooleanSetting("Pearls", "Renders nametags on thrown ender pearls", false));
   Setting droppedItems = this.registerSetting(new BooleanSetting("DroppedItems", "Renders nametags on dropped items", false));
   Setting itemsTag = this.registerSetting(new EnumSetting("ItemsTag", "Dropped items tag", NametagsNoneDistanceMode.Both, NametagsNoneDistanceMode.values(), this.droppedItems::atx));
   Setting itemsRange = this.registerSetting(new NumberSetting("ItemsRange", "Render nametags on items in range", 0.0F, 100.0F, 500.0F, this::isItemsRangeVisible));
   Setting fadeStart = this.registerSetting(new NumberSetting("FadeStart", "Fade start range", 0.0F, 90.0F, 500.0F, this::isFadeStartVisible));
   Setting sounds = this.registerSetting(new BooleanSetting("Sounds", "Renders nametags on sounds", false));
   Setting scale = this.registerSetting(new NumberSetting("Scale", "Scale of text", 0.0F, 1.0F, 3.0F));
   Setting height = this.registerSetting(new NumberSetting("Height", "Nametag offset", -3.0F, 0.0F, 3.0F));
   Setting worldTextScale = this.registerSetting(new NumberSetting("WorldTextScale", "Scale of text", 0.0F, 1.0F, 3.0F));
   Setting factor = this.registerSetting(new BooleanSetting("Factor", "scale + (factor && pos.distanceTo(x, eyeY, z) > distance ? (pos.distanceTo(x, eyeY, z) - distance) * 0.1 * scaling : 0)", true));
   Setting scaling = this.registerSetting(new NumberSetting("Scaling", "scale + (factor && pos.distanceTo(x, eyeY, z) > distance ? (pos.distanceTo(x, eyeY, z) - distance) * 0.1 * scaling : 0)", 0.0F, 1.0F, 3.0F, this.factor::atx));
   Setting distance = this.registerSetting(new NumberSetting("Distance", "scale + (factor && pos.distanceTo(x, eyeY, z) > distance ? (pos.distanceTo(x, eyeY, z) - distance) * 0.1 * scaling : 0)", 0.0F, 6.0F, 20.0F, this.factor::atx));
   public static boolean ck;

   public Nametags() {
      super("Nametags", "Renders info on player nametags", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static Nametags HF() {
      return INSTANCE;
   }

   public static int HG(int n) {
      int n2;
      if ((n & -67108864) == 0) {
         n2 = n | 0xFF000000;
      } else {
         n2 = n;
      }

      return n2;
   }

   @Override
   public void awe() {
      this.map.clear();
   }

   @EventListener
   public void HH(Render3DContext render3DContext) {
      Vector3f vector3f;
      Vector3f vector3f2;
      label1912: {
         vector3f = null;
         vector3f2 = null;
         Camera camera = null;
         boolean flag = false;
         boolean flag2 = false;
         float f2 = 0.0F;
         float f3 = 0.0F;
         NametagsNoneDistanceMode nametagsNoneDistanceMode = null;
         Iterator iterator2 = null;
         Entity entity2 = null;
         Object obj = null;
         byte b = 0;
         Object obj2 = null;
         Object obj3 = null;
         Object obj4 = null;
         float f4 = 0.0F;
         double d = 0.0;
         Vec3d vec = null;
         Vec3d vec2 = null;
         ItemStack stack = null;
         String str = null;
         int n = 0;
         double d2 = 0.0;
         double d3 = 0.0;
         Entity entity3 = null;
         PlayerEntity player2 = null;
         double d4 = 0.0;
         double d5 = 0.0;
         long l = 0L;
         Entry entry2 = null;
         float f5 = 0.0F;
         double d6 = 0.0;
         int n2;
         int n3;
         int n4;
         if (client.gameRenderer == null) {
            n2 = 1039725552;
            n4 = 470787663;
            n3 = 2084861071;
         } else {
            label1816:
            if (client.world != null) {
               if (client.getCameraEntity() == null) {
                  return;
               }

               label1813: {
                  label1812: {
                     label1913: {
                        vector3f = RenderSystem.shaderLightDirections[0];
                        vector3f2 = RenderSystem.shaderLightDirections[1];
                        RenderSystem.enablePolygonOffset();
                        RenderSystem.polygonOffset(1.0F, -2000000.0F);
                        RenderSystem.disableDepthTest();
                        DiffuseLighting.method_56819(client.getEntityRenderDispatcher().getRotation());
                        camera = client.gameRenderer.getCamera();
                        flag = (Boolean)this.droppedItems.atx();
                        flag2 = (Boolean)this.pearls.atx();
                        f2 = (Float)this.itemsRange.atx();
                        f3 = (Float)this.fadeStart.atx();
                        nametagsNoneDistanceMode = (NametagsNoneDistanceMode)this.itemsTag.atx();
                        FontLoader.fontRenderer2.aaY();
                        iterator2 = client.world.getEntities().iterator();
                        if (iterator2.hasNext()) {
                           label1809:
                           while (true) {
                              entity2 = (Entity)iterator2.next();
                              if (RenderUtils.acy(entity2.getBoundingBox().stretch(0.0, 2.0, 0.0))) {
                                 obj = entity2;
                                 b = 0;
                                 switch (entity2 == null
                                    ? -1
                                    : (
                                       true && entity2 instanceof PlayerEntity
                                          ? 0
                                          : (true && entity2 instanceof ItemEntity ? 1 : (true && entity2 instanceof EnderPearlEntity ? 2 : 3))
                                    )) {
                                    case 0:
                                       break label1913;
                                    case 1:
                                       break label1812;
                                    case 2:
                                       label2200: {
                                          label1783: {
                                             label1782:
                                             while (true) {
                                                obj4 = (EnderPearlEntity)entity2;
                                                if (flag2) {
                                                   break;
                                                }

                                                b = 3;
                                                switch (entity2 == null
                                                   ? -1
                                                   : (
                                                      (((((3)))))
                                                   )) {
                                                   case 0:
                                                      break label1913;
                                                   case 1:
                                                      break label1812;
                                                   case 2:
                                                      break label1813;
                                                }

                                                do {
                                                   if (!iterator2.hasNext()) {
                                                      break label1809;
                                                   }

                                                   entity2 = (Entity)iterator2.next();
                                                } while (!RenderUtils.acy(entity2.getBoundingBox().stretch(0.0, 2.0, 0.0)));

                                                while (true) {
                                                   obj = entity2;
                                                   b = 0;
                                                   switch (entity2 == null
                                                      ? -1
                                                      : (
                                                         true && entity2 instanceof PlayerEntity
                                                            ? 0
                                                            : (
                                                               true && entity2 instanceof ItemEntity
                                                                  ? 1
                                                                  : (true && entity2 instanceof EnderPearlEntity ? 2 : 3)
                                                            )
                                                      )) {
                                                      case 0:
                                                         break label1913;
                                                      case 1:
                                                         obj3 = (ItemEntity)entity2;
                                                         if (!flag) {
                                                            b = 2;
                                                            switch (entity2 == null
                                                               ? -1
                                                               : (
                                                                  ((
                                                                        ((true && entity2 instanceof EnderPearlEntity ? 2 : 3))
                                                                     ))
                                                               )) {
                                                               case 0:
                                                                  break label1913;
                                                               case 1:
                                                                  break label1812;
                                                               case 2:
                                                                  break label1813;
                                                            }

                                                            do {
                                                               if (!iterator2.hasNext()) {
                                                                  break label1809;
                                                               }

                                                               entity2 = (Entity)iterator2.next();
                                                            } while (!RenderUtils.acy(entity2.getBoundingBox().stretch(0.0, 2.0, 0.0)));

                                                            label1719:
                                                            while (true) {
                                                               obj = entity2;
                                                               b = 0;
                                                               switch (entity2 == null
                                                                  ? -1
                                                                  : (
                                                                     true && entity2 instanceof PlayerEntity
                                                                        ? 0
                                                                        : (
                                                                           true && entity2 instanceof ItemEntity
                                                                              ? 1
                                                                              : (true && entity2 instanceof EnderPearlEntity ? 2 : 3)
                                                                        )
                                                                  )) {
                                                                  case 0:
                                                                     break label1913;
                                                                  case 1:
                                                                     break label1812;
                                                                  case 2:
                                                                     obj4 = (EnderPearlEntity)entity2;
                                                                     if (flag2) {
                                                                        break label1782;
                                                                     }

                                                                     label1712:
                                                                     while (true) {
                                                                        b = 3;
                                                                        switch (entity2 == null
                                                                           ? -1
                                                                           : (
                                                                              ((
                                                                                    (((3)))
                                                                                 ))
                                                                           )) {
                                                                           case 0:
                                                                              break label1913;
                                                                           case 1:
                                                                              break label1812;
                                                                           case 2:
                                                                              obj4 = (EnderPearlEntity)entity2;
                                                                              if (flag2) {
                                                                                 entity3 = ((EnderPearlEntity)entity2).getOwner();
                                                                                 if (!(entity3 instanceof PlayerEntity)) {
                                                                                    n2 = 1320418234;
                                                                                    n4 = -487562477;
                                                                                    n3 = 1137831179;
                                                                                    break label1816;
                                                                                 }

                                                                                 label1674:
                                                                                 while (true) {
                                                                                    player2 = (PlayerEntity)entity3;
                                                                                    vec = EntityBoxUtils.Bz((EnderPearlEntity)entity2, render3DContext.f);
                                                                                    d4 = ((EnderPearlEntity)entity2).getX() - vec.getX();
                                                                                    d5 = ((EnderPearlEntity)entity2).getY() - vec.getY();
                                                                                    d2 = ((EnderPearlEntity)entity2).getZ() - vec.getZ();
                                                                                    RenderUtils.acw(
                                                                                       render3DContext.matrices,
                                                                                       ((PlayerEntity)entity3).getName().getString(),
                                                                                       d4,
                                                                                       d5,
                                                                                       d2,
                                                                                       this.HU((PlayerEntity)entity3)
                                                                                    );

                                                                                    label1672:
                                                                                    while (true) {
                                                                                       if (!iterator2.hasNext()) {
                                                                                          break label1809;
                                                                                       }

                                                                                       entity2 = (Entity)iterator2.next();
                                                                                       if (RenderUtils.acy(entity2.getBoundingBox().stretch(0.0, 2.0, 0.0))) {
                                                                                          while (true) {
                                                                                             obj = entity2;
                                                                                             b = 0;
                                                                                             switch (entity2 == null
                                                                                                ? -1
                                                                                                : (
                                                                                                   true && entity2 instanceof PlayerEntity
                                                                                                      ? 0
                                                                                                      : (
                                                                                                         true && entity2 instanceof ItemEntity
                                                                                                            ? 1
                                                                                                            : (
                                                                                                               true && entity2 instanceof EnderPearlEntity
                                                                                                                  ? 2
                                                                                                                  : 3
                                                                                                            )
                                                                                                      )
                                                                                                )) {
                                                                                                case 0:
                                                                                                   break label1913;
                                                                                                case 1:
                                                                                                   break label1812;
                                                                                                case 2:
                                                                                                   obj4 = (EnderPearlEntity)entity2;
                                                                                                   if (flag2) {
                                                                                                      break label1782;
                                                                                                   }

                                                                                                   label1647:
                                                                                                   while (true) {
                                                                                                      b = 3;
                                                                                                      switch (entity2 == null
                                                                                                         ? -1
                                                                                                         : (
                                                                                                            ((
                                                                                                                  ((
                                                                                                                        (3)
                                                                                                                     ))
                                                                                                               ))
                                                                                                         )) {
                                                                                                         case 0:
                                                                                                            break label1913;
                                                                                                         case 1:
                                                                                                            obj3 = (ItemEntity)entity2;
                                                                                                            if (flag) {
                                                                                                               break label1719;
                                                                                                            }

                                                                                                            while (true) {
                                                                                                               b = 2;
                                                                                                               switch (entity2 == null
                                                                                                                  ? -1
                                                                                                                  : (
                                                                                                                     ((
                                                                                                                           ((
                                                                                                                                 true
                                                                                                                                       && entity2 instanceof EnderPearlEntity
                                                                                                                                    ? 2
                                                                                                                                    : 3
                                                                                                                              ))
                                                                                                                        ))
                                                                                                                  )) {
                                                                                                                  case 0:
                                                                                                                     break label1913;
                                                                                                                  case 1:
                                                                                                                     obj3 = (ItemEntity)entity2;
                                                                                                                     if (flag) {
                                                                                                                        label1592:
                                                                                                                        while (true) {
                                                                                                                           d3 = client.player
                                                                                                                              .distanceTo(entity2);
                                                                                                                           if (!(d3 > f2)) {
                                                                                                                              n2 = 951530438;
                                                                                                                              n4 = 808582115;
                                                                                                                              n3 = 2030810297;
                                                                                                                              break label1816;
                                                                                                                           }

                                                                                                                           label1590:
                                                                                                                           while (true) {
                                                                                                                              if (!iterator2.hasNext()) {
                                                                                                                                 break label1809;
                                                                                                                              }

                                                                                                                              entity2 = (Entity)iterator2.next();
                                                                                                                              if (RenderUtils.acy(
                                                                                                                                 entity2.getBoundingBox()
                                                                                                                                    .stretch(0.0, 2.0, 0.0)
                                                                                                                              )) {
                                                                                                                                 while (true) {
                                                                                                                                    obj = entity2;
                                                                                                                                    b = 0;
                                                                                                                                    switch (entity2 == null
                                                                                                                                       ? -1
                                                                                                                                       : (
                                                                                                                                          true
                                                                                                                                                && entity2 instanceof PlayerEntity
                                                                                                                                             ? 0
                                                                                                                                             : (
                                                                                                                                                true
                                                                                                                                                      && entity2 instanceof ItemEntity
                                                                                                                                                   ? 1
                                                                                                                                                   : (
                                                                                                                                                      true
                                                                                                                                                            && entity2 instanceof EnderPearlEntity
                                                                                                                                                         ? 2
                                                                                                                                                         : 3
                                                                                                                                                   )
                                                                                                                                             )
                                                                                                                                       )) {
                                                                                                                                       case 0:
                                                                                                                                          break label1913;
                                                                                                                                       case 1:
                                                                                                                                          break label1812;
                                                                                                                                       case 2:
                                                                                                                                          obj4 = (EnderPearlEntity)entity2;
                                                                                                                                          if (flag2) {
                                                                                                                                             break label1782;
                                                                                                                                          }

                                                                                                                                          label1565:
                                                                                                                                          while (true) {
                                                                                                                                             b = 3;
                                                                                                                                             switch (entity2
                                                                                                                                                   == null
                                                                                                                                                ? -1
                                                                                                                                                : (
                                                                                                                                                   ((
                                                                                                                                                         ((
                                                                                                                                                               (3)
                                                                                                                                                            ))
                                                                                                                                                      ))
                                                                                                                                                )) {
                                                                                                                                                case 0:
                                                                                                                                                   break label1913;
                                                                                                                                                case 1:
                                                                                                                                                   obj3 = (ItemEntity)entity2;
                                                                                                                                                   if (flag) {
                                                                                                                                                      break label1719;
                                                                                                                                                   }

                                                                                                                                                   while (true) {
                                                                                                                                                      b = 2;
                                                                                                                                                      switch (entity2
                                                                                                                                                            == null
                                                                                                                                                         ? -1
                                                                                                                                                         : (
                                                                                                                                                            ((
                                                                                                                                                                  ((
                                                                                                                                                                        true
                                                                                                                                                                              && entity2 instanceof EnderPearlEntity
                                                                                                                                                                           ? 2
                                                                                                                                                                           : 3
                                                                                                                                                                     ))
                                                                                                                                                               ))
                                                                                                                                                         )) {
                                                                                                                                                         case 0:
                                                                                                                                                            obj2 = (PlayerEntity)entity2;
                                                                                                                                                            if ((PlayerEntity)entity2
                                                                                                                                                               == client.player
                                                                                                                                                               )
                                                                                                                                                             {
                                                                                                                                                               if (Freecam.gP()
                                                                                                                                                                  .awg()
                                                                                                                                                                  )
                                                                                                                                                                {
                                                                                                                                                                  n2 = 2021866416;
                                                                                                                                                                  n4 = 1066665675;
                                                                                                                                                                  n3 = 1977691393;
                                                                                                                                                                  break label1816;
                                                                                                                                                               }

                                                                                                                                                               label1508:
                                                                                                                                                               while (true) {
                                                                                                                                                                  if (!iterator2.hasNext()
                                                                                                                                                                     )
                                                                                                                                                                   {
                                                                                                                                                                     break label1809;
                                                                                                                                                                  }

                                                                                                                                                                  entity2 = (Entity)iterator2.next();
                                                                                                                                                                  if (RenderUtils.acy(
                                                                                                                                                                     entity2.getBoundingBox()
                                                                                                                                                                        .stretch(
                                                                                                                                                                           0.0,
                                                                                                                                                                           2.0,
                                                                                                                                                                           0.0
                                                                                                                                                                        )
                                                                                                                                                                  )
                                                                                                                                                                     )
                                                                                                                                                                   {
                                                                                                                                                                     while (true) {
                                                                                                                                                                        obj = entity2;
                                                                                                                                                                        b = 0;
                                                                                                                                                                        switch (entity2
                                                                                                                                                                              == null
                                                                                                                                                                           ? -1
                                                                                                                                                                           : (
                                                                                                                                                                              true
                                                                                                                                                                                    && entity2 instanceof PlayerEntity
                                                                                                                                                                                 ? 0
                                                                                                                                                                                 : (
                                                                                                                                                                                    true
                                                                                                                                                                                          && entity2 instanceof ItemEntity
                                                                                                                                                                                       ? 1
                                                                                                                                                                                       : (
                                                                                                                                                                                          true
                                                                                                                                                                                                && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                             ? 2
                                                                                                                                                                                             : 3
                                                                                                                                                                                       )
                                                                                                                                                                                 )
                                                                                                                                                                           )) {
                                                                                                                                                                           case 0:
                                                                                                                                                                              break label1913;
                                                                                                                                                                           case 1:
                                                                                                                                                                              break label1812;
                                                                                                                                                                           case 2:
                                                                                                                                                                              obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                              if (flag2
                                                                                                                                                                                 )
                                                                                                                                                                               {
                                                                                                                                                                                 break label1782;
                                                                                                                                                                              }

                                                                                                                                                                              label1483:
                                                                                                                                                                              while (true) {
                                                                                                                                                                                 b = 3;
                                                                                                                                                                                 switch (entity2
                                                                                                                                                                                       == null
                                                                                                                                                                                    ? -1
                                                                                                                                                                                    : (
                                                                                                                                                                                       ((
                                                                                                                                                                                             ((
                                                                                                                                                                                                   (3)
                                                                                                                                                                                                ))
                                                                                                                                                                                          ))
                                                                                                                                                                                    )) {
                                                                                                                                                                                    case 0:
                                                                                                                                                                                       break label1913;
                                                                                                                                                                                    case 1:
                                                                                                                                                                                       obj3 = (ItemEntity)entity2;
                                                                                                                                                                                       if (flag
                                                                                                                                                                                          )
                                                                                                                                                                                        {
                                                                                                                                                                                          break label1719;
                                                                                                                                                                                       }

                                                                                                                                                                                       while (true) {
                                                                                                                                                                                          b = 2;
                                                                                                                                                                                          switch (entity2
                                                                                                                                                                                                == null
                                                                                                                                                                                             ? -1
                                                                                                                                                                                             : (
                                                                                                                                                                                                ((
                                                                                                                                                                                                      ((
                                                                                                                                                                                                            true
                                                                                                                                                                                                                  && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                               ? 2
                                                                                                                                                                                                               : 3
                                                                                                                                                                                                         ))
                                                                                                                                                                                                   ))
                                                                                                                                                                                             )) {
                                                                                                                                                                                             case 0:
                                                                                                                                                                                                break label1913;
                                                                                                                                                                                             case 1:
                                                                                                                                                                                                obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                if (flag
                                                                                                                                                                                                   )
                                                                                                                                                                                                 {
                                                                                                                                                                                                   d3 = client.player
                                                                                                                                                                                                      .distanceTo(
                                                                                                                                                                                                         entity2
                                                                                                                                                                                                      );
                                                                                                                                                                                                   if (!(
                                                                                                                                                                                                      d3
                                                                                                                                                                                                         > f2
                                                                                                                                                                                                   )
                                                                                                                                                                                                      )
                                                                                                                                                                                                    {
                                                                                                                                                                                                      if (!(
                                                                                                                                                                                                         d3
                                                                                                                                                                                                            > f3
                                                                                                                                                                                                      )
                                                                                                                                                                                                         )
                                                                                                                                                                                                       {
                                                                                                                                                                                                         break label1783;
                                                                                                                                                                                                      }

                                                                                                                                                                                                      label1428:
                                                                                                                                                                                                      while (true) {
                                                                                                                                                                                                         d6 = 1.0
                                                                                                                                                                                                            - (
                                                                                                                                                                                                                  d3
                                                                                                                                                                                                                     - f3
                                                                                                                                                                                                               )
                                                                                                                                                                                                               / (
                                                                                                                                                                                                                  f2
                                                                                                                                                                                                                     - f3
                                                                                                                                                                                                               );
                                                                                                                                                                                                         d = d6;
                                                                                                                                                                                                         if (!(
                                                                                                                                                                                                            d6
                                                                                                                                                                                                               < 0.01
                                                                                                                                                                                                         )
                                                                                                                                                                                                            )
                                                                                                                                                                                                          {
                                                                                                                                                                                                            break label2200;
                                                                                                                                                                                                         }

                                                                                                                                                                                                         label1425:
                                                                                                                                                                                                         while (true) {
                                                                                                                                                                                                            if (!iterator2.hasNext()
                                                                                                                                                                                                               )
                                                                                                                                                                                                             {
                                                                                                                                                                                                               break label1809;
                                                                                                                                                                                                            }

                                                                                                                                                                                                            entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                            if (RenderUtils.acy(
                                                                                                                                                                                                               entity2.getBoundingBox()
                                                                                                                                                                                                                  .stretch(
                                                                                                                                                                                                                     0.0,
                                                                                                                                                                                                                     2.0,
                                                                                                                                                                                                                     0.0
                                                                                                                                                                                                                  )
                                                                                                                                                                                                            )
                                                                                                                                                                                                               )
                                                                                                                                                                                                             {
                                                                                                                                                                                                               while (true) {
                                                                                                                                                                                                                  obj = entity2;
                                                                                                                                                                                                                  b = 0;
                                                                                                                                                                                                                  switch (entity2
                                                                                                                                                                                                                        == null
                                                                                                                                                                                                                     ? -1
                                                                                                                                                                                                                     : (
                                                                                                                                                                                                                        true
                                                                                                                                                                                                                              && entity2 instanceof PlayerEntity
                                                                                                                                                                                                                           ? 0
                                                                                                                                                                                                                           : (
                                                                                                                                                                                                                              true
                                                                                                                                                                                                                                    && entity2 instanceof ItemEntity
                                                                                                                                                                                                                                 ? 1
                                                                                                                                                                                                                                 : (
                                                                                                                                                                                                                                    true
                                                                                                                                                                                                                                          && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                       ? 2
                                                                                                                                                                                                                                       : 3
                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                           )
                                                                                                                                                                                                                     )) {
                                                                                                                                                                                                                     case 0:
                                                                                                                                                                                                                        break label1913;
                                                                                                                                                                                                                     case 1:
                                                                                                                                                                                                                        break label1812;
                                                                                                                                                                                                                     case 2:
                                                                                                                                                                                                                        obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                        if (flag2
                                                                                                                                                                                                                           )
                                                                                                                                                                                                                         {
                                                                                                                                                                                                                           break label1782;
                                                                                                                                                                                                                        }

                                                                                                                                                                                                                        label1400:
                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                           b = 3;
                                                                                                                                                                                                                           switch (entity2
                                                                                                                                                                                                                                 == null
                                                                                                                                                                                                                              ? -1
                                                                                                                                                                                                                              : (
                                                                                                                                                                                                                                 ((
                                                                                                                                                                                                                                       ((
                                                                                                                                                                                                                                             (3)
                                                                                                                                                                                                                                          ))
                                                                                                                                                                                                                                    ))
                                                                                                                                                                                                                              )) {
                                                                                                                                                                                                                              case 0:
                                                                                                                                                                                                                                 break label1913;
                                                                                                                                                                                                                              case 1:
                                                                                                                                                                                                                                 obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                 if (flag
                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                  {
                                                                                                                                                                                                                                    break label1719;
                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                 while (true) {
                                                                                                                                                                                                                                    b = 2;
                                                                                                                                                                                                                                    switch (entity2
                                                                                                                                                                                                                                          == null
                                                                                                                                                                                                                                       ? -1
                                                                                                                                                                                                                                       : (
                                                                                                                                                                                                                                          ((
                                                                                                                                                                                                                                                ((
                                                                                                                                                                                                                                                      true
                                                                                                                                                                                                                                                            && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                                         ? 2
                                                                                                                                                                                                                                                         : 3
                                                                                                                                                                                                                                                   ))
                                                                                                                                                                                                                                             ))
                                                                                                                                                                                                                                       )) {
                                                                                                                                                                                                                                       case 0:
                                                                                                                                                                                                                                          break label1913;
                                                                                                                                                                                                                                       case 1:
                                                                                                                                                                                                                                          obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                          if (flag
                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                             d3 = client.player
                                                                                                                                                                                                                                                .distanceTo(
                                                                                                                                                                                                                                                   entity2
                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                             if (!(
                                                                                                                                                                                                                                                d3
                                                                                                                                                                                                                                                   > f2
                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                              {
                                                                                                                                                                                                                                                if (!(
                                                                                                                                                                                                                                                   d3
                                                                                                                                                                                                                                                      > f3
                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                 {
                                                                                                                                                                                                                                                   break label1428;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                continue label1428;
                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                             continue label1425;
                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                          break;
                                                                                                                                                                                                                                       case 2:
                                                                                                                                                                                                                                          obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                          if (flag2
                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                             entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                .getOwner();
                                                                                                                                                                                                                                             if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                              {
                                                                                                                                                                                                                                                player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                                vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                                   (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                                   render3DContext.f
                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                                d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                      .getX()
                                                                                                                                                                                                                                                   - vec.getX();
                                                                                                                                                                                                                                                d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                      .getY()
                                                                                                                                                                                                                                                   - vec.getY();
                                                                                                                                                                                                                                                d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                      .getZ()
                                                                                                                                                                                                                                                   - vec.getZ();
                                                                                                                                                                                                                                                RenderUtils.acw(
                                                                                                                                                                                                                                                   render3DContext.matrices,
                                                                                                                                                                                                                                                   ((PlayerEntity)entity3)
                                                                                                                                                                                                                                                      .getName()
                                                                                                                                                                                                                                                      .getString(),
                                                                                                                                                                                                                                                   d4,
                                                                                                                                                                                                                                                   d5,
                                                                                                                                                                                                                                                   d2,
                                                                                                                                                                                                                                                   this.HU(
                                                                                                                                                                                                                                                      (PlayerEntity)entity3
                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                             continue label1425;
                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                          continue label1400;
                                                                                                                                                                                                                                       default:
                                                                                                                                                                                                                                          continue label1425;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                              case 2:
                                                                                                                                                                                                                                 obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                 if (flag2
                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                  {
                                                                                                                                                                                                                                    entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                       .getOwner();
                                                                                                                                                                                                                                    if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                     {
                                                                                                                                                                                                                                       player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                       vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                          (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                          render3DContext.f
                                                                                                                                                                                                                                       );
                                                                                                                                                                                                                                       d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                             .getX()
                                                                                                                                                                                                                                          - vec.getX();
                                                                                                                                                                                                                                       d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                             .getY()
                                                                                                                                                                                                                                          - vec.getY();
                                                                                                                                                                                                                                       d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                             .getZ()
                                                                                                                                                                                                                                          - vec.getZ();
                                                                                                                                                                                                                                       RenderUtils.acw(
                                                                                                                                                                                                                                          render3DContext.matrices,
                                                                                                                                                                                                                                          ((PlayerEntity)entity3)
                                                                                                                                                                                                                                             .getName()
                                                                                                                                                                                                                                             .getString(),
                                                                                                                                                                                                                                          d4,
                                                                                                                                                                                                                                          d5,
                                                                                                                                                                                                                                          d2,
                                                                                                                                                                                                                                          this.HU(
                                                                                                                                                                                                                                             (PlayerEntity)entity3
                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                       );
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    break label1400;
                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                                 break;
                                                                                                                                                                                                                              default:
                                                                                                                                                                                                                                 break label1400;
                                                                                                                                                                                                                           }
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                  }

                                                                                                                                                                                                                  do {
                                                                                                                                                                                                                     if (!iterator2.hasNext()
                                                                                                                                                                                                                        )
                                                                                                                                                                                                                      {
                                                                                                                                                                                                                        break label1912;
                                                                                                                                                                                                                     }

                                                                                                                                                                                                                     entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                  } while (
                                                                                                                                                                                                                     !RenderUtils.acy(
                                                                                                                                                                                                                        entity2.getBoundingBox()
                                                                                                                                                                                                                           .stretch(
                                                                                                                                                                                                                              0.0,
                                                                                                                                                                                                                              2.0,
                                                                                                                                                                                                                              0.0
                                                                                                                                                                                                                           )
                                                                                                                                                                                                                     )
                                                                                                                                                                                                                  );
                                                                                                                                                                                                               }
                                                                                                                                                                                                            }
                                                                                                                                                                                                         }
                                                                                                                                                                                                      }

                                                                                                                                                                                                      label1345:
                                                                                                                                                                                                      while (true) {
                                                                                                                                                                                                         d6 = 1.0;
                                                                                                                                                                                                         d = 1.0;
                                                                                                                                                                                                         vec2 = EntityBoxUtils.BA(
                                                                                                                                                                                                            (ItemEntity)entity2,
                                                                                                                                                                                                            render3DContext.f
                                                                                                                                                                                                         );
                                                                                                                                                                                                         stack = ((ItemEntity)entity2)
                                                                                                                                                                                                            .getStack();
                                                                                                                                                                                                         str = this.HK(
                                                                                                                                                                                                            stack,
                                                                                                                                                                                                            nametagsNoneDistanceMode,
                                                                                                                                                                                                            d3
                                                                                                                                                                                                         );
                                                                                                                                                                                                         n = 16777215
                                                                                                                                                                                                            | (int)(
                                                                                                                                                                                                                  255.0
                                                                                                                                                                                                                     * 1.0
                                                                                                                                                                                                               )
                                                                                                                                                                                                               << 24;
                                                                                                                                                                                                         RenderUtils.acw(
                                                                                                                                                                                                            render3DContext.matrices,
                                                                                                                                                                                                            str,
                                                                                                                                                                                                            vec2.getX(),
                                                                                                                                                                                                            vec2.getY(),
                                                                                                                                                                                                            vec2.getZ(),
                                                                                                                                                                                                            n
                                                                                                                                                                                                         );

                                                                                                                                                                                                         label1258:
                                                                                                                                                                                                         while (true) {
                                                                                                                                                                                                            if (!iterator2.hasNext()
                                                                                                                                                                                                               )
                                                                                                                                                                                                             {
                                                                                                                                                                                                               break label1809;
                                                                                                                                                                                                            }

                                                                                                                                                                                                            entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                            if (RenderUtils.acy(
                                                                                                                                                                                                               entity2.getBoundingBox()
                                                                                                                                                                                                                  .stretch(
                                                                                                                                                                                                                     0.0,
                                                                                                                                                                                                                     2.0,
                                                                                                                                                                                                                     0.0
                                                                                                                                                                                                                  )
                                                                                                                                                                                                            )
                                                                                                                                                                                                               )
                                                                                                                                                                                                             {
                                                                                                                                                                                                               while (true) {
                                                                                                                                                                                                                  obj = entity2;
                                                                                                                                                                                                                  b = 0;
                                                                                                                                                                                                                  switch (entity2
                                                                                                                                                                                                                        == null
                                                                                                                                                                                                                     ? -1
                                                                                                                                                                                                                     : (
                                                                                                                                                                                                                        true
                                                                                                                                                                                                                              && entity2 instanceof PlayerEntity
                                                                                                                                                                                                                           ? 0
                                                                                                                                                                                                                           : (
                                                                                                                                                                                                                              true
                                                                                                                                                                                                                                    && entity2 instanceof ItemEntity
                                                                                                                                                                                                                                 ? 1
                                                                                                                                                                                                                                 : (
                                                                                                                                                                                                                                    true
                                                                                                                                                                                                                                          && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                       ? 2
                                                                                                                                                                                                                                       : 3
                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                           )
                                                                                                                                                                                                                     )) {
                                                                                                                                                                                                                     case 0:
                                                                                                                                                                                                                        break label1913;
                                                                                                                                                                                                                     case 1:
                                                                                                                                                                                                                        break label1812;
                                                                                                                                                                                                                     case 2:
                                                                                                                                                                                                                        obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                        if (flag2
                                                                                                                                                                                                                           )
                                                                                                                                                                                                                         {
                                                                                                                                                                                                                           break label1782;
                                                                                                                                                                                                                        }

                                                                                                                                                                                                                        label1233:
                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                           b = 3;
                                                                                                                                                                                                                           switch (entity2
                                                                                                                                                                                                                                 == null
                                                                                                                                                                                                                              ? -1
                                                                                                                                                                                                                              : (
                                                                                                                                                                                                                                 ((
                                                                                                                                                                                                                                       ((
                                                                                                                                                                                                                                             (3)
                                                                                                                                                                                                                                          ))
                                                                                                                                                                                                                                    ))
                                                                                                                                                                                                                              )) {
                                                                                                                                                                                                                              case 0:
                                                                                                                                                                                                                                 break label1913;
                                                                                                                                                                                                                              case 1:
                                                                                                                                                                                                                                 obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                 if (flag
                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                  {
                                                                                                                                                                                                                                    break label1719;
                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                 while (true) {
                                                                                                                                                                                                                                    b = 2;
                                                                                                                                                                                                                                    switch (entity2
                                                                                                                                                                                                                                          == null
                                                                                                                                                                                                                                       ? -1
                                                                                                                                                                                                                                       : (
                                                                                                                                                                                                                                          ((
                                                                                                                                                                                                                                                ((
                                                                                                                                                                                                                                                      true
                                                                                                                                                                                                                                                            && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                                         ? 2
                                                                                                                                                                                                                                                         : 3
                                                                                                                                                                                                                                                   ))
                                                                                                                                                                                                                                             ))
                                                                                                                                                                                                                                       )) {
                                                                                                                                                                                                                                       case 0:
                                                                                                                                                                                                                                          break label1913;
                                                                                                                                                                                                                                       case 1:
                                                                                                                                                                                                                                          obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                          if (flag
                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                             d3 = client.player
                                                                                                                                                                                                                                                .distanceTo(
                                                                                                                                                                                                                                                   entity2
                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                             if (!(
                                                                                                                                                                                                                                                d3
                                                                                                                                                                                                                                                   > f2
                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                              {
                                                                                                                                                                                                                                                if (!(
                                                                                                                                                                                                                                                   d3
                                                                                                                                                                                                                                                      > f3
                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                 {
                                                                                                                                                                                                                                                   break label1783;
                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                label1343:
                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                   d6 = 1.0
                                                                                                                                                                                                                                                      - (
                                                                                                                                                                                                                                                            d3
                                                                                                                                                                                                                                                               - f3
                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                         / (
                                                                                                                                                                                                                                                            f2
                                                                                                                                                                                                                                                               - f3
                                                                                                                                                                                                                                                         );
                                                                                                                                                                                                                                                   d = d6;
                                                                                                                                                                                                                                                   if (!(
                                                                                                                                                                                                                                                      d6
                                                                                                                                                                                                                                                         < 0.01
                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                    {
                                                                                                                                                                                                                                                      break label2200;
                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                   label1340:
                                                                                                                                                                                                                                                   while (true) {
                                                                                                                                                                                                                                                      if (!iterator2.hasNext()
                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                       {
                                                                                                                                                                                                                                                         break label1809;
                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                      entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                                                      if (RenderUtils.acy(
                                                                                                                                                                                                                                                         entity2.getBoundingBox()
                                                                                                                                                                                                                                                            .stretch(
                                                                                                                                                                                                                                                               0.0,
                                                                                                                                                                                                                                                               2.0,
                                                                                                                                                                                                                                                               0.0
                                                                                                                                                                                                                                                            )
                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                       {
                                                                                                                                                                                                                                                         while (true) {
                                                                                                                                                                                                                                                            obj = entity2;
                                                                                                                                                                                                                                                            b = 0;
                                                                                                                                                                                                                                                            switch (entity2
                                                                                                                                                                                                                                                                  == null
                                                                                                                                                                                                                                                               ? -1
                                                                                                                                                                                                                                                               : (
                                                                                                                                                                                                                                                                  true
                                                                                                                                                                                                                                                                        && entity2 instanceof PlayerEntity
                                                                                                                                                                                                                                                                     ? 0
                                                                                                                                                                                                                                                                     : (
                                                                                                                                                                                                                                                                        true
                                                                                                                                                                                                                                                                              && entity2 instanceof ItemEntity
                                                                                                                                                                                                                                                                           ? 1
                                                                                                                                                                                                                                                                           : (
                                                                                                                                                                                                                                                                              true
                                                                                                                                                                                                                                                                                    && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                                                                 ? 2
                                                                                                                                                                                                                                                                                 : 3
                                                                                                                                                                                                                                                                           )
                                                                                                                                                                                                                                                                     )
                                                                                                                                                                                                                                                               )) {
                                                                                                                                                                                                                                                               case 0:
                                                                                                                                                                                                                                                                  break label1913;
                                                                                                                                                                                                                                                               case 1:
                                                                                                                                                                                                                                                                  break label1812;
                                                                                                                                                                                                                                                               case 2:
                                                                                                                                                                                                                                                                  obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                  if (flag2
                                                                                                                                                                                                                                                                     )
                                                                                                                                                                                                                                                                   {
                                                                                                                                                                                                                                                                     break label1782;
                                                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                                                                  label1316:
                                                                                                                                                                                                                                                                  while (true) {
                                                                                                                                                                                                                                                                     b = 3;
                                                                                                                                                                                                                                                                     switch (entity2
                                                                                                                                                                                                                                                                           == null
                                                                                                                                                                                                                                                                        ? -1
                                                                                                                                                                                                                                                                        : (
                                                                                                                                                                                                                                                                           ((
                                                                                                                                                                                                                                                                                 ((
                                                                                                                                                                                                                                                                                       (3)
                                                                                                                                                                                                                                                                                    ))
                                                                                                                                                                                                                                                                              ))
                                                                                                                                                                                                                                                                        )) {
                                                                                                                                                                                                                                                                        case 0:
                                                                                                                                                                                                                                                                           break label1913;
                                                                                                                                                                                                                                                                        case 1:
                                                                                                                                                                                                                                                                           obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                                                           if (flag
                                                                                                                                                                                                                                                                              )
                                                                                                                                                                                                                                                                            {
                                                                                                                                                                                                                                                                              break label1719;
                                                                                                                                                                                                                                                                           }

                                                                                                                                                                                                                                                                           while (true) {
                                                                                                                                                                                                                                                                              b = 2;
                                                                                                                                                                                                                                                                              switch (entity2
                                                                                                                                                                                                                                                                                    == null
                                                                                                                                                                                                                                                                                 ? -1
                                                                                                                                                                                                                                                                                 : (
                                                                                                                                                                                                                                                                                    ((
                                                                                                                                                                                                                                                                                          ((
                                                                                                                                                                                                                                                                                                true
                                                                                                                                                                                                                                                                                                      && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                                                                                   ? 2
                                                                                                                                                                                                                                                                                                   : 3
                                                                                                                                                                                                                                                                                             ))
                                                                                                                                                                                                                                                                                       ))
                                                                                                                                                                                                                                                                                 )) {
                                                                                                                                                                                                                                                                                 case 0:
                                                                                                                                                                                                                                                                                    obj2 = (PlayerEntity)entity2;
                                                                                                                                                                                                                                                                                    if ((PlayerEntity)entity2
                                                                                                                                                                                                                                                                                       != client.player
                                                                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                                                                     {
                                                                                                                                                                                                                                                                                       break label1508;
                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                    if (Freecam.gP()
                                                                                                                                                                                                                                                                                       .awg()
                                                                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                                                                     {
                                                                                                                                                                                                                                                                                       if (!((PlayerEntity)entity2)
                                                                                                                                                                                                                                                                                          .isAlive()
                                                                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                          n2 = 1288686448;
                                                                                                                                                                                                                                                                                          n4 = 2085595383;
                                                                                                                                                                                                                                                                                          n3 = 1101913537;
                                                                                                                                                                                                                                                                                          break label1816;
                                                                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                                                                       if ((Boolean)this.invisibles
                                                                                                                                                                                                                                                                                          .atx()
                                                                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                          n2 = 402713586;
                                                                                                                                                                                                                                                                                          n4 = 1229032215;
                                                                                                                                                                                                                                                                                          n3 = 1505520781;
                                                                                                                                                                                                                                                                                          break label1816;
                                                                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                                                                       if (!((PlayerEntity)entity2)
                                                                                                                                                                                                                                                                                          .isInvisible()
                                                                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                          obj3 = this.HS(
                                                                                                                                                                                                                                                                                             (PlayerEntity)entity2
                                                                                                                                                                                                                                                                                          );
                                                                                                                                                                                                                                                                                          obj4 = EntityBoxUtils.BA(
                                                                                                                                                                                                                                                                                             (PlayerEntity)entity2,
                                                                                                                                                                                                                                                                                             render3DContext.f
                                                                                                                                                                                                                                                                                          );
                                                                                                                                                                                                                                                                                          if (!Font.kT()
                                                                                                                                                                                                                                                                                             .awg()
                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                                                                             n2 = 51114926;
                                                                                                                                                                                                                                                                                             n4 = 865234895;
                                                                                                                                                                                                                                                                                             n3 = 1119319249;
                                                                                                                                                                                                                                                                                             break label1816;
                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                          f5 = FontLoader.fontRenderer2
                                                                                                                                                                                                                                                                                                .abc(
                                                                                                                                                                                                                                                                                                   (String)obj3
                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                             / 2.0F;
                                                                                                                                                                                                                                                                                          f4 = f5;
                                                                                                                                                                                                                                                                                          this.HL(
                                                                                                                                                                                                                                                                                             render3DContext.matrices,
                                                                                                                                                                                                                                                                                             (String)obj3,
                                                                                                                                                                                                                                                                                             f5
                                                                                                                                                                                                                                                                                                / 2.0F,
                                                                                                                                                                                                                                                                                             (PlayerEntity)entity2,
                                                                                                                                                                                                                                                                                             ((Vec3d)obj4)
                                                                                                                                                                                                                                                                                                .getX(),
                                                                                                                                                                                                                                                                                             ((Vec3d)obj4)
                                                                                                                                                                                                                                                                                                .getY(),
                                                                                                                                                                                                                                                                                             ((Vec3d)obj4)
                                                                                                                                                                                                                                                                                                .getZ(),
                                                                                                                                                                                                                                                                                             camera
                                                                                                                                                                                                                                                                                          );

                                                                                                                                                                                                                                                                                          label1164:
                                                                                                                                                                                                                                                                                          while (true) {
                                                                                                                                                                                                                                                                                             if (!iterator2.hasNext()
                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                              {
                                                                                                                                                                                                                                                                                                break label1809;
                                                                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                                                                             entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                                                                                             if (RenderUtils.acy(
                                                                                                                                                                                                                                                                                                entity2.getBoundingBox()
                                                                                                                                                                                                                                                                                                   .stretch(
                                                                                                                                                                                                                                                                                                      0.0,
                                                                                                                                                                                                                                                                                                      2.0,
                                                                                                                                                                                                                                                                                                      0.0
                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                              {
                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                   obj = entity2;
                                                                                                                                                                                                                                                                                                   b = 0;
                                                                                                                                                                                                                                                                                                   switch (entity2
                                                                                                                                                                                                                                                                                                         == null
                                                                                                                                                                                                                                                                                                      ? -1
                                                                                                                                                                                                                                                                                                      : (
                                                                                                                                                                                                                                                                                                         true
                                                                                                                                                                                                                                                                                                               && entity2 instanceof PlayerEntity
                                                                                                                                                                                                                                                                                                            ? 0
                                                                                                                                                                                                                                                                                                            : (
                                                                                                                                                                                                                                                                                                               true
                                                                                                                                                                                                                                                                                                                     && entity2 instanceof ItemEntity
                                                                                                                                                                                                                                                                                                                  ? 1
                                                                                                                                                                                                                                                                                                                  : (
                                                                                                                                                                                                                                                                                                                     true
                                                                                                                                                                                                                                                                                                                           && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                                                                                                        ? 2
                                                                                                                                                                                                                                                                                                                        : 3
                                                                                                                                                                                                                                                                                                                  )
                                                                                                                                                                                                                                                                                                            )
                                                                                                                                                                                                                                                                                                      )) {
                                                                                                                                                                                                                                                                                                      case 0:
                                                                                                                                                                                                                                                                                                         break label1913;
                                                                                                                                                                                                                                                                                                      case 1:
                                                                                                                                                                                                                                                                                                         break label1812;
                                                                                                                                                                                                                                                                                                      case 2:
                                                                                                                                                                                                                                                                                                         obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                                                         if (flag2
                                                                                                                                                                                                                                                                                                            )
                                                                                                                                                                                                                                                                                                          {
                                                                                                                                                                                                                                                                                                            break label1782;
                                                                                                                                                                                                                                                                                                         }

                                                                                                                                                                                                                                                                                                         label1139:
                                                                                                                                                                                                                                                                                                         while (true) {
                                                                                                                                                                                                                                                                                                            b = 3;
                                                                                                                                                                                                                                                                                                            switch (entity2
                                                                                                                                                                                                                                                                                                                  == null
                                                                                                                                                                                                                                                                                                               ? -1
                                                                                                                                                                                                                                                                                                               : (
                                                                                                                                                                                                                                                                                                                  ((
                                                                                                                                                                                                                                                                                                                        ((
                                                                                                                                                                                                                                                                                                                              (3)
                                                                                                                                                                                                                                                                                                                           ))
                                                                                                                                                                                                                                                                                                                     ))
                                                                                                                                                                                                                                                                                                               )) {
                                                                                                                                                                                                                                                                                                               case 0:
                                                                                                                                                                                                                                                                                                                  break label1913;
                                                                                                                                                                                                                                                                                                               case 1:
                                                                                                                                                                                                                                                                                                                  obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                                                                                                  if (flag
                                                                                                                                                                                                                                                                                                                     )
                                                                                                                                                                                                                                                                                                                   {
                                                                                                                                                                                                                                                                                                                     break label1719;
                                                                                                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                                                                                                                  while (true) {
                                                                                                                                                                                                                                                                                                                     b = 2;
                                                                                                                                                                                                                                                                                                                     switch (entity2
                                                                                                                                                                                                                                                                                                                           == null
                                                                                                                                                                                                                                                                                                                        ? -1
                                                                                                                                                                                                                                                                                                                        : (
                                                                                                                                                                                                                                                                                                                           ((
                                                                                                                                                                                                                                                                                                                                 ((
                                                                                                                                                                                                                                                                                                                                       true
                                                                                                                                                                                                                                                                                                                                             && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                                                                                                                          ? 2
                                                                                                                                                                                                                                                                                                                                          : 3
                                                                                                                                                                                                                                                                                                                                    ))
                                                                                                                                                                                                                                                                                                                              ))
                                                                                                                                                                                                                                                                                                                        )) {
                                                                                                                                                                                                                                                                                                                        case 0:
                                                                                                                                                                                                                                                                                                                           break label1913;
                                                                                                                                                                                                                                                                                                                        case 1:
                                                                                                                                                                                                                                                                                                                           obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                                                                                                           if (flag
                                                                                                                                                                                                                                                                                                                              )
                                                                                                                                                                                                                                                                                                                            {
                                                                                                                                                                                                                                                                                                                              d3 = client.player
                                                                                                                                                                                                                                                                                                                                 .distanceTo(
                                                                                                                                                                                                                                                                                                                                    entity2
                                                                                                                                                                                                                                                                                                                                 );
                                                                                                                                                                                                                                                                                                                              if (!(
                                                                                                                                                                                                                                                                                                                                 d3
                                                                                                                                                                                                                                                                                                                                    > f2
                                                                                                                                                                                                                                                                                                                              )
                                                                                                                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                                                                                                                               {
                                                                                                                                                                                                                                                                                                                                 if (!(
                                                                                                                                                                                                                                                                                                                                    d3
                                                                                                                                                                                                                                                                                                                                       > f3
                                                                                                                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                                                                                                                  {
                                                                                                                                                                                                                                                                                                                                    break label1783;
                                                                                                                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                                                                                                                 d6 = 1.0
                                                                                                                                                                                                                                                                                                                                    - (
                                                                                                                                                                                                                                                                                                                                          d3
                                                                                                                                                                                                                                                                                                                                             - f3
                                                                                                                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                                                                                                                       / (
                                                                                                                                                                                                                                                                                                                                          f2
                                                                                                                                                                                                                                                                                                                                             - f3
                                                                                                                                                                                                                                                                                                                                       );
                                                                                                                                                                                                                                                                                                                                 d = d6;
                                                                                                                                                                                                                                                                                                                                 if (!(
                                                                                                                                                                                                                                                                                                                                    d6
                                                                                                                                                                                                                                                                                                                                       < 0.01
                                                                                                                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                                                                                                                  {
                                                                                                                                                                                                                                                                                                                                    label1084:
                                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                                       vec2 = EntityBoxUtils.BA(
                                                                                                                                                                                                                                                                                                                                          (ItemEntity)entity2,
                                                                                                                                                                                                                                                                                                                                          render3DContext.f
                                                                                                                                                                                                                                                                                                                                       );
                                                                                                                                                                                                                                                                                                                                       stack = ((ItemEntity)entity2)
                                                                                                                                                                                                                                                                                                                                          .getStack();
                                                                                                                                                                                                                                                                                                                                       str = this.HK(
                                                                                                                                                                                                                                                                                                                                          stack,
                                                                                                                                                                                                                                                                                                                                          nametagsNoneDistanceMode,
                                                                                                                                                                                                                                                                                                                                          d3
                                                                                                                                                                                                                                                                                                                                       );
                                                                                                                                                                                                                                                                                                                                       n = 16777215
                                                                                                                                                                                                                                                                                                                                          | (int)(
                                                                                                                                                                                                                                                                                                                                                255.0
                                                                                                                                                                                                                                                                                                                                                   * d6
                                                                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                                                                             << 24;
                                                                                                                                                                                                                                                                                                                                       RenderUtils.acw(
                                                                                                                                                                                                                                                                                                                                          render3DContext.matrices,
                                                                                                                                                                                                                                                                                                                                          str,
                                                                                                                                                                                                                                                                                                                                          vec2.getX(),
                                                                                                                                                                                                                                                                                                                                          vec2.getY(),
                                                                                                                                                                                                                                                                                                                                          vec2.getZ(),
                                                                                                                                                                                                                                                                                                                                          n
                                                                                                                                                                                                                                                                                                                                       );

                                                                                                                                                                                                                                                                                                                                       label1082:
                                                                                                                                                                                                                                                                                                                                       while (true) {
                                                                                                                                                                                                                                                                                                                                          if (!iterator2.hasNext()
                                                                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                                                                                                                             break label1809;
                                                                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                                                                          entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                                                                                                                                          if (RenderUtils.acy(
                                                                                                                                                                                                                                                                                                                                             entity2.getBoundingBox()
                                                                                                                                                                                                                                                                                                                                                .stretch(
                                                                                                                                                                                                                                                                                                                                                   0.0,
                                                                                                                                                                                                                                                                                                                                                   2.0,
                                                                                                                                                                                                                                                                                                                                                   0.0
                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                                                                                                                             while (true) {
                                                                                                                                                                                                                                                                                                                                                obj = entity2;
                                                                                                                                                                                                                                                                                                                                                b = 0;
                                                                                                                                                                                                                                                                                                                                                switch (entity2
                                                                                                                                                                                                                                                                                                                                                      == null
                                                                                                                                                                                                                                                                                                                                                   ? -1
                                                                                                                                                                                                                                                                                                                                                   : (
                                                                                                                                                                                                                                                                                                                                                      true
                                                                                                                                                                                                                                                                                                                                                            && entity2 instanceof PlayerEntity
                                                                                                                                                                                                                                                                                                                                                         ? 0
                                                                                                                                                                                                                                                                                                                                                         : (
                                                                                                                                                                                                                                                                                                                                                            true
                                                                                                                                                                                                                                                                                                                                                                  && entity2 instanceof ItemEntity
                                                                                                                                                                                                                                                                                                                                                               ? 1
                                                                                                                                                                                                                                                                                                                                                               : (
                                                                                                                                                                                                                                                                                                                                                                  true
                                                                                                                                                                                                                                                                                                                                                                        && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                                                                                                                                                     ? 2
                                                                                                                                                                                                                                                                                                                                                                     : 3
                                                                                                                                                                                                                                                                                                                                                               )
                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                   )) {
                                                                                                                                                                                                                                                                                                                                                   case 0:
                                                                                                                                                                                                                                                                                                                                                      break label1913;
                                                                                                                                                                                                                                                                                                                                                   case 1:
                                                                                                                                                                                                                                                                                                                                                      break label1812;
                                                                                                                                                                                                                                                                                                                                                   case 2:
                                                                                                                                                                                                                                                                                                                                                      obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                                                                                                      if (flag2
                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                       {
                                                                                                                                                                                                                                                                                                                                                         break label1782;
                                                                                                                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                                                                                                                      label1057:
                                                                                                                                                                                                                                                                                                                                                      while (true) {
                                                                                                                                                                                                                                                                                                                                                         b = 3;
                                                                                                                                                                                                                                                                                                                                                         switch (entity2
                                                                                                                                                                                                                                                                                                                                                               == null
                                                                                                                                                                                                                                                                                                                                                            ? -1
                                                                                                                                                                                                                                                                                                                                                            : (
                                                                                                                                                                                                                                                                                                                                                               ((
                                                                                                                                                                                                                                                                                                                                                                     ((
                                                                                                                                                                                                                                                                                                                                                                           (3)
                                                                                                                                                                                                                                                                                                                                                                        ))
                                                                                                                                                                                                                                                                                                                                                                  ))
                                                                                                                                                                                                                                                                                                                                                            )) {
                                                                                                                                                                                                                                                                                                                                                            case 0:
                                                                                                                                                                                                                                                                                                                                                               break label1913;
                                                                                                                                                                                                                                                                                                                                                            case 1:
                                                                                                                                                                                                                                                                                                                                                               obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                                                                                                                                               if (flag
                                                                                                                                                                                                                                                                                                                                                                  )
                                                                                                                                                                                                                                                                                                                                                                {
                                                                                                                                                                                                                                                                                                                                                                  break label1719;
                                                                                                                                                                                                                                                                                                                                                               }

                                                                                                                                                                                                                                                                                                                                                               while (true) {
                                                                                                                                                                                                                                                                                                                                                                  b = 2;
                                                                                                                                                                                                                                                                                                                                                                  switch (entity2
                                                                                                                                                                                                                                                                                                                                                                        == null
                                                                                                                                                                                                                                                                                                                                                                     ? -1
                                                                                                                                                                                                                                                                                                                                                                     : (
                                                                                                                                                                                                                                                                                                                                                                        ((
                                                                                                                                                                                                                                                                                                                                                                              ((
                                                                                                                                                                                                                                                                                                                                                                                    true
                                                                                                                                                                                                                                                                                                                                                                                          && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                                                                                                                                                                       ? 2
                                                                                                                                                                                                                                                                                                                                                                                       : 3
                                                                                                                                                                                                                                                                                                                                                                                 ))
                                                                                                                                                                                                                                                                                                                                                                           ))
                                                                                                                                                                                                                                                                                                                                                                     )) {
                                                                                                                                                                                                                                                                                                                                                                     case 0:
                                                                                                                                                                                                                                                                                                                                                                        break label1913;
                                                                                                                                                                                                                                                                                                                                                                     case 1:
                                                                                                                                                                                                                                                                                                                                                                        obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                                                                                                                                                        if (flag
                                                                                                                                                                                                                                                                                                                                                                           )
                                                                                                                                                                                                                                                                                                                                                                         {
                                                                                                                                                                                                                                                                                                                                                                           d3 = client.player
                                                                                                                                                                                                                                                                                                                                                                              .distanceTo(
                                                                                                                                                                                                                                                                                                                                                                                 entity2
                                                                                                                                                                                                                                                                                                                                                                              );
                                                                                                                                                                                                                                                                                                                                                                           if (!(
                                                                                                                                                                                                                                                                                                                                                                              d3
                                                                                                                                                                                                                                                                                                                                                                                 > f2
                                                                                                                                                                                                                                                                                                                                                                           )
                                                                                                                                                                                                                                                                                                                                                                              )
                                                                                                                                                                                                                                                                                                                                                                            {
                                                                                                                                                                                                                                                                                                                                                                              if (!(
                                                                                                                                                                                                                                                                                                                                                                                 d3
                                                                                                                                                                                                                                                                                                                                                                                    > f3
                                                                                                                                                                                                                                                                                                                                                                              )
                                                                                                                                                                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                                                                                                                                                                               {
                                                                                                                                                                                                                                                                                                                                                                                 d6 = 1.0;
                                                                                                                                                                                                                                                                                                                                                                                 d = 1.0;
                                                                                                                                                                                                                                                                                                                                                                                 vec2 = EntityBoxUtils.BA(
                                                                                                                                                                                                                                                                                                                                                                                    (ItemEntity)entity2,
                                                                                                                                                                                                                                                                                                                                                                                    render3DContext.f
                                                                                                                                                                                                                                                                                                                                                                                 );
                                                                                                                                                                                                                                                                                                                                                                                 stack = ((ItemEntity)entity2)
                                                                                                                                                                                                                                                                                                                                                                                    .getStack();
                                                                                                                                                                                                                                                                                                                                                                                 str = this.HK(
                                                                                                                                                                                                                                                                                                                                                                                    stack,
                                                                                                                                                                                                                                                                                                                                                                                    nametagsNoneDistanceMode,
                                                                                                                                                                                                                                                                                                                                                                                    d3
                                                                                                                                                                                                                                                                                                                                                                                 );
                                                                                                                                                                                                                                                                                                                                                                                 n = 16777215
                                                                                                                                                                                                                                                                                                                                                                                    | (int)(
                                                                                                                                                                                                                                                                                                                                                                                          255.0
                                                                                                                                                                                                                                                                                                                                                                                             * 1.0
                                                                                                                                                                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                                                                                                                                                                       << 24;
                                                                                                                                                                                                                                                                                                                                                                                 RenderUtils.acw(
                                                                                                                                                                                                                                                                                                                                                                                    render3DContext.matrices,
                                                                                                                                                                                                                                                                                                                                                                                    str,
                                                                                                                                                                                                                                                                                                                                                                                    vec2.getX(),
                                                                                                                                                                                                                                                                                                                                                                                    vec2.getY(),
                                                                                                                                                                                                                                                                                                                                                                                    vec2.getZ(),
                                                                                                                                                                                                                                                                                                                                                                                    n
                                                                                                                                                                                                                                                                                                                                                                                 );

                                                                                                                                                                                                                                                                                                                                                                                 do {
                                                                                                                                                                                                                                                                                                                                                                                    if (!iterator2.hasNext()
                                                                                                                                                                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                                                                                                                                                                     {
                                                                                                                                                                                                                                                                                                                                                                                       break label1809;
                                                                                                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                                                                                                    entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                                                                                                                                                                                 } while (
                                                                                                                                                                                                                                                                                                                                                                                    !RenderUtils.acy(
                                                                                                                                                                                                                                                                                                                                                                                       entity2.getBoundingBox()
                                                                                                                                                                                                                                                                                                                                                                                          .stretch(
                                                                                                                                                                                                                                                                                                                                                                                             0.0,
                                                                                                                                                                                                                                                                                                                                                                                             2.0,
                                                                                                                                                                                                                                                                                                                                                                                             0.0
                                                                                                                                                                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                                                                                                                                                                 );

                                                                                                                                                                                                                                                                                                                                                                                 label974:
                                                                                                                                                                                                                                                                                                                                                                                 while (true) {
                                                                                                                                                                                                                                                                                                                                                                                    obj = entity2;
                                                                                                                                                                                                                                                                                                                                                                                    b = 0;
                                                                                                                                                                                                                                                                                                                                                                                    switch (entity2
                                                                                                                                                                                                                                                                                                                                                                                          == null
                                                                                                                                                                                                                                                                                                                                                                                       ? -1
                                                                                                                                                                                                                                                                                                                                                                                       : (
                                                                                                                                                                                                                                                                                                                                                                                          true
                                                                                                                                                                                                                                                                                                                                                                                                && entity2 instanceof PlayerEntity
                                                                                                                                                                                                                                                                                                                                                                                             ? 0
                                                                                                                                                                                                                                                                                                                                                                                             : (
                                                                                                                                                                                                                                                                                                                                                                                                true
                                                                                                                                                                                                                                                                                                                                                                                                      && entity2 instanceof ItemEntity
                                                                                                                                                                                                                                                                                                                                                                                                   ? 1
                                                                                                                                                                                                                                                                                                                                                                                                   : (
                                                                                                                                                                                                                                                                                                                                                                                                      true
                                                                                                                                                                                                                                                                                                                                                                                                            && entity2 instanceof EnderPearlEntity
                                                                                                                                                                                                                                                                                                                                                                                                         ? 2
                                                                                                                                                                                                                                                                                                                                                                                                         : 3
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                                                                                                                       )) {
                                                                                                                                                                                                                                                                                                                                                                                       case 0:
                                                                                                                                                                                                                                                                                                                                                                                          break label1913;
                                                                                                                                                                                                                                                                                                                                                                                       case 1:
                                                                                                                                                                                                                                                                                                                                                                                          break label1812;
                                                                                                                                                                                                                                                                                                                                                                                       case 2:
                                                                                                                                                                                                                                                                                                                                                                                          obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                                                                                                                                          if (flag2
                                                                                                                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                                                                                                                                                                             break label1782;
                                                                                                                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                                                                                                                          b = 3;
                                                                                                                                                                                                                                                                                                                                                                                          switch (entity2
                                                                                                                                                                                                                                                                                                                                                                                                == null
                                                                                                                                                                                                                                                                                                                                                                                             ? -1
                                                                                                                                                                                                                                                                                                                                                                                             : (
                                                                                                                                                                                                                                                                                                                                                                                                ((
                                                                                                                                                                                                                                                                                                                                                                                                      ((
                                                                                                                                                                                                                                                                                                                                                                                                            (3)
                                                                                                                                                                                                                                                                                                                                                                                                         ))
                                                                                                                                                                                                                                                                                                                                                                                                   ))
                                                                                                                                                                                                                                                                                                                                                                                             )) {
                                                                                                                                                                                                                                                                                                                                                                                             case 0:
                                                                                                                                                                                                                                                                                                                                                                                                break label1913;
                                                                                                                                                                                                                                                                                                                                                                                             case 1:
                                                                                                                                                                                                                                                                                                                                                                                                break label1812;
                                                                                                                                                                                                                                                                                                                                                                                             case 2:
                                                                                                                                                                                                                                                                                                                                                                                                break label1813;
                                                                                                                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                                                                                                                          while (
                                                                                                                                                                                                                                                                                                                                                                                             iterator2.hasNext()
                                                                                                                                                                                                                                                                                                                                                                                          ) {
                                                                                                                                                                                                                                                                                                                                                                                             entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                                                                                                                                                                                             if (RenderUtils.acy(
                                                                                                                                                                                                                                                                                                                                                                                                entity2.getBoundingBox()
                                                                                                                                                                                                                                                                                                                                                                                                   .stretch(
                                                                                                                                                                                                                                                                                                                                                                                                      0.0,
                                                                                                                                                                                                                                                                                                                                                                                                      2.0,
                                                                                                                                                                                                                                                                                                                                                                                                      0.0
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                              {
                                                                                                                                                                                                                                                                                                                                                                                                continue label974;
                                                                                                                                                                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                                                                                                                          int n5 = (1094500875);
                                                                                                                                                                                                                                                                                                                                                                                          int n6 = (503989554 + -1959879283 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + 164156575
                                                                                                                                                                                                                                                                                                                                                                                                * ~(
                                                                                                                                                                                                                                                                                                                                                                                                   n5
                                                                                                                                                                                                                                                                                                                                                                                                      | 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                      | n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1853111567
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   1810671603
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                            0
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            1810671603
                                                                                                                                                                                                                                                                                                                                                                                                               ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1821565449
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~1810671603
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                         & 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                      ^ 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                      ^ 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                      ^ 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (1094845922 + 1094845922 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + -620290281
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                            | 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (n5)
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-942140985 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + -1971330922
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   0
                                                                                                                                                                                                                                                                                                                                                                                                      | 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                         ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         ^ ~1810671603
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 1555242573
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~(
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                            ^ 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      | (
                                                                                                                                                                                                                                                                                                                                                                                                            1810671603
                                                                                                                                                                                                                                                                                                                                                                                                               ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 722456580
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         (-1 + -(n5))
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                            | 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 298726170
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (-1)
                                                                                                                                                                                                                                                                                                                                                                                                      | (n5)
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ 1810671603
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                                                                                                                                                                          n3 = (1874030841);
                                                                                                                                                                                                                                                                                                                                                                                          n6 ^= 1484170031;
                                                                                                                                                                                                                                                                                                                                                                                          client.getBufferBuilders()
                                                                                                                                                                                                                                                                                                                                                                                             .getEntityVertexConsumers()
                                                                                                                                                                                                                                                                                                                                                                                             .draw();
                                                                                                                                                                                                                                                                                                                                                                                          n3 *= -1568354605;
                                                                                                                                                                                                                                                                                                                                                                                          n5 = (1094500875);
                                                                                                                                                                                                                                                                                                                                                                                          n6 = (503989554 + -1959879283 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + 164156575
                                                                                                                                                                                                                                                                                                                                                                                                * ~(
                                                                                                                                                                                                                                                                                                                                                                                                   n5
                                                                                                                                                                                                                                                                                                                                                                                                      | -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                      | n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1853111567
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                            0
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                               ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1821565449
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~-1062552091
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                         & -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                      ^ -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                      ^ -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                      ^ -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (1094845922 + 1094845922 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + -620290281
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                            | -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (n5)
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-942140985 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + -1971330922
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   0
                                                                                                                                                                                                                                                                                                                                                                                                      | -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                         ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         ^ ~-1062552091
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 1555242573
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~(
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                            ^ -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      | (
                                                                                                                                                                                                                                                                                                                                                                                                            -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                               ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 722456580
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         (-1 + -(n5))
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                            | -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 298726170
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (-1)
                                                                                                                                                                                                                                                                                                                                                                                                      | (n5)
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ -1062552091
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                                                                                                                                                                          int n7 = (1211024471 * n3);
                                                                                                                                                                                                                                                                                                                                                                                          n6 ^= n3;
                                                                                                                                                                                                                                                                                                                                                                                          client.getBufferBuilders()
                                                                                                                                                                                                                                                                                                                                                                                             .getEffectVertexConsumers()
                                                                                                                                                                                                                                                                                                                                                                                             .draw();
                                                                                                                                                                                                                                                                                                                                                                                          n3 = (1854535763 * n7);
                                                                                                                                                                                                                                                                                                                                                                                          n5 = (-286718117);
                                                                                                                                                                                                                                                                                                                                                                                          n6 = 1045335236
                                                                                                                                                                                                                                                                                                                                                                                             + -354220887
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   1427263368
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         (-1427263369)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 355536188
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~(
                                                                                                                                                                                                                                                                                                                                                                                                         1427263368
                                                                                                                                                                                                                                                                                                                                                                                                            & n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ 0
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -443776416
                                                                                                                                                                                                                                                                                                                                                                                                * ~(
                                                                                                                                                                                                                                                                                                                                                                                                   ~1427263368
                                                                                                                                                                                                                                                                                                                                                                                                      & n5
                                                                                                                                                                                                                                                                                                                                                                                                      & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 2117481317
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   n5
                                                                                                                                                                                                                                                                                                                                                                                                      & ~1427263368
                                                                                                                                                                                                                                                                                                                                                                                                      & (
                                                                                                                                                                                                                                                                                                                                                                                                         (1427263368)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1593809646
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         ~n5
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ 1427263368
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      & (
                                                                                                                                                                                                                                                                                                                                                                                                         (1427263368)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-369645001)
                                                                                                                                                                                                                                                                                                                                                                                             + 2038650952
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   1427263368
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                      | n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (467481384)
                                                                                                                                                                                                                                                                                                                                                                                             + 794005218
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   1427263368
                                                                                                                                                                                                                                                                                                                                                                                                      | ~(
                                                                                                                                                                                                                                                                                                                                                                                                         1427263368
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-1446811958 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + 794251131
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~1427263368
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ 1427263368
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         1427263368
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-1390082209 + -1390082209 * n5);
                                                                                                                                                                                                                                                                                                                                                                                          int n8 = (1211024471 * n3);
                                                                                                                                                                                                                                                                                                                                                                                          n6 ^= n3;
                                                                                                                                                                                                                                                                                                                                                                                          FontLoader.fontRenderer2
                                                                                                                                                                                                                                                                                                                                                                                             .aba();
                                                                                                                                                                                                                                                                                                                                                                                          n3 = (-1258219087 * n8);
                                                                                                                                                                                                                                                                                                                                                                                          n5 = (-1140225840);
                                                                                                                                                                                                                                                                                                                                                                                          n6 = -2084167185
                                                                                                                                                                                                                                                                                                                                                                                             + 435138673
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                               ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                            | 0
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      & (-1 + -(n5))
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 636212337
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (-1917581857)
                                                                                                                                                                                                                                                                                                                                                                                                      | (-1 + -(n5))
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 1748811298
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                      ^ ~n5
                                                                                                                                                                                                                                                                                                                                                                                                         & ~-1917581857
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1401999914
                                                                                                                                                                                                                                                                                                                                                                                                * ~(
                                                                                                                                                                                                                                                                                                                                                                                                   -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                      & n5
                                                                                                                                                                                                                                                                                                                                                                                                      & (
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                            ^ -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -255936316
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                            (-1917581857)
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         ^ -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                            & n5
                                                                                                                                                                                                                                                                                                                                                                                                      | -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                      | -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                      | -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1977719700
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   0
                                                                                                                                                                                                                                                                                                                                                                                                      | n5
                                                                                                                                                                                                                                                                                                                                                                                                         & -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 687982717
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                            n5
                                                                                                                                                                                                                                                                                                                                                                                                               | -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      | (n5)
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 423162320
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                      | n5
                                                                                                                                                                                                                                                                                                                                                                                                      | -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1892340969
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~-1917581857
                                                                                                                                                                                                                                                                                                                                                                                                      | (
                                                                                                                                                                                                                                                                                                                                                                                                            -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                               ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-1882108924 + -1882108924 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + (1042353229)
                                                                                                                                                                                                                                                                                                                                                                                             + -1138979773
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (n5)
                                                                                                                                                                                                                                                                                                                                                                                                      | -1917581857
                                                                                                                                                                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                                                                                                                                                                          int n9 = (1211024471 * n3);
                                                                                                                                                                                                                                                                                                                                                                                          n6 ^= n3;
                                                                                                                                                                                                                                                                                                                                                                                          RenderSystem.polygonOffset(
                                                                                                                                                                                                                                                                                                                                                                                             1.0F,
                                                                                                                                                                                                                                                                                                                                                                                             2000000.0F
                                                                                                                                                                                                                                                                                                                                                                                          );
                                                                                                                                                                                                                                                                                                                                                                                          n3 = (-1999174511 * n9);
                                                                                                                                                                                                                                                                                                                                                                                          n5 = (-286718117);
                                                                                                                                                                                                                                                                                                                                                                                          n6 = 1045335236
                                                                                                                                                                                                                                                                                                                                                                                             + -354220887
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   1423199676
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         (-1423199677)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 355536188
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~(
                                                                                                                                                                                                                                                                                                                                                                                                         1423199676
                                                                                                                                                                                                                                                                                                                                                                                                            & n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ 0
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -443776416
                                                                                                                                                                                                                                                                                                                                                                                                * ~(
                                                                                                                                                                                                                                                                                                                                                                                                   ~1423199676
                                                                                                                                                                                                                                                                                                                                                                                                      & n5
                                                                                                                                                                                                                                                                                                                                                                                                      & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 2117481317
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   n5
                                                                                                                                                                                                                                                                                                                                                                                                      & ~1423199676
                                                                                                                                                                                                                                                                                                                                                                                                      & (
                                                                                                                                                                                                                                                                                                                                                                                                         (1423199676)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1593809646
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         ~n5
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ 1423199676
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      & (
                                                                                                                                                                                                                                                                                                                                                                                                         (1423199676)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (1424838403)
                                                                                                                                                                                                                                                                                                                                                                                             + 2038650952
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   1423199676
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                      | n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-932709908)
                                                                                                                                                                                                                                                                                                                                                                                             + 794005218
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   1423199676
                                                                                                                                                                                                                                                                                                                                                                                                      | ~(
                                                                                                                                                                                                                                                                                                                                                                                                         1423199676
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-1446811958 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + 794251131
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~1423199676
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ 1423199676
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         1423199676
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-1390082209 + -1390082209 * n5);
                                                                                                                                                                                                                                                                                                                                                                                          int n10 = (1211024471 * n3);
                                                                                                                                                                                                                                                                                                                                                                                          n6 ^= n3;
                                                                                                                                                                                                                                                                                                                                                                                          RenderSystem.disablePolygonOffset();
                                                                                                                                                                                                                                                                                                                                                                                          n3 = (849859683 * n10);
                                                                                                                                                                                                                                                                                                                                                                                          n5 = (-286718117);
                                                                                                                                                                                                                                                                                                                                                                                          n6 = 1045335236
                                                                                                                                                                                                                                                                                                                                                                                             + -354220887
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -1478358422
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         (1478358421)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 355536188
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~(
                                                                                                                                                                                                                                                                                                                                                                                                         -1478358422
                                                                                                                                                                                                                                                                                                                                                                                                            & n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ 0
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -443776416
                                                                                                                                                                                                                                                                                                                                                                                                * ~(
                                                                                                                                                                                                                                                                                                                                                                                                   ~-1478358422
                                                                                                                                                                                                                                                                                                                                                                                                      & n5
                                                                                                                                                                                                                                                                                                                                                                                                      & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 2117481317
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   n5
                                                                                                                                                                                                                                                                                                                                                                                                      & ~-1478358422
                                                                                                                                                                                                                                                                                                                                                                                                      & (
                                                                                                                                                                                                                                                                                                                                                                                                         (-1478358422)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1593809646
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         ~n5
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ -1478358422
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      & (
                                                                                                                                                                                                                                                                                                                                                                                                         (-1478358422)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (1053142741)
                                                                                                                                                                                                                                                                                                                                                                                             + 2038650952
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -1478358422
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                      | n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (1262490354)
                                                                                                                                                                                                                                                                                                                                                                                             + 794005218
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -1478358422
                                                                                                                                                                                                                                                                                                                                                                                                      | ~(
                                                                                                                                                                                                                                                                                                                                                                                                         -1478358422
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-1446811958 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + 794251131
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~-1478358422
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ -1478358422
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         -1478358422
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-1390082209 + -1390082209 * n5);
                                                                                                                                                                                                                                                                                                                                                                                          int n11 = (1211024471 * n3);
                                                                                                                                                                                                                                                                                                                                                                                          n6 ^= n3;
                                                                                                                                                                                                                                                                                                                                                                                          RenderSystem.enableDepthTest();
                                                                                                                                                                                                                                                                                                                                                                                          n3 = (-1212443893 * n11);
                                                                                                                                                                                                                                                                                                                                                                                          n5 = (1094500875);
                                                                                                                                                                                                                                                                                                                                                                                          n6 = (503989554 + -1959879283 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + 164156575
                                                                                                                                                                                                                                                                                                                                                                                                * ~(
                                                                                                                                                                                                                                                                                                                                                                                                   n5
                                                                                                                                                                                                                                                                                                                                                                                                      | -335694807
                                                                                                                                                                                                                                                                                                                                                                                                      | n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1853111567
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -335694807
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                            0
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            -335694807
                                                                                                                                                                                                                                                                                                                                                                                                               ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1821565449
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~-335694807
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                         & -335694807
                                                                                                                                                                                                                                                                                                                                                                                                      ^ -335694807
                                                                                                                                                                                                                                                                                                                                                                                                      ^ -335694807
                                                                                                                                                                                                                                                                                                                                                                                                      ^ -335694807
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (1094845922 + 1094845922 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + -620290281
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                            | -335694807
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ -335694807
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (n5)
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-942140985 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + -1971330922
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   0
                                                                                                                                                                                                                                                                                                                                                                                                      | -335694807
                                                                                                                                                                                                                                                                                                                                                                                                         ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         ^ ~-335694807
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 1555242573
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~(
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                            ^ -335694807
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      | (
                                                                                                                                                                                                                                                                                                                                                                                                            -335694807
                                                                                                                                                                                                                                                                                                                                                                                                               ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 722456580
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         (-1 + -(n5))
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ -335694807
                                                                                                                                                                                                                                                                                                                                                                                                            | -335694807
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 298726170
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (-1)
                                                                                                                                                                                                                                                                                                                                                                                                      | (n5)
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ -335694807
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                                                                                                                                                                          int n12 = (1211024471 * n3);
                                                                                                                                                                                                                                                                                                                                                                                          n6 ^= n3;
                                                                                                                                                                                                                                                                                                                                                                                          RenderSystem.setShaderLights(
                                                                                                                                                                                                                                                                                                                                                                                             vector3f,
                                                                                                                                                                                                                                                                                                                                                                                             vector3f2
                                                                                                                                                                                                                                                                                                                                                                                          );
                                                                                                                                                                                                                                                                                                                                                                                          n3 = (-1029112397 * n12);
                                                                                                                                                                                                                                                                                                                                                                                          n5 = (-286718117);
                                                                                                                                                                                                                                                                                                                                                                                          n6 = 1045335236
                                                                                                                                                                                                                                                                                                                                                                                             + -354220887
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -632784026
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         (632784025)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 355536188
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~(
                                                                                                                                                                                                                                                                                                                                                                                                         -632784026
                                                                                                                                                                                                                                                                                                                                                                                                            & n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ 0
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -443776416
                                                                                                                                                                                                                                                                                                                                                                                                * ~(
                                                                                                                                                                                                                                                                                                                                                                                                   ~-632784026
                                                                                                                                                                                                                                                                                                                                                                                                      & n5
                                                                                                                                                                                                                                                                                                                                                                                                      & n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + 2117481317
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   n5
                                                                                                                                                                                                                                                                                                                                                                                                      & ~-632784026
                                                                                                                                                                                                                                                                                                                                                                                                      & (
                                                                                                                                                                                                                                                                                                                                                                                                         (-632784026)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + -1593809646
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   (
                                                                                                                                                                                                                                                                                                                                                                                                         ~n5
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ -632784026
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      & (
                                                                                                                                                                                                                                                                                                                                                                                                         (-632784026)
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (1225723609)
                                                                                                                                                                                                                                                                                                                                                                                             + 2038650952
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -632784026
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                      | n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-2082752226)
                                                                                                                                                                                                                                                                                                                                                                                             + 794005218
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   -632784026
                                                                                                                                                                                                                                                                                                                                                                                                      | ~(
                                                                                                                                                                                                                                                                                                                                                                                                         -632784026
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-1446811958 * n5)
                                                                                                                                                                                                                                                                                                                                                                                             + 794251131
                                                                                                                                                                                                                                                                                                                                                                                                * (
                                                                                                                                                                                                                                                                                                                                                                                                   ~-632784026
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            n5
                                                                                                                                                                                                                                                                                                                                                                                                               ^ -632784026
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ (
                                                                                                                                                                                                                                                                                                                                                                                                         -632784026
                                                                                                                                                                                                                                                                                                                                                                                                            | n5
                                                                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                                                                      ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                             + (-1390082209 + -1390082209 * n5);
                                                                                                                                                                                                                                                                                                                                                                                          int n13 = (1211024471 * n3);
                                                                                                                                                                                                                                                                                                                                                                                          n6 ^= n3;
                                                                                                                                                                                                                                                                                                                                                                                          if ((Boolean)this.sounds
                                                                                                                                                                                                                                                                                                                                                                                             .atx()
                                                                                                                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                                                                                                                                                                             n3 = (-262505149 * n13);
                                                                                                                                                                                                                                                                                                                                                                                             n5 = (-286718117);
                                                                                                                                                                                                                                                                                                                                                                                             n6 = 1045335236
                                                                                                                                                                                                                                                                                                                                                                                                + -354220887
                                                                                                                                                                                                                                                                                                                                                                                                   * (
                                                                                                                                                                                                                                                                                                                                                                                                      733384150
                                                                                                                                                                                                                                                                                                                                                                                                         ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                         ^ (
                                                                                                                                                                                                                                                                                                                                                                                                            (-733384151)
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                                + 355536188
                                                                                                                                                                                                                                                                                                                                                                                                   * (
                                                                                                                                                                                                                                                                                                                                                                                                      ~(
                                                                                                                                                                                                                                                                                                                                                                                                            733384150
                                                                                                                                                                                                                                                                                                                                                                                                               & n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         ^ 0
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                                + -443776416
                                                                                                                                                                                                                                                                                                                                                                                                   * ~(
                                                                                                                                                                                                                                                                                                                                                                                                      ~733384150
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                         & n5
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                                + 2117481317
                                                                                                                                                                                                                                                                                                                                                                                                   * (
                                                                                                                                                                                                                                                                                                                                                                                                      n5
                                                                                                                                                                                                                                                                                                                                                                                                         & ~733384150
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            (733384150)
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                                + -1593809646
                                                                                                                                                                                                                                                                                                                                                                                                   * (
                                                                                                                                                                                                                                                                                                                                                                                                      (
                                                                                                                                                                                                                                                                                                                                                                                                            ~n5
                                                                                                                                                                                                                                                                                                                                                                                                               | n5
                                                                                                                                                                                                                                                                                                                                                                                                                  ^ 733384150
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         & (
                                                                                                                                                                                                                                                                                                                                                                                                            (733384150)
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                                + (-367878039)
                                                                                                                                                                                                                                                                                                                                                                                                + 2038650952
                                                                                                                                                                                                                                                                                                                                                                                                   * (
                                                                                                                                                                                                                                                                                                                                                                                                      733384150
                                                                                                                                                                                                                                                                                                                                                                                                            & n5
                                                                                                                                                                                                                                                                                                                                                                                                         | n5
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                                + (-1314958770)
                                                                                                                                                                                                                                                                                                                                                                                                + 794005218
                                                                                                                                                                                                                                                                                                                                                                                                   * (
                                                                                                                                                                                                                                                                                                                                                                                                      733384150
                                                                                                                                                                                                                                                                                                                                                                                                         | ~(
                                                                                                                                                                                                                                                                                                                                                                                                            733384150
                                                                                                                                                                                                                                                                                                                                                                                                               | n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                                + (-1446811958 * n5)
                                                                                                                                                                                                                                                                                                                                                                                                + 794251131
                                                                                                                                                                                                                                                                                                                                                                                                   * (
                                                                                                                                                                                                                                                                                                                                                                                                      ~733384150
                                                                                                                                                                                                                                                                                                                                                                                                            & (
                                                                                                                                                                                                                                                                                                                                                                                                               n5
                                                                                                                                                                                                                                                                                                                                                                                                                  ^ 733384150
                                                                                                                                                                                                                                                                                                                                                                                                            )
                                                                                                                                                                                                                                                                                                                                                                                                         ^ (
                                                                                                                                                                                                                                                                                                                                                                                                            733384150
                                                                                                                                                                                                                                                                                                                                                                                                               | n5
                                                                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                                                                         ^ n5
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                                + (-1390082209 + -1390082209 * n5);
                                                                                                                                                                                                                                                                                                                                                                                             int n14 = (1211024471 * n3);
                                                                                                                                                                                                                                                                                                                                                                                             n6 ^= n3;
                                                                                                                                                                                                                                                                                                                                                                                             l = System.currentTimeMillis();
                                                                                                                                                                                                                                                                                                                                                                                             n6 = (-432810743 * n14);
                                                                                                                                                                                                                                                                                                                                                                                             n2 = 419852596;
                                                                                                                                                                                                                                                                                                                                                                                             n4 = 1438376296;
                                                                                                                                                                                                                                                                                                                                                                                             n3 = n6;
                                                                                                                                                                                                                                                                                                                                                                                          } else {
                                                                                                                                                                                                                                                                                                                                                                                             n2 = 500448228;
                                                                                                                                                                                                                                                                                                                                                                                             n4 = 880847463;
                                                                                                                                                                                                                                                                                                                                                                                             n3 = 1550367899;
                                                                                                                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                                                                                                                          break label1816;
                                                                                                                                                                                                                                                                                                                                                                                       default:
                                                                                                                                                                                                                                                                                                                                                                                          while (true) {
                                                                                                                                                                                                                                                                                                                                                                                             if (!iterator2.hasNext()
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                              {
                                                                                                                                                                                                                                                                                                                                                                                                break label1912;
                                                                                                                                                                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                                                                                                                                                                             entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                                                                                                                                                                                             if (RenderUtils.acy(
                                                                                                                                                                                                                                                                                                                                                                                                entity2.getBoundingBox()
                                                                                                                                                                                                                                                                                                                                                                                                   .stretch(
                                                                                                                                                                                                                                                                                                                                                                                                      0.0,
                                                                                                                                                                                                                                                                                                                                                                                                      2.0,
                                                                                                                                                                                                                                                                                                                                                                                                      0.0
                                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                              {
                                                                                                                                                                                                                                                                                                                                                                                                break;
                                                                                                                                                                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                                                                                                                                                                              }

                                                                                                                                                                                                                                                                                                                                                                              d6 = 1.0
                                                                                                                                                                                                                                                                                                                                                                                 - (
                                                                                                                                                                                                                                                                                                                                                                                       d3
                                                                                                                                                                                                                                                                                                                                                                                          - f3
                                                                                                                                                                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                                                                                                                                                                    / (
                                                                                                                                                                                                                                                                                                                                                                                       f2
                                                                                                                                                                                                                                                                                                                                                                                          - f3
                                                                                                                                                                                                                                                                                                                                                                                    );
                                                                                                                                                                                                                                                                                                                                                                              d = d6;
                                                                                                                                                                                                                                                                                                                                                                              if (!(
                                                                                                                                                                                                                                                                                                                                                                                 d6
                                                                                                                                                                                                                                                                                                                                                                                    < 0.01
                                                                                                                                                                                                                                                                                                                                                                              )
                                                                                                                                                                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                                                                                                                                                                               {
                                                                                                                                                                                                                                                                                                                                                                                 continue label1084;
                                                                                                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                                                                                                           }
                                                                                                                                                                                                                                                                                                                                                                           continue label1082;
                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                        break;
                                                                                                                                                                                                                                                                                                                                                                     case 2:
                                                                                                                                                                                                                                                                                                                                                                        obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                                                                                                                        if (flag2
                                                                                                                                                                                                                                                                                                                                                                           )
                                                                                                                                                                                                                                                                                                                                                                         {
                                                                                                                                                                                                                                                                                                                                                                           entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                                                              .getOwner();
                                                                                                                                                                                                                                                                                                                                                                           if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                                                                                                                                                              )
                                                                                                                                                                                                                                                                                                                                                                            {
                                                                                                                                                                                                                                                                                                                                                                              player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                                                                                                                                                              vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                                                                                                                                                                 (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                                                                                                                                                                 render3DContext.f
                                                                                                                                                                                                                                                                                                                                                                              );
                                                                                                                                                                                                                                                                                                                                                                              d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                                                                    .getX()
                                                                                                                                                                                                                                                                                                                                                                                 - vec.getX();
                                                                                                                                                                                                                                                                                                                                                                              d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                                                                    .getY()
                                                                                                                                                                                                                                                                                                                                                                                 - vec.getY();
                                                                                                                                                                                                                                                                                                                                                                              d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                                                                    .getZ()
                                                                                                                                                                                                                                                                                                                                                                                 - vec.getZ();
                                                                                                                                                                                                                                                                                                                                                                              RenderUtils.acw(
                                                                                                                                                                                                                                                                                                                                                                                 render3DContext.matrices,
                                                                                                                                                                                                                                                                                                                                                                                 ((PlayerEntity)entity3)
                                                                                                                                                                                                                                                                                                                                                                                    .getName()
                                                                                                                                                                                                                                                                                                                                                                                    .getString(),
                                                                                                                                                                                                                                                                                                                                                                                 d4,
                                                                                                                                                                                                                                                                                                                                                                                 d5,
                                                                                                                                                                                                                                                                                                                                                                                 d2,
                                                                                                                                                                                                                                                                                                                                                                                 this.HU(
                                                                                                                                                                                                                                                                                                                                                                                    (PlayerEntity)entity3
                                                                                                                                                                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                                                                                                                                                                              );
                                                                                                                                                                                                                                                                                                                                                                           }
                                                                                                                                                                                                                                                                                                                                                                           continue label1082;
                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                        continue label1057;
                                                                                                                                                                                                                                                                                                                                                                     default:
                                                                                                                                                                                                                                                                                                                                                                        continue label1082;
                                                                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                                                                               }
                                                                                                                                                                                                                                                                                                                                                            case 2:
                                                                                                                                                                                                                                                                                                                                                               obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                                                                                                               if (flag2
                                                                                                                                                                                                                                                                                                                                                                  )
                                                                                                                                                                                                                                                                                                                                                                {
                                                                                                                                                                                                                                                                                                                                                                  entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                                                     .getOwner();
                                                                                                                                                                                                                                                                                                                                                                  if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                                                                                                                                                     )
                                                                                                                                                                                                                                                                                                                                                                   {
                                                                                                                                                                                                                                                                                                                                                                     player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                                                                                                                                                     vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                                                                                                                                                        (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                                                                                                                                                        render3DContext.f
                                                                                                                                                                                                                                                                                                                                                                     );
                                                                                                                                                                                                                                                                                                                                                                     d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                                                           .getX()
                                                                                                                                                                                                                                                                                                                                                                        - vec.getX();
                                                                                                                                                                                                                                                                                                                                                                     d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                                                           .getY()
                                                                                                                                                                                                                                                                                                                                                                        - vec.getY();
                                                                                                                                                                                                                                                                                                                                                                     d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                                                           .getZ()
                                                                                                                                                                                                                                                                                                                                                                        - vec.getZ();
                                                                                                                                                                                                                                                                                                                                                                     RenderUtils.acw(
                                                                                                                                                                                                                                                                                                                                                                        render3DContext.matrices,
                                                                                                                                                                                                                                                                                                                                                                        ((PlayerEntity)entity3)
                                                                                                                                                                                                                                                                                                                                                                           .getName()
                                                                                                                                                                                                                                                                                                                                                                           .getString(),
                                                                                                                                                                                                                                                                                                                                                                        d4,
                                                                                                                                                                                                                                                                                                                                                                        d5,
                                                                                                                                                                                                                                                                                                                                                                        d2,
                                                                                                                                                                                                                                                                                                                                                                        this.HU(
                                                                                                                                                                                                                                                                                                                                                                           (PlayerEntity)entity3
                                                                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                                                                     );
                                                                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                                                                                  break label1057;
                                                                                                                                                                                                                                                                                                                                                               }
                                                                                                                                                                                                                                                                                                                                                               break;
                                                                                                                                                                                                                                                                                                                                                            default:
                                                                                                                                                                                                                                                                                                                                                               break label1057;
                                                                                                                                                                                                                                                                                                                                                         }
                                                                                                                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                do {
                                                                                                                                                                                                                                                                                                                                                   if (!iterator2.hasNext()
                                                                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                                                                    {
                                                                                                                                                                                                                                                                                                                                                      break label1912;
                                                                                                                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                                                                                                                   entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                                                                                                                                                } while (
                                                                                                                                                                                                                                                                                                                                                   !RenderUtils.acy(
                                                                                                                                                                                                                                                                                                                                                      entity2.getBoundingBox()
                                                                                                                                                                                                                                                                                                                                                         .stretch(
                                                                                                                                                                                                                                                                                                                                                            0.0,
                                                                                                                                                                                                                                                                                                                                                            2.0,
                                                                                                                                                                                                                                                                                                                                                            0.0
                                                                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                                                              continue label1164;
                                                                                                                                                                                                                                                                                                                           }
                                                                                                                                                                                                                                                                                                                           break;
                                                                                                                                                                                                                                                                                                                        case 2:
                                                                                                                                                                                                                                                                                                                           obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                                                                           if (flag2
                                                                                                                                                                                                                                                                                                                              )
                                                                                                                                                                                                                                                                                                                            {
                                                                                                                                                                                                                                                                                                                              entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                 .getOwner();
                                                                                                                                                                                                                                                                                                                              if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                                                                                                                               {
                                                                                                                                                                                                                                                                                                                                 player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                                                                                                                 vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                                                                                                                    (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                                                                                                                    render3DContext.f
                                                                                                                                                                                                                                                                                                                                 );
                                                                                                                                                                                                                                                                                                                                 d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                       .getX()
                                                                                                                                                                                                                                                                                                                                    - vec.getX();
                                                                                                                                                                                                                                                                                                                                 d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                       .getY()
                                                                                                                                                                                                                                                                                                                                    - vec.getY();
                                                                                                                                                                                                                                                                                                                                 d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                                       .getZ()
                                                                                                                                                                                                                                                                                                                                    - vec.getZ();
                                                                                                                                                                                                                                                                                                                                 RenderUtils.acw(
                                                                                                                                                                                                                                                                                                                                    render3DContext.matrices,
                                                                                                                                                                                                                                                                                                                                    ((PlayerEntity)entity3)
                                                                                                                                                                                                                                                                                                                                       .getName()
                                                                                                                                                                                                                                                                                                                                       .getString(),
                                                                                                                                                                                                                                                                                                                                    d4,
                                                                                                                                                                                                                                                                                                                                    d5,
                                                                                                                                                                                                                                                                                                                                    d2,
                                                                                                                                                                                                                                                                                                                                    this.HU(
                                                                                                                                                                                                                                                                                                                                       (PlayerEntity)entity3
                                                                                                                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                                                                                                                 );
                                                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                                                              continue label1164;
                                                                                                                                                                                                                                                                                                                           }
                                                                                                                                                                                                                                                                                                                           continue label1139;
                                                                                                                                                                                                                                                                                                                        default:
                                                                                                                                                                                                                                                                                                                           continue label1164;
                                                                                                                                                                                                                                                                                                                     }
                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                               case 2:
                                                                                                                                                                                                                                                                                                                  obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                                                                  if (flag2
                                                                                                                                                                                                                                                                                                                     )
                                                                                                                                                                                                                                                                                                                   {
                                                                                                                                                                                                                                                                                                                     entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                        .getOwner();
                                                                                                                                                                                                                                                                                                                     if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                      {
                                                                                                                                                                                                                                                                                                                        player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                                                                                                        vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                                                                                                           (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                                                                                                           render3DContext.f
                                                                                                                                                                                                                                                                                                                        );
                                                                                                                                                                                                                                                                                                                        d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                              .getX()
                                                                                                                                                                                                                                                                                                                           - vec.getX();
                                                                                                                                                                                                                                                                                                                        d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                              .getY()
                                                                                                                                                                                                                                                                                                                           - vec.getY();
                                                                                                                                                                                                                                                                                                                        d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                                              .getZ()
                                                                                                                                                                                                                                                                                                                           - vec.getZ();
                                                                                                                                                                                                                                                                                                                        RenderUtils.acw(
                                                                                                                                                                                                                                                                                                                           render3DContext.matrices,
                                                                                                                                                                                                                                                                                                                           ((PlayerEntity)entity3)
                                                                                                                                                                                                                                                                                                                              .getName()
                                                                                                                                                                                                                                                                                                                              .getString(),
                                                                                                                                                                                                                                                                                                                           d4,
                                                                                                                                                                                                                                                                                                                           d5,
                                                                                                                                                                                                                                                                                                                           d2,
                                                                                                                                                                                                                                                                                                                           this.HU(
                                                                                                                                                                                                                                                                                                                              (PlayerEntity)entity3
                                                                                                                                                                                                                                                                                                                           )
                                                                                                                                                                                                                                                                                                                        );
                                                                                                                                                                                                                                                                                                                     }
                                                                                                                                                                                                                                                                                                                     break label1139;
                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                                  break;
                                                                                                                                                                                                                                                                                                               default:
                                                                                                                                                                                                                                                                                                                  break label1139;
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                         }
                                                                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                                                                   do {
                                                                                                                                                                                                                                                                                                      if (!iterator2.hasNext()
                                                                                                                                                                                                                                                                                                         )
                                                                                                                                                                                                                                                                                                       {
                                                                                                                                                                                                                                                                                                         break label1912;
                                                                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                                                                      entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                                                                                                   } while (
                                                                                                                                                                                                                                                                                                      !RenderUtils.acy(
                                                                                                                                                                                                                                                                                                         entity2.getBoundingBox()
                                                                                                                                                                                                                                                                                                            .stretch(
                                                                                                                                                                                                                                                                                                               0.0,
                                                                                                                                                                                                                                                                                                               2.0,
                                                                                                                                                                                                                                                                                                               0.0
                                                                                                                                                                                                                                                                                                            )
                                                                                                                                                                                                                                                                                                      )
                                                                                                                                                                                                                                                                                                   );
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    continue label1340;
                                                                                                                                                                                                                                                                                 case 1:
                                                                                                                                                                                                                                                                                    obj3 = (ItemEntity)entity2;
                                                                                                                                                                                                                                                                                    if (flag
                                                                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                                                                     {
                                                                                                                                                                                                                                                                                       d3 = client.player
                                                                                                                                                                                                                                                                                          .distanceTo(
                                                                                                                                                                                                                                                                                             entity2
                                                                                                                                                                                                                                                                                          );
                                                                                                                                                                                                                                                                                       if (!(
                                                                                                                                                                                                                                                                                          d3
                                                                                                                                                                                                                                                                                             > f2
                                                                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                          if (!(
                                                                                                                                                                                                                                                                                             d3
                                                                                                                                                                                                                                                                                                > f3
                                                                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                                                                             continue label1345;
                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                          continue label1343;
                                                                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                                                                       continue label1340;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    break;
                                                                                                                                                                                                                                                                                 case 2:
                                                                                                                                                                                                                                                                                    obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                                    if (flag2
                                                                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                                                                     {
                                                                                                                                                                                                                                                                                       entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                          .getOwner();
                                                                                                                                                                                                                                                                                       if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                          player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                                                                          vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                                                                             (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                                                                             render3DContext.f
                                                                                                                                                                                                                                                                                          );
                                                                                                                                                                                                                                                                                          d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                .getX()
                                                                                                                                                                                                                                                                                             - vec.getX();
                                                                                                                                                                                                                                                                                          d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                .getY()
                                                                                                                                                                                                                                                                                             - vec.getY();
                                                                                                                                                                                                                                                                                          d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                                .getZ()
                                                                                                                                                                                                                                                                                             - vec.getZ();
                                                                                                                                                                                                                                                                                          RenderUtils.acw(
                                                                                                                                                                                                                                                                                             render3DContext.matrices,
                                                                                                                                                                                                                                                                                             ((PlayerEntity)entity3)
                                                                                                                                                                                                                                                                                                .getName()
                                                                                                                                                                                                                                                                                                .getString(),
                                                                                                                                                                                                                                                                                             d4,
                                                                                                                                                                                                                                                                                             d5,
                                                                                                                                                                                                                                                                                             d2,
                                                                                                                                                                                                                                                                                             this.HU(
                                                                                                                                                                                                                                                                                                (PlayerEntity)entity3
                                                                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                                                                          );
                                                                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                                                                       continue label1340;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    continue label1316;
                                                                                                                                                                                                                                                                                 default:
                                                                                                                                                                                                                                                                                    continue label1340;
                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                           }
                                                                                                                                                                                                                                                                        case 2:
                                                                                                                                                                                                                                                                           obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                                                           if (flag2
                                                                                                                                                                                                                                                                              )
                                                                                                                                                                                                                                                                            {
                                                                                                                                                                                                                                                                              entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                 .getOwner();
                                                                                                                                                                                                                                                                              if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                                                                 )
                                                                                                                                                                                                                                                                               {
                                                                                                                                                                                                                                                                                 player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                                                                 vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                                                                    (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                                                                    render3DContext.f
                                                                                                                                                                                                                                                                                 );
                                                                                                                                                                                                                                                                                 d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                       .getX()
                                                                                                                                                                                                                                                                                    - vec.getX();
                                                                                                                                                                                                                                                                                 d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                       .getY()
                                                                                                                                                                                                                                                                                    - vec.getY();
                                                                                                                                                                                                                                                                                 d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                                                       .getZ()
                                                                                                                                                                                                                                                                                    - vec.getZ();
                                                                                                                                                                                                                                                                                 RenderUtils.acw(
                                                                                                                                                                                                                                                                                    render3DContext.matrices,
                                                                                                                                                                                                                                                                                    ((PlayerEntity)entity3)
                                                                                                                                                                                                                                                                                       .getName()
                                                                                                                                                                                                                                                                                       .getString(),
                                                                                                                                                                                                                                                                                    d4,
                                                                                                                                                                                                                                                                                    d5,
                                                                                                                                                                                                                                                                                    d2,
                                                                                                                                                                                                                                                                                    this.HU(
                                                                                                                                                                                                                                                                                       (PlayerEntity)entity3
                                                                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                                                                 );
                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                              break label1316;
                                                                                                                                                                                                                                                                           }
                                                                                                                                                                                                                                                                           break;
                                                                                                                                                                                                                                                                        default:
                                                                                                                                                                                                                                                                           break label1316;
                                                                                                                                                                                                                                                                     }
                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                               default:
                                                                                                                                                                                                                                                                  while (true) {
                                                                                                                                                                                                                                                                     if (!iterator2.hasNext()
                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                      {
                                                                                                                                                                                                                                                                        break label1912;
                                                                                                                                                                                                                                                                     }

                                                                                                                                                                                                                                                                     entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                                                                     if (RenderUtils.acy(
                                                                                                                                                                                                                                                                        entity2.getBoundingBox()
                                                                                                                                                                                                                                                                           .stretch(
                                                                                                                                                                                                                                                                              0.0,
                                                                                                                                                                                                                                                                              2.0,
                                                                                                                                                                                                                                                                              0.0
                                                                                                                                                                                                                                                                           )
                                                                                                                                                                                                                                                                     )
                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                      {
                                                                                                                                                                                                                                                                        break;
                                                                                                                                                                                                                                                                     }
                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                         }
                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                   }
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                             continue label1258;
                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                          break;
                                                                                                                                                                                                                                       case 2:
                                                                                                                                                                                                                                          obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                          if (flag2
                                                                                                                                                                                                                                             )
                                                                                                                                                                                                                                           {
                                                                                                                                                                                                                                             entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                .getOwner();
                                                                                                                                                                                                                                             if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                              {
                                                                                                                                                                                                                                                player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                                vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                                   (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                                   render3DContext.f
                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                                d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                      .getX()
                                                                                                                                                                                                                                                   - vec.getX();
                                                                                                                                                                                                                                                d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                      .getY()
                                                                                                                                                                                                                                                   - vec.getY();
                                                                                                                                                                                                                                                d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                                      .getZ()
                                                                                                                                                                                                                                                   - vec.getZ();
                                                                                                                                                                                                                                                RenderUtils.acw(
                                                                                                                                                                                                                                                   render3DContext.matrices,
                                                                                                                                                                                                                                                   ((PlayerEntity)entity3)
                                                                                                                                                                                                                                                      .getName()
                                                                                                                                                                                                                                                      .getString(),
                                                                                                                                                                                                                                                   d4,
                                                                                                                                                                                                                                                   d5,
                                                                                                                                                                                                                                                   d2,
                                                                                                                                                                                                                                                   this.HU(
                                                                                                                                                                                                                                                      (PlayerEntity)entity3
                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                );
                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                             continue label1258;
                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                          continue label1233;
                                                                                                                                                                                                                                       default:
                                                                                                                                                                                                                                          continue label1258;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                              case 2:
                                                                                                                                                                                                                                 obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                                                 if (flag2
                                                                                                                                                                                                                                    )
                                                                                                                                                                                                                                  {
                                                                                                                                                                                                                                    entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                       .getOwner();
                                                                                                                                                                                                                                    if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                                                       )
                                                                                                                                                                                                                                     {
                                                                                                                                                                                                                                       player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                                                       vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                                                          (EnderPearlEntity)entity2,
                                                                                                                                                                                                                                          render3DContext.f
                                                                                                                                                                                                                                       );
                                                                                                                                                                                                                                       d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                             .getX()
                                                                                                                                                                                                                                          - vec.getX();
                                                                                                                                                                                                                                       d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                             .getY()
                                                                                                                                                                                                                                          - vec.getY();
                                                                                                                                                                                                                                       d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                                                             .getZ()
                                                                                                                                                                                                                                          - vec.getZ();
                                                                                                                                                                                                                                       RenderUtils.acw(
                                                                                                                                                                                                                                          render3DContext.matrices,
                                                                                                                                                                                                                                          ((PlayerEntity)entity3)
                                                                                                                                                                                                                                             .getName()
                                                                                                                                                                                                                                             .getString(),
                                                                                                                                                                                                                                          d4,
                                                                                                                                                                                                                                          d5,
                                                                                                                                                                                                                                          d2,
                                                                                                                                                                                                                                          this.HU(
                                                                                                                                                                                                                                             (PlayerEntity)entity3
                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                       );
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    break label1233;
                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                                 break;
                                                                                                                                                                                                                              default:
                                                                                                                                                                                                                                 break label1233;
                                                                                                                                                                                                                           }
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                  }

                                                                                                                                                                                                                  do {
                                                                                                                                                                                                                     if (!iterator2.hasNext()
                                                                                                                                                                                                                        )
                                                                                                                                                                                                                      {
                                                                                                                                                                                                                        break label1912;
                                                                                                                                                                                                                     }

                                                                                                                                                                                                                     entity2 = (Entity)iterator2.next();
                                                                                                                                                                                                                  } while (
                                                                                                                                                                                                                     !RenderUtils.acy(
                                                                                                                                                                                                                        entity2.getBoundingBox()
                                                                                                                                                                                                                           .stretch(
                                                                                                                                                                                                                              0.0,
                                                                                                                                                                                                                              2.0,
                                                                                                                                                                                                                              0.0
                                                                                                                                                                                                                           )
                                                                                                                                                                                                                     )
                                                                                                                                                                                                                  );
                                                                                                                                                                                                               }
                                                                                                                                                                                                            }
                                                                                                                                                                                                         }
                                                                                                                                                                                                      }
                                                                                                                                                                                                   }
                                                                                                                                                                                                   continue label1508;
                                                                                                                                                                                                }
                                                                                                                                                                                                break;
                                                                                                                                                                                             case 2:
                                                                                                                                                                                                obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                                if (flag2
                                                                                                                                                                                                   )
                                                                                                                                                                                                 {
                                                                                                                                                                                                   entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                      .getOwner();
                                                                                                                                                                                                   if (entity3 instanceof PlayerEntity
                                                                                                                                                                                                      )
                                                                                                                                                                                                    {
                                                                                                                                                                                                      player2 = (PlayerEntity)entity3;
                                                                                                                                                                                                      vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                         (EnderPearlEntity)entity2,
                                                                                                                                                                                                         render3DContext.f
                                                                                                                                                                                                      );
                                                                                                                                                                                                      d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                            .getX()
                                                                                                                                                                                                         - vec.getX();
                                                                                                                                                                                                      d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                            .getY()
                                                                                                                                                                                                         - vec.getY();
                                                                                                                                                                                                      d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                            .getZ()
                                                                                                                                                                                                         - vec.getZ();
                                                                                                                                                                                                      RenderUtils.acw(
                                                                                                                                                                                                         render3DContext.matrices,
                                                                                                                                                                                                         ((PlayerEntity)entity3)
                                                                                                                                                                                                            .getName()
                                                                                                                                                                                                            .getString(),
                                                                                                                                                                                                         d4,
                                                                                                                                                                                                         d5,
                                                                                                                                                                                                         d2,
                                                                                                                                                                                                         this.HU(
                                                                                                                                                                                                            (PlayerEntity)entity3
                                                                                                                                                                                                         )
                                                                                                                                                                                                      );
                                                                                                                                                                                                   }
                                                                                                                                                                                                   continue label1508;
                                                                                                                                                                                                }
                                                                                                                                                                                                continue label1483;
                                                                                                                                                                                             default:
                                                                                                                                                                                                continue label1508;
                                                                                                                                                                                          }
                                                                                                                                                                                       }
                                                                                                                                                                                    case 2:
                                                                                                                                                                                       obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                                                       if (flag2
                                                                                                                                                                                          )
                                                                                                                                                                                        {
                                                                                                                                                                                          entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                             .getOwner();
                                                                                                                                                                                          if (entity3 instanceof PlayerEntity
                                                                                                                                                                                             )
                                                                                                                                                                                           {
                                                                                                                                                                                             player2 = (PlayerEntity)entity3;
                                                                                                                                                                                             vec = EntityBoxUtils.Bz(
                                                                                                                                                                                                (EnderPearlEntity)entity2,
                                                                                                                                                                                                render3DContext.f
                                                                                                                                                                                             );
                                                                                                                                                                                             d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                   .getX()
                                                                                                                                                                                                - vec.getX();
                                                                                                                                                                                             d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                   .getY()
                                                                                                                                                                                                - vec.getY();
                                                                                                                                                                                             d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                                                   .getZ()
                                                                                                                                                                                                - vec.getZ();
                                                                                                                                                                                             RenderUtils.acw(
                                                                                                                                                                                                render3DContext.matrices,
                                                                                                                                                                                                ((PlayerEntity)entity3)
                                                                                                                                                                                                   .getName()
                                                                                                                                                                                                   .getString(),
                                                                                                                                                                                                d4,
                                                                                                                                                                                                d5,
                                                                                                                                                                                                d2,
                                                                                                                                                                                                this.HU(
                                                                                                                                                                                                   (PlayerEntity)entity3
                                                                                                                                                                                                )
                                                                                                                                                                                             );
                                                                                                                                                                                          }
                                                                                                                                                                                          break label1483;
                                                                                                                                                                                       }
                                                                                                                                                                                       break;
                                                                                                                                                                                    default:
                                                                                                                                                                                       break label1483;
                                                                                                                                                                                 }
                                                                                                                                                                              }
                                                                                                                                                                        }

                                                                                                                                                                        do {
                                                                                                                                                                           if (!iterator2.hasNext()
                                                                                                                                                                              )
                                                                                                                                                                            {
                                                                                                                                                                              break label1912;
                                                                                                                                                                           }

                                                                                                                                                                           entity2 = (Entity)iterator2.next();
                                                                                                                                                                        } while (
                                                                                                                                                                           !RenderUtils.acy(
                                                                                                                                                                              entity2.getBoundingBox()
                                                                                                                                                                                 .stretch(
                                                                                                                                                                                    0.0,
                                                                                                                                                                                    2.0,
                                                                                                                                                                                    0.0
                                                                                                                                                                                 )
                                                                                                                                                                           )
                                                                                                                                                                        );
                                                                                                                                                                     }
                                                                                                                                                                  }
                                                                                                                                                               }
                                                                                                                                                            }

                                                                                                                                                            n2 = 878553726;
                                                                                                                                                            n4 = -624337391;
                                                                                                                                                            n3 = 1977691393;
                                                                                                                                                            break label1816;
                                                                                                                                                         case 1:
                                                                                                                                                            obj3 = (ItemEntity)entity2;
                                                                                                                                                            if (flag
                                                                                                                                                               )
                                                                                                                                                             {
                                                                                                                                                               continue label1592;
                                                                                                                                                            }
                                                                                                                                                            break;
                                                                                                                                                         case 2:
                                                                                                                                                            obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                            if (flag2
                                                                                                                                                               )
                                                                                                                                                             {
                                                                                                                                                               entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                                  .getOwner();
                                                                                                                                                               if (entity3 instanceof PlayerEntity
                                                                                                                                                                  )
                                                                                                                                                                {
                                                                                                                                                                  player2 = (PlayerEntity)entity3;
                                                                                                                                                                  vec = EntityBoxUtils.Bz(
                                                                                                                                                                     (EnderPearlEntity)entity2,
                                                                                                                                                                     render3DContext.f
                                                                                                                                                                  );
                                                                                                                                                                  d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                                        .getX()
                                                                                                                                                                     - vec.getX();
                                                                                                                                                                  d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                                        .getY()
                                                                                                                                                                     - vec.getY();
                                                                                                                                                                  d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                                        .getZ()
                                                                                                                                                                     - vec.getZ();
                                                                                                                                                                  RenderUtils.acw(
                                                                                                                                                                     render3DContext.matrices,
                                                                                                                                                                     ((PlayerEntity)entity3)
                                                                                                                                                                        .getName()
                                                                                                                                                                        .getString(),
                                                                                                                                                                     d4,
                                                                                                                                                                     d5,
                                                                                                                                                                     d2,
                                                                                                                                                                     this.HU(
                                                                                                                                                                        (PlayerEntity)entity3
                                                                                                                                                                     )
                                                                                                                                                                  );
                                                                                                                                                               }
                                                                                                                                                               continue label1590;
                                                                                                                                                            }
                                                                                                                                                            continue label1565;
                                                                                                                                                         default:
                                                                                                                                                            continue label1590;
                                                                                                                                                      }
                                                                                                                                                   }
                                                                                                                                                case 2:
                                                                                                                                                   obj4 = (EnderPearlEntity)entity2;
                                                                                                                                                   if (flag2) {
                                                                                                                                                      entity3 = ((EnderPearlEntity)entity2)
                                                                                                                                                         .getOwner();
                                                                                                                                                      if (entity3 instanceof PlayerEntity
                                                                                                                                                         )
                                                                                                                                                       {
                                                                                                                                                         player2 = (PlayerEntity)entity3;
                                                                                                                                                         vec = EntityBoxUtils.Bz(
                                                                                                                                                            (EnderPearlEntity)entity2,
                                                                                                                                                            render3DContext.f
                                                                                                                                                         );
                                                                                                                                                         d4 = ((EnderPearlEntity)entity2)
                                                                                                                                                               .getX()
                                                                                                                                                            - vec.getX();
                                                                                                                                                         d5 = ((EnderPearlEntity)entity2)
                                                                                                                                                               .getY()
                                                                                                                                                            - vec.getY();
                                                                                                                                                         d2 = ((EnderPearlEntity)entity2)
                                                                                                                                                               .getZ()
                                                                                                                                                            - vec.getZ();
                                                                                                                                                         RenderUtils.acw(
                                                                                                                                                            render3DContext.matrices,
                                                                                                                                                            ((PlayerEntity)entity3)
                                                                                                                                                               .getName()
                                                                                                                                                               .getString(),
                                                                                                                                                            d4,
                                                                                                                                                            d5,
                                                                                                                                                            d2,
                                                                                                                                                            this.HU(
                                                                                                                                                               (PlayerEntity)entity3
                                                                                                                                                            )
                                                                                                                                                         );
                                                                                                                                                      }
                                                                                                                                                      break label1565;
                                                                                                                                                   }
                                                                                                                                                   break;
                                                                                                                                                default:
                                                                                                                                                   break label1565;
                                                                                                                                             }
                                                                                                                                          }
                                                                                                                                    }

                                                                                                                                    do {
                                                                                                                                       if (!iterator2.hasNext()) {
                                                                                                                                          break label1912;
                                                                                                                                       }

                                                                                                                                       entity2 = (Entity)iterator2.next();
                                                                                                                                    } while (
                                                                                                                                       !RenderUtils.acy(
                                                                                                                                          entity2.getBoundingBox()
                                                                                                                                             .stretch(
                                                                                                                                                0.0, 2.0, 0.0
                                                                                                                                             )
                                                                                                                                       )
                                                                                                                                    );
                                                                                                                                 }
                                                                                                                              }
                                                                                                                           }
                                                                                                                        }
                                                                                                                     }
                                                                                                                     break;
                                                                                                                  case 2:
                                                                                                                     obj4 = (EnderPearlEntity)entity2;
                                                                                                                     if (flag2) {
                                                                                                                        entity3 = ((EnderPearlEntity)entity2)
                                                                                                                           .getOwner();
                                                                                                                        if (entity3 instanceof PlayerEntity) {
                                                                                                                           player2 = (PlayerEntity)entity3;
                                                                                                                           vec = EntityBoxUtils.Bz(
                                                                                                                              (EnderPearlEntity)entity2, render3DContext.f
                                                                                                                           );
                                                                                                                           d4 = ((EnderPearlEntity)entity2)
                                                                                                                                 .getX()
                                                                                                                              - vec.getX();
                                                                                                                           d5 = ((EnderPearlEntity)entity2)
                                                                                                                                 .getY()
                                                                                                                              - vec.getY();
                                                                                                                           d2 = ((EnderPearlEntity)entity2)
                                                                                                                                 .getZ()
                                                                                                                              - vec.getZ();
                                                                                                                           RenderUtils.acw(
                                                                                                                              render3DContext.matrices,
                                                                                                                              ((PlayerEntity)entity3)
                                                                                                                                 .getName()
                                                                                                                                 .getString(),
                                                                                                                              d4,
                                                                                                                              d5,
                                                                                                                              d2,
                                                                                                                              this.HU((PlayerEntity)entity3)
                                                                                                                           );
                                                                                                                        }
                                                                                                                        break label1647;
                                                                                                                     }
                                                                                                                     continue label1647;
                                                                                                                  default:
                                                                                                                     break label1647;
                                                                                                               }
                                                                                                            }
                                                                                                         case 2:
                                                                                                            obj4 = (EnderPearlEntity)entity2;
                                                                                                            if (flag2) {
                                                                                                               entity3 = ((EnderPearlEntity)entity2).getOwner();
                                                                                                               if (entity3 instanceof PlayerEntity) {
                                                                                                                  continue label1674;
                                                                                                               }
                                                                                                               continue label1672;
                                                                                                            }
                                                                                                            break;
                                                                                                         default:
                                                                                                            continue label1672;
                                                                                                      }
                                                                                                   }
                                                                                             }

                                                                                             do {
                                                                                                if (!iterator2.hasNext()) {
                                                                                                   break label1912;
                                                                                                }

                                                                                                entity2 = (Entity)iterator2.next();
                                                                                             } while (
                                                                                                !RenderUtils.acy(entity2.getBoundingBox().stretch(0.0, 2.0, 0.0))
                                                                                             );
                                                                                          }
                                                                                       }
                                                                                    }
                                                                                 }
                                                                              }
                                                                              break;
                                                                           default:
                                                                              break label1712;
                                                                        }
                                                                     }
                                                               }

                                                               do {
                                                                  if (!iterator2.hasNext()) {
                                                                     break label1912;
                                                                  }

                                                                  entity2 = (Entity)iterator2.next();
                                                               } while (!RenderUtils.acy(entity2.getBoundingBox().stretch(0.0, 2.0, 0.0)));
                                                            }
                                                         }

                                                         n2 = 951002124;
                                                         n4 = 1245946842;
                                                         n3 = 1478124217;
                                                         break label1816;
                                                      case 2:
                                                         continue label1782;
                                                   }

                                                   do {
                                                      if (!iterator2.hasNext()) {
                                                         break label1912;
                                                      }

                                                      entity2 = (Entity)iterator2.next();
                                                   } while (!RenderUtils.acy(entity2.getBoundingBox().stretch(0.0, 2.0, 0.0)));
                                                }
                                             }

                                             n2 = 1348691910;
                                             n4 = -428755187;
                                             n3 = 1560375671;
                                             break label1816;
                                          }

                                          n2 = 2027484692;
                                          n4 = 1922890511;
                                          n3 = 1975181477;
                                          break label1816;
                                       }

                                       n2 = 1450867892;
                                       n4 = -881594019;
                                       n3 = 1528326661;
                                       break label1816;
                                 }
                              }

                              if (!iterator2.hasNext()) {
                                 break label1912;
                              }
                           }
                        }

                        n2 = 432677968;
                        n4 = 1810671603;
                        n3 = 1484170031;
                        break label1816;
                     }

                     n2 = 492470906;
                     n4 = 555205562;
                     n3 = 2112865487;
                     break label1816;
                  }

                  n2 = 475464344;
                  n4 = -2067785242;
                  n3 = 2095203373;
                  break label1816;
               }

               n2 = 611024388;
               n4 = -1148827444;
               n3 = 1155414193;
            } else {
               n2 = 485569082;
               n4 = 734227622;
               n3 = 2084861071;
            }
         }

         while (true) {
            int n15 = n3;
            if (n4 == 0) {
               n3 *= 1211024471;
            }

            switch (n2 ^ n15) {
               case -2125079621:
                  if ((PlayerEntity)obj2 == client.player) {
                     if (!Freecam.gP().awg()) {
                        int n16 = n3 * -1183239833;
                        n2 = 276024764;
                        n4 = -461617284;
                        n3 = n16;
                     } else {
                        n2 = 2021866416;
                        n4 = 1066665675;
                        n3 = 1977691393;
                     }
                  } else {
                     n2 = 878553726;
                     n4 = -624337391;
                     n3 = 1977691393;
                  }
                  break;
               case -2088940365:
                  d = d6;
                  int n17 = n3 * 1687197957;
                  n2 = 122206070;
                  n4 = 1320908397;
                  n3 = n17;
                  break;
               case -2075173661:
                  d5 = ((EnderPearlEntity)obj4).getY() - vec.getY();
                  int n18 = n3 * -1240590651;
                  n2 = 369901264;
                  n4 = -2136455899;
                  n3 = n18;
                  break;
               case -2047494911:
                  RenderSystem.setShaderLights(vector3f, vector3f2);
                  int n19 = n3 * -1029112397;
                  n2 = 905988886;
                  n4 = -632784026;
                  n3 = n19;
                  break;
               case -2025687795:
                  if (entity3 instanceof PlayerEntity) {
                     player2 = (PlayerEntity)entity3;
                     int n20 = n3 * -1634241421;
                     n2 = 463773914;
                     n4 = -1839694436;
                     n3 = n20;
                  } else {
                     n2 = 1320418234;
                     n4 = -487562477;
                     n3 = 1137831179;
                  }
                  break;
               case -1963373351:
                  RenderUtils.acw(render3DContext.matrices, player2.getName().getString(), d4, d5, d2, this.HU(player2));
                  int n21 = n3 * -2009062557;
                  n2 = 593800126;
                  n4 = 832570912;
                  n3 = n21;
                  break;
               case -1960437305:
                  obj = entity2;
                  b = 0;
                  int n22 = n3 * -1711462211;
                  n2 = 31260806;
                  n4 = -1543110988;
                  n3 = n22;
                  break;
               case -1946098069:
                  n2 = 555260788;
                  n4 = -1367592316;
                  n3 = 1101913537;
                  break;
               case -1936069619:
                  str = this.HK(stack, nametagsNoneDistanceMode, d3);
                  int n23 = n3 * -125485833;
                  n2 = 35008996;
                  n4 = -1991448022;
                  n3 = n23;
                  break;
               case -1836304311:
                  obj2 = ((PosSoundEvent)entry2.getKey()).getPos();
                  int n24 = n3 * 192319771;
                  n2 = 777089766;
                  n4 = -2047748251;
                  n3 = n24;
                  break;
               case -1740834867:
                  flag = (Boolean)this.droppedItems.atx();
                  int n25 = n3 * -1627243943;
                  n2 = 743310244;
                  n4 = -581960086;
                  n3 = n25;
                  break;
               case -1714258821:
                  RenderUtils.acw(
                     render3DContext.matrices,
                     ((PosSoundEvent)entry2.getKey()).soundEvent().getId().toShortTranslationKey(),
                     ((Vec3d)obj2).x,
                     ((Vec3d)obj2).y,
                     ((Vec3d)obj2).z,
                     -1
                  );
                  int n26 = n3 * -1965243727;
                  n2 = 1824227070;
                  n4 = -2007622443;
                  n3 = n26;
                  break;
               case -1705208315:
                  vec2 = EntityBoxUtils.BA((ItemEntity)obj3, render3DContext.f);
                  int n27 = n3 * -183784845;
                  n2 = 520292374;
                  n4 = -1343050917;
                  n3 = n27;
                  break;
               case -1531559411:
                  d2 = ((EnderPearlEntity)obj4).getZ() - vec.getZ();
                  int n28 = n3 * 320668071;
                  n2 = 2080716638;
                  n4 = 232415763;
                  n3 = n28;
                  break;
               case -1529468949:
                  n2 = 1288686448;
                  n4 = 2085595383;
                  n3 = 1101913537;
                  break;
               case -1517790551:
                  n2 = 1354960280;
                  n4 = 1487900159;
                  n3 = 1570839337;
                  break;
               case -1469242769:
                  if (!flag) {
                     b = 2;
                     n2 = 550814028;
                     n4 = -1547096003;
                     n3 = 1634223667;
                  } else {
                     n2 = 951002124;
                     n4 = 1245946842;
                     n3 = 1478124217;
                  }
                  break;
               case -1465260709:
                  if ((Boolean)this.sounds.atx()) {
                     int n29 = n3 * -262505149;
                     n2 = 58291162;
                     n4 = 733384150;
                     n3 = n29;
                  } else {
                     n2 = 500448228;
                     n4 = 880847463;
                     n3 = 1550367899;
                  }
                  break;
               case -1435478633:
                  n2 = 555260788;
                  n4 = -1367592316;
                  n3 = 1101913537;
                  break;
               case -1359148569:
                  if (!flag2) {
                     b = 3;
                     n2 = 31260806;
                     n4 = -1543110988;
                     n3 = 1634223667;
                  } else {
                     n2 = 1348691910;
                     n4 = -428755187;
                     n3 = 1560375671;
                  }
                  break;
               case -1354520921:
                  f2 = (Float)this.itemsRange.atx();
                  int n30 = n3 * -1177597349;
                  n2 = 1040157182;
                  n4 = 880830948;
                  n3 = n30;
                  break;
               case -1335766059:
                  d4 = ((EnderPearlEntity)obj4).getX() - vec.getX();
                  int n31 = n3 * -34575609;
                  n2 = 244057568;
                  n4 = -837569120;
                  n3 = n31;
                  break;
               case -1311170307:
                  flag2 = (Boolean)this.pearls.atx();
                  int n32 = n3 * -52834255;
                  n2 = 414651752;
                  n4 = -2048327113;
                  n3 = n32;
                  break;
               case -1272228705:
                  n2 = 1024744860;
                  n4 = -1977868868;
                  n3 = 1570839337;
                  break;
               case -1252428481:
                  iterator2 = client.world.getEntities().iterator();
                  int n33 = n3 * 1041692899;
                  n2 = 555260788;
                  n4 = -1367592316;
                  n3 = n33;
                  break;
               case -1229451229:
                  n2 = 471702614;
                  n4 = -1111305947;
                  n3 = 1570839337;
                  break;
               case -1198965361:
                  if (((PlayerEntity)obj2).isAlive()) {
                     if (!(Boolean)this.invisibles.atx()) {
                        if (((PlayerEntity)obj2).isInvisible()) {
                           int n34 = n3 * -1349357931;
                           n2 = 413649116;
                           n4 = -493488289;
                           n3 = n34;
                        } else {
                           n2 = 956901944;
                           n4 = -1633855602;
                           n3 = 1505520781;
                        }
                     } else {
                        n2 = 402713586;
                        n4 = 1229032215;
                        n3 = 1505520781;
                     }
                  } else {
                     n2 = 1288686448;
                     n4 = 2085595383;
                     n3 = 1101913537;
                  }
                  break;
               case -1130871849:
                  vector3f2 = RenderSystem.shaderLightDirections[1];
                  int n35 = n3 * -1343514737;
                  n2 = 1273858604;
                  n4 = 91509443;
                  n3 = n35;
                  break;
               case -953588351:
                  camera = client.gameRenderer.getCamera();
                  int n36 = n3 * -1483578593;
                  n2 = 312633808;
                  n4 = -661428507;
                  n3 = n36;
                  break;
               case -739511351:
                  f4 = f5;
                  int n37 = n3 * -358895141;
                  n2 = 934801082;
                  n4 = 1281003669;
                  n3 = n37;
                  break;
               case -701995335:
                  FontLoader.fontRenderer2.aba();
                  int n38 = n3 * -1258219087;
                  n2 = 1583980752;
                  n4 = -1917581857;
                  n3 = n38;
                  break;
               case -660918773:
                  if (d3 > f2) {
                     n2 = 555260788;
                     n4 = -1367592316;
                     n3 = 1101913537;
                  } else {
                     n2 = 951530438;
                     n4 = 808582115;
                     n3 = 2030810297;
                  }
                  break;
               case -613147931:
                  if (d3 > f3) {
                     int n39 = n3 * 1946450519;
                     n2 = 858002812;
                     n4 = 1980120736;
                     n3 = n39;
                  } else {
                     n2 = 2027484692;
                     n4 = 1922890511;
                     n3 = 1975181477;
                  }
                  break;
               case -524103053:
                  vector3f = RenderSystem.shaderLightDirections[0];
                  int n40 = n3 * -1449236569;
                  n2 = 1921779476;
                  n4 = -979220689;
                  n3 = n40;
                  break;
               case -523532091:
                  RenderSystem.disableDepthTest();
                  int n41 = n3 * -1022946439;
                  n2 = 602900728;
                  n4 = 1275624827;
                  n3 = n41;
                  break;
               case -512072573:
                  obj = this.map.entrySet().iterator();
                  int n42 = n3 * 1303515545;
                  n2 = 146147556;
                  n4 = 1721524788;
                  n3 = n42;
                  break;
               case -459934381:
                  obj2 = (PlayerEntity)obj;
                  int n43 = n3 * 1778193969;
                  n2 = 889336438;
                  n4 = -2059126731;
                  n3 = n43;
                  break;
               case -426354967:
                  String str2 = (String)obj3;
                  f5 = client.textRenderer.getWidth(str2);
                  int n44 = n3 * -330535873;
                  n2 = 756332556;
                  n4 = 975677422;
                  n3 = n44;
                  break;
               case -378326459:
                  RenderSystem.disablePolygonOffset();
                  int n45 = n3 * 849859683;
                  n2 = 337230422;
                  n4 = -1478358422;
                  n3 = n45;
                  break;
               case -350911683:
                  n2 = 471702614;
                  n4 = -1111305947;
                  n3 = 1570839337;
                  break;
               case -317275609:
                  l = System.currentTimeMillis();
                  int n46 = n3 * -432810743;
                  n2 = 419852596;
                  n4 = 1438376296;
                  n3 = n46;
                  break;
               case -220091337:
                  return;
               case -212145639:
                  RenderSystem.polygonOffset(1.0F, -2000000.0F);
                  int n47 = n3 * 655980087;
                  n2 = 164254508;
                  n4 = 785397754;
                  n3 = n47;
                  break;
               case -113003737:
                  entity3 = ((EnderPearlEntity)obj4).getOwner();
                  int n48 = n3 * -1474740189;
                  n2 = 490993350;
                  n4 = -539992168;
                  n3 = n48;
                  break;
               case -80048295:
                  client.getBufferBuilders().getEffectVertexConsumers().draw();
                  int n49 = n3 * 1854535763;
                  n2 = 871077250;
                  n4 = 1427263368;
                  n3 = n49;
                  break;
               case -79273861:
                  return;
               case -42113681:
                  Set set = this.map.entrySet();
                  final long l2 = l;
                  Predicate predicate = new Predicate() {
                     @Override
                     public boolean test(Object render3DContext) {
                        return Nametags.HZ(l2, (Entry)render3DContext);
                     }
                  };
                  set.removeIf(predicate);
                  int n50 = n3 * 1511168489;
                  n2 = 171468240;
                  n4 = 951112627;
                  n3 = n50;
                  break;
               case -22145999:
                  f3 = (Float)this.fadeStart.atx();
                  int n51 = n3 * -581294483;
                  n2 = 283316108;
                  n4 = -2024446448;
                  n3 = n51;
                  break;
               case -14116031:
                  if (Font.kT().awg()) {
                     int n52 = n3 * -1901540631;
                     n2 = 95384312;
                     n4 = 1817544759;
                     n3 = n52;
                  } else {
                     n2 = 51114926;
                     n4 = 865234895;
                     n3 = 1119319249;
                  }
                  break;
               case 70547779:
                  client.getBufferBuilders().getEntityVertexConsumers().draw();
                  int n53 = n3 * -1568354605;
                  n2 = 160365636;
                  n4 = -1062552091;
                  n3 = n53;
                  break;
               case 101587155:
                  if (d < 0.01) {
                     n2 = 1137342;
                     n4 = -1717375955;
                     n3 = 1101913537;
                  } else {
                     n2 = 1450867892;
                     n4 = -881594019;
                     n3 = 1528326661;
                  }
                  break;
               case 224532145:
                  int n54 = (-1140225840);
                  n2 = (1238008435 + (n4 ^ n54));
                  n4 = 0;
                  break;
               case 449334071:
                  f5 = FontLoader.fontRenderer2.abc((String)obj3) / 2.0F;
                  n2 = 203258310;
                  n4 = 770398967;
                  n3 = 1302462137;
                  break;
               case 536051255:
                  n2 = 146147556;
                  n4 = 1721524788;
                  n3 = 1745047121;
                  break;
               case 679721443:
                  RenderSystem.polygonOffset(1.0F, 2000000.0F);
                  int n55 = n3 * -1999174511;
                  n2 = 176079698;
                  n4 = 1423199676;
                  n3 = n55;
                  break;
               case 825889291:
                  if (client.gameRenderer != null) {
                     if (client.world != null) {
                        if (client.getCameraEntity() == null) {
                           int n56 = n3 * -1669856289;
                           n2 = 485569082;
                           n4 = 734227622;
                           n3 = n56;
                        } else {
                           n2 = 533715536;
                           n4 = 953542272;
                           n3 = 2138741989;
                        }
                     } else {
                        n2 = 485569082;
                        n4 = 734227622;
                        n3 = 2084861071;
                     }
                  } else {
                     n2 = 1039725552;
                     n4 = 470787663;
                     n3 = 2084861071;
                  }
                  break;
               case 838595967:
                  d6 = 1.0 - (d3 - f3) / (f2 - f3);
                  n2 = 712988972;
                  n4 = -160375705;
                  n3 = 1807998547;
                  break;
               case 852115737:
                  if (!RenderUtils.acy(entity2.getBoundingBox().stretch(0.0, 2.0, 0.0))) {
                     int n57 = n3 * 1841459425;
                     n2 = 373078520;
                     n4 = -1925106537;
                     n3 = n57;
                  } else {
                     n2 = 1840722136;
                     n4 = 505492971;
                     n3 = 1624581737;
                  }
                  break;
               case 909931397:
                  ItemEntity itemEntity = (ItemEntity)obj3;
                  d3 = client.player.distanceTo((Entity)itemEntity);
                  int n58 = n3 * -1286365665;
                  n2 = 1017973374;
                  n4 = 891612317;
                  n3 = n58;
                  break;
               case 972266619:
                  if (((Iterator)obj).hasNext()) {
                     entry2 = (Entry)((Iterator)obj).next();
                     int n59 = n3 * -1098600517;
                     n2 = 121307176;
                     n4 = -982641342;
                     n3 = n59;
                  } else {
                     n2 = 500448228;
                     n4 = 880847463;
                     n3 = 1550367899;
                  }
                  break;
               case 1027408365:
                  vec = EntityBoxUtils.Bz((EnderPearlEntity)obj4, render3DContext.f);
                  int n60 = n3 * -1905545059;
                  n2 = 167896768;
                  n4 = -5045826;
                  n3 = n60;
                  break;
               case 1102874495:
                  int n61 = (1094500875);
                  n2 = (829584500 + (n4 ^ n61));
                  n4 = 0;
                  break;
               case 1130465087:
                  nametagsNoneDistanceMode = (NametagsNoneDistanceMode)this.itemsTag.atx();
                  int n62 = n3 * 651973151;
                  n2 = 77739262;
                  n4 = -1145714997;
                  n3 = n62;
                  break;
               case 1170718747:
                  RenderSystem.enablePolygonOffset();
                  int n63 = n3 * -708939893;
                  n2 = 48665564;
                  n4 = 694474222;
                  n3 = n63;
                  break;
               case 1221832407:
                  this.HL(
                     render3DContext.matrices,
                     (String)obj3,
                     f4 / 2.0F,
                     (PlayerEntity)obj2,
                     ((Vec3d)obj4).getX(),
                     ((Vec3d)obj4).getY(),
                     ((Vec3d)obj4).getZ(),
                     camera
                  );
                  int n64 = n3 * 2036906347;
                  n2 = 352243950;
                  n4 = -205844171;
                  n3 = n64;
                  break;
               case 1270710107:
                  if (iterator2.hasNext()) {
                     entity2 = (Entity)iterator2.next();
                     int n65 = n3 * -121075145;
                     n2 = 890314948;
                     n4 = 1931197942;
                     n3 = n65;
                  } else {
                     n2 = 432677968;
                     n4 = 1810671603;
                     n3 = 1484170031;
                  }
                  break;
               case 1330040661:
                  obj3 = (ItemEntity)obj;
                  int n66 = n3 * 1453710273;
                  n2 = 498976756;
                  n4 = -2084230021;
                  n3 = n66;
                  break;
               case 1401040253:
                  n = 16777215 | (int)(255.0 * d) << 24;
                  int n67 = n3 * 600633947;
                  n2 = 306281090;
                  n4 = -1311487895;
                  n3 = n67;
                  break;
               case 1513391331:
                  obj4 = (EnderPearlEntity)obj;
                  int n68 = n3 * -1382730751;
                  n2 = 1081582230;
                  n4 = 639087517;
                  n3 = n68;
                  break;
               case 1559510115:
                  DiffuseLighting.method_56819(client.getEntityRenderDispatcher().getRotation());
                  int n69 = n3 * -1623422889;
                  n2 = 376874178;
                  n4 = 109452384;
                  n3 = n69;
                  break;
               case 1616105757:
                  obj3 = this.HS((PlayerEntity)obj2);
                  int n70 = n3 * 1662410591;
                  n2 = 643093066;
                  n4 = -1709086865;
                  n3 = n70;
                  break;
               case 1622492853:
                  int n71 = (-286718117);
                  n2 = (-914658117 + (n4 ^ n71));
                  n4 = 0;
                  break;
               case 1714062607:
                  RenderSystem.enableDepthTest();
                  int n72 = n3 * -1212443893;
                  n2 = 410704104;
                  n4 = -335694807;
                  n3 = n72;
                  break;
               case 1803046125:
                  obj4 = EntityBoxUtils.BA((PlayerEntity)obj2, render3DContext.f);
                  int n73 = n3 * 1610567011;
                  n2 = 1196357016;
                  n4 = -684688091;
                  n3 = n73;
                  break;
               case 1843730167:
                  d6 = 1.0;
                  int n74 = n3 * 238013633;
                  n2 = 1721878754;
                  n4 = 587045053;
                  n3 = n74;
                  break;
               case 1860553397:
                  FontLoader.fontRenderer2.aaY();
                  int n75 = n3 * -878849189;
                  n2 = 100379154;
                  n4 = -23649835;
                  n3 = n75;
                  break;
               case 1906770731:
                  RenderUtils.acw(render3DContext.matrices, str, vec2.getX(), vec2.getY(), vec2.getZ(), n);
                  int n76 = n3 * -1603311125;
                  n2 = 374784988;
                  n4 = 396649056;
                  n3 = n76;
                  break;
               case 1943116939:
                  n2 = 555260788;
                  n4 = -1367592316;
                  n3 = 1101913537;
                  break;
               case 1962942617:
                  Entity entity4 = (Entity)obj;
                  byte b2 = b;
                  Entity entity5 = entity4;
                  switch (entity5 == null
                     ? -1
                     : (
                        b2 <= 0 && entity5 instanceof PlayerEntity
                           ? 0
                           : (b2 <= 1 && entity5 instanceof ItemEntity ? 1 : (b2 <= 2 && entity5 instanceof EnderPearlEntity ? 2 : 3))
                     )) {
                     case 0:
                        n2 = 492470906;
                        n4 = 555205562;
                        n3 = 2112865487;
                        continue;
                     case 1:
                        n2 = 475464344;
                        n4 = -2067785242;
                        n3 = 2095203373;
                        continue;
                     case 2:
                        n2 = 611024388;
                        n4 = -1148827444;
                        n3 = 1155414193;
                        continue;
                     default:
                        n2 = 2099366292;
                        n4 = 805866441;
                        n3 = 1883496229;
                        continue;
                  }
               case 2130662061:
                  stack = ((ItemEntity)obj3).getStack();
                  int n77 = n3 * 903523055;
                  n2 = 174603806;
                  n4 = -723115277;
                  n3 = n77;
                  break;
               default:
                  return;
            }
         }
      }

      client.getBufferBuilders().getEntityVertexConsumers().draw();
      client.getBufferBuilders().getEffectVertexConsumers().draw();
      FontLoader.fontRenderer2.aba();
      RenderSystem.polygonOffset(1.0F, 2000000.0F);
      RenderSystem.disablePolygonOffset();
      RenderSystem.enableDepthTest();
      RenderSystem.setShaderLights(vector3f, vector3f2);
      if ((Boolean)this.sounds.atx()) {
         long l3 = System.currentTimeMillis();
         Set temp = this.map.entrySet();
         final long l4 = l3;
         Predicate predicate2 = new Predicate() {
            @Override
            public boolean test(Object render3DContext) {
               return Nametags.HZ(l4, (Entry)render3DContext);
            }
         };
         temp.removeIf(predicate2);
         Iterator iterator3 = this.map.entrySet().iterator();

         while (iterator3.hasNext()) {
            Entry entry3 = (Entry)iterator3.next();
            Vec3d vec3 = ((PosSoundEvent)entry3.getKey()).getPos();
            RenderUtils.acw(render3DContext.matrices, ((PosSoundEvent)entry3.getKey()).soundEvent().getId().toShortTranslationKey(), vec3.x, vec3.y, vec3.z, -1);
         }
      }
   }

   @EventListener
   public void onRenderLabelIfPresent(RenderLabelIfPresentEvent renderLabelIfPresentEvent) {
      if (renderLabelIfPresentEvent.getEntity() instanceof PlayerEntity && renderLabelIfPresentEvent.getEntity() != client.player) {
         renderLabelIfPresentEvent.aim();
      }
   }

   @EventListener
   public void onPlaySound(PlaySoundEvent playSoundEvent) {
      if ((Boolean)this.sounds.atx()) {
         Map temp = this.map;
         PosSoundEvent temp2 = new PosSoundEvent(playSoundEvent.getVec(), playSoundEvent.getSoundEvent());
         Long longValue = System.currentTimeMillis();
         PosSoundEvent posSoundEvent = temp2;
         temp.put(posSoundEvent, longValue);
      }
   }

   private String HK(ItemStack stack, NametagsNoneDistanceMode nametagsNoneDistanceMode, double d) {
      StringBuilder stringBuilder = new StringBuilder(stack.getName().getString());
      if (nametagsNoneDistanceMode.ahK() && stack.getCount() > 1) {
         StringBuilder temp = stringBuilder.append(" x");
         int n = stack.getCount();
         temp.append(n);
      }

      if (nametagsNoneDistanceMode.ahL()) {
         StringBuilder stringBuilder2 = stringBuilder.append(' ');
         String str = bo.format(d);
         stringBuilder2.append(str).append('m');
      }

      return stringBuilder.toString();
   }

   private void HL(MatrixStack matrices2, String str, float f2, PlayerEntity player2, double d, double d2, double d3, Camera camera) {
      Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
      Vec3d vec2 = camera.getPos();
      matrices2.push();
      double d4 = d2 + (double)player2.getHeight();
      float f3;
      if (player2.isSneaking()) {
         f3 = 0.4F;
      } else {
         f3 = 0.43F;
      }

      double d5 = d4 + (double)f3 + (double)((Float)this.height.atx()).floatValue();
      double d6 = vec2.squaredDistanceTo(d, d5, d3);
      float f4 = (Float)this.distance.atx();
      double d7 = (double)(-0.025F * ((Float)this.scale.atx()).floatValue());
      if ((Boolean)this.factor.atx() && d6 > (double)(f4 * f4)) {
         d7 += (Math.sqrt(d6) - (double)f4) * -0.0024999999441206455 * (double)((Float)this.scaling.atx()).floatValue();
      }

      float f5 = (float)d7;
      matrices2.translate(d - vec2.getX(), d5 + (double)((f5 / -0.025F - 1.0F) / 4.0F) - vec2.getY(), d3 - vec2.getZ());
      matrices2.multiply(client.getEntityRenderDispatcher().getRotation());
      matrices2.scale(-f5, f5, 1.0F);
      float f6 = (float)client.textRenderer.fontHeight;
      if ((Boolean)this.background.atx()) {
         VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayer.getGuiOverlay());
         HN(vertexConsumer, matrices2, -f2 - 1.0F, -1.0F, f2 + 1.0F, f6, 1426064384);
      }

      if ((Boolean)this.border.atx()) {
         int n;
         if (Managers.friendManager.awq(player2.getDisplayName().getString())) {
            n = ColorHelper.wx().wC();
         } else {
            n = ColorHelper3.auO().auX().intValue();
         }

         VertexConsumer vertexConsumer2 = immediate.getBuffer(RenderLayer.getDebugLineStrip(3.0));
         float f7 = -f2 - 1.0F;
         float f8 = -1.0F;
         float f9 = f2 + 1.0F;
         float f10 = (float)Argb.getAlpha(n) / 255.0F;
         float f11 = (float)Argb.getRed(n) / 255.0F;
         float f12 = (float)Argb.getGreen(n) / 255.0F;
         float f13 = (float)Argb.getBlue(n) / 255.0F;
         Matrix4f matrix4f = matrices2.peek().getPositionMatrix();
         vertexConsumer2.vertex(matrix4f, f7, f8, 0.0F).color(f11, f12, f13, f10);
         vertexConsumer2.vertex(matrix4f, f7, f6, 0.0F).color(f11, f12, f13, f10);
         vertexConsumer2.vertex(matrix4f, f9, f6, 0.0F).color(f11, f12, f13, f10);
         vertexConsumer2.vertex(matrix4f, f9, f8, 0.0F).color(f11, f12, f13, f10);
         vertexConsumer2.vertex(matrix4f, f7, f8, 0.0F).color(f11, f12, f13, f10);
      }

      int n2 = this.HU(player2);
      this.HM(player2);
      float f14 = 0.0F;
      int n3 = 0;

      for (int n4 = 0; n4 < this.list.size(); n4++) {
         float f15;
         if (n4 == 0) {
            f15 = 9.0F;
         } else {
            f15 = 8.0F;
         }

         f14 -= f15;
         int n5 = ((ItemStack)this.list.get(n4)).getEnchantments().getEnchantments().size();
         if (n5 > n3) {
            n3 = n5;
         }
      }

      float f16 = this.HQ(n3);
      boolean flag = (Boolean)this.armor.atx();
      boolean flag2 = (Boolean)this.items.atx();
      boolean flag3 = (Boolean)this.enchantments.atx();
      boolean flag4 = (Boolean)this.durability.atx();
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         ItemStack stack = (ItemStack)iterator2.next();
         boolean flag5 = stack.getItem() instanceof ArmorItem;
         if (flag && flag5 || flag2 && !flag5) {
            float f17;
            if (!flag5 && !flag3) {
               f17 = -18.5F;
            } else {
               f17 = f16;
            }

            matrices2.push();
            matrices2.translate(f14, f17, 0.0F);
            if (stack.hasGlint()) {
               matrices2.translate(8.0F, 12.0F, 0.0F);
               matrices2.scale(32.0F, -32.0F, 1.0E-8F);
               BakedModel bakedModel = client.getItemRenderer().getModel(stack, client.world, null, 0);
               client.getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, false, matrices2, immediate, 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
            } else {
               matrices2.translate(8.0F, 8.0F, 0.0F);
               matrices2.scale(16.0F, -16.0F, 0.01F);
               BakedModel bakedModel2 = client.getItemRenderer().getModel(stack, client.world, null, 0);
               client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, matrices2, immediate, 15728880, OverlayTexture.DEFAULT_UV, bakedModel2);
            }

            matrices2.pop();
            matrices2.push();
            if (stack.getCount() != 1) {
               String str2 = String.valueOf(stack.getCount());
               float f18 = f14 + 17.0F - (float)client.textRenderer.getWidth(str2);
               TextRendererMixin temp = (TextRendererMixin)client.textRenderer;
               temp.hookDrawLayer(
                  str2,
                  f18,
                  f17 + 9.0F,
                  HG(-1),
                  true,
                  matrices2.peek().getPositionMatrix(),
                  client.getBufferBuilders().getEffectVertexConsumers(),
                  TextLayerType.SEE_THROUGH,
                  0,
                  15728880
               );
               TextRendererMixin temp2 = (TextRendererMixin)client.textRenderer;
               temp2.hookDrawLayer(
                  str2,
                  f18,
                  f17 + 9.0F,
                  HG(-1),
                  false,
                  matrices2.peek().getPositionMatrix(),
                  client.getBufferBuilders().getEffectVertexConsumers(),
                  TextLayerType.SEE_THROUGH,
                  0,
                  15728880
               );
            }

            if (stack.isItemBarVisible()) {
               int n6 = stack.getItemBarStep();
               float f19 = f14 + 2.0F;
               float f20 = f17 + 13.0F;
               HN(client.getBufferBuilders().getEffectVertexConsumers().getBuffer(RenderLayer.getGuiOverlay()), matrices2, f19, f20, f19 + 13.0F, f20 + 2.0F, -16777216);
               int n7 = stack.getItemBarColor() | -16777216;
               HN(client.getBufferBuilders().getEffectVertexConsumers().getBuffer(RenderLayer.getGuiOverlay()), matrices2, f19, f20, f19 + (float)n6, f20 + 1.0F, n7);
            }

            matrices2.scale(0.5F, 0.5F, 0.5F);
            if (stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
               float f21;
               if (flag) {
                  f21 = 1.0F;
               } else {
                  f21 = -13.5F;
               }

               this.HO(matrices2, "God", (f14 + 2.0F) * 2.0F, (f16 + f21) * 2.0F, -3977663);
            } else if (flag3) {
               this.HP(matrices2, stack, f14 + 2.0F, f16);
            }

            matrices2.pop();
         }

         if (flag4 && stack.isDamageable()) {
            matrices2.scale(0.5F, 0.5F, 0.5F);
            int n8 = stack.getMaxDamage();
            int n9 = stack.getDamage();
            int n10 = (int)((float)(n8 - n9) / (float)n8 * 100.0F);
            String str3 = n10 + "%";
            this.HO(
               matrices2,
               str3,
               (f14 + 2.0F) * 2.0F,
               (f16 - 4.5F) * 2.0F,
               ColorUtils.iz((float)(n8 - n9) / (float)n8 * 120.0F, 100.0F, 50.0F, 1.0F).getRGB()
            );
            matrices2.scale(2.0F, 2.0F, 2.0F);
         }

         f14 += 16.0F;
      }

      this.HO(matrices2, str, -f2, 0.0F, n2);
      ItemStack stack2 = player2.getMainHandStack();
      if (!stack2.isEmpty() && (Boolean)this.itemName.atx()) {
         matrices2.scale(0.5F, 0.5F, 0.5F);
         float f22;
         if (flag4) {
            f22 = f16 - 10.0F;
         } else {
            f22 = f16 - 5.5F;
         }

         this.HR(matrices2, stack2, 0.0F, f22);
         matrices2.scale(2.0F, 2.0F, 2.0F);
      }

      matrices2.pop();
   }

   private void HM(PlayerEntity player2) {
      this.list.clear();
      ItemStack stack = player2.getMainHandStack();
      if (!stack.isEmpty()) {
         this.list.add(stack);
      }

      for (int n = 3; n >= 0; n--) {
         int n2 = n;
         ItemStack stack2 = (ItemStack)player2.getInventory().armor.get(n2);
         if (!stack2.isEmpty()) {
            this.list.add(stack2);
         }
      }

      ItemStack stack3 = player2.getOffHandStack();
      if (!stack3.isEmpty()) {
         this.list.add(stack3);
      }
   }

   private static void HN(VertexConsumer vertexConsumer, MatrixStack matrices2, float f2, float f3, float f4, float f5, int n) {
      float f6 = Argb.getAlpha(n) / 255.0F;
      float f7 = Argb.getRed(n) / 255.0F;
      float f8 = Argb.getGreen(n) / 255.0F;
      float f9 = Argb.getBlue(n) / 255.0F;
      Matrix4f matrix4f = matrices2.peek().getPositionMatrix();
      vertexConsumer.vertex(matrix4f, f2, f3, 0.0F).color(f7, f8, f9, f6);
      vertexConsumer.vertex(matrix4f, f2, f5, 0.0F).color(f7, f8, f9, f6);
      vertexConsumer.vertex(matrix4f, f4, f5, 0.0F).color(f7, f8, f9, f6);
      vertexConsumer.vertex(matrix4f, f4, f3, 0.0F).color(f7, f8, f9, f6);
   }

   private void HO(MatrixStack matrices2, String str, float f2, float f3, int n) {
      if (Font.kT().awg()) {
         matrices2.push();
         float f4 = f3 - 1.0F;
         matrices2.translate(f2, f4, 0.0F);
         matrices2.scale(0.5F, 0.5F, 0.5F);
         FontLoader.fontRenderer2.aaR(matrices2, str, 0.0, 0.0, n);
         matrices2.pop();
      } else {
         Immediate immediate = client.getBufferBuilders().getEffectVertexConsumers();
         TextRendererMixin temp = (TextRendererMixin)client.textRenderer;
         Matrix4f temp2 = matrices2.peek().getPositionMatrix();
         TextLayerType textLayerType = TextLayerType.SEE_THROUGH;
         Matrix4f matrix4f = temp2;
         temp.hookDrawLayer(str, f2, f3, n, true, matrix4f, immediate, textLayerType, 0, 15728880);
         temp = (TextRendererMixin)client.textRenderer;
         temp2 = matrices2.peek().getPositionMatrix();
         textLayerType = TextLayerType.SEE_THROUGH;
         matrix4f = temp2;
         temp.hookDrawLayer(str, f2, f3, n, false, matrix4f, immediate, textLayerType, 0, 15728880);
      }
   }

   private void HP(MatrixStack matrices2, ItemStack stack, float f2, float f3) {
      if (stack.hasEnchantments()) {
         Set set = EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries();
         float f4 = 0.0F;

         for (Iterator iterator2 = set.iterator(); iterator2.hasNext(); f4 += 4.5F) {
            it.unimi.dsi.fastutil.objects.Object2IntMap.Entry entry2 = (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry)iterator2.next();
            int n = entry2.getIntValue();
            String str = Enchantment.getName((RegistryEntry)entry2.getKey(), n).getString();
            String str2;
            if (str.contains("Vanish")) {
               str2 = "Van";
            } else if (str.contains("Bind")) {
               str2 = "Bind";
            } else {
               byte b;
               if (n > 1) {
                  b = 2;
               } else {
                  b = 3;
               }

               byte b2 = b;
               String str3;
               if (str.length() > b2) {
                  String temp = str;
                  byte b3 = b2;
                  str3 = temp.substring(0, b3);
               } else {
                  str3 = str;
               }

               str2 = str3 + n;
            }

            this.HO(matrices2, str2, f2 * 2.0F, (f3 + f4) * 2.0F, -1);
         }
      }
   }

   private float HQ(int n) {
      if ((Boolean)this.enchantments.atx() && n > 3) {
         return -14.0F - (n - 3) * 4.5F;
      }

      float f2;
      if ((Boolean)this.armor.atx()) {
         f2 = -18.0F;
      } else {
         f2 = -3.5F;
      }

      return f2;
   }

   private void HR(MatrixStack matrices2, ItemStack stack, float f2, float f3) {
      String str = stack.getName().getString();
      float f4;
      if (Font.kT().awg()) {
         f4 = FontLoader.fontRenderer2.abc(str) / 2.0F;
      } else {
         f4 = client.textRenderer.getWidth(str);
      }

      float f5 = f4 / 4.0F;
      this.HO(matrices2, str, (f2 - f5) * 2.0F, f3 * 2.0F, -1);
   }

   private String HS(PlayerEntity player2) {
      StringBuilder stringBuilder = new StringBuilder();
      PotionTracker potionTracker = PotionTracker.Ec();
      if (potionTracker != null && potionTracker.Ee()) {
         StringBuilder temp = stringBuilder;
         String str = potionTracker.Eh(player2);
         temp.append(str);
      }

      StringBuilder stringBuilder2 = stringBuilder;
      String str2 = player2.getName().getString();
      stringBuilder2.append(str2).append(' ');
      if ((Boolean)this.entityId.atx()) {
         stringBuilder2 = stringBuilder.append("ID: ");
         int n = player2.getId();
         stringBuilder2.append(n).append(' ');
      }

      if ((Boolean)this.gamemode.atx()) {
         if (player2.isCreative()) {
            stringBuilder.append("[C] ");
         } else if (player2.isSpectator()) {
            stringBuilder.append("[I] ");
         } else {
            stringBuilder.append("[S] ");
         }
      }

      if ((Boolean)this.ping.atx() && client.getNetworkHandler() != null) {
         ClientPlayNetworkHandler network2 = client.getNetworkHandler();
         UUID uuid = player2.getGameProfile().getId();
         PlayerListEntry playerListEntry = network2.getPlayerListEntry(uuid);
         if (playerListEntry != null) {
            stringBuilder2 = stringBuilder;
            int n2 = playerListEntry.getLatency();
            stringBuilder2.append(n2).append("ms ");
         }
      }

      if ((Boolean)this.health.atx()) {
         double d = player2.getHealth() + player2.getAbsorptionAmount();
         if (!Double.isNaN(d) && !Double.isInfinite(d)) {
            Formatting formatting;
            if (d > 18.0) {
               formatting = Formatting.GREEN;
            } else if (d > 16.0) {
               formatting = Formatting.DARK_GREEN;
            } else if (d > 12.0) {
               formatting = Formatting.YELLOW;
            } else if (d > 8.0) {
               formatting = Formatting.GOLD;
            } else if (d > 4.0) {
               formatting = Formatting.RED;
            } else {
               formatting = Formatting.DARK_RED;
            }

            long l = Math.round(d * 10.0);
            stringBuilder2 = stringBuilder.append(formatting);
            double d2 = l / 10.0;
            stringBuilder2.append(d2).append(' ');
         }
      }

      if ((Boolean)this.totems.atx() && player2 != client.player) {
         int n3 = Managers.entityEntityStatusS2CPacketManager.acp(player2);
         if (n3 > 0) {
            stringBuilder2 = stringBuilder;
            Formatting formatting2 = HT(n3);
            stringBuilder2 = stringBuilder2.append(formatting2);
            int n4 = -n3;
            stringBuilder2.append(n4).append(' ');
         }
      }

      return stringBuilder.toString().trim();
   }

   private static Formatting HT(int n) {
      if (n > 5) {
         return Formatting.DARK_RED;
      } else if (n > 4) {
         return Formatting.RED;
      } else if (n > 3) {
         return Formatting.GOLD;
      } else if (n > 2) {
         return Formatting.YELLOW;
      } else {
         return n > 1 ? Formatting.DARK_GREEN : Formatting.GREEN;
      }
   }

   private int HU(PlayerEntity player2) {
      if (player2 == client.player) {
         return ColorHelper3.auO().auY(255);
      } else if (Managers.friendManager.awt(player2.getName())) {
         return ColorHelper.wx().wC();
      } else if (player2.isInvisible()) {
         return -56064;
      } else if (player2 instanceof PlayerWorldOtherClientPlayerEntity) {
         return -1113785;
      } else {
         return player2.isSneaking() ? -26368 : -1;
      }
   }

   public Float HV() {
      return (Float)this.worldTextScale.atx();
   }

   public Boolean HW() {
      return (Boolean)this.factor.atx();
   }

   public Float HX() {
      return (Float)this.scaling.atx();
   }

   public Float HY() {
      return (Float)this.distance.atx();
   }

   static boolean HZ(long l, Entry entry2) {
      boolean flag;
      if (l - (Long)entry2.getValue() > 1000L) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isFadeStartVisible() {
      return (Boolean)this.droppedItems.atx();
   }

   private Boolean isItemsRangeVisible() {
      return (Boolean)this.droppedItems.atx();
   }

   private static ArrayList e() {
      return new ArrayList(6);
   }
}
