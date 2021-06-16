package GameGDX.GUIData.IAction;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IActionList extends IMultiAction {

    private Map<String,IAction> map = new HashMap<>();

    private GDX.Func1<IAction,String> getIAction;

    public <T> T GetIAction(String name)
    {
        if (getIAction==null) Init();
        return (T)getIAction.Run(name);
    }
    private void Init()
    {
        Map<String,IAction> m = new HashMap<>(map);
        Foreach(i->Load(i,m));
        getIAction = m::get;
    }
    private void Load(IAction iAction,Map<String,IAction> m)
    {
        m.put(iAction.name,iAction);
        if (iAction instanceof IMultiAction){
            IMultiAction iMul = (IMultiAction) iAction;
            iMul.Foreach(i->Load(i,m));
        }
    }
    //extend
    public void SetRunAction(String nameAction,Runnable run)
    {
        IRunAction iRun = GetIAction(nameAction);
        iRun.runnable = run;
    }

    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        return null;
    }

    public IAction Get(String key) {
        return map.get(key);
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
