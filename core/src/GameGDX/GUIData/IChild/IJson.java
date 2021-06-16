package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Json;
import GameGDX.Reflect;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class IJson extends Json {
    public static GDX.Func1<IActor,String> getIChild;

    public static <T> T FromJson(String jsData)
    {
        IJson json = new IJson();
        JsonValue js = DataToJson(jsData);
        Class type = Reflect.GetClass(js.getString(stClass));
        return (T)json.ToObject(type,Reflect.NewInstance(type),js);
    }
    public static String ToJsonData(Object object)
    {
        IJson json = new IJson();
        return json.ToJson(object,Reflect.GetDefaultObject(object.getClass()),true)
                .toJson(JsonWriter.OutputType.minimal);
    }
    public static <T> T FromJson(JsonValue js)
    {
        IJson json = new IJson();
        Class type = Reflect.GetClass(js.getString(stClass));
        return (T)json.ToObject(type,Reflect.NewInstance(type),js);
    }
    public static JsonValue ToJson(Object object)
    {
        IJson json = new IJson();
        return json.ToJson(object,Reflect.GetDefaultObject(object.getClass()),true);
    }

    @Override
    protected Object GetObjectToWrite(Object object, Object defaultObject) {
        try {
            String prefab1 = ((IActor)object).prefab;
            String prefab2 = ((IActor)defaultObject).prefab;
            if (prefab1.equals(prefab2)) return defaultObject;
            return Reflect.Clone(getIChild.Run(prefab1));
        }catch (Exception e){}
        return defaultObject;
    }

    @Override
    protected Object GetObjectToRead(Object object, JsonValue js) {
        String prefab = js.getString("prefab","");
        if (!prefab.equals("")) return Reflect.Clone(getIChild.Run(prefab));
        return object;
    }
}
