package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IActionList extends IMultiAction {

    private Map<String,IAction> map = new HashMap<>();

    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        return null;
    }

    public <T extends IAction> T Get(String name)
    {
        return (T)map.get(name);
    }
    public boolean Contains(String key)
    {
        return map.containsKey(key);
    }

    @Override
    public void Move(int del, IAction iAction) {

    }

    @Override
    public void Add(IAction iAction) {
        map.put(iAction.name,iAction);
    }

    @Override
    public void Remove(IAction iAction) {
        map.remove(iAction.name);
    }
    public void Remove(String key)
    {
        map.remove(key);
    }

    @Override
    public List<IAction> GetAll() {

        return new ArrayList<>(map.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IActionList)) return false;
        if (!super.equals(o)) return false;
        IActionList that = (IActionList) o;
        return map.equals(that.map);
    }
}
