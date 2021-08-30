package Extend.Box2d.IAction;

import Extend.Box2d.IBody;
import GameGDX.GUIData.IAction.IParallel;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

public class IBegin extends IParallel {
    public String categoryB = "";

    public IBegin()
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
        ParallelAction par = (ParallelAction) super.Get(iActor);

        iBody.AddListener(new IBody.IBodyListener(){
            @Override
            public void BeginContact() {
                if (!iBodyB.category.equals(categoryB)) return;
                for (Action i : par.getActions())
                {
                    RunnableAction acRun = (RunnableAction) i;
                    acRun.run();
                }
            }
        });
    }
}
