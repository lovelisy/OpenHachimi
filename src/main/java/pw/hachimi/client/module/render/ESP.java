package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.render.ESPOption;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.BoxEntityUtils;
import pw.hachimi.client.utils.world.EntityBoxUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class ESP extends ClientModule {
   private static ESP INSTANCE;
   Setting range = this.registerSetting(new NumberSetting("Range", "The ESP render range", 10.0F, 50.0F, 200.0F));
   Setting mode = this.registerSetting(new EnumSetting("Mode", "ESP rendering mode", ESPOption.a_, ESPOption.values()));
   Setting shape = this.registerSetting(new EnumSetting("Shape", "Box rendering mode", ShapeMode.Lines, ShapeMode.values()));
   Setting width = this.registerSetting(new NumberSetting("Width", "ESP rendering line width", 0.1F, 2.0F, 5.0F, this::isWidthVisible));
   Setting players = this.registerSetting(new BooleanSetting("Players", "Render players through walls", true));
   Setting self = this.registerSetting(new BooleanSetting("Self", "Render self through walls", true));
   Setting playersBox = this.registerSetting(new ColorSetting("PlayersBox", "The render color for players", new Color(200, 60, 60), true, this::isPlayersBoxVisible));
   Setting playersFill = this.registerSetting(new ColorSetting("PlayersFill", "The render color for players", new Color(200, 60, 60), true, this::isPlayersFillVisible));
   Setting monsters = this.registerSetting(new BooleanSetting("Monsters", "Render monsters through walls", true));
   Setting monstersBox = this.registerSetting(new ColorSetting("MonstersBox", "The render color for monsters", new Color(200, 60, 60), true, this::isMonstersBoxVisible));
   Setting monstersFill = this.registerSetting(new ColorSetting("MonstersFill", "The render color for monsters", new Color(200, 60, 60), true, this::isMonstersFillVisible));
   Setting animals = this.registerSetting(new BooleanSetting("Animals", "Render animals through walls", true));
   Setting animalsBox = this.registerSetting(new ColorSetting("AnimalsBox", "The render color for animals", new Color(0, 200, 0), true, this::isAnimalsBoxVisible));
   Setting animalsFill = this.registerSetting(new ColorSetting("AnimalsFill", "The render color for animals", new Color(0, 200, 0), true, this::isAnimalsFillVisible));
   Setting vehicles = this.registerSetting(new BooleanSetting("Vehicles", "Render vehicles through walls", false));
   Setting vehiclesBox = this.registerSetting(new ColorSetting("VehiclesBox", "The render color for vehicles", new Color(200, 100, 0), true, this::isVehiclesBoxVisible));
   Setting vehiclesFill = this.registerSetting(new ColorSetting("VehiclesFill", "The render color for vehicles", new Color(200, 100, 0), true, this::isVehiclesFillVisible));
   Setting items = this.registerSetting(new BooleanSetting("Items", "Render dropped items through walls", false));
   Setting itemsBox = this.registerSetting(new ColorSetting("ItemsBox", "The render color for items", new Color(200, 100, 0), true, this::isItemsBoxVisible));
   Setting itemsFill = this.registerSetting(new ColorSetting("ItemsFill", "The render color for items", new Color(200, 100, 0), true, this::isItemsFillVisible));
   Setting endCrystals = this.registerSetting(new BooleanSetting("EndCrystals", "Render end crystals through walls", false));
   Setting endCrystalsBox = this.registerSetting(new ColorSetting("EndCrystalsBox", "The render color for end crystals", new Color(200, 100, 200), true, this::isEndCrystalsBoxVisible));
   Setting endCrystalsFill = this.registerSetting(new ColorSetting("EndCrystalsFill", "The render color for end crystals", new Color(200, 100, 200), true, this::isEndCrystalsFillVisible));

   public ESP() {
      super("ESP", "See entities and objects through walls", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static ESP Ia() {
      return INSTANCE;
   }

   @EventListener
   public void Ib(Render3DContext render3DContext) {
      if (this.mode.atx() == ESPOption.a_) {
         Iterator iterator2 = client.world.getEntities().iterator();

         while (iterator2.hasNext()) {
            Entity entity2 = (Entity)iterator2.next();
            if (entity2 != client.player && !(client.player.squaredDistanceTo(entity2) > ((NumberSetting)this.range).abq()) && this.Ie(entity2)) {
               Color color = this.Ic(entity2);
               Color color2 = this.Id(entity2);
               Box box = EntityBoxUtils.BE(entity2);
               render3DContext.boxRenderer.Zd(box, color2, color, (ShapeMode)this.shape.atx());
            }
         }
      }
   }

   public Color Ic(Entity entity2) {
      if (entity2 instanceof PlayerEntity) {
         return Managers.friendManager.awt(((PlayerEntity)entity2).getName()) ? ColorHelper.wx().wB() : (Color)this.playersBox.atx();
      } else if (BoxEntityUtils.kF(entity2)) {
         return (Color)this.monstersBox.atx();
      } else if (BoxEntityUtils.kH(entity2) || BoxEntityUtils.kI(entity2)) {
         return (Color)this.animalsBox.atx();
      } else if (BoxEntityUtils.kJ(entity2)) {
         return (Color)this.vehiclesBox.atx();
      } else if (entity2 instanceof EndCrystalEntity) {
         return (Color)this.endCrystalsBox.atx();
      } else {
         return entity2 instanceof ItemEntity ? (Color)this.itemsBox.atx() : null;
      }
   }

   public Color Id(Entity entity2) {
      if (entity2 instanceof PlayerEntity) {
         return Managers.friendManager.awt(((PlayerEntity)entity2).getName()) ? ColorHelper.wx().wB() : (Color)this.playersFill.atx();
      } else if (BoxEntityUtils.kF(entity2)) {
         return (Color)this.monstersFill.atx();
      } else if (BoxEntityUtils.kH(entity2) || BoxEntityUtils.kI(entity2)) {
         return (Color)this.animalsFill.atx();
      } else if (BoxEntityUtils.kJ(entity2)) {
         return (Color)this.vehiclesFill.atx();
      } else if (entity2 instanceof EndCrystalEntity) {
         return (Color)this.endCrystalsFill.atx();
      } else {
         return entity2 instanceof ItemEntity ? (Color)this.itemsFill.atx() : null;
      }
   }

   public boolean Ie(Entity entity2) {
      return entity2 instanceof PlayerEntity && (Boolean)this.players.atx()
         ? (Boolean)this.self.atx() || entity2 != client.player
         : BoxEntityUtils.kF(entity2) && (Boolean)this.monsters.atx()
            || (BoxEntityUtils.kH(entity2) || BoxEntityUtils.kI(entity2)) && (Boolean)this.animals.atx()
            || BoxEntityUtils.kJ(entity2) && (Boolean)this.vehicles.atx()
            || entity2 instanceof EndCrystalEntity && (Boolean)this.endCrystals.atx()
            || entity2 instanceof ItemEntity && (Boolean)this.items.atx();
   }

   private Boolean isEndCrystalsFillVisible() {
      return (Boolean)this.endCrystals.atx();
   }

   private Boolean isEndCrystalsBoxVisible() {
      return (Boolean)this.endCrystals.atx();
   }

   private Boolean isItemsFillVisible() {
      return (Boolean)this.items.atx();
   }

   private Boolean isItemsBoxVisible() {
      return (Boolean)this.items.atx();
   }

   private Boolean isVehiclesFillVisible() {
      return (Boolean)this.vehicles.atx();
   }

   private Boolean isVehiclesBoxVisible() {
      return (Boolean)this.vehicles.atx();
   }

   private Boolean isAnimalsFillVisible() {
      return (Boolean)this.animals.atx();
   }

   private Boolean isAnimalsBoxVisible() {
      return (Boolean)this.animals.atx();
   }

   private Boolean isMonstersFillVisible() {
      return (Boolean)this.monsters.atx();
   }

   private Boolean isMonstersBoxVisible() {
      return (Boolean)this.monsters.atx();
   }

   private Boolean isPlayersFillVisible() {
      return (Boolean)this.players.atx() || (Boolean)this.self.atx();
   }

   private Boolean isPlayersBoxVisible() {
      return (Boolean)this.players.atx() || (Boolean)this.self.atx();
   }

   private Boolean isWidthVisible() {
      return this.mode.atx() == ESPOption.a_;
   }
}
