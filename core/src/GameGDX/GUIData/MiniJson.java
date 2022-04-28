package GameGDX.GUIData;

import GameGDX.GDX;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class MiniJson {
    public Map<String,String> enMap = new HashMap<>();
    private GDX.Func<Map<String,String>> getDeMap;
    public GDX.Func1<Boolean,String> isWriteValue;
    private int nameID;

    private void Init()
    {
        Map<String,String> deMap = new HashMap<>();
        for(Map.Entry<String,String> e : enMap.entrySet())
            deMap.put(e.getValue(),e.getKey());
        getDeMap = ()->deMap;
    }

    private String NewKey()
    {
        return "i"+nameID++;
    }

    public JsonValue Decode(JsonValue js)
    {
        DoDecode(js);
        return js;
    }
    private void DoDecode(JsonValue js)
    {
        for(JsonValue i : js)
        {
            if (i.name!=null && enMap.containsKey(i.name)) i.setName(enMap.get(i.name));
            if (i.isString()){
                String st = i.asString();
                if (enMap.containsKey(st)) i.set(enMap.get(st));
            }
            else DoDecode(i);
        }
    }

    public JsonValue EnCode(JsonValue js)
    {
        DoEnCode(js);
        return js;
    }
    private void DoEnCode(JsonValue js)
    {
        for(JsonValue i : js)
        {
            String name = i.name;
            if (name!=null) Put(name,i::setName);
            if (IsWriteValue(name) && i.isString()) Put(i.asString(),i::set);
            else DoEnCode(i);
        }
    }
    private void Put(String value, GDX.Runnable<String> cb)
    {
        if (value.length()<5) return;
        if (getDeMap==null) Init();
        Map<String,String> deMap = getDeMap.Run();
        if (deMap.containsKey(value)) cb.Run(deMap.get(value));
        else {
            String stName = NewKey();
            enMap.put(stName,value);
            deMap.put(value,stName);
            cb.Run(stName);
        }
    }
    private boolean IsWriteValue(String key)
    {
        if (key==null) return false;
        if (isWriteValue==null) return false;
        return isWriteValue.Run(key);
    }
}
