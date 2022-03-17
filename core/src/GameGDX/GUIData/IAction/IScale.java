package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IScale extends IBaseAction {
    public float scaleX=1,scaleY=1;
    public boolean usedX=true,usedY=true;

    public IScale()
    {
        name = "scale";
    }

    @Override
    public Action Get(IActor iActor) {
        Actor actor = iActor.GetActor();
        float sx = actor.getScaleX();
        float sy = actor.getScaleY();
        if (usedX) sx = scaleX;
        if (usedY) sy = scaleY;
        return Get(sx,sy);
    }
    private Action Get(float x,float y)
    {
        return Actions.scaleTo(x,y,GetDuration(),GetInterpolation());
    }
}
