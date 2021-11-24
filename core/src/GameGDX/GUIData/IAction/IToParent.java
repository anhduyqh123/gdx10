package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IToParent extends IAction{
    public enum Type
    {
        Add,
        At,
        After,
        Before
    }
    public Type type = Type.Add;
    public int index;
    public String stParent = "";
    public String stActor = "";

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
            IGroup iParent = iActor.GetIParent().GetIActor(stParent);
            Group parent = iParent.GetActor();
            Vector2 sPos = iActor.GetPos();
            Vector2 pos = actor.getParent().localToActorCoordinates(parent,sPos);
            actor.setPosition(pos.x,pos.y);
            switch (type)
            {
                case Add:
                    parent.addActor(actor);
                    break;
                case At:
                    parent.addActorAt(index,actor);
                    break;
                case After:
                    parent.addActorAfter(iParent.FindChild(stActor),actor);
                    break;
                case Before:
                    parent.addActorBefore(iParent.FindChild(stActor),actor);
                    break;
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
