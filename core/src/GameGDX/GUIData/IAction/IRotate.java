package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IRotate extends IBaseAction{
    public int angle;
    public String delta="0";
    public boolean used = true;//by used current

    public IRotate()
    {
        name = "rotate";
    }

    @Override
    public Action Get(IActor iActor) {
        int value = (int) iActor.GetActor().getRotation();
        if (used) value = angle;
        return Get(value+GetInit(delta));
    }
    private Action Get(int angle)
    {
        return Actions.rotateTo(angle,GetDuration(), iInter.value);
    }
}
