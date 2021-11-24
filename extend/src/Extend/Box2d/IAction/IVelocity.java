package Extend.Box2d.IAction;

import Extend.Box2d.IBody;
import com.badlogic.gdx.math.Vector2;

public class IVelocity extends IBodyAction{
    public Vector2 value = new Vector2();
    public boolean usedX=true,usedY=true;
    public IVelocity()
    {
        name = "velocity";
    }

    @Override
    protected void Set(IBody iBody) {
        Vector2 v = iBody.GetBody().getLinearVelocity();
        if (usedX) v.x = value.x;
        if (usedY) v.y = value.y;
        iBody.GetBody().setLinearVelocity(v);
    }
}
