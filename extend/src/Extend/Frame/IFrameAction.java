package Extend.Frame;

import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IFrameAction extends IAction {
    public String ani = "";
    public String nextAni = "";

    public IFrameAction()
    {
        name = "frame";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }

    @Override
    public void Run(IActor iActor) {
        GFrame frame = iActor.GetActor();
        if (nextAni.equals("")) frame.SetAnimation(ani);
        else frame.SetAnimation(ani,()->frame.SetAnimation(nextAni));
    }
}
