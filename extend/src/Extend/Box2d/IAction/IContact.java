package Extend.Box2d.IAction;

import Extend.Box2d.IBody;
import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IAction.IParallel;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IContact extends IParallel {
    public enum Kind{
        Begin,
        End,
        RayCast
    }

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
    private void Run(IActor iActor)
    {
        IBody iBody = iActor.GetComponent(IBody.class);
        if (iBody==null) return;

        if (kind==Kind.Begin)
            iBody.AddListener(new IBody.IBodyListener(){
                @Override
                public void BeginContact() {
                    Run(iActor,iBodyB);
                }
            });
        if (kind==Kind.End)
            iBody.AddListener(new IBody.IBodyListener(){
                @Override
                public void EndContact() {
                    Run(iActor,iBodyB);
                }
            });
        if (kind==Kind.RayCast)
            iBody.AddListener(new IBody.IBodyListener(){
                @Override
                public void OnRayCast(String name) {
                    if (categoryB.equals("") || name.equals(categoryB))
                        RunActions(iActor);
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
