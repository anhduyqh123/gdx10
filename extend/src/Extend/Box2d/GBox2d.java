package Extend.Box2d;

import GameGDX.Config;
import GameGDX.GDX;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GBox2d extends Actor {
    private static GBox2d i;
    private static World world;

    public static boolean active=true;
    private static float PTM = 0.01f;
    private static final float TIME_STEP = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6, POSITION_ITERATIONS = 2;
    private float accumulator = 0;

    //debug
    public static boolean debug;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private List<Body> bodies = new ArrayList<>();
    private List<String> category = new ArrayList<>();

    public GBox2d()
    {
        i = this;
        Box2D.init();
        SetCategory(Config.i.Get("category").asStringArray());

        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                GObject ob1 = (GObject) contact.getFixtureA().getBody().getUserData();
                GObject ob2 = (GObject) contact.getFixtureB().getBody().getUserData();
                ob1.OnBeginContact(ob2);
                ob2.OnBeginContact(ob1);
            }

            @Override
            public void endContact(Contact contact) {
                GObject ob1 = (GObject) contact.getFixtureA().getBody().getUserData();
                GObject ob2 = (GObject) contact.getFixtureB().getBody().getUserData();
                ob1.OnEndContact(ob2);
                ob2.OnEndContact(ob1);
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }
    public void Debug()
    {
        debug = true;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,Scene.width*PTM,Scene.height*PTM);
        debugRenderer = new Box2DDebugRenderer();
    }
    public void Zoom(float delta)
    {
        camera.zoom += delta;
    }
    public void Position(Vector2 p)
    {
        camera.position.set(GameToPhysics(p),0);
    }

    private void DoPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    public void act(float delta) {
        if (active)
            DoPhysicsStep(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (debug)
        {
            camera.update();
            debugRenderer.render(world, camera.combined);
        }
    }

    public static void Clear()
    {
        List<Body> list = new ArrayList<>(i.bodies);
        for (Body b : list) Destroy(b);
    }
    public static void Destroy(Body body)
    {
        if (!i.bodies.contains(body)) return;
        i.bodies.remove(body);
        body.setActive(false);
        GDX.PostRunnable(()->world.destroyBody(body));
    }
    public static Body NewBody(BodyDef bodyDef)
    {
        Body body = world.createBody(bodyDef);
        i.bodies.add(body);
        return body;
    }

    public static Vector2 GetGameCenter(Body body)
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
        SetTransform(body,body.getPosition(),angle);
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

    public static Vector2 GameToPhysics(Vector2 pos)
    {
        return new Vector2(pos).scl(PTM);
    }
    public static float GameToPhysics(float value)
    {
        return value*PTM;
    }
}
