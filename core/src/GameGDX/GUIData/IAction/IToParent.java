package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import GameGDX.Scene;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IToParent extends IAction{
    public enum Type
    {
        Add,
        AddKeepTransform,
        At,
        After,
        Before
    }
    public Type type = Type.Add;
    public int index;
    public String stParent = "parent",stActor = "";//parent->current parent

    public IToParent()
    {
        name = "toParent";
    }
    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }

    @Override
    public void Run(IActor iActor) {
        try {
            Actor actor = iActor.GetActor();
            IGroup iParent = iActor.GetIParent();
            if (!stParent.equals("parent")) iParent = iParent.GetIActor(stParent);
            Group parent = iParent.GetActor();
            switch (type)
            {
                case Add:
                    actor.remove();
                    parent.addActor(actor);
                    break;
                case AddKeepTransform:
                    Scene.AddActorKeepTransform(actor,parent);
                    break;
                case At:
                    actor.remove();
                    parent.addActorAt(index,actor);
                    break;
                case After:
                    actor.remove();
                    parent.addActorAfter(iParent.FindChild(stActor),actor);
                    break;
                case Before:
                    actor.remove();
                    parent.addActorBefore(iParent.FindChild(stActor),actor);
                    break;
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
