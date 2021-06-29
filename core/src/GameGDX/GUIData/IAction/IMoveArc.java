package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.math.Vector2;

public class IMoveArc extends IMovePath{
    public float percent;
    public IMoveArc()
    {
        name = "moveArc";
        points.add(new IPosX());
        points.add(new IPosX());
    }
    protected Vector2[] GetPath(IActor iActor)
    {
        Vector2[] path = new Vector2[3];
        path[0] = points.get(0).Get(iActor);
        path[2] = points.get(1).Get(iActor);
        path[1] = GetNormalPos(path[0],path[2],percent);
        return path;
    }
    private Vector2 GetNormalPos(Vector2 pos1, Vector2 pos2, float percent)
    {
        Vector2 dir = new Vector2(pos1);
        dir.sub(pos2);
        float l = dir.len()/2;
        if (percent>0) dir.set(-dir.y,dir.x);
        else dir.set(dir.y,-dir.x);
        Vector2 mid = new Vector2(pos1);
        mid.add(pos2);
        mid.scl(0.5f);
        mid.add(dir.setLength(l*Math.abs(percent)));
        return mid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IMoveArc)) return false;
        if (!super.equals(o)) return false;
        IMoveArc iMoveArc = (IMoveArc) o;
        return Float.compare(iMoveArc.percent, percent) == 0;
    }
}
