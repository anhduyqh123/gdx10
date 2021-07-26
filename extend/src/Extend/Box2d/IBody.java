package Extend.Box2d;

import GameGDX.Reflect;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import java.util.ArrayList;
import java.util.List;

public class IBody {
    public BodyDef.BodyType type = BodyDef.BodyType.StaticBody;
    public float linearDamping = 0;
    public float angularDamping = 0;
    public boolean fixedRotation,bullet,allowSleep=true;
    public int category=1,mark=-1;

    public List<IFixture> fixtures = new ArrayList<>();

    public IBody()
    {
        fixtures.add(new IFixture());
    }
    private BodyDef GetBodyDef()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.linearDamping = linearDamping;
        bodyDef.angularDamping = angularDamping;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.bullet = bullet;
        bodyDef.allowSleep = allowSleep;
        return bodyDef;
    }
    public Body Get()
    {
        Body body = GBox2d.NewBody(GetBodyDef());
        for (IFixture i : fixtures)
            i.OnCreateFixture(fix->{
                fix.filter.categoryBits = (short)category;
                fix.filter.maskBits = (short) mark;
                body.createFixture(fix);
            });
        return body;
    }
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
