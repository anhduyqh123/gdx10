package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IPrismatic extends IDistance{
    public Vector2 localAxis = new Vector2(0,1); //from Object A
    public boolean enableLimit,enableMotor;
    public float lower, upper,motorSpeed,maxMotorSpeed=10;

    @Override
    protected Joint Create(Actor obA, Actor obB) {
        Body bodyA = GetBody(objectA);
        Body bodyB = GetBody(objectB);
        Vector2 pA = anchorA.GetPhysicPos(obA);
        Vector2 pB = anchorB.GetPhysicPos(obB);

        PrismaticJointDef jointDef = new PrismaticJointDef();
        jointDef.bodyA = bodyA;
        jointDef.bodyB = bodyB;
        jointDef.localAnchorA.set(bodyA.getLocalPoint(pA));
        jointDef.localAnchorB.set(bodyB.getLocalPoint(pB));
        jointDef.localAxisA.set(bodyA.getLocalVector(localAxis));
        jointDef.referenceAngle = bodyB.getAngle() - bodyA.getAngle();

        jointDef.collideConnected = collideConnected;

        jointDef.enableLimit = enableLimit;
        jointDef.lowerTranslation = GBox2d.GameToPhysics(lower);
        jointDef.upperTranslation = GBox2d.GameToPhysics(upper);
        jointDef.enableMotor = enableMotor;
        jointDef.motorSpeed = motorSpeed;
        jointDef.maxMotorForce = maxMotorSpeed;
        return GBox2d.CreateJoint(jointDef);
    }
}
