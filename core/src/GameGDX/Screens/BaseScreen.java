package GameGDX.Screens;

import GameGDX.GUIData.IAction.IRunAction;
import GameGDX.Scene;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseScreen extends Group {
    protected static List<BaseScreen> list = new ArrayList<>();

    protected Group main;

    private Map<String,Runnable> runMap = new HashMap<>();
    public Runnable showDone,hideDone;

    public Runnable onHide,onShow,onBackHandle;

    public BaseScreen()
    {
        setSize(Scene.width,Scene.height);
        InitMain();
        SetStateAction();
    }
    protected void InitMain()
    {
        main = new Group();
        this.addActor(main);
    }
    public Group Main()
    {
        return main;
    }
    protected void SetStateAction()
    {
        hideDone = ()->{};
        showDone = ()->{};

        PutRun("hideDone",()->{
            this.remove();
            OnHideDone();
            hideDone.run();
        });
        PutRun("showDone",()->{
            OnShowDone();
            showDone.run();
        });
        PutRun("show",this::ShowAction);
        PutRun("hide",this::HideAction);
    }
    protected void ShowAction()
    {
        main.addAction(AlphaAction(0,1,0.4f,GetRun("showDone")));
    }
    protected void HideAction()
    {
        main.addAction(AlphaAction(1,0,0.4f,GetRun("hideDone")));
    }
    public void Run(Runnable run, float delay) //delay by second
    {
        Action a1 = Actions.delay(delay);
        Action a2 = Actions.run(run);
        addAction(Actions.sequence(a1,a2));
    }
    protected Action AlphaAction(float from, float to,float duration, Runnable done)
    {
        Action ac1 = Actions.alpha(from);
        Action ac2 = Actions.alpha(to,duration, Interpolation.fade);
        Action ac3 = Actions.run(done);
        return Actions.sequence(ac1,ac2,ac3);
    }
    protected void DoRun(Runnable event)
    {
        if (event==null) return;
        event.run();
    }
    protected void PutRun(String key,Runnable event)
    {
        runMap.put(key,event);
    }
    protected Runnable GetRun(String key)
    {
        return runMap.get(key);
    }

    public void OnShow(){}
    public void OnHide(){}
    public void OnShowDone(){}
    public void OnHideDone(){}

    public void Show()
    {
        OnShow();
        DoRun(onShow);
        list.add(this);
        Scene.ui.addActor(this);
        main.setTouchable(Touchable.enabled);

        main.clearActions();
        GetRun("show").run();
    }
    public void Hide()
    {
        OnHide();
        DoRun(onHide);
        list.remove(this);

        main.setTouchable(Touchable.disabled);
        main.clearActions();
        GetRun("hide").run();
    }
    public void OnBackButtonPressed()
    {
        DoRun(onBackHandle);
    }
    public boolean IsLatest()
    {
        return this.equals(GetLatest());
    }

    public static void BackButtonEvent()
    {
        BaseScreen screen = GetLatest();
        if (screen==null) return;
        screen.OnBackButtonPressed();
    }
    public static BaseScreen GetLatest()
    {
        if (list.size()<=0) return null;
        return list.get(list.size()-1);
    }

    public static void AddClick(Actor actor, Runnable rEvent)
    {
        actor.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rEvent.run();
            }
        });
    }
    //Event
    public static List<EventListener> GetEvents(Actor actor)
    {
        List<EventListener> events = new ArrayList<>();
        for(EventListener e : actor.getListeners())
            events.add(e);
        return events;
    }
    public static Runnable Returns(List<Actor> list)
    {
        List<Runnable> runs = new ArrayList<>();
        for(Actor a : list) runs.add(Return(a));
        return ()->{
            for(Runnable r : runs) r.run();
        };
    }
    public static Runnable Return(Actor actor)
    {
        Group parent = actor.getParent();
        int index = actor.getZIndex();
        List<EventListener> listeners = GetEvents(actor);
        return ()->{
            Scene.AddActorKeepPosition(actor,parent);
            actor.setZIndex(index);
            actor.clearListeners();
            for(EventListener e : listeners)
                actor.addListener(e);
        };
    }
}
