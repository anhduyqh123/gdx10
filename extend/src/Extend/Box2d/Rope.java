package Extend.Box2d;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class Rope extends Component {
    public int len = 5;

    @Override
    public void Refresh(Actor actor) {
        IGroup iGroup = GetIActor();
        IActor i1 = iGroup.GetIActor(0);
        IActor i2 = iGroup.GetIActor(1);
        List<IActor> list = Clones(len,i1);
        list.add(0,i1);
        list.add(i2);
        GDX.PostRunnable(()->{
//            float dis = GBox2d.GameToPhysics(i1.GetActor().getWidth());
//            for (int i=1;i<list.size()-1;i++)
//                Rope(list.get(0),list.get(i),i*dis);
            for (int i=0;i<list.size()-1;i++)
                Revolute(list.get(i),list.get(i+1));
        });
    }
    private List<IActor> Clones(int len,IActor iActor)
    {
        float dis = iActor.GetActor().getWidth();
        IGroup iGroup = GetIActor();
        List<IActor> iActors = new ArrayList<>();
        for(int i=0;i<len;i++)
            iActors.add(Reflect.Clone(iActor));

        int x=1;
        for(IActor i : iActors){
            i.SetConnect(iGroup::GetActor);
            i.iPos.delX=dis*x++;
            IBody iBody = i.GetComponent(IBody.class);
            iBody.type = BodyDef.BodyType.DynamicBody;
        }
        for (IActor i : iActors) i.Refresh();
        return iActors;
    }
    private void Rope(IActor i1,IActor i2,float dis)
    {
        Body b1 = i1.GetComponent(IBody.class).GetBody();
        Body b2 = i2.GetComponent(IBody.class).GetBody();
        Vector2 p1 = GBox2d.GameToPhysics(Scene.GetStagePosition(i1.GetActor(), Align.right));
        Vector2 p2 = GBox2d.GameToPhysics(Scene.GetStagePosition(i2.GetActor(), Align.left));

        RopeJointDef jointDef = new RopeJointDef();
        jointDef.bodyA = b1;
        jointDef.bodyB = b2;
        jointDef.localAnchorA.set(b1.getLocalPoint(p1));
        jointDef.localAnchorB.set(b2.getLocalPoint(p2));
        jointDef.maxLength = dis;
        GBox2d.CreateJoint(jointDef);
    }
    private void Revolute(IActor i1,IActor i2)
    {
        Body b1 = i1.GetComponent(IBody.class).GetBody();
        Body b2 = i2.GetComponent(IBody.class).GetBody();
        Vector2 p1 = GBox2d.GameToPhysics(Scene.GetStagePosition(i1.GetActor(), Align.right));
        Vector2 p2 = GBox2d.GameToPhysics(Scene.GetStagePosition(i2.GetActor(), Align.left));

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = b1;
        jointDef.bodyB = b2;
        jointDef.localAnchorA.set(b1.getLocalPoint(p1));
        jointDef.localAnchorB.set(b2.getLocalPoint(p2));
        jointDef.collideConnected = true;
        RevoluteJoint join = (RevoluteJoint) GBox2d.CreateJoint(jointDef);
    }
    private void Distance(IActor i1,IActor i2)
    {
        Body b1 = i1.GetComponent(IBody.class).GetBody();
        Body b2 = i2.GetComponent(IBody.class).GetBody();
        Vector2 p1 = GBox2d.GameToPhysics(Scene.GetStagePosition(i1.GetActor(), Align.right));
        Vector2 p2 = GBox2d.GameToPhysics(Scene.GetStagePosition(i2.GetActor(), Align.left));

        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.bodyA = b1;
        jointDef.bodyB = b2;
        jointDef.localAnchorA.set(b1.getLocalPoint(p1));
        jointDef.localAnchorB.set(b2.getLocalPoint(p2));
        jointDef.length = p1.dst(p2);
        GBox2d.CreateJoint(jointDef);
    }
}
