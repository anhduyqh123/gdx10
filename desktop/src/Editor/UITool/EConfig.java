package Editor.UITool;

import GameGDX.Config;
import com.badlogic.gdx.utils.JsonValue;

public class EConfig extends Config {
    public static EConfig e = new EConfig();

    public boolean Contains(String name)
    {
        return data.has(name);
    }
    public void Remove(String name)
    {
        data.get(name).remove();
    }
    public void Rename(String oldName,String newName)
    {
        JsonValue js = data.get(oldName);
        js.remove();
        data.addChild(newName,js);
    }
    public void Add(String name,JsonValue value)
    {
        if (data.has(name)) return;
        data.addChild(name,value);
    }
}
