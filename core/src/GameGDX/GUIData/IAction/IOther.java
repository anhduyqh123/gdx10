package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

public class IOther extends IAction{
    public enum Type{
        Remove,
        ClearAction,
        Visible,
        Disable,
        TouchEnable,
        TouchDisable,
        ToFront,
        RemoveComponent,
        Clone, //for IGroup
        RemoveChild,
        AddChild
    }
    public Type type = Type.Remove;

    public IOther()
    {
        name = "other";
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
        return Actions.run(()->{
            actor.getActions().removeAll(arr,false);
        });
    }

    public void Run(IActor iActor)
    {
        Actor actor = iActor.GetActor();
        switch (type)
        {
            case Remove:
                iActor.Remove();
                break;
            case Visible:
                actor.setVisible(true);
                break;
            case Disable:
                actor.setVisible(false);
                break;
            case TouchEnable:
                actor.setTouchable(Touchable.enabled);
                break;
            case TouchDisable:
                actor.setTouchable(Touchable.disabled);
                break;
            case ToFront:
                actor.toFront();
                break;
            case RemoveComponent:
                Component cp = iActor.GetComponent(name);
                if (cp!=null) cp.Remove();
                iActor.GetComponent().remove(name);
                break;
            default:
                Run((IGroup) iActor);
        }
    }
    private void Run(IGroup iGroup)
    {
        switch (type)
        {
            case Clone:
                if (iGroup.Contain(name))
                    iGroup.Clone(name);
                break;
            case RemoveChild:
                iGroup.GetIChild(name).GetActor().remove();
                break;
            case AddChild:
                iGroup.GetIChild(name).JointParent();
                break;
        }
    }
}
