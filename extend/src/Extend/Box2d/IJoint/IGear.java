package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.GearJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IGear extends IJoint{
    public String joint1 = "",joint2 = "";
    public float ratio = 1;

    @Override
    public void BeforeRefresh() {
        Joint joint = Get();
        if (joint==null) return;
        GBox2d.DestroyJoint(joint);
        get = null;
    }

    @Override
    protected Joint Create(Actor obA, Actor obB) {
        Body bodyA = GetBody(objectA);
        Body bodyB = GetBody(objectB);

        GearJointDef jointDef = new GearJointDef();
        jointDef.bodyA = bodyA;
        jointDef.bodyB = bodyB;
        jointDef.joint1 = GetJoint(joint1);
        jointDef.joint2 = GetJoint(joint2);
        jointDef.ratio = ratio;

        return GBox2d.CreateJoint(jointDef);
    }
    private Joint GetJoint(String name)
    {
        IJoint iJoint = GetIActor().GetComponent(name);
        return iJoint.Get();
    }
}
