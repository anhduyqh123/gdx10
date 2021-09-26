package GameGDX.GUIData;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.Reflect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ITable extends IGroup {

    public float childWidth,childHeight;
    public IAlign contentAlign = IAlign.center,rowAlign = IAlign.center;
    public int column = 0,clone;
    public float spaceX,spaceY,padX,padY;

    private GDX.Func<List<IActor>> getChildren;

    @Override
    protected Actor NewActor() {
        return new Table();
    }

    public void ForIChild(GDX.Runnable<IActor> cb)
    {
        if (getChildren==null) return;
        for(IActor i : getChildren.Run())
            cb.Run(i);
    }

    @Override
    public <T extends IActor> List<T> GetChildren() {
        if (getChildren==null) return null;
        List<T> children = new ArrayList<>();
        ForIChild(i->children.add((T)i));
        return children;
    }

    @Override
    public void StopAction() {
        super.StopAction();
        Table table = GetActor();
        table.layout();
    }

    @Override
    public void AddChildAndConnect(String name, IActor child) {
        AddChild(name,child);
    }

    @Override
    public void Refresh() {
        InitActor();
        BaseRefresh();
        if (clone>0) CloneChild(clone);
        else RefreshChildren();
    }
    private void RefreshChildren()
    {
        List<IActor> iActors = new ArrayList<>();
        for (String n : list) iActors.add(GetIChild(n));
        RefreshIActor(iActors);
    }
    public void RefreshChildren(Collection<Actor> children)
    {
        Table table = GetActor();
        table.clearChildren();

        int i=0;
        NewRow(table);
        for(Actor child : children)
        {
            Cell cell = table.add(child);
            if (childWidth==0) cell.width(child.getWidth());
            if (childHeight==0) cell.height(child.getHeight());
            if (column==0) continue;
            i++;
            if (i%column==0) NewRow(table);
        }
        table.align(contentAlign.value);
        table.validate();
    }
    public void RefreshIActor(List<IActor> iActors)
    {
        List<Actor> children = new ArrayList<>();
        for(IActor i : iActors) i.SetConnect(this::GetChild);
        for(IActor i : iActors)
        {
            i.Refresh();
            children.add(i.GetActor());
        }
        RefreshChildren(children);
        getChildren = ()->iActors;
        //SetMain(getMain);
    }
    private void NewRow(Table table)
    {
        Cell cell = table.row().spaceRight(spaceX).spaceTop(spaceY).padRight(padX).padTop(padY);
        cell.align(rowAlign.value);
        if (childWidth!=0) cell.width(childWidth);
        if (childHeight!=0) cell.height(childHeight);
    }
    public List<IActor> CloneChild(int amount)
    {
        List<IActor> iActors = new ArrayList<>();
        if (list.size()<=0) return iActors;
        IActor child = GetIChild(list.get(0));
        child.Refresh();

        for(int i=0;i<amount;i++)
            iActors.add(Reflect.Clone(child));
        RefreshIActor(iActors);
        return iActors;
    }
    public <T> List<IActor> CloneChild(List<T> list, GDX.Runnable2<T,IActor> cb)
    {
        List<IActor> iActors = CloneChild(list.size());
        for(int i=0;i<list.size();i++)
            cb.Run(list.get(i),iActors.get(i));
        return iActors;
    }
}
