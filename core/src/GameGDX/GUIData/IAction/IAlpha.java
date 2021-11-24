package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IAlpha extends IBaseAction{

    public float alpha; //0->1

    public IAlpha()
    {
        name = "alpha";
    }
    public IAlpha(float alpha)
    {
        this();
        this.alpha = alpha;
    }
    public IAlpha(float alpha,float duration)
    {
        this(alpha);
        this.duration = duration;
    }
    public IAlpha(float alpha,float duration,IInterpolation iInterpolation)
    {
        this(alpha,duration);
        this.iInter = iInterpolation;
    }
    public Action Get(float alpha)
    {
        return Actions.alpha(alpha,duration, iInter.value);
    }

    @Override
    public Action Get(IActor iActor) {

        if (current) return Get(iActor.GetActor().getColor().a);
        return Get(alpha);
    }
}
