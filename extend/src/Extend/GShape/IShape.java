package Extend.GShape;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class IShape extends Component {
    public Shape shape = new Shape.Circle();
    private GDX.Func<ShapeRenderer> getRenderer;
    public IShape(){}
    public <T extends Shape> T GetShape()
    {
        return (T)shape;
    }
    public <T extends Shape> T GetShape(Class<T> type)
    {
        return (T)shape;
    }
    @Override
    public void New() {
        float w = GetActor().getWidth(),h = GetActor().getHeight();
        Shape.Polygon shape = new Shape.Polygon();
        shape.points.add(new Vector2());
        shape.points.add(new Vector2(w,0));
        shape.points.add(new Vector2(w,h));
        shape.points.add(new Vector2(0,h));
        shape.points.add(new Vector2());
        this.shape = shape;
    }

    @Override
    protected void Draw(Batch batch, float parentAlpha, Runnable onDraw) {
        if (getRenderer==null)
        {
            ShapeRenderer renderer = new ShapeRenderer();
            renderer.setAutoShapeType(true);
            getRenderer = ()->renderer;
        }
        batch.end();
        DrawShape(batch,onDraw);
        batch.begin();
    }
    private void DrawShape(Batch batch,Runnable onDraw)
    {
        ShapeRenderer renderer = getRenderer.Run();
        renderer.setColor(GetActor().getColor());
        renderer.setProjectionMatrix(GetActor().getStage().getCamera().combined);
        renderer.begin();

        batch.begin();
        onDraw.run();
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shape.getStagePos = GetActor()::localToStageCoordinates;
        shape.Draw(renderer);
        renderer.flush();
        renderer.end();
    }
}
