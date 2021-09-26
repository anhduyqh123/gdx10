package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

public class IOther extends IAction{
    public enum Type{
        Remove,
        ClearAction,
        ToFront,
        RemoveComponent
    }
    public Type type = Type.Remove;

    public IOther()
    {
        name = "other";
    }
    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        if (type==Type.ClearAction) return ClearAction(iActor);
        return Actions.run(()->Run(iActor));
    }
    private Action ClearAction(IActor iActor)
    {
        Actor actor = iActor.GetActor();
        Array<Action> arr = new Array<>(actor.getActions());
        arr.removeIndex(0);
        return Actions.run(()->{
            for (Action a : arr)
                actor.removeAction(a);
        });
    }

    private void Run(IActor iActor)
    {
        Actor actor = iActor.GetActor();
        switch (type)
        {
            case Remove:
                iActor.Remove();
                //actor.remove();
                break;
            case ToFront:
                actor.toFront();
                break;
            case RemoveComponent:
                Component cp = iActor.GetComponent(name);
                if (cp!=null) cp.Remove();
                iActor.GetComponentData().remove(name);
                break;
            default:
        }
    }
}
