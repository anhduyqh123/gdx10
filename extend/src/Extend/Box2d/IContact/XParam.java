package Extend.Box2d.IContact;

import Extend.Box2d.GBox2d;
import Extend.Box2d.IBody;
import GameGDX.GDX;
import GameGDX.Reflect;

public abstract class XParam {
    public GDX.Func<IBody> getIBody;

    public IBody IBody()
    {
        return getIBody.Run();
    }
    public abstract void Run();
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }

    public static class Velocity extends XParam
    {
        public IBody.Velocity velocity = new IBody.Velocity();
        @Override
        public void Run() {
            IBody().SetVelocity(velocity);
        }
    }
    public static class AngularVelocity extends XParam
    {
        public float value;
        @Override
        public void Run() {
            IBody().SetAngularVelocity(GBox2d.GameToPhysics(value));
        }
    }
}
