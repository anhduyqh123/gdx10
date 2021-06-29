package GameGDX.Screens;

import GameGDX.Actions.FrameAction;
import GameGDX.GMusic;
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
        IRunAction iRun = iGroup.acList.GetIMulti(name).GetIAction("done");
        iRun.runnable = GetRun(runName);
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
        AddClick_NoPop(name,()->{
            GMusic.PlaySound("pop");
            event.run();
        });
    }
    public void AddClick_NoPop(String name, Runnable event)
    {
        AddClick(GetActor(name),event);
    }

    public <T extends Actor> T GetActor(String name)
    {
        return iGroup.GetActor(name);
    }
    public <T extends Actor> T GetActor(String name,Class<T> type)
    {
        return GetActor(name);
    }
    public <T extends IActor> T GetIActor(String name)
    {
        return iGroup.GetIActor(name);
    }
    public <T extends IActor> T GetIActor(String name, Class<T> type)
    {
        return GetIActor(name);
    }

    //Get IChild
    public IGroup GetIGroup()
    {
        return iGroup;
    }
    public IGroup GetIGroup(String name)
    {
        return GetIActor(name);
    }
    public ILabel GetILabel(String name)
    {
        return GetIActor(name);
    }
    public IImage GetIImage(String name)
    {
        return GetIActor(name);
    }
    public ITable GetITable(String name)
    {
        return GetIActor(name);
    }
    public IParticle GetIParticle(String name)
    {
        return GetIActor(name);
    }
}
