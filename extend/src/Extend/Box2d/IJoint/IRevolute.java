package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IRevolute extends IDistance{
    public boolean enableLimit;
    public float lowerAngle,upperAngle,motorSpeed;
    @Override
    protected Joint Create(Actor obA, Actor obB) {
        Body bodyA = GetBody(objectA);
        Body bodyB = GetBody(objectB);
        Vector2 pA = anchorA.GetPhysicPos(obA);
        Vector2 pB = anchorB.GetPhysicPos(obB);

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = bodyA;
        jointDef.bodyB = bodyB;
        jointDef.localAnchorA.set(bodyA.getLocalPoint(pA));
        jointDef.localAnchorB.set(bodyB.getLocalPoint(pB));

        jointDef.collideConnected = collideConnected;
        jointDef.enableLimit = enableLimit;
        jointDef.lowerAngle = (float) Math.toRadians(lowerAngle);
        jointDef.upperAngle = (float) Math.toRadians(upperAngle);
        jointDef.motorSpeed = (float) Math.toRadians(motorSpeed);
        return GBox2d.CreateJoint(jointDef);
    }
}
