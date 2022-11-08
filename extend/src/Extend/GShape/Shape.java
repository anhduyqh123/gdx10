package Extend.GShape;

import GameGDX.GDX;
import GameGDX.Reflect;
import GameGDX.Util;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public abstract class Shape {
    public GDX.Func1<Vector2,Vector2> getStagePos;
    public ShapeRenderer.ShapeType type = ShapeRenderer.ShapeType.Line;
    public void Draw(ShapeRenderer renderer)
    {
        Draw(renderer,0,0);
    }
    protected Vector2 GetStagePos(Vector2 p)
    {
        return getStagePos.Run(p);
    }
    //check this function
    public abstract void Draw(ShapeRenderer renderer,float delX,float delY);
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }

    public static class Circle extends Shape
    {
        public Vector2 pos = new Vector2();
        public float radius = 100;
        public Circle(){};
        public Circle(float x,float y,float radius)
        {
            pos.set(x,y);
            this.radius = radius;
        }
        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            Vector2 p = GetStagePos(new Vector2(pos).add(delX,delY));
            renderer.circle(p.x,p.y,radius);
        }
    }
    public static class Polygon extends Shape
    {
        public List<Vector2> points = new ArrayList<>();

        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            Util.ForTriangles(points.toArray(new Vector2[0]), arr->{
                for (int i=0;i<arr.length;i++)
                    arr[i] = GetStagePos(new Vector2(arr[i]));
                Draw(renderer,arr,0,0);
            });
        }

        protected void Draw(ShapeRenderer renderer, Vector2[] tri,float delX,float delY)
        {
            renderer.triangle(tri[0].x+delX,tri[0].y+delY,
                    tri[1].x+delX,tri[1].y+delY,tri[2].x+delX,tri[2].y+delY);
        }
    }
    public static class Triangle extends Polygon
    {
        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            Vector2[] arr = points.toArray(new Vector2[0]);
            for (int i=0;i<arr.length;i++)
                arr[i] = GetStagePos(new Vector2(arr[i]));
            Draw(renderer,arr,0,0);
        }
    }
    public static class Line extends Shape
    {
        public Vector2 pos1 = new Vector2(),pos2 = new Vector2();
        public float width;

        public Line(){};
        public Line(Vector2 pos1,Vector2 pos2)
        {
            this.pos1 = pos1;
            this.pos2 = pos2;
        }
        public void SetPos(Vector2 p1,Vector2 p2)
        {
            pos1.set(p1);
            pos2.set(p2);
        }
        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            Vector2 p1 = GetStagePos(new Vector2(pos1).add(delX,delY));
            Vector2 p2 = GetStagePos(new Vector2(pos2).add(delX,delY));
            if (width<=0) renderer.line(p1,p2);
            else renderer.rectLine(p1,p2,width);
        }
        protected void DrawLine(ShapeRenderer renderer,Vector2 pos1,Vector2 pos2)
        {
            Vector2 p1 = GetStagePos(new Vector2(pos1));
            Vector2 p2 = GetStagePos(new Vector2(pos2));
            if (width<=0) renderer.line(p1,p2);
            else renderer.rectLine(p1,p2,width);
        }
    }
    public static class Rectangle extends Line
    {
        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            Vector2 d = Util.GetDirect(pos2,pos1);
            Vector2 p3 = new Vector2(pos1).add(0,Math.abs(d.y));
            Vector2 p4 = new Vector2(pos2).sub(0,Math.abs(d.y));
            DrawLine(renderer,pos1,p3);
            DrawLine(renderer,pos1,p4);
            DrawLine(renderer,pos2,p3);
            DrawLine(renderer,pos2,p4);
        }
    }
    public static class Grid extends Line
    {
        public int colum = 3, row = 3;

        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            float w = Math.abs(pos1.x-pos2.x);
            float h = Math.abs(pos1.y-pos2.y);
            float sizeW = w/colum;
            float sizeH = h/row;
            for (int i=0;i<=colum;i++)
            {
                Vector2 p1 = new Vector2(pos1).add(sizeW*i,0);
                Vector2 p2 = new Vector2(pos1).add(sizeW*i,h);
                DrawLine(renderer,p1,p2);
            }
            for (int i=0;i<=row;i++)
            {
                Vector2 p1 = new Vector2(pos1).add(0,sizeH*i);
                Vector2 p2 = new Vector2(pos1).add(w,sizeH*i);
                DrawLine(renderer,p1,p2);
            }
        }
    }
    public static class Path extends Shape
    {
        public List<Vector2> points = new ArrayList<>();

        public Path(){}
        public Path(Vector2 pos1,Vector2 pos2)
        {
            points.add(pos1);
            points.add(pos2);
        }
        @Override
        public void Draw(ShapeRenderer renderer) {
        }

        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {

        }
    }
}
