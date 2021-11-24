package Extend.Box2d;

import GameGDX.Config;
import GameGDX.GDX;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

import java.util.*;

public class GBox2d extends Actor {
    public static GBox2d i;
    public static World world;

    private static boolean active=true;
    private static final float PTM = 0.01f;
    private static final float TIME_STEP = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6, POSITION_ITERATIONS = 2;

    //debug
    private OrthographicCamera camera0,camera;
    private Box2DDebugRenderer debugRenderer;

    private List<Body> bodies = new ArrayList<>();
    private List<String> category = new ArrayList<>();
    private Map<String,GDX.Runnable<Body>> destroyEvent = new HashMap<>();

    public GBox2d()
    {
        i = this;
        Box2D.init();

        JsonValue js = Config.i.Get("category");
        if (js!=null) SetCategory(js.asStringArray());

        world = new World(new Vector2(0, -10f), true);
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

    @Override
    public void setDebug(boolean debug) {
        super.setDebug(debug);
        if (getStage()!=null)
            SetDebug((OrthographicCamera) getStage().getCamera());
    }

    @Override
    public void act(float delta) {
        Act(delta);
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        if (debugRenderer==null) return;
        camera.zoom = camera0.zoom;
        camera.position.set(GameToPhysics(camera0.position));
        camera.update();
        debugRenderer.render(world, camera.combined);
    }

    private void SetDebug(OrthographicCamera camera0)
    {
        if (debugRenderer!=null) return;
        this.camera0 = camera0;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,camera0.viewportWidth*PTM,camera0.viewportHeight*PTM);
        debugRenderer = new Box2DDebugRenderer();
    }

    private void DoPhysicsStep(float deltaTime) {
        world.step( Math.min(deltaTime, 0.1f), VELOCITY_ITERATIONS, POSITION_ITERATIONS);//to front
//        accumulator += Math.min(deltaTime, 0.25f);
//        if (accumulator >= TIME_STEP) {
//            accumulator -= TIME_STEP;
//            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
//        }
    }
    private void Act(float delta)
    {
        if (active)
            DoPhysicsStep(delta);
    }
    public static void SetActive(boolean value)
    {
        //world.setContinuousPhysics(value);
        active = value;
    }
    public static boolean GetActive()
    {
        return active;
    }
    public static void Clear()
    {
        for (Joint i : GetJoints()) world.destroyJoint(i);

        List<Body> list = new ArrayList<>(i.bodies);
        for (Body i : list) Destroy(i);
    }
    public static boolean IsDestroyed(Body body)
    {
        return !i.bodies.contains(body);
    }
    public static void Destroy(Body body)
    {
        if (IsDestroyed(body)) return;
        for (GDX.Runnable<Body> b : i.destroyEvent.values()) b.Run(body);
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
    public static Joint CreateJoint(JointDef def)
    {
        return world.createJoint(def);
    }
    public static Array<Joint> GetJoints()
    {
        Array<Joint> joints = new Array<>();
        world.getJoints(joints);
        return joints;
    }
    public static void DestroyJoint(Joint joint)
    {
        if (GetJoints().contains(joint,false))
            world.destroyJoint(joint);
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

    //event
    public static void AddDestroyEvent(String key, GDX.Runnable<Body> cb)
    {
        i.destroyEvent.put(key, cb);
    }
}
