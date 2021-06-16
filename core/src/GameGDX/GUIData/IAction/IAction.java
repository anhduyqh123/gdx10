package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class IAction {
    public String name = "";
    public abstract Action Get();
    public abstract Action Get(IActor iActor);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IAction)) return false;
        IAction iAction = (IAction) o;
        return name.equals(iAction.name);
    }
}
