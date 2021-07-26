package Editor.UITool;

import GameGDX.GDX;
import GameGDX.Json;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.*;

public class UIConfig extends Json.JsonObject {
    public static UIConfig i;
    public int screen_width = 360, screen_height = 640;
    public int game_width = 720, game_height = 1280;

    public Runnable onReload;

    private Map<String,List<String>> mapToLoad = new HashMap<>();

    public List<String> GetPacks(String pack)
    {
        if (mapToLoad.containsKey(pack)) return mapToLoad.get(pack);
        mapToLoad.put(pack,new ArrayList());
        mapToLoad.get(pack).add(pack);
        return mapToLoad.get(pack);
    }
    public void Reload()
    {
        if (onReload==null) return;
        onReload.run();
    }

    @Override
    protected void ReadJson(JsonValue js) {
        screen_width = js.getInt("screen_width",screen_width);
        screen_height = js.getInt("screen_height",screen_height);
        game_width = js.getInt("game_width",game_width);
        game_height = js.getInt("game_height",game_height);

        if (!js.has("mapToLoad")) return;
        for (JsonValue i : js.get("mapToLoad"))
        {
            List<String> list = Json.ToList(String.class,i);
            mapToLoad.put(i.name,list);
        }
    }

    public static void Save()
    {
        String data = Json.ToJson(i).prettyPrint(JsonWriter.OutputType.json,0);
        GDX.WriteToFile("uiConfig.txt",data);
    }

    public static UIConfig NewConfig()
    {
        try {
            FileHandle fileHandle = new FileHandle("uiConfig.txt");
            String data = fileHandle.readString();
            i = Json.FromJson(UIConfig.class,data);
        }catch (Exception e)
        {
            i = new UIConfig();
        }
        return i;
    }
}
