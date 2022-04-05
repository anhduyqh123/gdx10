package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IDelay extends IAction {
    public String duration = "0";

    public IDelay()
    {
        name = "delay";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.delay(GetDuration());
    }

    @Override
    public void Run(IActor iActor) {

    }
    protected float GetDuration()
    {
        return GetFloat(duration);
    }
    public void SetDuration(float value)
    {
        duration = value+"";
    }
}
