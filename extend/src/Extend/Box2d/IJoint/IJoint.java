package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import Extend.Box2d.IBody;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class IJoint extends Component {
    public String objectA = "";
    public String objectB = "";
    public boolean collideConnected;

    protected GDX.Func<Joint> get;

    public void BeforeRefresh() {
        Joint joint = Get();
        if (joint==null) return;
        GBox2d.DestroyJoint(joint);
        get = null;
    }

    public void Refresh() {
        try {
            Actor obA = FindIChild(objectA).GetActor();
            Actor obB = FindIChild(objectB).GetActor();
            GDX.PostRunnable(()->{
                Joint joint = Create(obA,obB);
                get = ()->joint;
            });
        }catch (Exception e){}
    }
    protected abstract Joint Create(Actor obA,Actor obB);

    public void Remove() {
        Joint joint = Get();
        if (joint!=null) GBox2d.DestroyJoint(joint);
    }

    protected Body GetBody(String name)
    {
        IBody iBody = FindIChild(name).GetComponent(IBody.class);
        return iBody.GetBody();
    }
    public <T extends Joint> T Get()
    {
        if (get==null) return null;
        return (T)get.Run();
    }

}
