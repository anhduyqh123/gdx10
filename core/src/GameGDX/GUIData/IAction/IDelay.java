package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IDelay extends IAction {
    public float duration;

    public IDelay()
    {
        name = "delay";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.delay(duration);
    }

    @Override
    public void Run(IActor iActor) {

    }
}
