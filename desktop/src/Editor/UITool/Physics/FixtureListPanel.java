package Editor.UITool.Physics;

import Extend.Box2d.IFixture;
import GameGDX.GDX;
import GameGDX.Reflect;

import java.util.ArrayList;
import java.util.List;

public class FixtureListPanel {
    private List<IFixture> list;
    public GDX.Func<Integer> getIndex;
    public GDX.Runnable<Integer> onRefresh;

    public FixtureListPanel(List<IFixture> list)
    {
        this.list = list;
    }
    public void New()
    {
        list.add(new IFixture());
        onRefresh.Run(list.size()-1);
    }
    public void Clone()
    {
        int index= getIndex.Run();
        IFixture clone = Reflect.Clone(list.get(index));
        list.add(clone);
        onRefresh.Run(list.size()-1);
    }
    public void Delete()
    {
        int index= getIndex.Run();
        list.remove(index);
        onRefresh.Run(index-1);
    }
    public List<String> GetData()
    {
        List<String> l = new ArrayList<>();
        for (int i=0;i<list.size();i++)
            l.add("ifix_"+i);
        return l;
    }
}
