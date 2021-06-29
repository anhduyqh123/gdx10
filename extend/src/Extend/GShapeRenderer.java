package Extend;

import GameGDX.Scene;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

public class GShapeRenderer extends Actor {
    protected ShapeRenderer renderer = new ShapeRenderer();
    private List<Shape> shapes = new ArrayList<>();

    public GShapeRenderer()
    {
        renderer.setProjectionMatrix(Scene.GetCamera().combined);
    }
    public GShapeRenderer(Group parent)
    {
        this();
        parent.addActor(this);
    }
    public void Clear()
    {
        shapes.clear();
    }

    @Override
    public void setColor(Color color) {
        renderer.setColor(color);
        super.setColor(color);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        renderer.getColor().a = parentAlpha;
        batch.end();
        for (Shape s : shapes) s.Draw();
        batch.begin();
    }
    public void NewPoint(Vector2 pos)
    {
        Circle circle = new Circle(pos.x,pos.y,5);
        circle.renderer = renderer;
        circle.type = ShapeRenderer.ShapeType.Filled;
        shapes.add(circle);
    }


    public static abstract class Shape
    {
        public ShapeRenderer.ShapeType type;
        public ShapeRenderer renderer;
        public abstract void Draw();
    }
    public static class Circle extends Shape
    {
        private float x,y,radius;
        public Circle(float x,float y,float radius)
        {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        @Override
        public void Draw() {
            renderer.begin(type);
            renderer.circle(x,y,radius);
            renderer.end();
        }
    }
}
