package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IWeld extends IDistance{
    public float frequencyHz = 2;
    public float dampingRatio = 0.7f;

    @Override
    protected Joint Create(Actor obA, Actor obB) {
        Body bodyA = GetBody(objectA);
        Body bodyB = GetBody(objectB);
        Vector2 pA = anchorA.GetPhysicPos(obA);
        Vector2 pB = anchorB.GetPhysicPos(obB);

        WeldJointDef jointDef = new WeldJointDef();
        jointDef.bodyA = bodyA;
        jointDef.bodyB = bodyB;
        jointDef.localAnchorA.set(bodyA.getLocalPoint(pA));
        jointDef.localAnchorB.set(bodyB.getLocalPoint(pB));
        jointDef.referenceAngle = bodyB.getAngle() - bodyA.getAngle();

        jointDef.collideConnected = collideConnected;
        jointDef.frequencyHz = frequencyHz;
        jointDef.dampingRatio = dampingRatio;

        return GBox2d.CreateJoint(jointDef);
    }
}
