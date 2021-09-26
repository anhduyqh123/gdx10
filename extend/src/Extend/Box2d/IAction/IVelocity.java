package Extend.Box2d.IAction;

import Extend.Box2d.IBody;

public class IVelocity extends IBodyAction {
    public IBody.Velocity velocity = new IBody.Velocity();
    public boolean isConst = true;//true if velocity is constant;
    public IVelocity()
    {
        name = "velocity";
    }
    @Override
    protected void Set(IBody iBody) {
        if (isConst) iBody.SetVelocity(velocity);
        else velocity.Run(iBody.GetBody());
    }
}
