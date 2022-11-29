package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.Scene;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IRotate extends IBaseAction{
    public int angle;
    public String delta="0";
    public boolean used = true;//by used current
    public boolean stage;

    public IRotate()
    {
        name = "rotate";
    }

    @Override
    public Action Get(IActor iActor) {
        int value = (int) iActor.GetActor().getRotation();
        if (used) value = angle;
        if (stage) value = (int)Scene.StageToLocalRotation(angle,iActor.GetActor());
        return Get(value+(int)GetFloat(delta));
    }
    private Action Get(int angle)
    {

        return Actions.rotateTo(angle,GetDuration(), iInter.value);
    }
}
