package Extend.Box2d;

import GameGDX.GDX;
import GameGDX.Ref;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

//RayCast
public class RayCast {
    public String name = "";
    public Vector2 p1, p2;
    public int category=1,mark = -1;
    public boolean isSensor;//contain fixture is sensor

    public RayCast() {}
    public RayCast(String name) {
        this.name = name;
    }
    public void SetPoint(Vector2 p1, Vector2 p2)
    {
        this.p1 = GBox2d.GameToPhysics(p1);
        this.p2 = GBox2d.GameToPhysics(p2);
    }

    private boolean Check(int category,int mark)
    {
        return (mark & category) != 0;
    }
    public void Run(GDX.Runnable2<IBody, Vector2> onRayCast) {
        GBox2d.world.rayCast((fixture, point, normal, fraction) -> {
            if (!isSensor && fixture.isSensor()) return 1;

            int categoryB = fixture.getFilterData().categoryBits;
            int markB = fixture.getFilterData().maskBits;
            if (!Check(category,markB) || !Check(categoryB,mark)) return 1;

            IBody iBody = (IBody) fixture.getBody().getUserData();
            iBody.OnRayCast(name);
            onRayCast.Run(iBody, GBox2d.PhysicsToGame(point));
            return 1;
        }, p1, p2);
    }

    public void RunClosest(GDX.Runnable2<IBody, Vector2> onRayCast) {
        Ref<Fixture> f = new Ref<>();
        Ref<Float> minFraction = new Ref<>();
        Vector2 p = new Vector2();
        GBox2d.world.rayCast((fixture, point, normal, fraction) -> {
            if (!isSensor && fixture.isSensor()) return 1;

            int categoryB = fixture.getFilterData().categoryBits;
            int markB = fixture.getFilterData().maskBits;
            if (!Check(category,markB) || !Check(categoryB,mark)) return 1;

            if (minFraction.Get() == null || fraction < minFraction.Get()) {
                minFraction.Set(fraction);
                f.Set(fixture);
                p.set(point);
            }
            return 1;
        }, p1, p2);
        if (f.Get() == null) return;
        IBody iBody = (IBody) f.Get().getBody().getUserData();
        iBody.OnRayCast(name);
        onRayCast.Run(iBody, GBox2d.PhysicsToGame(p));
    }
}
