package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IRope extends IDistance{
    @Override
    public Joint Create(Actor obA, Actor obB) {
        Body bodyA = GetBody(objectA);
        Body bodyB = GetBody(objectB);
        Vector2 pA = anchorA.GetPhysicPos(obA);
        Vector2 pB = anchorB.GetPhysicPos(obB);
        RopeJointDef jointDef = new RopeJointDef();
        jointDef.bodyA = bodyA;
        jointDef.bodyB = bodyB;
        jointDef.localAnchorA.set(bodyA.getLocalPoint(pA));
        jointDef.localAnchorB.set(bodyB.getLocalPoint(pB));
        jointDef.maxLength = Util.GetDistance(pA,pB);
        return GBox2d.CreateJoint(jointDef);
    }
}
