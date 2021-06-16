package GameGDX.Actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class FrameAction extends Action {
    private int count=0;
    private Runnable done;

    @Override
    public boolean act(float delta) {
        if (count<=0)
        {
            done.run();
            return true;
        }
        count--;
        return false;
    }
    public static FrameAction Get(int count, Runnable done)
    {
        FrameAction action = Actions.action(FrameAction.class);
        action.count = count;
        action.done = done;
        return action;
    }
}
