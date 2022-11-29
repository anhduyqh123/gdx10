package GameGDX.GUIData.IChild;

import GameGDX.*;
import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IAction.IActionList;
import GameGDX.GUIData.IGroup;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.*;

public class IActor {
    public String prefab = "";
    public boolean visible = true;
    public Touchable touchable = Touchable.enabled;
    public String hexColor = Color.WHITE.toString();
    public ISize iSize = new ISize();
    public IPos iPos = new IPos();

    public IActionList acList = new IActionList();
    private Map<String,Component> componentMap = new HashMap<>();
    protected GDX.Func<Map> getComponent;

    protected Map<String,String> mapParam = new HashMap<>();
    protected GDX.Func<Map> getParam;
    protected GDX.Func1<Actor,String> connect;
    private GDX.Func<Actor> getActor;
    private GDX.Func<Object> userObject;
    private GDX.Func<String> getName;

    private GDX.Func<Map<String, GDX.Runnable<IActor>>> getRunMap;
    private GDX.Func<List<OSound>> loopSounds;

    public IActor(){}
    public <T> T Clone()
    {
        return Reflect.Clone(this);
    }

    //userObject
    public <T> T GetUserObject()
    {
        if (userObject==null) return null;
        return (T)userObject.Run();
    }
    public void SetUserObject(Object object)
    {
        userObject = ()->object;
    }

    public <T extends IActor> T GetIActor(String name)
    {
        return GetIActor(GetActor(name));
    }
    public <T extends IGroup> T GetIParent()
    {
        return GetIActor("");
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
        return new Actor(){
            @Override
            public void act(float delta) {
                super.act(delta);
                Update(delta);
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                OnDraw(batch,parentAlpha,()->super.draw(batch, parentAlpha));
            }
        };
    }
    protected void OnDraw(Batch batch, float parentAlpha,Runnable onDraw)
    {
        if (GetComponent("main")==null) onDraw.run();
        else GetComponent("main").Draw(batch,parentAlpha,onDraw);
    }
    public void SetActor(Actor actor)
    {
        getActor = ()->actor;
    }

    public void InitActor()
    {
        if (getActor==null) SetActor(NewActor());
        Clear();
        GetActor().setUserObject(this);
        JointParent();
    }
    public void JointParent()
    {
        try {
            Group parent = GetActor("");
            parent.addActor(GetActor());
        }catch (Exception e){}
    }
    protected void Clear()
    {
        GetActor().clear();
    }

    protected Color GetColor()
    {
        return Color.valueOf(hexColor);
    }
    public Vector2 GetSize()
    {
        return new Vector2(iSize.GetWidth(), iSize.GetHeight());
    }
    public void RefreshPosition()
    {
        SetPos(iPos.Get(),iPos.align.value);
    }
    protected void BaseRefresh()
    {
        Actor actor = GetActor();
        iSize.Set(actor);
        RefreshPosition();
        actor.setColor(GetColor());
        actor.setTouchable(touchable);
        actor.setVisible(visible);
    }
    protected void Update(float delta)
    {
        ForComponent((k,p)-> p.Update(delta));
        UpdateSound();
    }
    private boolean ContainsEvent(String... events)
    {
        for (String s : events)
            if (acList.Contains(s)) return true;
        return false;
    }
    private void InitEvent()
    {
        String name = GetName()==null?"":GetName();
        if(ContainsEvent("musicOn"))
            GAudio.i.AddMusicEvent(name, vl->{
                if (vl!=0) RunAction("musicOn");
                else RunAction("musicOff");
            });
        if(ContainsEvent("soundOn"))
            GAudio.i.AddSoundEvent(name, vl->{
                if (vl!=0) RunAction("soundOn");
                else RunAction("soundOff");
            });
        if(ContainsEvent("vibrateOn"))
            GAudio.i.AddVibrateEvent(name, vl->{
                if (vl!=0) RunAction("vibrateOn");
                else RunAction("vibrateOff");
            });
    }
    protected void RefreshEvent()
    {
        InitSound();
        acList.Init(this);
        RunEventAction("init");

        InitEvent();
        //input
        Actor actor = GetActor();
        actor.clearListeners();
        if(!ContainsEvent("enter","clicked","exit","touchDown","touchUp","touchDragged")) return;
        actor.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RunAction("clicked");
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                RunAction("enter");
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                RunAction("exit");
                super.exit(event,x,y,pointer,actor);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                RunAction("touchDown");
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                RunAction("touchDragged");
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                RunAction("touchUp");
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    public void RefreshContent(){}
    public void RefreshLanguage(){}
    public void Refresh()
    {
        InitActor();
        BaseRefresh();
        //RefreshEvent();

        AfterRefresh();
    }
    protected void AfterRefresh()
    {
        getParam = null;
        RefreshComponent();
        RefreshEvent();
    }
    public void SetConnect(GDX.Func1<Actor,String> connect)//this call when this created by json
    {
        this.connect = connect;
        iPos.getIActor = ()->this;
        iSize.getIActor = ()->this;
        //iPos.getTarget = connect;
        //iSize.getTarget = connect;
    }
    public void Disconnect()
    {
        connect = null;
    }

    public void SetName(String name)
    {
        getName = ()->name;
    }
    public String GetName()
    {
        if (getName==null) return "";
        return getName.Run();
    }

    public void Remove()
    {
        if (getActor==null) return;
        ForComponent((n,p)->p.Remove());
        if (connect!=null) RunEvent("remove");
        GetActor().remove();
        ForComponent((n,p)->p.AfterRemove());

        StopSound();
    }
    //Component
    public Map<String,Component> GetComponentData()
    {
        return componentMap;
    }
    public Map<String,Component> GetComponent()
    {
        if (getComponent==null)
        {
            Map map = new HashMap(componentMap);
            getComponent = ()->map;
        }
        return getComponent.Run();
    }
    protected void RefreshComponent()
    {
        getComponent = null;
        //ForComponent((k,p)->p.BeforeRefresh());
        ForComponent((k,p)->{
            p.getMain = ()->this;
            p.Refresh();
        });
        ForComponent((k,p)->p.AfterRefresh());
    }
    public void ForComponent(GDX.Runnable2<String,Component> cb)
    {
        Map<String,Component> map = GetComponent();
        for (String key : map.keySet())
            cb.Run(key,map.get(key));
//        for (String key : componentMap.keySet())
//            cb.Run(key,componentMap.get(key));
    }
    public <T extends Component> T GetComponent(String name)
    {
        return (T)GetComponent().get(name);
        //return (T)componentMap.get(name);
    }
    public <T extends Component> T GetComponent(Class<T> type)
    {
        for (Component p : GetComponent().values())
            if (Reflect.isAssignableFrom(p.getClass(),type)) return (T)p;
//        for (Component p : componentMap.values())
//            if (Reflect.isAssignableFrom(p.getClass(),type)) return (T)p;
        return null;
    }
    public void AddComponentSafety(String name,Component p)
    {
        GDX.PostRunnable(()->AddComponent(name, p));
    }
    public void AddComponent(String name,Component p)
    {
        GetComponent().put(name,p);
    }
    public void RemoveComponent(String name)
    {
        GetComponent().remove(name);
    }
    public void RemoveComponentSafety(String name)
    {
        GDX.PostRunnable(()->RemoveComponent(name));
    }
    //action
    public void RemoveEvent(String name)
    {
        acList.Remove(name);
    }
    private void RunEvent(String name)
    {
        if (!acList.Contains(name)) return;
        acList.Get(name).Run(this);
    }
    protected void RunEventAction(String event)//init,destroy
    {
        if (!acList.Contains(event)) return;
        RunAction(event);
    }

    public void StopAction()
    {
        BaseRefresh();
        GetActor().clearActions();
    }
    public void RunAction(String actionName)
    {
        actionName = actionName.replace("\n","");
        if (!acList.Contains(actionName)) return;
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

    public void SetColor(Color color)
    {
        GetActor().setColor(color);
    }
    public void Runnable(GDX.Runnable<IActor> cb)
    {
        cb.Run(this);
    }

    //param
    public Map<String,String> GetParamMap()
    {
        return mapParam;
    }
    public boolean HasParam(String name)
    {
        return mapParam.containsKey(name);
    }
    public Map<String,String> GetParam()
    {
        if (getParam==null)
        {
            Map map = new HashMap(mapParam);
            getParam = ()->map;
        }
        return getParam.Run();
    }
    public <T> T GetParam(String name,T value0)
    {
        try {
            String result = GetParam().get(name);
            return Reflect.GetConfig(result,value0);
        }catch (Exception e){}
        return value0;
    }
    public <T> void SetParam(String name,T value)
    {
        try {
            GetParam().put(name,value+"");
        }catch (Exception e){}
    }

    //Runnable
    private void InitRunMap()
    {
        if (getRunMap!=null) return;
        Map<String, GDX.Runnable<IActor>> map = new HashMap<>();
        getRunMap = ()->map;
    }
    public void SetRunnable(String name,GDX.Runnable<IActor> run)
    {
        InitRunMap();
        getRunMap.Run().put(name,run);
    }
    public GDX.Runnable<IActor> GetRunnable(String name)
    {
        if (getRunMap==null) return null;
        return getRunMap.Run().get(name);
    }

    //used function
    public Vector2 GetLocalPos(int align)
    {
        return Scene.GetLocal(GetActor(),align);
    }
    public Vector2 GetPos()
    {
        return Scene.GetPosition(GetActor(),Align.bottomLeft);
    }
    public Vector2 GetPos(int align)
    {
        return Scene.GetPosition(GetActor(),align);
    }
    public Vector2 GetStagePos()
    {
        return Scene.GetStagePosition(GetActor(),Align.bottomLeft);
    }
    public Vector2 GetStagePos(int align)
    {
        return Scene.GetStagePosition(GetActor(),align);
    }
    public Vector2 GetActorPos(int align,Actor actor)
    {
        return GetActor().getParent().localToActorCoordinates(actor,GetPos(align));
    }
    public void SetPos(Vector2 pos,int align)
    {
        Scene.SetPosition(GetActor(),pos,align);
    }
    public void SetPos(Vector2 pos)
    {
        SetPos(pos,Align.bottomLeft);
    }
    public void SetStagePos(Vector2 pos,int align)
    {
        Scene.SetStagePosition(GetActor(),pos,align);
    }

    public float GetStageRotate()
    {
        return Scene.GetStageRotation(GetActor());
    }
    public void SetStageRotate(float angle)
    {
        Scene.SetStageRotation(GetActor(),angle);
    }

    public Action Delay(Runnable run,float delay)
    {
        Action ac1 = Actions.delay(delay);
        Action ac2 = Actions.run(run);
        Action ac12 = Actions.sequence(ac1,ac2);
        GetActor().addAction(ac12);
        return ac12;
    }

    //event
    public void AddClick(Runnable onClick)
    {
        GetActor().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getPointer()!=0) return;
                onClick.run();
            }
        });
    }

    //Sound
    private void InitSound()
    {
        if (loopSounds!=null) StopSound();
        else {
            List sounds = new ArrayList();
            loopSounds = ()->sounds;
        }
    }
    public void PlaySound(String name,boolean loop)//for game sound
    {
        OSound oSound = new OSound(name,loop);
        if (loop) loopSounds.Run().add(oSound);
    }
    private void UpdateSound()
    {
        if (loopSounds==null || loopSounds.Run().size()<=0) return;
        for (OSound i : loopSounds.Run()) i.Loop();
    }
    private void StopSound()
    {
        if (loopSounds ==null) return;
        for (OSound i : loopSounds.Run()) i.Stop();
        loopSounds.Run().clear();
    }
    private void SetPan(GDX.Runnable2<Float,Float> cb)
    {
        Camera camera = GetActor().getStage().getCamera();
        Vector2 dir = GetStagePos(Align.center).sub(camera.position.x,camera.position.y);
        float size = camera.viewportWidth/2+100;
        float vol = 1-dir.len()/size;
        float pan = dir.x/size;
        vol = GetSoundValue(vol,0,1)*90;
        cb.Run(MathUtils.sinDeg(vol),GetSoundValue(pan,-1,1));
    }
    private float GetSoundValue(float value,float min,float max)
    {
        if (value<min) return min;
        if (value>max) return max;
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }

    public static <T extends IActor> T GetIActor(Actor actor)
    {
        return (T)actor.getUserObject();
    }

    public class OSound
    {
        public String name;
        public long id = -2;
        private boolean waiting,stop;

        public OSound(String name,boolean loop)
        {
            this.name = name;
            SetPan((vol,pan)->{
                if (loop){
                    waiting = true;
                    GAudio.i.PlayLoop(name,vol,pan,i->{
                        id=i;
                        waiting = false;
                        if (stop) Stop();
                    });
                }
                else GAudio.i.PlaySound(name,vol,pan);
            });
        }
        public void Loop()
        {
            if (waiting) return;
            SetPan((vol,pan)->{
                if (id==-1){
                    waiting = true;
                    GAudio.i.PlayLoop(name,vol,pan,i->{
                        id=i;
                        waiting = false;
                        if (stop) Stop();
                    });
                }
                else GAudio.i.SetPan(name,id,vol,pan);
            });
        }
        public void Stop()
        {
            stop = true;
            if (waiting) return;
            GAudio.i.StopSound(name,id);
        }
    }
}
