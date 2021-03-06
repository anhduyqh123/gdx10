package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IRepeat extends IForever{
    public int count = 1;

    public IRepeat()
    {
        name = "repeat";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.repeat(count,list.get(0).Get(iActor));
    }
}
