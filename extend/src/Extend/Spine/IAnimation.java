package Extend.Spine;

import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IAnimation extends IAction {

    public String aniName = "";
    public String idle = "idle";
    public boolean loop;

    public IAnimation()
    {
        name = "animation";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }

    @Override
    public void Run(IActor iActor) {
        GSpine spine = iActor.GetActor();
        if (loop) spine.SetAnimation(aniName,true);
        else spine.SetAnimation(aniName,idle);
    }
}
