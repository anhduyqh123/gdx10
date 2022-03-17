package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IPos;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IMove extends IBaseAction{
    public boolean useX = true, useY = true;
    public IPos iPos = new IPos();
    public String delX="0",delY="0";
    public IMove()
    {
        name = "move";
    }

    @Override
    public Action Get(IActor iActor) {
        Actor actor = iActor.GetActor();
        int align = iPos.align.value;
        float x0 = actor.getX(align);
        float y0 = actor.getY(align);
        //if (current) return Actions.moveToAligned(x0,y0,align,duration,iInter.value);
        iPos.getIActor = iActor.iPos.getIActor;
        Vector2 pos = iPos.Get();
        if (useX) x0 = pos.x;
        if (useY) y0 = pos.y;
        return Actions.moveToAligned(x0+GetInit(delX),y0+GetInit(delY),align,GetDuration(),iInter.value);
    }
}
