package Extend.Box2d;

import GameGDX.GDX;
import GameGDX.Reflect;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public abstract class IShape {

    public abstract void OnCreate(Vector2 origin,GDX.Runnable<Shape> onCreate);

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
        public void OnCreate(Vector2 origin,GDX.Runnable<Shape> onCreate) {
            CircleShape shape = new CircleShape();
            shape.setPosition(GBox2d.GameToPhysics(new Vector2(pos).sub(origin)));
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
        public void OnCreate(Vector2 origin,GDX.Runnable<Shape> onCreate) {
            PolygonShape shape = new PolygonShape();
            Vector2[] points = GetPoints(origin);
            if (IsConvexPolygon(points))
            {
                shape.set(points);
                onCreate.Run(shape);
            }
            else {
                Util.ForTriangles(points,arr->{
                    shape.set(arr);
                    onCreate.Run(shape);
                });
            }
        }
        protected Vector2[] GetPoints(Vector2 origin)
        {
            Vector2[] arr = new Vector2[points.size()];
            for(int i=0;i<arr.length;i++)
                arr[i] = GBox2d.GameToPhysics(new Vector2(points.get(i)).sub(origin));
            return arr;
        }

        //check ConvexPolygon
        private boolean IsConvexPolygon(Vector2[] points)
        {
            if (points.length<3||points.length>8) return false;
            for(int i=0;i< points.length;i++)
                if (GetAngle(points,i)>=180) return false;
            return true;
        }
        private float GetAngle(Vector2[] points, int index)
        {
            Vector2 p1 = GetValue(points, index-1);
            Vector2 p2 = GetValue(points, index);
            Vector2 p3 = GetValue(points, index+1);
            return Util.GetAngle(p1,p2,p3);
        }
        private Vector2 GetValue(Vector2[] points,int index)
        {
            if (index<0) index = points.length-1;
            if (index>= points.length) index = 0;
            return points[index];
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
        public void OnCreate(Vector2 origin,GDX.Runnable<Shape> onCreate) {
            ChainShape shape = new ChainShape();
            shape.createChain(GetPoints(origin));
            onCreate.Run(shape);
        }
    }
    public static class IEdge extends IPolygon{
        public IEdge(){}
        public IEdge(float size)
        {
            points.add(new Vector2(0,0));
            points.add(new Vector2(size,0));
        }

        @Override
        public void OnCreate(Vector2 origin,GDX.Runnable<Shape> onCreate) {
            Vector2[] arr = GetPoints(origin);
            EdgeShape shape = new EdgeShape();
            for (int i=0;i< arr.length-1;i++)
            {
                shape.set(arr[i],arr[i+1]);
                onCreate.Run(shape);
            }
        }
    }
}
