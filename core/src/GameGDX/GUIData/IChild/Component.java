package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Reflect;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Component {
    public GDX.Func<IActor> getMain;
    public GDX.Func1<IActor,String> findIChild;

    public void New(){}//for editor

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

    public void Refresh(){}
    public void AfterRefresh()
    {
    }
    protected void Update(float delta)
    {

    }
    protected void Draw(Batch batch, float parentAlpha, Runnable onDraw)
    {
        onDraw.run();
    }

    public void Remove(){}
    public void AfterRemove(){}
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
