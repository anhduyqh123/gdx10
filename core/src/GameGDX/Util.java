package GameGDX;

import com.badlogic.gdx.math.Vector2;

public class Util {
    //<editor-fold desc="Vector">
    public static Vector2 GetMidPosition(Vector2 pos1, Vector2 pos2)
    {
        Vector2 pos = new Vector2(pos1);
        pos.add(pos2);
        return pos.scl(0.5f);
    }
    public static Vector2 GetDirect(Vector2 pos1, Vector2 pos2)
    {
        Vector2 pos = new Vector2(pos2);
        return pos.sub(pos1);
    }
    public static float GetDistance(Vector2 pos1, Vector2 pos2)
    {
        return GetDirect(pos1,pos2).len();
    }
    public static void Round(Vector2 v)
    {
        v.x = Math.round(v.x);
        v.y = Math.round(v.y);
    }
    public static Vector2 GetNormalPos(Vector2 pos1, Vector2 pos2, float percent)
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
    public static Vector2[] GetPath(Vector2 start,Vector2 end,float percent)
    {
        Vector2 mid = Util.GetNormalPos(start,end,percent);
        return new Vector2[]{start,mid,end};
    }
    //</editor-fold>
}
