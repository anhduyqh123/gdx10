package GameGDX;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Util {
    //<editor-fold desc="Vector">
    public static float GetAngle(Vector2 p1, Vector2 p2,Vector2 p3)
    {
        Vector2 dir1 = GetDirect(p2,p1);
        Vector2 dir2 = GetDirect(p2,p3);
        return dir1.angleDeg(dir2);
    }
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
    public static float GetDistance(Vector2 pos,Vector2 p1, Vector2 p2)//p1,p2 is points of line
    {
        float a = GetDistance(p1,p2);
        float b = GetDistance(p1,pos);
        float c = GetDistance(pos,p2);
        float p = (a+b+c)/2;
        return 2*((float)Math.sqrt(p*(p-a)*(p-b)*(p-c))/a);
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
    public static short[] GetTriangles(float[] vert)
    {
        EarClippingTriangulator triangulate = new EarClippingTriangulator();
        return triangulate.computeTriangles(vert).toArray();
    }
    public static float[] GetVertices(Vector2[] arr)
    {
        float[] vert = new float[arr.length*2];
        for (int i=0;i<arr.length;i++)
        {
            vert[i*2] = arr[i].x;
            vert[i*2+1] = arr[i].y;
        }
        return vert;
    }
    public static void ForTriangles(Vector2[] points, GDX.Runnable<Vector2[]> cb)
    {
        short[] tri = GetTriangles(GetVertices(points));
        for (int i=0;i<tri.length;i+=3)
        {
            Vector2[] arr = {points[tri[i]],points[tri[i+1]],points[tri[i+2]]};
            cb.Run(arr);
        }
    }
    //</editor-fold>
    public static <T> void Reverse(List<T> list, GDX.Runnable<T> cb)
    {
        for (int i=list.size()-1;i>=0;i--)
            cb.Run(list.get(i));
    }
    public static <T> T Random(T[] arr)
    {
        return arr[MathUtils.random(0,arr.length-1)];
    }
    public static <T> T Random(List<T> l)
    {
        int id = MathUtils.random(0,l.size()-1);
        return l.get(id);
    }
    public static void Repeat(int repeat,Runnable cb)
    {
        for (int i=0;i<repeat;i++)
            cb.run();
    }
    public static void For(int from, int to, GDX.Runnable<Integer> cb)
    {
        for (int i=from;i<=to;i++) cb.Run(i);
    }
    public static <T> void For(List<T> list,GDX.Runnable<T> cb)
    {
        for (T i : list) cb.Run(i);
    }
    public static void ForIndex(List list,GDX.Runnable<Integer> cb)
    {
        For(0,list.size()-1,cb);
    }
}
