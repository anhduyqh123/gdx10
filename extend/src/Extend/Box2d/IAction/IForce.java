package Extend.Box2d.IAction;

import Extend.Box2d.IBody;
import com.badlogic.gdx.math.Vector2;

public class IForce extends IBodyAction {
    public Vector2 force = new Vector2();

    public IForce()
    {
        name = "force";
    }
    @Override
    protected void Set(IBody iBody) {
        iBody.GetBody().applyForceToCenter(force,true);
    }
}
