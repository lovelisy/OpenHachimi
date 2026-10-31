package pw.hachimi.client.module.combat;

import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.player.StackPlayerUtils2;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockStateUtils;
import pw.hachimi.client.utils.world.Vec3dEntityUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class MainHand extends ClientModule {
   private static MainHand INSTANCE;
   public final Setting totem = this.registerSetting(new BooleanSetting("Totem", "Swaps to a totem in your mainhand", false));
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting health = this.registerSetting(new NumberSetting("Health", "The health required to fall below before swapping to a totem", 0.0F, 14.0F, 36.0F));
   Setting crapple = this.registerSetting(new BooleanSetting("Crapple", "Uses a normal golden apple if Absorption is present", true));
   Setting swordGap = this.registerSetting(new BooleanSetting("SwordGap", "Equips a golden apple if holding down the item use button", false));
   Setting pickaxeGap = this.registerSetting(new BooleanSetting("PickaxeGap", "Equips a golden apple if holding down the item use button", false));
   Setting switchBack = this.registerSetting(new BooleanSetting("SwitchBack", "Switch to old slot", true));
   Setting lethal = this.registerSetting(new BooleanSetting("Lethal", "Calculates lethal damage sources", false));
   Setting fall = this.registerSetting(new BooleanSetting("Fall", "Calculates fall damage sources", false));
   Setting totemSlot = this.registerSetting(new NumberSetting("TotemSlot", "Slot to use for mainhand totem", 1, 1, 9, this::isTotemSlotVisible));
   Setting totemGap = this.registerSetting(new BooleanSetting("TotemGap", "Equips a golden apple if holding down the item use button", true, this::isTotemGapVisible));
   Setting always = this.registerSetting(new BooleanSetting("Always", "Always equips a golden apple if holding down the item use button", true, this::isAlwaysVisible));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "The item steal delay", 0.0F, 0.15F, 2.0F));
   boolean n;
   int o;
   private boolean p;
   Setting chorus;
   private Item item;
   public static boolean ef;

   public MainHand() {
      super("MainHand", "Automatically replenishes the item in your main hand", ModuleCategory.COMBAT);
      KeybindSetting keybindSetting = new KeybindSetting("Chorus", "Swaps chorus item when click", i(this.Kn() + "-chorusswap", this::isChorusVisible));
      this.chorus = this.registerSetting(keybindSetting);
      INSTANCE = this;
      Managers.playerScreenManager.akr((UnidentifiedHelper17)this.chorus.atx());
   }

   public static MainHand hz() {
      return INSTANCE;
   }

   private void isChorusVisible() {
      if (this.awg()) {
         boolean flag;
         if (!this.p) {
            flag = true;
         } else {
            flag = false;
         }

         this.p = flag;
      }
   }

   @Override
   public void awe() {
      this.item = null;
   }

   public boolean hB() {
      boolean flag;
      if (this.awg() && this.hD() && (Boolean)this.totem.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   @EventListener(priority = 2147483646)
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (client.player != null && tickPreEvent.getEventPhase() == EventPhase.PRE) {
         int n2 = (Integer)this.totemSlot.atx() - 1;
         boolean flag = this.hD();
         if (flag && (Boolean)this.totem.atx() && !this.p) {
            this.item = Items.TOTEM_OF_UNDYING;
            if (Managers.inventoryTracker.JW().getItem() != Items.TOTEM_OF_UNDYING
               && Managers.inventoryTracker.JS() != n2
               && client.player.getInventory().getStack(n2).getItem() == Items.TOTEM_OF_UNDYING) {
               if ((Boolean)this.switchBack.atx()) {
                  this.o = client.player.getInventory().selectedSlot;
               }

               client.player.getInventory().selectedSlot = n2;
               Managers.inventoryTracker.JF(n2);
            }

            Managers.inventoryTracker.JI();
         }

         if (this.p) {
            ItemCooldownManager temp = client.player.getItemCooldownManager();
            Item item2 = Items.CHORUS_FRUIT;
            if (temp.isCoolingDown(item2)) {
               this.p = false;
               this.onTickPre(tickPreEvent);
               return;
            }

            this.item = Items.CHORUS_FRUIT;
            if (client.player.getMainHandStack().getItem() != Items.CHORUS_FRUIT
               && client.player.getInventory().selectedSlot != n2
               && client.player.getInventory().getStack(n2).getItem() == Items.CHORUS_FRUIT) {
               Managers.inventoryTracker.JU(n2);
            }
         } else if (!client.options.useKey.isPressed()
            || !(Boolean)this.totemGap.atx()
            || !(Boolean)this.totem.atx()
            || client.player.getInventory().selectedSlot != n2
            || flag && !(Boolean)this.always.atx()) {
            if (!flag) {
               if (this.n) {
                  this.n = false;
                  this.item = Items.TOTEM_OF_UNDYING;
               }

               Item item3 = client.player.getMainHandStack().getItem();
               if (!client.options.useKey.isPressed()
                  || (!(item3 instanceof PickaxeItem) || !(Boolean)this.pickaxeGap.atx()) && (!(item3 instanceof SwordItem) || !(Boolean)this.swordGap.atx())) {
                  if (!client.options.useKey.isPressed() || (Boolean)this.totem.atx() && client.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
                     this.item = Items.TOTEM_OF_UNDYING;
                     if (this.o != -1) {
                        Managers.inventoryTracker.JU(this.o);
                        this.o = -1;
                     }
                  }
               } else {
                  int n3 = -1;

                  for (int n4 = 0; n4 < 9; n4++) {
                     PlayerInventory playerInventory = client.player.getInventory();
                     int n5 = n4;
                     if (playerInventory.getStack(n5).getItem() == this.hF()) {
                        n3 = n4;
                        break;
                     }
                  }

                  if (n3 != -1) {
                     this.o = client.player.getInventory().selectedSlot;
                     Managers.inventoryTracker.JU(n3);
                  }
               }
            }
         } else {
            HitResult hitResult = client.crosshairTarget;
            if (hitResult instanceof BlockHitResult) {
               ClientWorld world2 = client.world;
               BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
               if (!BlockStateUtils.Ww(world2.getBlockState(pos))) {
                  this.item = this.hF();
                  this.n = true;
               }
            } else {
               this.item = this.hF();
               this.n = true;
            }
         }

         if ((Boolean)this.totem.atx()
            && this.stopwatch.afq((Float)this.delay.atx() * 1000.0F)
            && this.item != null
            && client.player.getInventory().getStack(n2).getItem() != this.item) {
            for (int n6 = 35; n6 >= 0; n6--) {
               PlayerInventory playerInventory2 = client.player.getInventory();
               int n7 = n6;
               if (playerInventory2.getStack(n7).getItem() == this.item) {
                  int n8;
                  if (n6 < 9) {
                     n8 = n6 + 36;
                  } else {
                     n8 = n6;
                  }

                  ClientPlayerEntity player2 = client.player;
                  SlotActionType slotActionType = SlotActionType.SWAP;
                  client.interactionManager.clickSlot(0, n8, n2, slotActionType, (PlayerEntity)player2);
                  this.stopwatch.afr(29432, 5265492, (byte)40);
                  this.item = null;
                  return;
               }
            }
         }
      }
   }

   public boolean hD() {
      float f = WorldUtils.Nc();
      boolean flag;
      if (!(f <= (Float)this.health.atx())
         && (!(Boolean)this.lethal.atx() || !this.hE(f))
         && (!(WorldUtils.Nd(client.player.fallDistance, 1.0F) + 0.5F > client.player.getHealth()) || !(Boolean)this.fall.atx())) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private boolean hE(float f) {
      Iterator iterator2 = Lists.newArrayList(client.world.getEntities()).iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2 != null && entity2.isAlive() && entity2 instanceof EndCrystalEntity && !(client.player.squaredDistanceTo(entity2) > 144.0)) {
            double d = Vec3dEntityUtils.vK(client.player, ((EndCrystalEntity)entity2).getPos(), false);
            if (!(f - d > ((Float)this.health.atx()).floatValue())) {
               return true;
            }
         }
      }

      return false;
   }

   private Item hF() {
      if ((Boolean)this.crapple.atx() && StackPlayerUtils2.abI(Items.GOLDEN_APPLE)) {
         RegistryEntry registryEntry = StatusEffects.ABSORPTION;
         if (client.player.hasStatusEffect(registryEntry) || !StackPlayerUtils2.abI(Items.ENCHANTED_GOLDEN_APPLE)) {
            return Items.GOLDEN_APPLE;
         }
      }

      return Items.ENCHANTED_GOLDEN_APPLE;
   }

   private Boolean isAlwaysVisible() {
      boolean flag;
      if ((Boolean)this.totem.atx() && (Boolean)this.totemGap.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isTotemGapVisible() {
      return (Boolean)this.totem.atx();
   }

   private Boolean isTotemSlotVisible() {
      return (Boolean)this.totem.atx();
   }

   private static UnidentifiedHelper17 i(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, -1, runnable);
   }
}
