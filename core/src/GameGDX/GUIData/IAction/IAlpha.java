package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IAlpha extends IBaseAction{

    public float alpha; //0->1
    public boolean used = true;//by used current

    public IAlpha()
    {
        name = "alpha";
    }
    public Action Get(float alpha)
    {
        return Actions.alpha(alpha,GetDuration(), iInter.value);
    }

    @Override
    public Action Get(IActor iActor) {
        float value = iActor.GetActor().getColor().a;
        if (used) value = alpha;
        return Get(value);
    }
}
