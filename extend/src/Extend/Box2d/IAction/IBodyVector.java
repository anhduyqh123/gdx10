package Extend.Box2d.IAction;

import Extend.Box2d.IBody;
import com.badlogic.gdx.math.Vector2;

public class IBodyVector extends IBodyAction{
    public enum Type
    {
        Velocity,
        Force,
        Impulse
    }
    public Type type = Type.Velocity;
    public Vector2 value = new Vector2();
    public boolean usedX=true,usedY=true;

    public IBodyVector() {
        name = "vector";
    }
    @Override
    protected void Set(IBody iBody) {
        switch (type)
        {
            case Velocity:
                Vector2 v = iBody.GetBody().getLinearVelocity();
                if (usedX) v.x = value.x;
                if (usedY) v.y = value.y;
                iBody.GetBody().setLinearVelocity(v);
                break;
            case Force:
                iBody.GetBody().applyForceToCenter(value,true);
                break;
            case Impulse:
                break;
            default:

        }
    }
}
