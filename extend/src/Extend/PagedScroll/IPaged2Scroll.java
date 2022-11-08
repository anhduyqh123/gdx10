package Extend.PagedScroll;

import GameGDX.GUIData.IGroup;
import GameGDX.GUIData.ITable;

public class IPaged2Scroll extends IGroup {

    public IPaged2Scroll()
    {
        AddChild("dot",new ITable());
        AddChild("page",new IPagedScroll());
    }

    @Override
    public void Refresh() {
        super.Refresh();
        PagedScroll pagedScroll = FindChild("page");
        pagedScroll.setOnScrollIndex(this::ScrollIndex);
    }
    private void ScrollIndex(int index)
    {
        try {
            FindIGroup("dot").ForEach(a->a.RunAction("off"));
            FindIGroup("dot").GetIChild(index).RunAction("on");
        }catch (Exception e){}
    }
    public void InitPage(int amount)//clone page
    {
        FindITable("dot").CloneChild(amount);
        IPagedScroll paged = FindIChild("page");
        paged.CloneChild(amount);
    }
}
