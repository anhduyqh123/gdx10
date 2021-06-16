package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IRunAction extends IAction {
    public Runnable runnable;

    public IRunAction()
    {
        name = "run";
    }
    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        Runnable run = runnable;
        return Actions.run(()->{
            if (run!=null) run.run();
        });
    }
}
