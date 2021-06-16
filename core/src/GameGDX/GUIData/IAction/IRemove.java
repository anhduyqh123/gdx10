package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IRemove extends IAction{

    public IRemove()
    {
        name = "remove";
    }
    @Override
    public Action Get() {
        return Actions.removeActor();
    }

    @Override
    public Action Get(IActor iActor) {
        return Get();
    }
}
