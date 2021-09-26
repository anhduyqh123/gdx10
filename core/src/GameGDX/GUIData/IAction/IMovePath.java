package GameGDX.GUIData.IAction;

import GameGDX.Actions.MovePath;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.GUIData.IChild.IPos;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class IMovePath extends IDelay{

    private boolean continuous,isRotate;
    public List<IPosX> points = new ArrayList<>();
    private float delAngle;
    private IInterpolation iInter = IInterpolation.linear;
    private IAlign iAlign = IAlign.center;

    public IMovePath()
    {
        name = "movePath";
    }
    protected Vector2[] GetPath(IActor iActor)
    {
        Vector2[] path = new Vector2[points.size()];
        for(int i=0;i<path.length;i++)
            path[i] = points.get(i).Get(iActor);
        return path;
    }

    @Override
    public Action Get(IActor iActor) {
        MovePath movePath = MovePath.Get(continuous,GetPath(iActor),duration,iInter.value);
        movePath.align = iAlign.value;
        movePath.delAngle = delAngle;
        movePath.isRotate = isRotate;
        return movePath;
    }

    public static class IPosX {

        public boolean useX = true, useY = true;
        public boolean current;
        public IPos iPos = new IPos();

        public Vector2 Get(IActor iActor)
        {
            Actor actor = iActor.GetActor();
            int align = iPos.align.value;
            float x0 = actor.getX(align)+iPos.delX;
            float y0 = actor.getY(align)+iPos.delY;
            if (current) return new Vector2(x0,y0);
            iPos.getIActor = iActor.iPos.getIActor;
            if (useX) x0 = iPos.GetX();
            if (useY) y0 = iPos.GetY();
            return new Vector2(x0,y0);
        }
        public int GetAlign()
        {
            return iPos.align.value;
        }
    }
}
