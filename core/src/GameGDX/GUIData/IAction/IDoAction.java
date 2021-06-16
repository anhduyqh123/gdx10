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
        return Actions.run(()-> iActor.GetMain().RunAction(actionName));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IDoAction)) return false;
        if (!super.equals(o)) return false;
        IDoAction iDoAction = (IDoAction) o;
        return actionName.equals(iDoAction.actionName);
    }
}
