package Extend.Box2d;

import GameGDX.GDX;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.ArrayList;
import java.util.List;

public class IPlatform extends IBody{
    public enum Side{
        Top(new Vector2(0,1)),
        Bottom(new Vector2(0,-1)),
        Left(new Vector2(-1,0)),
        Right(new Vector2(1,0));
        private Vector2 v;
        Side(Vector2 v)
        {
            this.v = v;
        }
        public boolean Check(Vector2 vel)
        {
            if (vel.x*v.x<0 || vel.y*v.y<0) return true;
            return false;
        }
    }
    private Side side = Side.Top;
    private GDX.Func<List<Fixture>> getFixtures;

    public IPlatform()
    {
        List<Fixture> list = new ArrayList<>();
        getFixtures = ()->list;
    }
    private List<Fixture> Fixtures()
    {
        return getFixtures.Run();
    }

    @Override
    public void OnBeginContact(IBody iBody, Fixture fixture, Contact contact) {
        Vector2[] points = contact.getWorldManifold().getPoints();
        int length = contact.getWorldManifold().getNumberOfContactPoints();
        //check if contact points are moving into platform
        for (int i = 0; i < length; i++) {
            //Vector2 pointVelPlatform = GetBody().getLinearVelocityFromWorldPoint(points[i]);
            Vector2 pointVelOther = iBody.GetBody().getLinearVelocityFromWorldPoint(points[i]);
            //Vector2 relativeVel = GetBody().getLocalVector(pointVelOther.sub(pointVelPlatform));

            if (side.Check(pointVelOther)) return;
//            if ( pointVelOther.y < 0 )
//                return;//point is moving down, leave contact solid and exit

//            if ( relativeVel.y < -1 ) //if moving down faster than 1 m/s, handle as before
//                return;//point is moving into platform, leave contact solid and exit
//            else if ( relativeVel.y < 1 ) { //if moving slower than 1 m/s
//                //borderline case, moving only slightly out of platform
//                Vector2 relativePoint = GetBody().getLocalPoint(points[i]);
//                float platformFaceY = 0.5f;//front of platform, from fixture definition :(
//                if ( relativePoint.y > platformFaceY - 0.05 )
//                    return;//contact point is less than 5cm inside front face of platfrom
//            }
//            else
//                ;//moving up faster than 1 m/s
        }

        //no points are moving into platform, contact should not be solid
        Fixtures().add(fixture);
        contact.setEnabled(false);

        super.OnBeginContact(iBody, fixture, contact);
    }

    @Override
    public void OnPreSolve(IBody iBody, Fixture fixture, Contact contact, Manifold oldManifold) {
        if (Fixtures().contains(fixture))
            contact.setEnabled(false);
        super.OnPreSolve(iBody, fixture, contact, oldManifold);
    }

    @Override
    public void OnEndContact(IBody iBody, Fixture fixture, Contact contact) {
        contact.setEnabled(true);
        Fixtures().remove(fixture);
        super.OnEndContact(iBody, fixture, contact);
    }
}
