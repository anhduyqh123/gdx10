package Extend;

import com.badlogic.gdx.graphics.Camera;
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
    private Camera camera;

    public GShapeRenderer(Camera camera,Group parent)
    {
        parent.addActor(this);
        this.camera = camera;
    }
    public void Clear()
    {
        shapes.clear();
    }
    public int Size()
    {
        return shapes.size();
    }

    @Override
    public void setColor(Color color) {
        renderer.setColor(color);
        super.setColor(color);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        renderer.setProjectionMatrix(camera.combined);
        renderer.getColor().a = parentAlpha;
        batch.end();
        for (Shape s : shapes) s.Draw();
        batch.begin();
    }
    public void NewCircle(Vector2 pos,float r)
    {
        Circle circle = new Circle(pos.x,pos.y,r);
        circle.renderer = renderer;
        circle.type = ShapeRenderer.ShapeType.Line;
        shapes.add(circle);
    }
    public void NewPoint(Vector2 pos)
    {
        Circle circle = new Circle(pos.x,pos.y,5);
        circle.renderer = renderer;
        circle.type = ShapeRenderer.ShapeType.Filled;
        shapes.add(circle);
    }
    public void NewLine(Vector2 pos1,Vector2 pos2)
    {
        Line line = new Line(pos1,pos2);
        line.renderer = renderer;
        shapes.add(line);
    }


    public static abstract class Shape
    {
        public ShapeRenderer.ShapeType type = ShapeRenderer.ShapeType.Line;
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
    public static class Line extends Shape
    {
        private Vector2 pos1,pos2;
        public Line(Vector2 pos1,Vector2 pos2)
        {
            this.pos1 = pos1;
            this.pos2 = pos2;
        }

        @Override
        public void Draw() {
            renderer.begin(type);
            renderer.line(pos1,pos2);
            renderer.end();
        }
    }
}
