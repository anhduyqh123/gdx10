package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IAction.IActionList;
import GameGDX.GUIData.IAction.ICountAction;
import GameGDX.GUIData.IAction.IMove;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Arrays;
import java.util.List;

public class IActor {
    public String prefab = "";

    public boolean visible = true;
    public Touchable touchable = Touchable.enabled;
    public String hexColor = Color.WHITE.toString();
    public ISize iSize = new ISize();
    public IPos iPos = new IPos();
    public IActionList acList = new IActionList();

    protected GDX.Func1<Actor,String> connect;
    protected GDX.Func<IActor> getMain;
    private GDX.Func<Actor> getActor;

    public IActor(){}
    public  <T> T Clone()
    {
        return Reflect.Clone(this);
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
        if (iSize.extendScreen) actor.setScale(Scene.scale);
        else actor.setScale(iSize.scale);
        actor.setRotation(iSize.rotate);
        actor.setPosition(iPos.GetX(), iPos.GetY(), iPos.align.value);
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

    public void Refresh()
    {
        InitActor();
        BaseRefresh();
        RefreshEvent();
    }
    public void SetConnect(GDX.Func1<Actor,String> connect)//this call when this created by json
    {
        this.connect = connect;
        iPos.getTarget = connect;
        iSize.getTarget = connect;
    }

    public void InitMain()
    {
        SetMain(()->this);
    }
    public IActor GetMain()
    {
        return getMain.Run();
    }
    public void SetMain(GDX.Func<IActor> getMain)
    {
        this.getMain = getMain;
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
            GetActor().addAction(GetIAction(actionName).Get(this));
    }
    public <T extends IAction> T GetIAction(String name)
    {
        return (T) acList.Get(name);
    }
    //Get IAction
    public IMove GetIMove(String name){
        return GetIAction(name);
    }
    public ICountAction GetICount(String name){
        return GetIAction(name);
    }

    public Action GetAction(String name)
    {
        return GetIAction(name).Get(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IActor)) return false;
        IActor iActor = (IActor) o;
        return prefab.equals(iActor.prefab) && hexColor.equals(iActor.hexColor) && iSize.equals(iActor.iSize) && iPos.equals(iActor.iPos) && acList.equals(iActor.acList);
    }
}
