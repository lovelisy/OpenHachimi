package pw.hachimi.client.module.misc;

import java.awt.Color;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.Base64.Encoder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Box;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.model.world.NamePos;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.annotation.EventListener;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.chat.StyleFormattingUtils;

public class Ping2 extends ClientModule {
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Sound", "When someone ping play sound", true));
   Setting numberSetting1 = this.registerSetting(new NumberSetting("ExistTime", "Existence time for mark", 0.0F, 10.0F, 100.0F));
   Setting colorSetting1 = this.registerSetting(new ColorSetting("Color", "color", i()));
   Setting settingSetting1_1;
   ConcurrentHashMap concurrentHashMap;

   public Ping2() {
      super("Ping", "Mark points for your friends", ModuleCategory.MISC);
      StringSetting stringSetting = createStringSetting("EncryptKey", "");
      this.settingSetting1_1 = this.registerSetting(stringSetting);
      this.concurrentHashMap = new ConcurrentHashMap();
      Setting setting2 = this.registerSetting(new KeybindSetting("Ping", "Ping key", d(this.Kn() + "-ping", this::LH)));
      Managers.playerScreenManager.akr((UnidentifiedHelper17)setting2.atx());
   }

   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public static SecretKeySpec Lu(String string) {
       try {
           Charset charset = StandardCharsets.UTF_8;
           byte[] byArray = string.getBytes(charset);
           return Ping2.a(Arrays.copyOf(MessageDigest.getInstance("SHA-256").digest(byArray), 16));
       }
       catch (Exception exception) {
           return null;
       }
   }

   @Override
   public void awe() {
      this.concurrentHashMap.clear();
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         Collection temp = this.concurrentHashMap.values();
         final Ping2 ping2 = this;
         Predicate predicate = new Predicate() {
            @Override
            public boolean test(Object tickPreEvent) {
               return ping2.LG((NamePos)tickPreEvent);
            }
         };
         temp.removeIf(predicate);
      }
   }

   public void Lw() {
      if (this.awg()) {
         HitResult hitResult = client.getCameraEntity().raycast(256.0, 0.0F, false);
         if (hitResult instanceof BlockHitResult) {
            BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
            ClientPlayNetworkHandler temp = client.getNetworkHandler();
            int temp2 = pos.getX();
            int temp3 = pos.getY();
            int temp4 = pos.getZ();
            int n = ((Color)this.colorSetting1.atx()).getRGB();
            int n2 = temp4;
            int n3 = temp3;
            int n4 = temp2;
            String str = this.LC("EnemyHere{" + n4 + "," + n3 + "," + n2 + "," + n + "}");
            temp.sendChatMessage(str);
         }
      }
   }

   @EventListener
   public void Lx(Render3DContext render3DContext) {
      Iterator iterator2 = this.concurrentHashMap.values().iterator();

      while (iterator2.hasNext()) {
         NamePos namePos = (NamePos)iterator2.next();
         render3DContext.boxRenderer
            .Zd(
               m(namePos.getPos().getX() + 0.25, namePos.getPos().getZ() + 0.25, namePos.getPos().getX() + 0.75, namePos.getPos().getZ() + 0.75),
               namePos.color(),
               namePos.color(),
               ShapeMode.Sides
            );
      }
   }

   @EventListener
   private void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof GameMessageS2CPacket && ((GameMessageS2CPacket)packet2).content() != null) {
            MinecraftClient temp = client;
            final GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)packet2;
            final Ping2 ping2 = this;
            Runnable runnable = new Runnable() {
               @Override
               public void run() {
                  ping2.LF(gameMessageS2CPacket);
               }
            };
            temp.execute(runnable);
         }

         packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof ChatMessageS2CPacket) {
            if (((ChatMessageS2CPacket)packet2).unsignedContent() != null) {
               MinecraftClient client2 = client;
               final ChatMessageS2CPacket chatMessageS2CPacket = (ChatMessageS2CPacket)packet2;
               final Ping2 ping22 = this;
               Runnable runnable2 = new Runnable() {
                  @Override
                  public void run() {
                     ping22.LE(chatMessageS2CPacket);
                  }
               };
               client2.execute(runnable2);
            } else {
               MinecraftClient client3 = client;
               final ChatMessageS2CPacket chatMessageS2CPacket2 = (ChatMessageS2CPacket)packet2;
               final Ping2 ping23 = this;
               Runnable runnable3 = new Runnable() {
                  @Override
                  public void run() {
                     ping23.LD(chatMessageS2CPacket2);
                  }
               };
               client3.execute(runnable3);
            }
         }
      }
   }

   private void Lz(String str) {
      if (str != null) {
         try {
            String str2 = this.LB(StyleFormattingUtils.Eq(str).replaceAll("<[^>]*> ", ""));
            if (str2 == null) {
               return;
            }

            if (str2.contains("EnemyHere")) {
               Pattern pattern = Pattern.compile("\\{(.*?)}");
               Matcher matcher = pattern.matcher(str2);
               if (matcher.find()) {
                  String[] strs = matcher.group(1).split(",");
                  if (strs.length == 3) {
                     if ((Boolean)this.booleanSetting1_1.atx()) {
                        client.world.playSound(
                           (PlayerEntity)client.player, client.player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 100.0F, 1.9F
                        );
                     }

                     String str3 = strs[0];
                     String str4 = strs[1];
                     String str5 = strs[2];
                     Pattern pattern2 = Pattern.compile("<(.*?)>");
                     Matcher matcher2 = pattern2.matcher(str);
                     if (!this.LA(str3)) {
                        return;
                     }

                     double d = Double.parseDouble(str3);
                     if (!this.LA(str4)) {
                        return;
                     }

                     double d2 = Double.parseDouble(str4);
                     if (!this.LA(str5)) {
                        return;
                     }

                     double d3 = Double.parseDouble(str5);
                     if (matcher2.find()) {
                        String str6 = matcher2.group(1);
                        NamePos namePos = new NamePos(str6, new Mutable(d, d2, d3), (Color)this.colorSetting1.atx(), new StopwatchImpl());
                        this.concurrentHashMap.put(str6, namePos);
                        ChatUtils.UB(str6 + " marked at §r(" + str3 + ", " + str4 + ", " + str5 + ")");
                     } else {
                        String str7 = UUID.randomUUID().toString();
                        NamePos namePos2 = new NamePos("Unknown", new Mutable(d, d2, d3), (Color)this.colorSetting1.atx(), new StopwatchImpl());
                        this.concurrentHashMap.put(str7, namePos2);
                        ChatUtils.UB("Unknown marked at §r(" + str3 + ", " + str4 + ", " + str5 + ")");
                     }
                  } else if (strs.length == 4) {
                     if ((Boolean)this.booleanSetting1_1.atx()) {
                        client.world.playSound(
                           (PlayerEntity)client.player, client.player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 100.0F, 1.9F
                        );
                     }

                     String str8 = strs[0];
                     String str9 = strs[1];
                     String str10 = strs[2];
                     String str11 = strs[3];
                     Pattern pattern3 = Pattern.compile("<(.*?)>");
                     Matcher matcher3 = pattern3.matcher(str);
                     if (!this.LA(str8)) {
                        return;
                     }

                     double d4 = Double.parseDouble(str8);
                     if (!this.LA(str9)) {
                        return;
                     }

                     double d5 = Double.parseDouble(str9);
                     if (!this.LA(str10)) {
                        return;
                     }

                     double d6 = Double.parseDouble(str10);
                     if (!this.LA(str11)) {
                        return;
                     }

                     double d7 = Double.parseDouble(str11);
                     if (matcher3.find()) {
                        String str12 = matcher3.group(1);
                        NamePos namePos3 = new NamePos(str12, new Mutable(d4, d5, d6), a((int)d7), new StopwatchImpl());
                        this.concurrentHashMap.put(str12, namePos3);
                        ChatUtils.UB(str12 + " marked at §r(" + str8 + ", " + str9 + ", " + str10 + ")");
                     } else {
                        String str13 = UUID.randomUUID().toString();
                        NamePos namePos4 = new NamePos("Unknown", new Mutable(d4, d5, d6), a((int)d7), new StopwatchImpl());
                        this.concurrentHashMap.put(str13, namePos4);
                        ChatUtils.UB("Unknown marked at §r(" + str8 + ", " + str9 + ", " + str10 + ")");
                     }
                  }
               }
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }
   }

   private boolean LA(String str) {
      return str.matches("-?\\d+(\\.\\d+)?");
   }

   public String LB(String str) {
      String temp;
      try {
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         SecretKeySpec secretKeySpec = Lu((String)this.settingSetting1_1.atx());
         IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]);
         cipher.init(2, secretKeySpec, ivParameterSpec);
         temp = new String(cipher.doFinal(Base64.getDecoder().decode(str)), StandardCharsets.UTF_8);
      } catch (Exception exception2) {
         return null;
      }

      return temp;
   }

   public String LC(String str) {
      String str2;
      try {
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         SecretKeySpec secretKeySpec = Lu((String)this.settingSetting1_1.atx());
         IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]);
         cipher.init(1, secretKeySpec, ivParameterSpec);
         Encoder temp = Base64.getEncoder();
         Cipher temp2 = cipher;
         Charset charset2 = StandardCharsets.UTF_8;
         byte[] bs = temp2.doFinal(str.getBytes(charset2));
         str2 = temp.encodeToString(bs);
      } catch (Exception exception2) {
         return null;
      }

      return str2;
   }

   void LD(ChatMessageS2CPacket chatMessageS2CPacket) {
      this.Lz(chatMessageS2CPacket.body().content());
   }

   void LE(ChatMessageS2CPacket chatMessageS2CPacket) {
      this.Lz(chatMessageS2CPacket.unsignedContent().getString());
   }

   void LF(GameMessageS2CPacket gameMessageS2CPacket) {
      this.Lz(gameMessageS2CPacket.content().getString());
   }

   boolean LG(NamePos namePos) {
      return namePos.timer().afq((Float)this.numberSetting1.atx() * 1000.0F);
   }

   private void LH() {
      this.Lw();
   }

   private static Color i() {
      return new Color(255, 255, 255, 100);
   }

   private static UnidentifiedHelper17 d(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, -1, runnable);
   }

   private static SecretKeySpec a(byte[] bs) {
      return new SecretKeySpec(bs, "AES");
   }

   private static StringSetting createStringSetting(String str, String str2) {
      return new StringSetting(str, str2, "IDKWTFTHIS");
   }

   private static Box m(double d2, double d3, double d4, double d5) {
      return new Box(d2, -60.0, d3, d4, 320.0, d5);
   }

   private static Color a(int n) {
      return new Color(n, true);
   }
}
