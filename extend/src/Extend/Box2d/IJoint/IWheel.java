package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IWheel extends IJoint{
    public float frequencyHz = 2;
    public float dampingRatio = 0.7f;
    public IAnchor anchorB = new IAnchor();
    public Vector2 axis = new Vector2();

    @Override
    protected Joint Create(Actor obA, Actor obB) { //obB is Wheel
        Body bodyA = GetBody(objectA);
        Body bodyB = GetBody(objectB);
        WheelJointDef jointDef = new WheelJointDef();
        jointDef.frequencyHz = frequencyHz;
        jointDef.dampingRatio = dampingRatio;
        jointDef.initialize(bodyA,bodyB,anchorB.GetPhysicPos(obB),axis);
        return GBox2d.CreateJoint(jointDef);
    }
}
