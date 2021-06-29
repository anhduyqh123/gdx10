package GameGDX.Actions;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class MovePath extends TemporalAction {
    public Path<Vector2> doPath;
    public float delAngle;
    public boolean isRotate;
    public int align = Align.center;

    @Override
    protected void update(float percent) {
        Vector2 out = new Vector2();
        doPath.valueAt(out, percent);
        actor.setPosition(out.x,out.y, align);
        if (isRotate)
        {
            doPath.derivativeAt(out, percent);
            actor.setRotation(out.angleDeg() +delAngle);
        }
    }
    public static MovePath Get(Vector2[] path, float duration)
    {
        return Get(path,duration, Interpolation.linear);
    }
    public static MovePath Get(Vector2[] path, float duration, Interpolation interpolation)
    {
        return Get(false,0,path,duration, interpolation);
    }
    public static MovePath Get(float delAngle, Vector2[] path, float duration)
    {
        return Get(delAngle,path,duration, Interpolation.linear);
    }
    public static MovePath Get(float delAngle, Vector2[] path, float duration, Interpolation interpolation)
    {
        return Get(true,delAngle,path,duration, interpolation);
    }
    private static MovePath Get(boolean isRotate, float delAngle, Vector2[] path, float duration, Interpolation interpolation)
    {
        MovePath movePath = Get(false,path,duration,interpolation);
        movePath.isRotate = isRotate;
        movePath.delAngle = delAngle;
        return movePath;
    }
    public static MovePath Get(boolean continuous, Vector2[] path, float duration, Interpolation interpolation)
    {
        MovePath movePath = Actions.action(MovePath.class);
        Vector2[] way = path;
        if (!continuous) way = GetPath(path);
        movePath.doPath = new CatmullRomSpline(way,continuous);
        movePath.setDuration(duration);
        movePath.setInterpolation(interpolation);
        return movePath;
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
