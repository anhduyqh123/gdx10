package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IAlign;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class IMove extends IBaseAction{

    public boolean useX = true, useY = true;
    public float x,y;
    public IAlign align = IAlign.bottomLeft;

    public IMove()
    {
        name = "move";
    }

    public void Set(Vector2 pos,IAlign align)
    {
        Set(pos.x,pos.y,align);
    }
    public void Set(float x,float y,IAlign align)
    {
        this.x = x;
        this.y = y;
        this.align = align;
    }
    @Override
    public Action Get() {
        return Get(GetX(),GetY(),align.value);
    }

    @Override
    public Action Get(IActor iActor) {
        Actor actor = iActor.GetActor();
        float x0 = actor.getX();
        float y0 = actor.getY();
        if (relocation) return Get(x0,y0, Align.bottomLeft);
        if (useX) x0 = GetX();
        if (useY) y0 = GetY();
        return Get(x0,y0,align.value);
    }
    private Action Get(float x,float y,int align)
    {
        return Actions.moveToAligned(x,y,align,duration,GetInterpolation());
    }

    private boolean Ratio(float value)
    {
        return value>0&&value<=1;
    }
    private float GetX()
    {
        if (Ratio(x)) return Scene.width*x;
        return x;
    }
    private float GetY()
    {
        if (Ratio(y)) return Scene.height*y;
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IMove)) return false;
        if (!super.equals(o)) return false;
        IMove iMove = (IMove) o;
        return useX == iMove.useX && useY == iMove.useY && Float.compare(iMove.x, x) == 0 && Float.compare(iMove.y, y) == 0 && align == iMove.align;
    }
}
