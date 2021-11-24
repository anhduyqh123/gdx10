package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IScale extends IBaseAction {
    public float scaleX=1,scaleY=1;

    public IScale()
    {
        name = "scale";
    }
    public IScale(float scaleX,float scaleY)
    {
        this();
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public Action Get(IActor iActor) {
        Actor actor = iActor.GetActor();
        if (current) return Get(actor.getScaleX(),actor.getScaleY());
        return Get(scaleX,scaleY);
    }
    private Action Get(float x,float y)
    {
        return Actions.scaleTo(x,y,duration,GetInterpolation());
    }
}
