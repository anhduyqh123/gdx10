package Extend.Box2d;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class IConveyor extends IBody{
    public float speed = 5;
    @Override
    public void OnPreSolve(IBody iBody, Fixture fixture, Contact contact, Manifold oldManifold) {
        super.OnPreSolve(iBody, fixture, contact, oldManifold);
        contact.setTangentSpeed(speed);
    }

}
