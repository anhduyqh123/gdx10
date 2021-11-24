package Extend.GShape;

import GameGDX.Reflect;
import GameGDX.Util;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public abstract class Shape {
    public ShapeRenderer.ShapeType type = ShapeRenderer.ShapeType.Line;
    public void Draw(ShapeRenderer renderer)
    {
        Draw(renderer,0,0);
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
            renderer.circle(pos.x+delX,pos.y+delY,radius);
        }
    }
    public static class Polygon extends Shape
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
    public static class Triangle extends Polygon
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
        public Line(){};
        public Line(Vector2 pos1,Vector2 pos2)
        {
            this.pos1 = pos1;
            this.pos2 = pos2;
        }

        @Override
        public void Draw(ShapeRenderer renderer, float delX, float delY) {
            renderer.set(type);
            renderer.line(pos1.x+delX,pos1.y+delY,pos2.x+delX,pos2.y+delY);
        }
    }
}
