package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ITarget extends IParallel{

    public String targetName = "";//""=> parent

    public ITarget() {
        name = "target";
    }

    @Override
    public Action Get(IActor iActor) {
        IActor iTarget = IActor.GetIActor(iActor.GetActor(targetName));
        Action action = super.Get(iTarget);
        return Actions.run(()->iTarget.GetActor().addAction(action));
    }
    @Override
    public void Run(IActor iActor) {
        IActor iTarget = IActor.GetIActor(iActor.GetActor(targetName));
        super.Run(iTarget);
    }
}
