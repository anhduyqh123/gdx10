package Extend.Box2d;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class IBody extends Component {
    public BodyDef.BodyType type = BodyDef.BodyType.StaticBody;
    public String category = "";
    public float linearDamping,angularDamping,gravityScale=1;
    public boolean fixedRotation,bullet,allowSleep=true;
    public List<IFixture> fixtures = new ArrayList<>();

    private GDX.Func<Body> getBody;
    private GDX.Func<List<IBodyListener>> contacts;

    //constant
    public GDX.Func<Velocity> getVelocity;
    public GDX.Func<Float> getAngularVelocity;

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
        bodyDef.gravityScale = gravityScale;
        return bodyDef;
    }
    private Body Get()
    {
        BodyDef bodyDef = GetBodyDef();
        Body body = GBox2d.NewBody(bodyDef);
        for (IFixture i : fixtures)
            i.OnCreateFixture(body::createFixture);
        return body;
    }
    protected void InitBody()
    {
        Body body = Get();
        body.setUserData(this);
        getBody = ()->body;
    }
    public Body GetBody()
    {
        if (getBody==null) return null;
        return getBody.Run();
    }
    @Override
    public void Refresh() {
        getVelocity = null;
        getAngularVelocity = null;
        InitContacts();

        DestroyBody();
        GDX.PostRunnable(()->{
            InitBody();
            UpdateGame();
            ForContacts(ContactListener::InitBody);
        });
    }

    @Override
    public void Remove() {
        DestroyBody();
    }

    protected void Update(float delta)
    {
        if (getBody==null) return;
//        if (GBox2d.active && type==BodyDef.BodyType.DynamicBody) UpdatePhysic();
//        else UpdateGame();
        if (!GBox2d.GetActive() || type==BodyDef.BodyType.StaticBody) UpdateGame();
        else UpdatePhysic();
        ForContacts(i->i.Update(delta));
    }
    private void UpdateGame()
    {
        Body body = GetBody();
        Actor actor = GetActor();
        Vector2 pos = Scene.GetStagePosition(actor);
        //GBox2d.SetTransform(body,pos,actor.getRotation());
        GBox2d.SetTransform(body,pos,Scene.GetStageRotation(actor));
    }
    private void UpdatePhysic()
    {
        UpdateConstVelocity();

        Body body = GetBody();
        Actor actor = GetActor();
        Vector2 pos = GBox2d.GetGamePosition(body);
        Scene.SetStagePosition(actor,pos);
        //actor.setRotation((float) Math.toDegrees(body.getAngle()));
        Scene.SetStageRotation(actor,(float) Math.toDegrees(body.getAngle()));
    }
    private void UpdateConstVelocity()
    {
        if (getVelocity!=null)
            getVelocity.Run().Run(GetBody());
        if (getAngularVelocity!=null)
            GetBody().setAngularVelocity(getAngularVelocity.Run());
    }
    public void SetVelocity(Velocity velocity)
    {
        getVelocity = ()->velocity;
    }
    public void SetAngularVelocity(float value)
    {
        getAngularVelocity = ()->value;
    }

    public void DestroyBody()
    {
        Body body = GetBody();
        if (body!=null) GBox2d.Destroy(body);
        getBody = null;
        ForContacts(IBodyListener::Destroy);
    }
    public void OnBeginContact(IBody iBody, Fixture fixture,Contact contact)
    {
        InitContact(iBody, fixture, contact);
        ForContacts(IBodyListener::BeginContact);
    }
    public void OnEndContact(IBody iBody, Fixture fixture,Contact contact)
    {
        InitContact(iBody, fixture, contact);
        ForContacts(IBodyListener::EndContact);
    }
    public void OnPreSolve(IBody iBody, Fixture fixture,Contact contact,Manifold oldManifold) {
        InitContact(iBody, fixture, contact);
        ForContacts(i->i.PreSolve(oldManifold));
    }

    public void OnPostSolve(IBody iBody, Fixture fixture,Contact contact,ContactImpulse impulse) {
        InitContact(iBody, fixture, contact);
        ForContacts(i->i.PostSolve(impulse));
    }
    public void OnRayCast(String name)
    {
        ForContacts(i->i.OnRayCast(name));
    }

    private void InitContact(IBody iBodyB, Fixture fixtureB,Contact contact)
    {
        IBody iBodyA = this;
        ForContacts(i->{
            i.iBodyA = iBodyA;
            i.iBodyB = iBodyB;
            i.fixtureB = fixtureB;
            i.fixtureA = fixtureB==contact.getFixtureA()?contact.getFixtureB():contact.getFixtureA();
            i.contact = contact;
        });
    }

    private void InitContacts()
    {
        List<IBodyListener> listeners = new ArrayList<>();
        contacts = ()->listeners;
    }
    private List<IBodyListener> GetContacts()
    {
        return contacts.Run();
    }
    public void AddListener(IBodyListener contact)
    {
        GetContacts().add(contact);
    }
    public void ClearContacts()
    {
        GetContacts().clear();
    }
    private void ForContacts(GDX.Runnable<IBodyListener> cb)
    {
        List<IBodyListener> listeners = new ArrayList<>(GetContacts());
        for (IBodyListener i : listeners)
            cb.Run(i);
    }

    public static class IBodyListener implements ContactListener
    {
        public IBody iBodyA,iBodyB;
        public Fixture fixtureA,fixtureB;
        public Contact contact;

        @Override
        public void InitBody() {

        }

        @Override
        public void Destroy() {

        }

        @Override
        public void Update(float delta) {

        }

        @Override
        public void BeginContact() {

        }

        @Override
        public void EndContact() {

        }

        @Override
        public void PreSolve(Manifold oldManifold) {

        }

        @Override
        public void PostSolve(ContactImpulse impulse) {

        }

        @Override
        public void OnRayCast(String name) {

        }
    }
    public interface ContactListener{
        void InitBody();
        void Destroy();
        void Update(float delta);
        void BeginContact();
        void EndContact();
        void PreSolve(Manifold oldManifold);
        void PostSolve(ContactImpulse impulse);
        void OnRayCast(String name);//name of RayCast
    }
    public static class Velocity
    {
        public boolean usedX,usedY;
        public Vector2 value = new Vector2();
        public void Run(Body body)
        {
            if (!usedX && !usedY) return;
            Vector2 v = body.getLinearVelocity();
            if (usedX) v.x = value.x;
            if (usedY) v.y = value.y;
            body.setLinearVelocity(v);
        }
        @Override
        public boolean equals(Object obj) {
            return Reflect.equals(this,obj);
        }
    }
}
