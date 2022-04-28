package Extend.GShape;

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

    public GShapeRenderer(){
        renderer.setAutoShapeType(true);
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
        Camera camera = getStage().getCamera();
        renderer.setProjectionMatrix(camera.combined);
        renderer.getColor().a = parentAlpha;
        batch.end();
        renderer.begin();
        for (Shape s : shapes) s.Draw(renderer);
        renderer.end();
        batch.begin();
    }
    public void AddShape(Shape shape)
    {
        shape.getStagePos = this::localToStageCoordinates;
        shapes.add(shape);
    }
    public void RemoveShape(Shape shape)
    {
        shapes.remove(shape);
    }

    public void NewCircle(Vector2 pos,float r)
    {
        Shape.Circle circle = new Shape.Circle(pos.x,pos.y,r);
        circle.type = ShapeRenderer.ShapeType.Line;
        AddShape(circle);
    }
    public void NewPoint(Vector2 pos)
    {
        Shape.Circle circle = new Shape.Circle(pos.x,pos.y,5);
        circle.type = ShapeRenderer.ShapeType.Filled;
        AddShape(circle);
    }
    public void NewLine(Vector2 pos1,Vector2 pos2)
    {
        Shape.Line line = new Shape.Line(pos1,pos2);
        AddShape(line);
    }
    public void NewLine(Vector2 pos1,Vector2 pos2,float width)
    {
        Shape.Line line = new Shape.Line(pos1,pos2);
        line.type = ShapeRenderer.ShapeType.Filled;
        line.width = width;
        AddShape(line);
    }
}
