package GameGDX;

import com.badlogic.gdx.math.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    //<editor-fold desc="Vector">
    public static Vector GetVector(String value)//(1,2)
    {
        value = value.replace("(","").replace(")","");
        String[] arr = value.split(",");
        if (arr.length==2) return new Vector2(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]));
        if (arr.length==3) return new Vector3(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]),Float.parseFloat(arr[2]));
        return null;
    }
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

    //grid
    public static <T> T Get(T[][] grid,Vector2 pos)
    {
        return grid[(int)pos.x][(int)pos.y];
    }
    public static <T> void Set(T[][] grid,Vector2 pos,T value)
    {
        grid[(int)pos.x][(int)pos.y] = value;
    }
    //readData
    public static String[][] ReadCSVFromName(String name)
    {
        return ReadCSV(GDX.GetStringFromName(name));
    }
    public static String[][] ReadCSV(String data)//[row][column]
    {
        Map<String,String> map0 = new HashMap<>();data = FindString(data,"\"\"","\"\"",map0);
        Map<String,String> map = new HashMap<>();
        data = FindString(data,"\"","\"",map);
        data = data.replace("\r","");
        String[] rows = data.split("\n");
        String[][] matrix = new String[rows.length][];
        for (int i=0;i<rows.length;i++)
        {
            matrix[i] = rows[i].split(",");
            for (int j=0;j<matrix[i].length;j++)
            {
                if (map.containsKey(matrix[i][j])) matrix[i][j] = map.get(matrix[i][j]);
                for (String key : map0.keySet())
                    if (matrix[i][j].contains(key)) matrix[i][j] = matrix[i][j].replace(key,map0.get(key));
            }
        }
        return matrix;
    }
    //String
    public static String FindString(String str,String c1,String c2)
    {
        return Try(()->{
            int s = str.indexOf(c1);
            int e = str.indexOf(c2,s+1);
            if (s==-1 || e==-1) return null;
            return str.substring(s,e+c2.length());
        },null);
    }
    public static String FindString(String str,String c1,String c2,Map<String,String> map)
    {
        String s = FindString(str,c1,c2);
        while (s!=null)
        {
            String key = "$"+map.size();
            map.put(key,s);
            str = str.replace(s,key);
            s = FindString(str,c1,c2);
        }
        return str;
    }
    //Try
    public static void Try(Runnable cb)
    {
        try {
            cb.run();
        }catch (Exception e){};
    }
    public static <T> T Try(GDX.Func<T> cb,T value0)
    {
        try {
            return cb.Run();
        }catch (Exception e){};
        return value0;
    }
}
