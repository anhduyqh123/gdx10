package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;

import java.util.ArrayList;
import java.util.List;

public class IParallel extends IMultiAction{
    public List<IAction> list = new ArrayList<>();

    public IParallel()
    {
        name = "parallel";
    }

    @Override
    public void Move(int del,IAction iAction) {
        int index = list.indexOf(iAction);
        int nIndex = index+del;
        if (nIndex<0) nIndex=0;
        if (nIndex>=list.size()) nIndex=list.size()-1;
        IAction nAction = list.get(nIndex);
        list.set(nIndex,iAction);
        list.set(index,nAction);
    }

    public void Add(IAction iAction)
    {
        list.add(iAction);
    }
    public void Remove(IAction iAction)
    {
        list.remove(iAction);
    }

    @Override
    public List<IAction> GetAll() {
        return list;
    }

    protected ParallelAction GetAction()
    {
        return Actions.parallel();
    }
    @Override
    public Action Get() {
        ParallelAction action = GetAction();
        for(IAction i : list) action.addAction(i.Get());
        return action;
    }

    @Override
    public Action Get(IActor iActor) {
        ParallelAction action = GetAction();
        for(IAction i : list) action.addAction(i.Get(iActor));
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IParallel)) return false;
        if (!super.equals(o)) return false;
        IParallel iParallel = (IParallel) o;
        return list.equals(iParallel.list);
    }
}
