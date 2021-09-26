package GameGDX.GUIData.IAction;

import GameGDX.GDX;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IMultiAction extends IAction {
    private GDX.Func<Map<String,IAction>> getMap;

    private void Init()
    {
        Map<String,IAction> m = new HashMap<>();
        Foreach(i->m.put(i.name,i));
        getMap = ()->m;
    }
    public <T extends IAction> T FindIAction(String name)
    {
        if (getMap==null) Init();
        if (getMap.Run().containsKey(name)) return (T)getMap.Run().get(name);
        for(IAction i : GetAll())
            if (i instanceof IMultiAction)
            {
                IMultiAction iMul = (IMultiAction) i;
                IAction iAction = iMul.FindIAction(name);
                if (iAction!=null) return (T)iAction;
            }
        return null;
    }
    public <T extends IAction> T FindIAction(String name, Class<T> type)
    {
        return FindIAction(name);
    }
    public IMultiAction GetIMulti(String name)
    {
        return FindIAction(name);
    }

    public abstract void Move(int del,IAction iAction);
    public abstract void Add(IAction iAction);
    public abstract void Remove(IAction iAction);
    public abstract <T extends IAction> List<T> GetAll();
    protected void Foreach(GDX.Runnable<IAction> cb)
    {
        for(IAction i : GetAll())
            cb.Run(i);
    }
    //extend
    public void SetIRun(String name,Runnable run)
    {
        IRunAction iRun = FindIAction(name);
        iRun.runnable = ia->run.run();
    }
}
