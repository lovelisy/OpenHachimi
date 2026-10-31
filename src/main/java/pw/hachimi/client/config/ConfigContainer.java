package pw.hachimi.client.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MinecraftClient;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.api.UnidentifiedInterface12;
import pw.hachimi.client.api.UnidentifiedInterface8;
import pw.hachimi.client.manager.TextManager;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.BooleanSetting4;
import pw.hachimi.client.setting.BooleanSetting5;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.ItemListSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.client.utils.UnidentifiedHelper17;

public class ConfigContainer implements UnidentifiedInterface8, UnidentifiedInterface12 {
   public static MinecraftClient client;
   public static Random random2 = ThreadLocalRandom.current();
   protected final String text2;
   private final Map map;
   private static boolean c;

   public ConfigContainer(String str) {
      LinkedHashMap linkedHashMap = new LinkedHashMap();
      this.map = Collections.synchronizedMap(linkedHashMap);
      this.text2 = str;
   }

   public Setting registerSetting(Setting setting2) {
      setting2.setConfigContainer(this);
      Map temp = this.map;
      String str = setting2.Kn();
      temp.put(str, setting2);
      return setting2;
   }

   protected void OJ(int n, long l, Setting... value) {
      int n2 = value.length;

      for (int n3 = 0; n3 < n2; n3++) {
         Setting setting2 = value[n3];
         this.registerSetting(setting2);
      }
   }

   public void OK(Setting setting2) {
      Map temp = this.map;
      String str = setting2.Kn();
      temp.remove(str);
   }

   protected void OL(Setting... value) {
      int n = value.length;

      for (int n2 = 0; n2 < n; n2++) {
         Setting setting2 = value[n2];
         this.OK(setting2);
      }
   }

   @Override
   public JsonObject LT(int n, int n2, char c2) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("name", this.getText2());
      jsonObject.addProperty("id", this.Kn());
      JsonArray jsonArray = new JsonArray();
      Iterator iterator2 = this.OQ().iterator();

      while (iterator2.hasNext()) {
         Object obj = iterator2.next();
         if (!(obj instanceof BooleanSetting5) && !(obj instanceof KeybindSetting)) {
            char c3 = 8590;
            char c4 = '겾';
            short s2 = 25681;
            jsonArray.add(((Setting)obj).LT(s2, c4, c3));
         }
      }

      jsonObject.add("configs", jsonArray);
      return jsonObject;
   }

   public Setting OM(JsonObject jsonObject) {
      if (jsonObject.has("configs")) {
         JsonElement jsonElement = jsonObject.get("configs");
         if (!jsonElement.isJsonArray()) {
            return null;
         }

         Iterator iterator2 = jsonElement.getAsJsonArray().iterator();

         while (iterator2.hasNext()) {
            Object obj = iterator2.next();
            if (((JsonElement)obj).isJsonObject()) {
               JsonObject jsonObject2 = ((JsonElement)obj).getAsJsonObject();
               JsonElement jsonElement2 = jsonObject2.get("id");
               if (jsonElement2 != null) {
                  Setting setting2 = this.OP(jsonElement2.getAsString());
                  if (setting2 != null) {
                     MinecraftClient temp = client;
                     final JsonObject jsonObject3 = jsonObject2;
                     final Setting setting3 = setting2;
                     Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                           ConfigContainer.OR(setting3, jsonObject3);
                        }
                     };
                     temp.executeSync(runnable);
                  }
               }
            }
         }
      }

      return null;
   }

   public String getText2() {
      return this.text2;
   }

   public String OO() {
      return TextManager.aip(this.Kn());
   }

   @Override
   public String Kn() {
      Object[] objs = new Object[]{this.text2.toLowerCase()};
      return String.format("%s-container", objs);
   }

   public Setting OP(String str) {
      return (Setting)this.map.get(str);
   }

   public Collection OQ() {
      return this.map.values();
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.OM(jsonObject);
   }

   // $VF: Handled exception range with multiple entry points by splitting it
   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   static void OR(Setting setting2, JsonObject jsonObject) {
      Setting temp = setting2;

      int temp2;
      try {
         Objects.requireNonNull(temp);
         temp2 = setting2 == null
            ? -1
            : (
               0 <= 0 && setting2 instanceof BooleanSetting4
                  ? 0
                  : (
                     0 <= 1 && setting2 instanceof BooleanSetting
                        ? 1
                        : (
                           0 <= 2 && setting2 instanceof ColorSetting
                              ? 2
                              : (
                                 0 <= 3 && setting2 instanceof EnumSetting
                                    ? 3
                                    : (
                                       0 <= 4 && setting2 instanceof ItemListSetting
                                          ? 4
                                          : (
                                             0 <= 5 && setting2 instanceof BlockListSetting
                                                ? 5
                                                : (
                                                   0 <= 6 && setting2 instanceof NumberSetting
                                                      ? 6
                                                      : (0 <= 7 && setting2 instanceof StringSetting ? 7 : (0 <= 8 && setting2 instanceof KeybindSetting ? 8 : 9))
                                                )
                                          )
                                    )
                              )
                        )
                  )
            );
      } catch (Exception exception2) {
         Object[] objs = new Object[]{setting2.getText()};
         Bootstrap.Oc("Couldn't parse Json for {}!", objs);
         exception2.printStackTrace();
         return;
      }

      switch (temp2) {
         case 0:
            try {
               Boolean flag = ((BooleanSetting4)setting2).Km(jsonObject);
               if (flag != ((BooleanSetting4)setting2).atx()) {
                  if (client.world == null || client.player == null) {
                     ((BooleanSetting4)setting2).Kl(flag);
                  } else if (flag) {
                     ((BooleanSetting4)setting2).NR();
                  } else {
                     ((BooleanSetting4)setting2).NS();
                  }
               }
            } catch (Exception exception3) {
               Object[] objs2 = new Object[]{setting2.getText()};
               Bootstrap.Oc("Couldn't parse Json for {}!", objs2);
               exception3.printStackTrace();
            }
            break;
         case 1:
            try {
               Boolean flag2 = ((BooleanSetting)setting2).Km(jsonObject);
               ((BooleanSetting)setting2).Kl(flag2);
            } catch (Exception exception4) {
               Object[] objs3 = new Object[]{setting2.getText()};
               Bootstrap.Oc("Couldn't parse Json for {}!", objs3);
               exception4.printStackTrace();
            }
            break;
         case 2:
            try {
               Color color = ((ColorSetting)setting2).TK(jsonObject);
               ((ColorSetting)setting2).aty(color);
            } catch (Exception exception5) {
               Object[] objs4 = new Object[]{setting2.getText()};
               Bootstrap.Oc("Couldn't parse Json for {}!", objs4);
               exception5.printStackTrace();
            }
            break;
         case 3:
            try {
               Enum enumValue = ((EnumSetting)setting2).mw(jsonObject);
               if (enumValue != null) {
                  ((EnumSetting)setting2).aty(enumValue);
               }
            } catch (Exception exception6) {
               Object[] objs5 = new Object[]{setting2.getText()};
               Bootstrap.Oc("Couldn't parse Json for {}!", objs5);
               exception6.printStackTrace();
            }
            break;
         case 4:
            try {
               List list = ((ItemListSetting)setting2).am(jsonObject);
               ((ItemListSetting)setting2).aty(list);
            } catch (Exception exception7) {
               Object[] objs6 = new Object[]{setting2.getText()};
               Bootstrap.Oc("Couldn't parse Json for {}!", objs6);
               exception7.printStackTrace();
            }
            break;
         case 5:
            try {
               List list2 = ((BlockListSetting)setting2).afX(jsonObject);
               ((BlockListSetting)setting2).aty(list2);
            } catch (Exception exception8) {
               Object[] objs7 = new Object[]{setting2.getText()};
               Bootstrap.Oc("Couldn't parse Json for {}!", objs7);
               exception8.printStackTrace();
            }
            break;
         case 6:
            try {
               Number number = ((NumberSetting)setting2).abs(jsonObject);
               ((NumberSetting)setting2).abr(number);
            } catch (Exception exception9) {
               Object[] objs8 = new Object[]{setting2.getText()};
               Bootstrap.Oc("Couldn't parse Json for {}!", objs8);
               exception9.printStackTrace();
            }
            break;
         case 7:
            try {
               String str = ((StringSetting)setting2).nC(jsonObject);
               ((StringSetting)setting2).aty(str);
            } catch (Exception exception10) {
               Object[] objs9 = new Object[]{setting2.getText()};
               Bootstrap.Oc("Couldn't parse Json for {}!", objs9);
               exception10.printStackTrace();
            }
            break;
         case 8:
            try {
               UnidentifiedHelper17 unidentifiedHelper17 = ((KeybindSetting)setting2).aU(jsonObject);
               ((KeybindSetting)setting2).aT(unidentifiedHelper17.agt());
            } catch (Exception exception11) {
               Object[] objs10 = new Object[]{setting2.getText()};
               Bootstrap.Oc("Couldn't parse Json for {}!", objs10);
               exception11.printStackTrace();
            }
      }
   }

   static {
      b();
   }

   public static void b() {
      c = false;
   }

   public static boolean B() {
      return false;
   }

   public static boolean C() {
      B();
      return true;
   }
}
