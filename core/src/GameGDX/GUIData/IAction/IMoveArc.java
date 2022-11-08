package GameGDX.GUIData.IAction;

import GameGDX.Actions.MovePath;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.math.Vector2;

public class IMoveArc extends IMovePath{
    public float percent;
    public IMoveArc()
    {
        name = "moveArc";
        points.add(new MPos());
        points.add(new MPos());
    }
    protected Vector2[] GetPath(IActor iActor)
    {
        Vector2[] path = new Vector2[3];
        path[0] = points.get(0).Get(iActor);
        path[2] = points.get(1).Get(iActor);
        path[1] = MovePath.GetNormalPos(path[0],path[2],percent);
        return path;
    }
}
