package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IVisible extends IAction{
    public boolean visible;

    public IVisible()
    {
        name = "visible";
    }
    @Override
    public Action Get() {
        return Actions.visible(visible);
    }

    @Override
    public Action Get(IActor iActor) {
        return Get();
    }
}
