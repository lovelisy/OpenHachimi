package pw.hachimi.client.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import javax.net.ssl.HttpsURLConnection;

public class FileManager {
   public static boolean a;

   public Map acL(String str) {
      TreeMap treeMap = new TreeMap(Collections.reverseOrder());
      HttpsURLConnection httpsURLConnection = null;

      try {
         String str2 = String.format("https://api.2b2t.vc/stats/player?playerName=%s", str);
         httpsURLConnection = (HttpsURLConnection)new URL(str2).openConnection();
         httpsURLConnection.setDoOutput(true);
         httpsURLConnection.setRequestMethod("GET");
         httpsURLConnection.setRequestProperty("Content-Type", "application/json");
         Scanner scanner = new Scanner(httpsURLConnection.getInputStream());
         StringBuilder stringBuilder = new StringBuilder();

         while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
            stringBuilder.append('\n');
         }

         scanner.close();
         String str3 = stringBuilder.toString();
         JsonObject jsonObject = JsonParser.parseString(str3).getAsJsonObject();
         if (httpsURLConnection != null) {
            httpsURLConnection.disconnect();
         }

         if (jsonObject == null) {
            return null;
         }

         treeMap.put("Join Count", String.valueOf(jsonObject.get("joinCount").getAsInt()));
         treeMap.put("Leave Count", String.valueOf(jsonObject.get("leaveCount").getAsInt()));
         treeMap.put("Death Count", String.valueOf(jsonObject.get("deathCount").getAsInt()));
         treeMap.put("Kill Count", String.valueOf(jsonObject.get("killCount").getAsInt()));
         treeMap.put("First Seen", jsonObject.get("firstSeen").getAsString());
         treeMap.put("Last Seen", jsonObject.get("lastSeen").getAsString());
         treeMap.put("Playtime", jsonObject.get("playtimeSeconds").getAsInt() / 3600 + "hrs");
         treeMap.put("Playtime Month", jsonObject.get("playtimeSecondsMonth").getAsInt() / 3600 + "hrs");
         treeMap.put("Chats Count", String.valueOf(jsonObject.get("chatsCount").getAsInt()));
         treeMap.put("Prio", String.valueOf(jsonObject.get("prio").getAsBoolean()));
      } catch (Exception exception) {
         exception.printStackTrace();
      } finally {
         if (httpsURLConnection != null) {
            httpsURLConnection.disconnect();
         }
      }

      return treeMap;
   }

   public String acM() {
      HttpsURLConnection httpsURLConnection = null;

      try {
         httpsURLConnection = (HttpsURLConnection)new URL("https://api.2b2t.vc/queue").openConnection();
         httpsURLConnection.setDoOutput(true);
         httpsURLConnection.setRequestMethod("GET");
         httpsURLConnection.setRequestProperty("Content-Type", "application/json");
         Scanner scanner = new Scanner(httpsURLConnection.getInputStream());
         StringBuilder stringBuilder = new StringBuilder();

         while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
            stringBuilder.append('\n');
         }

         scanner.close();
         String str = stringBuilder.toString();
         JsonObject jsonObject = JsonParser.parseString(str).getAsJsonObject();
         if (httpsURLConnection != null) {
            httpsURLConnection.disconnect();
         }

         if (jsonObject == null) {
            return null;
         }

         return String.format("§7Priority: §f%d, §7Regular: §f%d", jsonObject.get("prio").getAsInt(), jsonObject.get("regular").getAsInt());
      } catch (Exception exception) {
         exception.printStackTrace();
         return null;
      } finally {
         if (httpsURLConnection != null) {
            httpsURLConnection.disconnect();
         }
      }
   }
}
