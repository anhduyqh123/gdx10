package GameGDX.GUIData;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class IScrollPane extends IGroup {
    @Override
    protected Actor NewActor() {
        return new ScrollPane(null);
    }

    public void AddChildAndConnect(String childName, IActor child)
    {
        AddChild(childName,child);
        child.SetConnect(this::GetActor);
    }

    @Override
    public void Refresh() {
        InitActor();
        BaseRefresh();
        RefreshContent();
    }
    private void RefreshContent()
    {
        if (list.size()<=0) return;
        String name = list.get(0);
        IActor iActor = GetIActor(name);
        iActor.Refresh();

        ScrollPane scroll = GetActor();
        scroll.setActor(iActor.GetActor());
    }
}
