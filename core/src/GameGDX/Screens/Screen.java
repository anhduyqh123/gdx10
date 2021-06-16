package GameGDX.Screens;

import GameGDX.Actions.FrameAction;
import GameGDX.GMusic;
import GameGDX.GUIData.*;
import GameGDX.GUIData.IAction.IRunAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Screen extends BaseScreen {
    protected static GUIData guiData = new GUIData();

    protected IGroup iGroup;
    protected String name;

    public Screen(String screenName)
    {
        super();
        this.name = screenName;
        iGroup = guiData.Get(screenName).Clone();
        iGroup.SetConnect(name -> this);
        iGroup.Refresh();

        iGroup.InitMain();

        SetRunAction("showDone","showDone");
        SetRunAction("hideDone","hideDone");

        main = iGroup.GetActor();
    }
    protected void SetRunAction(String name,String runName)
    {
        IRunAction iRun = iGroup.acList.GetIAction(name);
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
    public void AddClick(Actor actor, Runnable rEvent)
    {
        actor.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rEvent.run();
            }
        });
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
