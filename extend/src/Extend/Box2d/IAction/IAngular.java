package Extend.Box2d.IAction;

import Extend.Box2d.IBody;

public class IAngular extends IBodyAction {
    public float value;
    public boolean isConst = true;//true if velocity is constant;

    public IAngular()
    {
        name = "angular";
    }

    @Override
    protected void Set(IBody iBody) {
        if (isConst) iBody.SetAngularVelocity(value);
        else iBody.GetBody().setAngularVelocity(value);
    }
}
