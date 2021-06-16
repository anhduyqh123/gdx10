package GameGDX.GUIData.IAction;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;

public class ISequence extends IParallel {

    public ISequence()
    {
        name = "sequence";
    }
    @Override
    protected ParallelAction GetAction() {
        return Actions.sequence();
    }
}
