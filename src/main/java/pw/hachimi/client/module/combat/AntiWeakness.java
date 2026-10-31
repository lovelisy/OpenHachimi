package pw.hachimi.client.module.combat;

import java.util.function.Predicate;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.entry.RegistryEntry;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.handler.EntityHandler;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.world.EntityInteractType;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.player.EnchantmentUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.eventbus.annotation.EventListener;

public class AntiWeakness extends ClientModule {
   public static AntiWeakness INSTANCE;
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "anti weak switch delay", 0.0F, 5.0F, 100.0F));
   StopwatchImpl stopwatchImpl = new StopwatchImpl();
   boolean d;

   public AntiWeakness() {
      super("AntiWeakness", "Prevent weakness inability to attack", ModuleCategory.COMBAT);
      INSTANCE = this;
   }

   @EventListener(priority = 99)
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null && client.world != null && !this.d) {
         if (this.stopwatchImpl.afq((Float)this.delay.atx() * 1000.0F)) {
            Packet packet2 = sendImmediatelyEvent.getPacket();
            if (packet2 instanceof EntityHandler && ((EntityHandler)packet2).Qd() == EntityInteractType.ATTACK) {
               ItemStack stack = Managers.inventoryTracker.JW();
               if (stack.getItem() instanceof PickaxeItem || stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem) {
                  return;
               }

               RegistryEntry registryEntry = StatusEffects.WEAKNESS;
               StatusEffectInstance statusEffectInstance = client.player.getStatusEffect(registryEntry);
               RegistryEntry registryEntry2 = StatusEffects.STRENGTH;
               StatusEffectInstance statusEffectInstance2 = client.player.getStatusEffect(registryEntry2);
               if (statusEffectInstance == null || statusEffectInstance2 != null && statusEffectInstance.getAmplifier() < statusEffectInstance2.getAmplifier()) {
                  return;
               }

               Predicate temp = new Predicate() {
                  @Override
                  public boolean test(Object sendImmediatelyEvent) {
                     return AntiWeakness.ju((ItemStack)sendImmediatelyEvent);
                  }
               };
               Setting setting2 = this.swapAction;
               StackSlotHelper stackSlotHelper = new StackSlotHelper(temp, (SwapMode)setting2.atx()).LK();
               if (stackSlotHelper.LL()) {
                  int n = Managers.inventoryTracker.JS();
                  this.d = true;
                  stackSlotHelper.LO(n);
                  Managers.serverManager.iP(sendImmediatelyEvent.getPacket());
                  stackSlotHelper.LP(n);
                  this.d = false;
                  this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                  sendImmediatelyEvent.aim();
               }
            }
         }
      }
   }

   public boolean js() {
      Item item2 = Managers.inventoryTracker.JW().getItem();
      if (!(item2 instanceof SwordItem) && !(item2 instanceof PickaxeItem)) {
         RegistryEntry registryEntry = StatusEffects.WEAKNESS;
         StatusEffectInstance statusEffectInstance = client.player.getStatusEffect(registryEntry);
         RegistryEntry registryEntry2 = StatusEffects.STRENGTH;
         StatusEffectInstance statusEffectInstance2 = client.player.getStatusEffect(registryEntry2);
         if (statusEffectInstance != null && (statusEffectInstance2 == null || statusEffectInstance.getAmplifier() >= statusEffectInstance2.getAmplifier())) {
            return !INSTANCE.awg() ? false : new StackSlotHelper(new Predicate() {
               @Override
               public boolean test(Object obj) {
                  return AntiWeakness.jt((ItemStack)obj);
               }
            }, (SwapMode)this.swapAction.atx()).LK().LL();
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   static boolean jt(ItemStack stack) {
      return stack.getItem() instanceof SwordItem;
   }

   static boolean ju(ItemStack stack) {
      boolean flag;
      if (EnchantmentUtils.VN(stack, Enchantments.SHARPNESS) > 0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
