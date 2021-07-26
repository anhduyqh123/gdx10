package GameGDX;

import com.badlogic.gdx.utils.JsonValue;

public class Config {
    public static Config i = new Config();

    protected JsonValue data = NewData();

    public JsonValue GetData()
    {
        return data;
    }
    public JsonValue Get(String name)
    {
        return data.get(name);
    }
    protected JsonValue NewData()
    {
        try {
            String st = GDX.GetString("config.json");
            return Json.DataToJson(st);
        }catch (Exception e ){}
        return new JsonValue(JsonValue.ValueType.object);
    }
    public void Save()
    {
        try {
            GDX.WriteToFile("config.json",data.toString());
        }catch (Exception e){}
    }
}
