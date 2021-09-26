package GameGDX.GUIData;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class IScrollPane extends IGroup {
    @Override
    protected Actor NewActor() {
        return new ScrollPane(null){
            @Override
            public void addActor(Actor actor) {
                setActor(actor);
            }
        };
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
    }
    private void RefreshChild()
    {
        if (list.size()<=0) return;
        IActor iActor = GetIChild(0);
        iActor.Refresh();
//        ScrollPane scroll = GetActor();
//        scroll.setActor(iActor.GetActor());
    }
}
