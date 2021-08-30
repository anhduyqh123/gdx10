package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Reflect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Component {
    public GDX.Func<IActor> getMain;
    public GDX.Func1<IActor,String> getIActor;

    public <T extends IActor> T GetIActor(String name)
    {
        return (T)getIActor.Run(name);
    }
    public <T extends IActor> T GetIActor()
    {
        return (T)getMain.Run();
    }

    public void BeforeRefresh(){}
    public abstract void Refresh(Actor actor);
    public void AfterRefresh()
    {

    }
    public void Remove(){}
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
