package Extend.Box2d;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class IRayCast extends Component {
    public String name = "";
    public String p1 = "p1",p2 = "p1End",hit="p2";

    public int category=1,mark = -1;
    public boolean active = true,isSensor;
    private GDX.Func<RayCast> getRayCast;


    private void Init()
    {
        if (getRayCast==null){
            RayCast rayCast = new RayCast();
            getRayCast = ()->rayCast;
        }
    }
    private RayCast GetRayCast()
    {
        return getRayCast.Run();
    }

    @Override
    public void Refresh() {
        Init();
        RayCast rayCast = GetRayCast();
        rayCast.name = name;
        rayCast.category = category;
        rayCast.mark = mark;
        rayCast.isSensor = isSensor;
    }

    @Override
    protected void Update(float delta) {
        try {
            RayCast();
        }catch (Exception e){}
    }

    private void RayCast()
    {
        IGroup iGroup = GetIActor();
        Actor a1 = iGroup.GetIChild(p1).GetActor();
        Actor a1End = iGroup.GetIChild(p2).GetActor();

        Vector2 p1 = Scene.GetStagePosition(a1, Align.center);
        Vector2 p1End = Scene.GetStagePosition(a1End, Align.center);

        IActor iHit = iGroup.GetIChild(hit);
        if (iHit!=null)
        {
            iHit.SetStagePos(p1End,Align.center);
            iHit.GetActor().setVisible(false);
        }

        if (!active) return;

        RayCast rayCast = GetRayCast();
        rayCast.SetPoint(p1,p1End);
        rayCast.RunClosest((ib, p)->{
            if (iHit!=null)
            {
                iHit.SetStagePos(p,Align.center);
                iHit.GetActor().setVisible(true);
            }
        });
    }
}
