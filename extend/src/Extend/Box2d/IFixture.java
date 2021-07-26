package Extend.Box2d;

import GameGDX.GDX;
import GameGDX.Reflect;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public class IFixture {
    public float density = 1;
    public float friction = 0.2f;
    public float restitution = 0.2f;
    //public int category=1,mark=-1;;//,groupID=0;

    public IShape iShape = new IShape.ICircle();

    private FixtureDef Get(Shape shape)
    {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        return fixtureDef;
    }
    public void OnCreateFixture(GDX.Runnable<FixtureDef> onCreate)
    {
        iShape.OnCreate(s->onCreate.Run(Get(s)));
    }
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
