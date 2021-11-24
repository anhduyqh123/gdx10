package Extend.Box2d;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class IBody extends Component {
    public BodyDef.BodyType type = BodyDef.BodyType.StaticBody;
    public String category = "";
    public float linearDamping,angularDamping,gravityScale=1;
    public boolean fixedRotation,bullet,allowSleep=true,active=true,updateGame;
    public List<IFixture> fixtures = new ArrayList<>();

    private GDX.Func<Body> getBody;
    private GDX.Func<List<IBodyListener>> contacts;

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
        bodyDef.active = active;
        return bodyDef;
    }
    private Body Get()
    {
        BodyDef bodyDef = GetBodyDef();
        Body body = GBox2d.NewBody(bodyDef);

        Vector2 origin = new Vector2(GetActor().getOriginX(),GetActor().getOriginY());
        for (IFixture i : fixtures)
            i.OnCreateFixture(origin,body::createFixture);
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
        InitContacts();

        DestroyBody();
        getBody = null;
        GDX.PostRunnable(()->{
            InitBody();
            UpdateGame();
            ForContacts(ContactListener::InitBody);
        });
    }

    @Override
    public void Remove() {
        DestroyBody();
        GDX.PostRunnable(()->getBody=null);
        if (contacts==null) return;
        ForContacts(IBodyListener::Destroy);
    }

    protected void Update(float delta)
    {
        if (getBody==null) return;
//        if (GBox2d.active && type==BodyDef.BodyType.DynamicBody) UpdatePhysic();
//        else UpdateGame();
        if (!GBox2d.GetActive() || !GetBody().isActive()
                || updateGame || type==BodyDef.BodyType.StaticBody) UpdateGame();
        else UpdatePhysic();
        ForContacts(i->i.Update(delta));
    }
    private void UpdateGame()
    {
        //int align = GetIActor().iSize.origin.value;
        Body body = GetBody();
        Actor actor = GetActor();
        //Vector2 pos = Scene.GetStagePosition(actor).add(actor.getOriginX(),actor.getOriginY());
        Vector2 local = Scene.GetLocalOrigin(actor);
        Vector2 pos = Scene.GetStagePosition(actor,local);
        //System.out.println(pos0);
        float angle = fixedRotation?0:Scene.GetStageRotation(actor);
        //GBox2d.SetTransform(body,pos,actor.getRotation());
        GBox2d.SetTransform(body,pos,angle);
    }
    private void UpdatePhysic()
    {
        int align = GetIActor().iSize.origin.value;
        Body body = GetBody();
        Actor actor = GetActor();
        Vector2 pos = GBox2d.GetGamePosition(body);
        Scene.SetStagePosition(actor,pos,align);
        //actor.setRotation((float) Math.toDegrees(body.getAngle()));
        Scene.SetStageRotation(actor,(float) Math.toDegrees(body.getAngle()));
    }

    private void DestroyBody()
    {
        Body body = GetBody();
        if (body!=null) GBox2d.Destroy(body);
        //getBody = null;
    }

    public void OnBeginContact(IBody iBody, Fixture fixture,Contact contact)
    {
        if (iBody.GetBody()==null) return;
        InitContact(iBody, fixture, contact);
        ForContacts(IBodyListener::BeginContact);
    }
    public void OnEndContact(IBody iBody, Fixture fixture,Contact contact)
    {
        if (iBody.GetBody()==null) return;
        InitContact(iBody, fixture, contact);
        ForContacts(IBodyListener::EndContact);
    }
    public void OnPreSolve(IBody iBody, Fixture fixture,Contact contact,Manifold oldManifold) {
        if (iBody.GetBody()==null) return;
        InitContact(iBody, fixture, contact);
        ForContacts(i->i.PreSolve(oldManifold));
    }

    public void OnPostSolve(IBody iBody, Fixture fixture,Contact contact,ContactImpulse impulse) {
        if (iBody.GetBody()==null) return;
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

        public Vector2 GetDirect()
        {
            Vector2 p = GBox2d.GameToPhysics(Scene.GetStagePosition(iBodyA.GetActor(), Align.center));
            return new Vector2(contact.getWorldManifold().getPoints()[0]).sub(p);
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
}
