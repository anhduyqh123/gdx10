package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IDoAction extends IAction{

    public String actionName = "";

    public IDoAction()
    {
        name = "do";
    }
    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()-> iActor.RunAction(actionName));
    }
}
