package GameGDX.Screens;

import GameGDX.Actions.FrameAction;
import GameGDX.GUIData.*;
import GameGDX.GUIData.IAction.IRunAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Screen extends BaseScreen {
    protected IGroup iGroup;
    protected String name;

    public Screen(String screenName)
    {
        super();
        this.name = screenName;
        iGroup = GUIData.i.Get(screenName).Clone();
        iGroup.SetConnect(name -> this);
        iGroup.Refresh();

        //iGroup.InitMain();

        SetRunAction("show","showDone");
        SetRunAction("hide","hideDone");

        main = iGroup.GetActor();
    }
    protected void SetRunAction(String name,String runName)
    {
        IRunAction iRun = iGroup.acList.GetIMulti(name).FindIAction("done");
        iRun.runnable = i->GetRun(runName).run();
    }

    @Override
    protected void ShowAction() {
        main.setVisible(false);
        iGroup.RunAction("show");
        addAction(FrameAction.Get(1,()->main.setVisible(true)));
    }

    @Override
    protected void HideAction() {
        iGroup.RunAction("hide");
    }

    public void AddClick(String name, Runnable event)
    {
        FindIActor(name).AddClick(event);
    }

    public <T extends Actor> T FindActor(String name)
    {
        return iGroup.FindChild(name);
    }
    public <T extends Actor> T FindActor(String name,Class<T> type)
    {
        return FindActor(name);
    }
    public <T extends IActor> T FindIActor(String name)
    {
        return iGroup.FindIChild(name);
    }
    public <T extends IActor> T FindIActor(String name, Class<T> type)
    {
        return FindIActor(name);
    }

    public IGroup GetIGroup()
    {
        return iGroup;
    }
    //Find IChild
    public IGroup FindIGroup(String name)
    {
        return FindIActor(name);
    }
    public ILabel FindILabel(String name)
    {
        return FindIActor(name);
    }
    public IImage FindIImage(String name)
    {
        return FindIActor(name);
    }
    public ITable FindITable(String name)
    {
        return FindIActor(name);
    }
    public IParticle FindIParticle(String name)
    {
        return FindIActor(name);
    }
}
