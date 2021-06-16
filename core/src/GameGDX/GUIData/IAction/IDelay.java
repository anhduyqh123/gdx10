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
    public Action Get() {
        return Actions.delay(duration);
    }

    @Override
    public Action Get(IActor iActor) {
        return Get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IDelay)) return false;
        if (!super.equals(o)) return false;
        IDelay iDelay = (IDelay) o;
        return Float.compare(iDelay.duration, duration) == 0;
    }
}
