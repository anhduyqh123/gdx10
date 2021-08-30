package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IDistance extends IJoint{
    public IAnchor anchorA = new IAnchor();
    public IAnchor anchorB = new IAnchor();

    @Override
    protected Joint Create(Actor obA, Actor obB) {
        Body bodyA = GetBody(objectA);
        Body bodyB = GetBody(objectB);
        Vector2 pA = anchorA.GetPhysicPos(obA);
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.initialize(bodyA,bodyB,pA,anchorB.GetPhysicPos(obB));
        jointDef.collideConnected = collideConnected;
        return GBox2d.CreateJoint(jointDef);
    }
}
