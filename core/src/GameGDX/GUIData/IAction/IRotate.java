package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IRotate extends IBaseAction{

    public int angle;

    public IRotate()
    {
        name = "rotate";
    }

    @Override
    public Action Get(IActor iActor) {
        if (current) return Get((int) iActor.GetActor().getRotation()+angle);
        return Get(angle);
    }
    private Action Get(int angle)
    {
        return Actions.rotateTo(angle,duration, iInter.value);
    }
}
