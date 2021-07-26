package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IOther extends IAction{
    public enum Type{
        Remove,
        ToFront
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
        return Actions.run(()->Run(iActor));
    }
    private void Run(IActor iActor)
    {
        Actor actor = iActor.GetActor();
        switch (type)
        {
            case Remove:
                actor.remove();
                break;
            case ToFront:
                actor.toFront();
                break;
            default:
        }
    }
}
