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
    public Action Get() {
        return Get(angle);
    }

    @Override
    public Action Get(IActor iActor) {
        if (relocation) return Get((int) iActor.GetActor().getRotation());
        return Get();
    }
    private Action Get(int angle)
    {
        return Actions.rotateTo(angle,duration, iInter.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IRotate)) return false;
        if (!super.equals(o)) return false;
        IRotate iRotate = (IRotate) o;
        return angle == iRotate.angle;
    }
}
