package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IForever extends IParallel{

    public IForever()
    {
        name = "forever";
    }
    @Override
    public void Add(IAction iAction) {
        if (list.size()>=1) return;
        super.Add(iAction);
    }

    @Override
    public Action Get() {
        return Actions.forever(list.get(0).Get());
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.forever(list.get(0).Get(iActor));
    }
}
