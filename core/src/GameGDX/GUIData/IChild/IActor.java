package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IAction.IActionList;
import GameGDX.Reflect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.HashMap;
import java.util.Map;

public class IActor {
    public String prefab = "";
    public boolean visible = true;
    public Touchable touchable = Touchable.enabled;
    public String hexColor = Color.WHITE.toString();
    public ISize iSize = new ISize();
    public IPos iPos = new IPos();

    public IActionList acList = new IActionList();
    private Map<String,Component> componentMap = new HashMap<>();

    protected GDX.Func1<Actor,String> connect;
    private GDX.Func<Actor> getActor;

    public IActor(){}
    public  <T> T Clone()
    {
        return Reflect.Clone(this);
    }

    public <T extends Actor> T GetActor(String name){
        return (T)connect.Run(name);
    }

    public <T extends Actor> T GetActor()
    {
        if (getActor==null) return null;
        return (T)getActor.Run();
    }
    protected Actor NewActor()
    {
        return new Actor();
    }
    public void SetActor(Actor actor)
    {
        getActor = ()->actor;
    }

    protected void InitActor()
    {
        if (getActor==null) SetActor(NewActor());
        try {
            Group parent = (Group) connect.Run("");
            parent.addActor(GetActor());
        }catch (Exception e){}
    }
    protected Color GetColor()
    {
        return Color.valueOf(hexColor);
    }
    public Vector2 GetSize()
    {
        return new Vector2(iSize.GetWidth(), iSize.GetHeight());
    }
    protected void BaseRefresh()
    {
        Actor actor = GetActor();
        Vector2 size = GetSize();
        actor.setSize(size.x,size.y);
        actor.setOrigin(iSize.origin.value);
        actor.setScale(iSize.GetScaleX(), iSize.GetScaleY());
        actor.setRotation(iSize.rotate);
        Vector2 pos = iPos.Get();
        actor.setPosition(pos.x, pos.y, iPos.align.value);
        actor.setColor(GetColor());
        actor.setTouchable(touchable);
        actor.setVisible(visible);
    }
    private boolean ContainsEvent(String... events)
    {
        for (String s : events)
            if (acList.Contains(s)) return true;
        return false;
    }
    protected void RefreshEvent()
    {
        Actor actor = GetActor();
        actor.clearListeners();
        if(!ContainsEvent("enter","clicked","idle")) return;
        if (acList.Contains("idle")) RunAction("idle");
        actor.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RunAction("clicked");
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer!=0) return;
                RunAction("enter");
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                RunAction("exit");
                super.exit(event, x, y, pointer, toActor);
            }
        });
    }

    public void RefreshContent()
    {

    }
    public void Refresh()
    {
        InitActor();
        BaseRefresh();
        //RefreshEvent();

        GetActor().clearActions();
        RefreshComponent();
        RefreshEvent();
    }
    public void SetConnect(GDX.Func1<Actor,String> connect)//this call when this created by json
    {
        this.connect = connect;
        iPos.getTarget = connect;
        iSize.getTarget = connect;
    }
    public void Remove()
    {
        GetActor().remove();
        ForComponent((n,p)->p.Remove());
    }
    //Component
    public Map<String,Component> GetComponentData()
    {
        return componentMap;
    }
    protected void RefreshComponent()
    {
        ForComponent((k,p)->p.BeforeRefresh());
        ForComponent((k,p)->{
            p.getMain = ()->this;
            p.Refresh(GetActor());
        });
        ForComponent((k,p)->p.AfterRefresh());
    }
    public void ForComponent(GDX.Runnable2<String,Component> cb)
    {
        for (String key : componentMap.keySet())
            cb.Run(key,componentMap.get(key));
    }
    public <T extends Component> T GetComponent(String name)
    {
        return (T)componentMap.get(name);
    }
    public <T extends Component> T GetComponent(Class<T> type)
    {
        for (Component p : componentMap.values())
            if (p.getClass().equals(type)) return (T)p;
        return null;
    }
    //action
    public void StopAction()
    {
        BaseRefresh();
        GetActor().clearActions();
    }
    public void RunAction(String actionName)
    {
        if (acList.Contains(actionName))
            GetActor().addAction(GetAction(actionName));
    }
    public <T extends IAction> T GetIAction(String name)
    {
        return (T) acList.Get(name);
    }
    public Action GetAction(String name)
    {
        return GetIAction(name).Get(this);
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
