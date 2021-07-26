package Extend.Box2d;

import GameGDX.GDX;
import GameGDX.Reflect;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public abstract class IShape {

    public abstract void OnCreate(GDX.Runnable<Shape> onCreate);

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }

    public static class ICircle extends IShape
    {
        public Vector2 pos = new Vector2();
        public float radius = 60;

        public ICircle(){}
        public ICircle(float size)
        {
            pos.set(size/2,size/2);
            radius = size/2;
        }

        @Override
        public void OnCreate(GDX.Runnable<Shape> onCreate) {
            CircleShape shape = new CircleShape();
            shape.setPosition(GBox2d.GameToPhysics(pos));
            shape.setRadius(GBox2d.GameToPhysics(radius));
            onCreate.Run(shape);
        }
    }
    public static class IPolygon extends IShape
    {
        public List<Vector2> points = new ArrayList<>();

        public IPolygon(){}
        public IPolygon(float width,float height)
        {
            points.add(new Vector2(0,0));
            points.add(new Vector2(width,0));
            points.add(new Vector2(width,height));
            points.add(new Vector2(0,height));
        }

        @Override
        public void OnCreate(GDX.Runnable<Shape> onCreate) {
            Vector2[] points = GetPoints();
            short[] tri = Util.GetTriangles(Util.GetVertices(points));
            for (int i=0;i<tri.length;i+=3)
            {
                Vector2[] arr = {points[tri[i]],points[tri[i+1]],points[tri[i+2]]};
                PolygonShape shape = new PolygonShape();
                shape.set(arr);
                onCreate.Run(shape);
            }
        }
        protected Vector2[] GetPoints()
        {
            Vector2[] arr = new Vector2[points.size()];
            for(int i=0;i<arr.length;i++)
                arr[i] = GBox2d.GameToPhysics(points.get(i));
            return arr;
        }
    }
    public static class IChain extends IPolygon{
        public IChain(){}
        public IChain(float size)
        {
            points.add(new Vector2(0,0));
            points.add(new Vector2(size,0));
        }

        @Override
        public void OnCreate(GDX.Runnable<Shape> onCreate) {
            ChainShape shape = new ChainShape();
            shape.createChain(GetPoints());
            onCreate.Run(shape);
        }
    }
}
