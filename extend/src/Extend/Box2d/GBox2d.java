package Extend.Box2d;

import GameGDX.Config;
import GameGDX.GDX;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.GearJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GBox2d{
    public static GBox2d i;
    private static World world;

    public static boolean active=true;
    private static final float PTM = 0.01f;
    private static final float TIME_STEP = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6, POSITION_ITERATIONS = 2;
    private float accumulator = 0;

    //debug
    public static boolean debug;
    private OrthographicCamera camera0,camera;
    private Box2DDebugRenderer debugRenderer;

    private List<Joint> joints = new ArrayList<>();
    private List<Body> bodies = new ArrayList<>();
    private List<String> category = new ArrayList<>();

    public GBox2d()
    {
        i = this;
        Box2D.init();
        SetCategory(Config.i.Get("category").asStringArray());

        world = new World(new Vector2(0, -10f), true);
        world.setContactFilter((fixtureA, fixtureB) -> {
            if (!IFixture.Mark(fixtureA,fixtureB)) return false;
            IBody ib1 = (IBody) fixtureA.getBody().getUserData();
            IBody ib2 = (IBody) fixtureB.getBody().getUserData();
            return ib1.ShouldCollide(fixtureB) && ib2.ShouldCollide(fixtureA);
        });
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                IBody ib1 = (IBody) contact.getFixtureA().getBody().getUserData();
                IBody ib2 = (IBody) contact.getFixtureB().getBody().getUserData();
                ib1.OnBeginContact(ib2,contact.getFixtureB(),contact);
                ib2.OnBeginContact(ib1,contact.getFixtureA(),contact);
            }

            @Override
            public void endContact(Contact contact) {
                IBody ib1 = (IBody) contact.getFixtureA().getBody().getUserData();
                IBody ib2 = (IBody) contact.getFixtureB().getBody().getUserData();
                ib1.OnEndContact(ib2,contact.getFixtureB(),contact);
                ib2.OnEndContact(ib1,contact.getFixtureA(),contact);
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                IBody ib1 = (IBody) contact.getFixtureA().getBody().getUserData();
                IBody ib2 = (IBody) contact.getFixtureB().getBody().getUserData();
                ib1.OnPreSolve(ib2,contact.getFixtureB(),contact,oldManifold);
                ib2.OnPreSolve(ib1,contact.getFixtureA(),contact,oldManifold);
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                IBody ib1 = (IBody) contact.getFixtureA().getBody().getUserData();
                IBody ib2 = (IBody) contact.getFixtureB().getBody().getUserData();
                ib1.OnPostSolve(ib2,contact.getFixtureB(),contact,impulse);
                ib2.OnPostSolve(ib1,contact.getFixtureA(),contact,impulse);
            }
        });
    }
    public void Debug(OrthographicCamera camera0)
    {
        debug = true;
        this.camera0 = camera0;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,Scene.width*PTM,Scene.height*PTM);
        debugRenderer = new Box2DDebugRenderer();
    }

    private void DoPhysicsStep(float deltaTime) {
//        float frameTime = Math.min(deltaTime, 0.25f);
//        accumulator += frameTime;
//        while (accumulator >= TIME_STEP) {
//            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
//            accumulator -= TIME_STEP;
//        }
        world.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }
    public void Act(float delta)
    {
        if (active)
            DoPhysicsStep(delta);
    }
    public void Render()
    {
        if (debug)
        {
            camera.zoom = camera0.zoom;
            camera.position.set(GameToPhysics(camera0.position));
            camera.update();
            debugRenderer.render(world, camera.combined);
        }
    }
    public static void Clear()
    {
        List<Joint> joints = new ArrayList<>(i.joints);
        for (Joint i : joints)
            DestroyJoint(i);

        List<Body> list = new ArrayList<>(i.bodies);
        for (Body b : list) Destroy(b);
    }
    public static void Destroy(Body body)
    {
        if (!i.bodies.contains(body)) return;
        i.bodies.remove(body);
        if (!world.isLocked()) body.setActive(false);
        GDX.PostRunnable(()->world.destroyBody(body));
    }
    public static Body NewBody(BodyDef bodyDef)
    {
        Body body = world.createBody(bodyDef);
        i.bodies.add(body);
        return body;
    }

    public static Vector2 GetGameCenter(Body body)//Get the world position of the center of body
    {
        Vector2 pos = new Vector2(body.getWorldCenter());
        return pos.scl(1f/PTM);
    }
    public static Vector2 GetGamePosition(Body body)
    {
        Vector2 pos = new Vector2(body.getPosition());
        return pos.scl(1f/PTM);
    }
    public static void SetTransform(Body body,Vector2 pos,float angle)
    {
        pos.scl(PTM);
        body.setTransform(pos, (float) Math.toRadians(angle));
    }
    public static void SetPosition(Body body,Vector2 pos)
    {
        SetTransform(body,pos,body.getAngle());
    }
    public static void SetAngle(Body body,float angle)
    {
        body.setTransform(body.getPosition(), (float) Math.toRadians(angle));
    }

    public static void SetCategory(String[] category)
    {
        i.category.clear();
        i.category.addAll(Arrays.asList(category));
    }
    public static String GetCategory(short bit)
    {
        int index = (int)MathUtils.log2(bit);
        return i.category.get(index);
    }
    public static short GetCategoryBit(String name)
    {
        int index = i.category.indexOf(name);
        return (short) Math.pow(2,index);
    }

    public static Vector2 PhysicsToGame(Vector2 pos)
    {
        return new Vector2(pos).scl(1f/PTM);
    }

    public static Vector3 GameToPhysics(Vector3 pos)
    {
        return new Vector3(pos).scl(PTM);
    }
    public static Vector2 GameToPhysics(Vector2 pos)
    {
        return new Vector2(pos).scl(PTM);
    }
    public static float GameToPhysics(float value)
    {
        return value*PTM;
    }

    public static Joint CreateJoint(JointDef def)
    {
        Joint joint = world.createJoint(def);
        if (joint instanceof MouseJoint) i.joints.add(joint);
        if (joint instanceof GearJoint) i.joints.add(joint);
        return joint;
    }
    public static void DestroyJoint(Joint joint)
    {
        if (!i.joints.contains(joint)) return;
        i.joints.remove(joint);
        GDX.PostRunnable(()->world.destroyJoint(joint));
    }
}
