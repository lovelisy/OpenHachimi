package pw.hachimi.client.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.io.IOUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.TippedArrowItem;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.setting.BooleanSetting5;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.player.UnidentifiedUtils6;
import pw.hachimi.eventbus.annotation.EventListener;

public final class StackPotionContentsComponentManager extends Module3 {
   public static final HashMap a_ = new HashMap();
   private static StackPotionContentsComponentManager INSTANCE;
   Setting textSetting1 = this.registerSetting(createStringSetting("Saved", "You cant see this setting", StackPotionContentsComponentManager::isTextSetting1Visible));
   Setting textSetting2 = this.registerSetting(createStringSetting("Name", "The name of the kit"));
   private static final Map d_ = new HashMap();
   private static final Map e_ = new HashMap();
   private static final Map f_ = new HashMap();
   private static final long a = 0L;
   private static final String[] d = null;
   private static final String[] e = null;
   private static final Map f = new HashMap();

   public StackPotionContentsComponentManager() {
      super("Kit", "Save your kit", ModuleCategory.MISC);
      INSTANCE = this;
      this.registerSetting(a("Load", "Load kit from file", this::Pm));
      this.registerSetting(a("Save", "Save kit from your inventory", this::Pl));
   }

   public static StackPotionContentsComponentManager OY() {
      return INSTANCE;
   }

   private static String OZ(ItemStack stack) {
      Item item2 = stack.getItem();
      String str = (String)d_.get(item2);
      if (str != null) {
         return str;
      }

      StringBuilder stringBuilder = new StringBuilder(stack.getItem().getTranslationKey());
      if (!Pa(item2)) {
         String str2 = stringBuilder.toString();
         d_.put(item2, str2);
         return str2;
      }

      PotionContentsComponent potionContentsComponent = PotionContentsComponent.DEFAULT;
      ComponentType componentType = DataComponentTypes.POTION_CONTENTS;
      Iterator iterator2 = ((PotionContentsComponent)stack.getOrDefault(componentType, potionContentsComponent)).getEffects().iterator();

      while (iterator2.hasNext()) {
         StatusEffectInstance statusEffectInstance = (StatusEffectInstance)iterator2.next();
         StringBuilder temp = stringBuilder.append(':');
         String str3 = statusEffectInstance.getTranslationKey();
         temp = temp.append(str3).append('-');
         int n = statusEffectInstance.getAmplifier();
         temp.append(n);
      }

      return stringBuilder.toString();
   }

   private static boolean Pa(Item item2) {
      boolean flag;
      if (!(item2 instanceof PotionItem) && !(item2 instanceof TippedArrowItem)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static UnidentifiedUtils6 Pb(ItemStack stack) {
      return Pc(OZ(stack), stack.getMaxCount(), false);
   }

   public static UnidentifiedUtils6 Pc(String str, int n, boolean flag) {
      if (flag) {
         int n2 = str.indexOf(58);
         if (n2 != -1) {
            int n3 = n2 + 1;
            int n4 = str.indexOf(58, n3);
            if (n4 != -1) {
               int n5 = n4 + 1;
               str = str.substring(n5);
            }
         }

         UnidentifiedUtils6 unidentifiedUtils6 = new UnidentifiedUtils6(str, n);
         String str2 = unidentifiedUtils6.text;
         UnidentifiedUtils6 unidentifiedUtils62 = (UnidentifiedUtils6)e_.get(str2);
         UnidentifiedUtils6 unidentifiedUtils63;
         if (unidentifiedUtils62 != null) {
            unidentifiedUtils63 = unidentifiedUtils62;
         } else {
            unidentifiedUtils63 = unidentifiedUtils6;
         }

         return unidentifiedUtils63;
      } else {
         return (UnidentifiedUtils6)e_.get(str);
      }
   }

   public static int Pd(ItemStack stack) {
      return Pe(OZ(stack));
   }

   public static int Pe(String str) {
      Map temp = f_;
      Integer integer = 0;
      return (Integer)temp.getOrDefault(str, integer);
   }

   @EventListener
   public void onOnGameJoin(OnGameJoinEvent onGameJoinEvent) {
      this.Ph(false);
   }

   public String Pg() {
      return (String)this.textSetting1.atx();
   }

   public void Ph(boolean flag) {
      String str = (String)this.textSetting1.atx();
      a_.clear();
      e_.clear();
      f_.clear();
      File file2 = new File(Bootstrap.clientPaths.file, str + ".kit");

      try {
         if (file2.exists()) {
            List lines;
            try (FileInputStream fileInputStream = new FileInputStream(file2)) {
               lines = IOUtils.readLines(fileInputStream, StandardCharsets.UTF_8);
            }

            Iterator iterator2 = lines.iterator();

            while (iterator2.hasNext()) {
               String line = (String)iterator2.next();
               String[] parts = line.split(":");
               UnidentifiedUtils6 unidentifiedUtils6 = Pc(line, Integer.parseInt(parts[1]), true);
               a_.put(Integer.parseInt(parts[0]), unidentifiedUtils6);
            }

            Iterator iterator3 = a_.values().iterator();

            while (iterator3.hasNext()) {
               UnidentifiedUtils6 unidentifiedUtils62 = (UnidentifiedUtils6)iterator3.next();
               e_.putIfAbsent(unidentifiedUtils62.text, unidentifiedUtils62);
               f_.merge(unidentifiedUtils62.text, 1, (a, b) -> (Integer)a + (Integer)b);
            }

            d_.clear();
            ChatUtils.UB("§fThe kit §s" + str + " §fhas been §sloaded§f!");
            return;
         }
      } catch (Exception exception) {
         Bootstrap.Ob("Failed to load kit " + str);
         if (flag) {
            ChatUtils.UL("§cFailed to load kit " + str);
         }

         exception.printStackTrace();
      }

      if (flag) {
         ChatUtils.UL("§cCould not found kit " + str);
      }
   }

   public void Pi(String str) {
      if (client.player != null && client.world != null) {
         try {
            if ((new File(Bootstrap.clientPaths.file, str + ".kit")).exists()) {
               this.textSetting1.aty(str);
               this.Ph(true);
            } else {
               ChatUtils.UL("§cCould not found kit " + str);
            }
         } catch (Exception exception) {
            Bootstrap.Ob("Failed to load kit " + str);
            ChatUtils.UL("§cFailed to load kit " + str);
            exception.printStackTrace();
         }
      }
   }

   public void Pj(String str) {
      if (client.player != null && client.world != null) {
         try {
            File file2 = new File(Bootstrap.clientPaths.file, str + ".kit");

            try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file2), StandardCharsets.UTF_8))) {
               for (int n = 0; n < 41; n++) {
                  ItemStack stack = client.player.getInventory().getStack(n);
                  printWriter.println(n + ":" + stack.getMaxCount() + ":" + OZ(stack));
               }
            }

            ChatUtils.UB("§fThe kit §s" + str + " §fhas been §ssaved§f!");
         } catch (Exception exception) {
            Bootstrap.Ob("Failed to save kit " + str);
            ChatUtils.UL("§cFailed to save kit " + str);
            exception.printStackTrace();
         }
      }
   }

   public void Pk(String str) {
      if (client.player != null && client.world != null) {
         try {
            File file2 = new File(Bootstrap.clientPaths.file, str + ".kit");
            if (file2.exists()) {
               file2.delete();
               ChatUtils.UB("§fThe kit §s" + str + " §fhas been §sdeleted§f!");
            } else {
               ChatUtils.UL("§cCould not found kit " + str);
            }
         } catch (Exception exception) {
            Bootstrap.Ob("Failed to delete kit " + str);
            ChatUtils.UL("§cFailed to delete kit " + str);
            exception.printStackTrace();
         }
      }
   }

   private void Pl() {
      this.Pj((String)this.textSetting2.atx());
   }

   private void Pm() {
      this.Pi((String)this.textSetting2.atx());
   }

   private static Boolean isTextSetting1Visible() {
      return false;
   }

   private static StringSetting createStringSetting(String str, String str2, Supplier supplier) {
      return new StringSetting(str, str2, "default", supplier);
   }

   private static BooleanSetting5 a(String str, String str2, Runnable runnable) {
      return new BooleanSetting5(str, str2, runnable);
   }

   private static StringSetting createStringSetting(String str, String str2) {
      return new StringSetting(str, str2, "default");
   }

   private static String a(byte[] bs) {
      int n = 0;
      int n2;
      char[] cs = new char[n2 = bs.length];

      for (int n3 = 0; n3 < n2; n3++) {
         int n4;
         if ((n4 = 255 & bs[n3]) < 192) {
            cs[n++] = (char)n4;
         } else if (n4 < 224) {
            char c2 = (char)((char)(n4 & 31) << 6);
            byte b = bs[++n3];
            c2 = (char)(c2 | (char)(b & 63));
            cs[n++] = c2;
         } else if (n3 < n2 - 2) {
            char c3 = (char)((char)(n4 & 15) << '\f');
            byte b2 = bs[++n3];
            c3 = (char)(c3 | (char)(b2 & 63) << 6);
            b2 = bs[++n3];
            c3 = (char)(c3 | (char)(b2 & 63));
            cs[n++] = c3;
         }
      }

      return new String(cs, 0, n);
   }

   private static String c(int n, long l) throws InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
      int n2 = n ^ (int)(l & 32767L) ^ 19186;
      if (e[n2] == null) {
         Object[] objs;
         try {
            Long longValue = Thread.currentThread().threadId();
            objs = (Object[])f.get(longValue);
            if (objs == null) {
               objs = new Object[]{Cipher.getInstance("DES/CBC/PKCS5Padding"), SecretKeyFactory.getInstance("DES"), new IvParameterSpec(new byte[8])};
               f.put(longValue, objs);
            }
         } catch (Exception exception) {
            throw new RuntimeException("pw/hachimi/client/\u0000jo\u200e", exception);
         }

         byte[] bs = new byte[8];
         bs[0] = (byte)(l >>> 56);

         for (int n3 = 1; n3 < 8; n3++) {
            bs[n3] = (byte)(l << n3 * 8 >>> 56);
         }

         DESKeySpec desKeySpec = new DESKeySpec(bs);
         SecretKey secretKey = ((SecretKeyFactory)objs[1]).generateSecret(desKeySpec);
         ((Cipher)objs[0]).init(2, secretKey, (IvParameterSpec)objs[2]);
         byte[] bs2 = d[n2].getBytes("ISO-8859-1");
         e[n2] = a(((Cipher)objs[0]).doFinal(bs2));
      }

      return e[n2];
   }

   private static Object c(MutableCallSite mutableCallSite, Object[] objs) throws InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
      int n = (Integer)objs[0];
      long l = (Long)objs[1];
      String str = c(n, l);
      MethodHandle methodHandle = MethodHandles.constant(String.class, str);
      mutableCallSite.setTarget(MethodHandles.dropArguments(methodHandle, 0, int.class, long.class));
      return str;
   }

   private static CallSite c(Lookup lookup, String str, MethodType methodType) {
      MutableCallSite mutableCallSite = new MutableCallSite(methodType);

      try {
         MethodHandle methodHandle = lookup.findStatic(StackPotionContentsComponentManager.class, "c", MethodType.methodType(Object.class, MutableCallSite.class, Object[].class));
         methodHandle = methodHandle.asCollector(Object[].class, methodType.parameterCount());
         methodHandle = MethodHandles.insertArguments(methodHandle, 0, mutableCallSite);
         mutableCallSite.setTarget(MethodHandles.explicitCastArguments(methodHandle, methodType));
      } catch (Exception exception) {
         throw new RuntimeException("pw/hachimi/client/\u0000jo\u200e : " + str + " : " + methodType, exception);
      }

      return mutableCallSite;
   }
}
