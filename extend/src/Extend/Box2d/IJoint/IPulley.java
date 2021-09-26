package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IPulley extends IDistance{
    public IAnchor groundA = new IAnchor();
    public IAnchor groundB = new IAnchor();
    public float ratio = 1;
    @Override
    protected Joint Create(Actor obA, Actor obB) {
        Body bodyA = GetBody(objectA);
        Body bodyB = GetBody(objectB);
        Vector2 pA = anchorA.GetPhysicPos(obA);
        Vector2 pB = anchorB.GetPhysicPos(obB);
        Vector2 gA = groundA.GetPhysicPos(obA);
        Vector2 gB = groundB.GetPhysicPos(obB);

        PulleyJointDef jointDef = new PulleyJointDef();
        jointDef.initialize(bodyA,bodyB,gA,gB,pA,pB,ratio);
        jointDef.collideConnected = collideConnected;
        return GBox2d.CreateJoint(jointDef);
    }
}
