package Editor.LanguageTool;

import GameGDX.GDX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translator {
    public static String Translate(String langFrom, String langTo, String text) throws IOException {
        // INSERT YOU URL HERE
        String urlStr = "https://script.google.com/macros/s/AKfycbxatToMOLXGrQQQbNZy4AkFo7yoHWOFHaMV3i29jUSJKShMeks/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    public static void Translate(String langFrom, String langTo, String text, GDX.Runnable<String> done)
    {
        new Thread(()->{
            try {
                done.Run(Translate(langFrom, langTo, text));
                return;
            }catch (Exception e){e.printStackTrace();}
            done.Run("");
        }).start();
    }

}
