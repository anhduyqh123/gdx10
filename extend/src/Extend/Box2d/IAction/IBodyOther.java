package Extend.Box2d.IAction;

import Extend.Box2d.IBody;

public class IBodyOther extends IBodyAction{
    public enum Type
    {
        Active,
        UnActive
    }
    private Type type = Type.Active;

    public IBodyOther() {
        name = "other";
    }

    @Override
    protected void Set(IBody iBody) {
        if (type==Type.Active) iBody.GetBody().setActive(true);
        if (type==Type.UnActive) iBody.GetBody().setActive(false);
    }

}
