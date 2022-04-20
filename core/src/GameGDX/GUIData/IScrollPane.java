package GameGDX.GUIData;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class IScrollPane extends IGroup {
    public float percentX,percentY;
    public String scrollToChild = "";
    @Override
    protected Actor NewActor() {
        return new ScrollPane(null){
            @Override
            public void addActor(Actor actor) {
                setActor(actor);
            }
        };
    }

    @Override
    protected void Clear() {
    }

    public void AddChildAndConnect(String childName, IActor child)
    {
        AddChild(childName,child);
        child.SetConnect(this::GetChild);
    }

    @Override
    public void Refresh() {
        InitActor();
        BaseRefresh();
        RefreshChild();

        ScrollPane scroll = GetActor();
        scroll.layout();
        scroll.setScrollPercentX(percentX);
        scroll.setScrollPercentY(percentY);
        scroll.updateVisualScroll();
        if (scrollToChild.equals("")) return;
        ScrollToChild();
    }
    private void ScrollToChild()
    {
        try {
            Actor child = FindChild(scrollToChild);
            ScrollPane scroll = GetActor();
            scroll.layout();
            scroll.scrollTo(child.getX(),child.getY()
                    ,child.getWidth(),child.getHeight(),true,true);
        }catch (Exception e){}
    }
    private void RefreshChild()
    {
        if (list.size()<=0) return;
        IActor iActor = GetIChild(0);
        iActor.Refresh();
    }
}
