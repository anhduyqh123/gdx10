package GameGDX.GUIData;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.Reflect;
import GameGDX.Util;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ITable extends IGroup {

    public float childWidth,childHeight;
    public IAlign contentAlign = IAlign.center,rowAlign = IAlign.center;
    public int column = 0,clone;
    public float spaceX,spaceY;
    public float padLeft,padRight,padTop,padBot;
    public boolean autoFill,reverse;

    private GDX.Func<List<IActor>> getChildren;//current

    @Override
    protected Actor NewActor() {
        return new Table();
    }

    public <T extends IActor> void ForIChild(GDX.Runnable<T> cb)
    {
        if (getChildren==null) return;
        for(IActor i : getChildren.Run())
            cb.Run((T)i);
    }
    public <T extends IActor> T GetIChild(int index){
        return (T)GetChildren().get(index);
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
        ForIChild(IActor::Remove);
        if (clone>0) CloneChild(clone);
        else RefreshChildren();
    }
    protected void RefreshChildren()
    {
        List<IActor> iActors = new ArrayList<>();
        for (String n : list) iActors.add(GetIChild(n));
        RefreshIActor(iActors);
    }
    public void RefreshChildren(Collection<Actor> children)
    {
        FillChildren(column,children);
        if (autoFill) AutoFill(children);
    }
    private void FillChildren(int column,Collection<Actor> children)
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
        table.pad(padTop,padLeft,padBot,padRight);
        table.align(contentAlign.value);
        table.validate();

        if (reverse)
        {
            List<Actor> reverse = new ArrayList<>(children);
            Collections.reverse(reverse);
            Util.ForIndex(reverse,x->reverse.get(x).setZIndex(x));
        }
    }
    private void AutoFill(Collection<Actor> children) //width/height
    {
        Table table = GetActor();
        if (table.getColumns()<=0) return;
        float divide = table.getWidth()/table.getHeight();
        float multiply = table.getPrefWidth()*table.getPrefHeight();
        float width = (float) Math.sqrt(divide*multiply);
        float columnWidth = table.getColumnWidth(0);
        if (width<table.getWidth()) return;
        int num = (int)(width/columnWidth);
        FillChildren(num,children);
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
        Cell cell = table.row().spaceRight(spaceX).spaceTop(spaceY);
        cell.align(rowAlign.value);
        if (childWidth!=0) cell.width(childWidth);
        if (childHeight!=0) cell.height(childHeight);
    }
    public <T extends IActor> List<T> CloneChild(int amount)
    {
        List<T> iActors = new ArrayList<>();
        if (list.size()<=0) return iActors;
        IActor child = GetIChild(list.get(0));
        //child.Refresh();

        for(int i=0;i<amount;i++)
            iActors.add(Reflect.Clone(child));
        RefreshIActor((List<IActor>) iActors);
        return iActors;
    }
    public <T,E extends IActor> List<E> CloneChild(List<T> list, GDX.Runnable2<T,E> cb)
    {
        return CloneChild(list,(i,t,e)->cb.Run(t,e));
    }
    public <T,E extends IActor> List<E> CloneChild(List<T> list, GDX.Runnable3<Integer,T,E> cb)
    {
        List<E> iActors = CloneChild(list.size());
        for(int i=0;i<list.size();i++)
            cb.Run(i,list.get(i),(E)iActors.get(i));
        return iActors;
    }
    public <T extends IActor> List<T> CloneChild(int amount,GDX.Runnable2<Integer,T> cb)
    {
        List<T> iActors = CloneChild(amount);
        for(int i=0;i<iActors.size();i++)
            cb.Run(i,iActors.get(i));
        return iActors;
    }

    public List<Actor> GetActors()
    {
        List<Actor> actors = new ArrayList<>();
        ForIChild(i->actors.add(i.GetActor()));
        return actors;
    }
    public IActor CloneChild()
    {
        IActor child = GetIChild(list.get(0));
        IActor clone = Reflect.Clone(child);
        clone.SetConnect(this::GetChild);
        clone.Refresh();
        AddChild(clone);
        RefreshChildren(GetActors());
        return clone;
    }
    private void AddChild(IActor iActor)
    {
        List<IActor> iActorList = getChildren.Run();
        iActorList.add(iActor);
    }
    public void ForEach(GDX.Runnable<IActor> cb)
    {
        ForIChild(cb);
    }
}
