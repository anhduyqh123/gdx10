package Extend.Box2d.IAction;

import Extend.Box2d.IBody;
import GameGDX.GUIData.IAction.IParallel;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.HashSet;

public class IContact extends IParallel {
    public enum Kind{
        Begin,
        End,
        PrePos,
        Post,
        RayCast,
        Collision //update when contact
    }
    public enum Type
    {
        Sensor,
        UnSensor,
        All
    }

    public Type type = Type.All;
    public boolean runB;//run with IBodyB
    public String categoryB = "";
    public Kind kind = Kind.Begin;

    public IContact()
    {
        name = "begin";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }
    private boolean CheckSensor(Fixture fixtureB)
    {
        if (type==Type.All) return true;
        if (type==Type.Sensor && fixtureB.isSensor()) return true;
        if (type==Type.UnSensor && !fixtureB.isSensor()) return true;
        return false;
    }
    public void Run(IActor iActor)
    {
        IBody iBody = iActor.GetComponent(IBody.class);
        if (iBody==null) return;
        switch (kind)
        {
            case Begin:
                iBody.AddListener(new IBody.IBodyListener(){
                    @Override
                    public void BeginContact() {
                        if (CheckSensor(fixtureB))
                            Run(iActor,iBodyB);
                    }
                });
                break;
            case End:
                iBody.AddListener(new IBody.IBodyListener(){
                    @Override
                    public void EndContact() {
                        if (CheckSensor(fixtureB))
                            Run(iActor,iBodyB);
                    }
                });
                break;
            case PrePos:
                iBody.AddListener(new IBody.IBodyListener(){
                    @Override
                    public void PreSolve(Manifold oldManifold) {
                        if (CheckSensor(fixtureB))
                            Run(iActor,iBodyB);
                    }
                });
                break;
            case Post:
                iBody.AddListener(new IBody.IBodyListener(){
                    @Override
                    public void PostSolve(ContactImpulse impulse) {
                        if (CheckSensor(fixtureB))
                            Run(iActor,iBodyB);
                    }
                });
                break;
            case RayCast:
                iBody.AddListener(new IBody.IBodyListener(){
                    @Override
                    public void OnRayCast(String name) {
                        if (categoryB.equals("") || name.equals(categoryB))
                            RunActions(iActor);
                    }
                });
                break;
            case Collision: //update when collision
                Collision(iActor,iBody);
                break;
            default:
        }
    }
    private void Collision(IActor iActor, IBody iBody)
    {
        HashSet<IBody> bodies = new HashSet<>();
        iBody.AddListener(new IBody.IBodyListener(){
            @Override
            public void BeginContact() {
                if (!CheckSensor(fixtureB)) return;
                if (categoryB.equals("") || iBodyB.category.equals(categoryB))
                    bodies.add(iBodyB);
            }

            @Override
            public void EndContact() {
                bodies.remove(iBodyB);
            }

            @Override
            public void Update(float delta) {
                if (bodies.size()<=0) return;
                if (runB)
                    for (IBody i : bodies) Run(iActor,i);
                else Run(iActor);
            }
        });
    }
    private void Run(IActor iMain,IBody iBodyB)
    {
        if (categoryB.equals("") || iBodyB.category.equals(categoryB))
        {
            if (runB) iMain = iBodyB.GetIActor();
            RunActions(iMain);
        }
    }
    private void RunActions(IActor iActor)
    {
        Action action = super.Get(iActor);
        iActor.GetActor().addAction(action);
//        for (IAction i : GetAll())
//            iActor.GetActor().addAction(i.Get(iActor));
    }
}
