package Extend.PagedScroll;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import GameGDX.Reflect;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class IPagedScroll extends IGroup {
    public int pageSpace = 25,scrollToIndex;
    public float flingTime = 0.1f;
    public boolean scaleChild;

    @Override
    protected Actor NewActor() {
        return new PagedScroll();
    }

    @Override
    public void Refresh() {
        //super.Refresh();
        InitActor();
        BaseRefresh();
        SetPage();
    }
    private void SetPage()
    {
        PagedScroll scroll = GetActor();
        scroll.setFlingTime(flingTime);
        scroll.setPageSpacing(pageSpace);
        scroll.setScaleChildDist(scaleChild);
        ForEach(IActor::Refresh);
        scroll.clearChildren();
        ForEach(i->scroll.addPage(i.GetActor()));
        try {
            ScrollToIndex();
        }catch (Exception e){}
    }
    private void ScrollToIndex()
    {
        Actor child = GetIChild(scrollToIndex).GetActor();
        PagedScroll scroll = GetActor();
        Delay(()->scroll.scrollTo(child),0.2f);
    }
    public <T extends IActor> List<T> CloneChild(int amount)
    {
        List<T> iActors = new ArrayList<>();
        if (list.size()<=0) return iActors;
        IActor child = GetIChild(list.get(0));

        for(int i=0;i<amount;i++)
            iActors.add(Reflect.Clone(child));

        PagedScroll scroll = GetActor();
        scroll.clearChildren();
        for (IActor i : iActors)
            scroll.addPage(i.GetActor());

        return iActors;
    }
}
