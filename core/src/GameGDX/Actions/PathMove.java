package GameGDX.Actions;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class PathMove extends TemporalAction {
    private Path<Vector2> doPath;
    private float delAngle;
    private boolean isRotate;

    @Override
    protected void begin() {
        actor.setOrigin(Align.center);
    }

    @Override
    protected void update(float percent) {
        Vector2 out = new Vector2();
        doPath.valueAt(out, percent);
        actor.setPosition(out.x,out.y, Align.center);
        if (isRotate)
        {
            doPath.derivativeAt(out, percent);
            actor.setRotation(out.angleDeg() +delAngle);
        }
    }
    public static PathMove Move(Vector2[] path, float duration)
    {
        return Move(path,duration, Interpolation.linear);
    }
    public static PathMove Move(Vector2[] path, float duration,Interpolation interpolation)
    {
        return Move(false,0,path,duration, interpolation);
    }
    public static PathMove Move(float delAngle,Vector2[] path, float duration)
    {
        return Move(delAngle,path,duration, Interpolation.linear);
    }
    public static PathMove Move(float delAngle,Vector2[] path, float duration,Interpolation interpolation)
    {
        return Move(true,delAngle,path,duration, interpolation);
    }
    private static PathMove Move(boolean isRotate,float delAngle,Vector2[] path, float duration, Interpolation interpolation)
    {
        PathMove pathMove = Actions.action(PathMove.class);
        pathMove.isRotate = isRotate;
        pathMove.delAngle = delAngle;
        Vector2[] way = GetPath(path);
        pathMove.doPath = new CatmullRomSpline(way,false);
        pathMove.setDuration(duration);
        pathMove.setInterpolation(interpolation);
        return pathMove;
    }
    private static Vector2[] GetPath(Vector2[] arr)
    {
        List<Vector2> list = new ArrayList<>();
        list.add(arr[0]);
        for(Vector2 pos : arr) list.add(pos);
        list.add(arr[arr.length-1]);
        return list.toArray(new Vector2[list.size()]);
    }
}
