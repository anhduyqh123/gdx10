package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;

public class IBaseAction extends IDelay{
    public boolean relocation;
    public IInterpolation iInter = IInterpolation.linear;

    public IBaseAction()
    {
        name = "base";
    }
    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        return null;
    }

    public Interpolation GetInterpolation()
    {
        return iInter.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IBaseAction)) return false;
        if (!super.equals(o)) return false;
        IBaseAction that = (IBaseAction) o;
        return relocation == that.relocation && iInter == that.iInter;
    }
}
