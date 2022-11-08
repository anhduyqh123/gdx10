package GameGDX.GUIData.IAction;

import GameGDX.Actions.MovePath;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.GUIData.IChild.IPos;
import GameGDX.Reflect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class IMovePath extends IDelay{

    private boolean continuous,isRotate;
    public List<MPos> points = new ArrayList<>();
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
        MovePath movePath = MovePath.Get(continuous,GetPath(iActor),iAlign.value,GetDuration(),iInter.value);
        movePath.delAngle = delAngle;
        movePath.isRotate = isRotate;
        return movePath;
    }

    public static class MPos {

        public boolean useX = true, useY = true;
        public IPos iPos = new IPos();

        public Vector2 Get(IActor iActor)
        {
            Actor actor = iActor.GetActor();
            int align = iPos.align.value;
            float x0 = actor.getX(align);
            float y0 = actor.getY(align);
            iPos.getIActor = iActor.iPos.getIActor;
            Vector2 pos = iPos.Get();
            if (useX) x0 = pos.x;
            if (useY) y0 = pos.y;
            return new Vector2(x0,y0);
        }
        public int GetAlign()
        {
            return iPos.align.value;
        }

        @Override
        public boolean equals(Object obj) {
            return Reflect.equals(this,obj);
        }
    }
}
