package GameGDX.GUIData.IAction;

import GameGDX.GDX;

import java.util.List;

public abstract class IMultiAction extends IAction {
    public abstract void Move(int del,IAction iAction);
    public abstract void Add(IAction iAction);
    public abstract void Remove(IAction iAction);
    public abstract List<IAction> GetAll();
    protected void Foreach(GDX.Runnable<IAction> cb)
    {
        for(IAction i : GetAll())
        {
            cb.Run(i);
            if (i instanceof IMultiAction)
            {
                IMultiAction iMul = (IMultiAction) i;
                iMul.Foreach(cb);
            }
        }
    }
}
