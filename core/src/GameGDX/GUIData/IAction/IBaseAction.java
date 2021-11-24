package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;

public class IBaseAction extends IDelay{
    public boolean current;
    public IInterpolation iInter = IInterpolation.linear;

    public IBaseAction()
    {
        name = "base";
    }

    @Override
    public Action Get(IActor iActor) {
        return null;
    }

    public Interpolation GetInterpolation()
    {
        return iInter.value;
    }
}
