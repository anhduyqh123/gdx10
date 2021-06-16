package GameGDX.GUIData;

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
    public int column = 0;
    public float spaceX,spaceY,padX,padY;

    @Override
    protected Actor NewActor() {
        return new Table();
    }

    @Override
    public void Refresh() {
        InitActor();
        BaseRefresh();
        RefreshChildren();
    }
    private void RefreshChildren()
    {
        List<Actor> children = new ArrayList<>();
        for(String name : list)
        {
            IActor iActor = GetIActor(name);
            iActor.SetConnect(null);
            iActor.Refresh();
            children.add(iActor.GetActor());
        }
        RefreshChildren(children);
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
        table.layout();
    }
    public void RefreshIActor(Collection<IActor> iActors)
    {
        List<Actor> children = new ArrayList<>();
        for(IActor i : iActors)
        {
            i.SetConnect(this::GetActor);
            i.Refresh();
            children.add(i.GetActor());
        }
        RefreshChildren(children);
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
        IActor child = GetIActor(list.get(0));
        for(int i=0;i<amount;i++)
            iActors.add(Reflect.Clone(child));
        RefreshIActor(iActors);
        return iActors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ITable)) return false;
        if (!super.equals(o)) return false;
        ITable table = (ITable) o;
        return Float.compare(table.childWidth, childWidth) == 0 && Float.compare(table.childHeight, childHeight) == 0 && column == table.column && Float.compare(table.spaceX, spaceX) == 0 && Float.compare(table.spaceY, spaceY) == 0 && Float.compare(table.padX, padX) == 0 && Float.compare(table.padY, padY) == 0 && contentAlign == table.contentAlign && rowAlign == table.rowAlign;
    }
}
