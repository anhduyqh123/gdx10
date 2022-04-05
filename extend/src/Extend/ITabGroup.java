package Extend;

import GameGDX.GDX;
import GameGDX.GUIData.IGroup;
import GameGDX.Screens.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;

public class ITabGroup extends IGroup {
    private GDX.Func<Integer> getSelected;

    public ITabGroup()
    {
        AddChild("buttons",new IGroup());
        AddChild("tabs",new IGroup());
    }
    public int GetSelected()
    {
        return getSelected.Run();
    }

    @Override
    public void Refresh() {
        super.Refresh();
        try {
            InitButton();
            SetIndex(0);
        }catch (Exception e){}
    }
    private void InitButton()
    {
        DisableButton();
        FindIGroup("buttons").ForEach(i->{
            int index = i.GetActor().getZIndex();
            Screen.AddClick(i.GetActor(),()->SetIndex(index));
        });
    }
    private void SetIndex(int index)
    {
        IGroup buttons = FindIGroup("buttons");
        IGroup tabs = FindIGroup("tabs");
        if (buttons.GetChildName().size()<=0) return;
        DisableButton();
        Group group = tabs.GetActor();
        group.clearChildren();
        group.addActor(tabs.GetIChild(index).GetActor());
        buttons.GetIChild(index).SetColor(Color.WHITE);
        getSelected = ()->index;
    }
    private void DisableButton()
    {
        FindIGroup("buttons").ForEach(i->i.SetColor(Color.GRAY));
    }
}
