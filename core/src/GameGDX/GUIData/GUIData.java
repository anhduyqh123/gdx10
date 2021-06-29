package GameGDX.GUIData;

import GameGDX.*;
import GameGDX.AssetLoading.AssetNode;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.*;

public class GUIData {
    public static GUIData i = new GUIData();

    private Map<String, IActor> map = new HashMap<>();
    private MiniJson miniJson = GetMiniJson();

    public GUIData()
    {
        miniJson.isWriteValue = name -> name.equals("class");
        IJson.getIChild = this::Get;
    }
    private MiniJson GetMiniJson()
    {
        try {
            return Json.FromJson(MiniJson.class,GDX.GetString("mini.d"));
        }catch (Exception e){}
        return new MiniJson();
    }
    private void SaveMiniJson()
    {
        String data = Json.ToJsonData(miniJson);
        GDX.WriteToFile("mini.d",data);
    }

    public IActor Get(String name)
    {
        if (map.containsKey(name)) return map.get(name);
        String pack = "default";
        if (name.contains("/")){
            String[] arr = name.split("/",2);
            pack = arr[0];
            name = arr[1];
        }
        else {
            AssetNode node = Assets.GetNode(name);
            pack = node.pack;
        }
        IActor ic = Get(pack,name);
        map.put(name,ic);
        return ic;
    }
    public IActor Get(String pack, String name)
    {
        AssetNode n = Assets.GetAssetPackage(pack).Get(name);
        String data = GDX.GetString(n.url);
        JsonValue jsData = miniJson.Decode(Json.DataToJson(data));
        IActor ic = IJson.FromJson(jsData);
        return ic;
    }
    public void Save(String url, IActor ic)
    {
        JsonValue jsData = IJson.ToJson(ic);
        GDX.WriteToFile(url,jsData.toJson(JsonWriter.OutputType.minimal));
    }

    public void UnMiniSave(AssetNode node)
    {
        IActor iActor = Get(node.pack,node.name);
        JsonValue jsData = IJson.ToJson(iActor);
        GDX.WriteToFile(node.url,jsData.toJson(JsonWriter.OutputType.minimal));
    }
    public void MiniSave(AssetNode node)
    {
        IActor iActor = Get(node.pack,node.name);
        JsonValue jsData = miniJson.EnCode(IJson.ToJson(iActor));
        SaveMiniJson();
        GDX.WriteToFile(node.url,jsData.toJson(JsonWriter.OutputType.minimal));
    }
    public List<String> GetAll()
    {
        return new ArrayList<>(map.keySet());
    }
}
