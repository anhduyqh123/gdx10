package Extend.Box2d.IAction;

import Extend.Box2d.IBody;
import com.badlogic.gdx.math.Vector2;

public class IBodyValue extends IBodyAction{
    public enum Type
    {
        Angular,
        GravityScale,
        DirectForce,
        ForwardVelocity
    }
    public Type type = Type.Angular;
    public float value;

    public IBodyValue()
    {
        name = "value";
    }

    @Override
    protected void Set(IBody iBody) {
        switch (type)
        {
            case Angular:
                iBody.GetBody().setAngularVelocity(value);
                break;
            case GravityScale:
                iBody.GetBody().setGravityScale(value);
                break;
            default:
                Forward(iBody);
        }
    }
    private void Forward(IBody iBody)
    {
        float angular = iBody.GetActor().getRotation();
        Vector2 dir = new Vector2(1,0).setAngleDeg(angular).setLength(value);
        switch (type)
        {
            case DirectForce:
                iBody.GetBody().applyForceToCenter(dir,true);
                break;
            case ForwardVelocity:
                iBody.GetBody().setLinearVelocity(dir);
                break;
        }
    }
}
