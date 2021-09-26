package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Reflect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Component {
    public GDX.Func<IActor> getMain;
    public GDX.Func1<IActor,String> findIChild;

    public <T extends IActor> T FindIChild(String name)
    {
        return (T) findIChild.Run(name);
    }
    public <T extends IActor> T GetIActor()
    {
        return (T)getMain.Run();
    }
    public <T extends Actor> T GetActor()
    {
        return GetIActor().GetActor();
    }

    public void BeforeRefresh(){}
    public void Refresh(){}
    public void AfterRefresh()
    {
    }
    protected void Update(float delta)
    {

    }
    public void Remove(){}
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
