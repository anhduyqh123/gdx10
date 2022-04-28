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
    public static class Polygon extends Shape //no local pos
    {
        public List<Vector2> points = new ArrayList<>();

        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            Util.ForTriangles(points.toArray(new Vector2[0]), arr->Draw(renderer,arr,delX,delY));
        }

        protected void Draw(ShapeRenderer renderer, Vector2[] tri,float delX,float delY)
        {
            renderer.triangle(tri[0].x+delX,tri[0].y+delY,
                    tri[1].x+delX,tri[1].y+delY,tri[2].x+delX,tri[2].y+delY);
        }
    }
    public static class Triangle extends Polygon //no local pos
    {
        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            Draw(renderer,points.toArray(new Vector2[0]), delX,delY);
        }
    }
    public static class Line extends Shape
    {
        public Vector2 pos1,pos2;
        public float width;

        public Line(){};
        public Line(Vector2 pos1,Vector2 pos2)
        {
            this.pos1 = pos1;
            this.pos2 = pos2;
        }

        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            Vector2 p1 = GetStagePos(new Vector2(pos1).add(delX,delY));
            Vector2 p2 = GetStagePos(new Vector2(pos2).add(delX,delY));
            if (width<=0) renderer.line(p1,p2);
            else renderer.rectLine(p1,p2,width);
        }
    }
}
