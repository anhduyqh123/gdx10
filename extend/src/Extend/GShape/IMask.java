package Extend.GShape;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IMask extends Component { //support IActor,IImage,IGroup
    public Shape shape = new Shape.Circle();
    private GDX.Func<ShapeRenderer> getRenderer;

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
        DrawMasks(()->{
            batch.begin();
            onDraw.run();
            batch.end();
        });
        batch.begin();
    }
    private void DrawMasks(Runnable drawMasked) {
        ShapeRenderer renderer = getRenderer.Run();
        renderer.begin();

        Actor actor = GetActor();
        Vector2 pos = GetIActor().GetStagePos();

        /* Clear our depth buffer info from previous frame. */
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        /* Set the depth function to LESS. */
        Gdx.gl.glDepthFunc(GL20.GL_LESS);

        /* Enable depth writing. */
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        /* Disable RGBA color writing. */
        Gdx.gl.glColorMask(false, false, false, false);
        /* Render mask elements. */
        renderer.setProjectionMatrix(actor.getStage().getCamera().combined);
        shape.type = ShapeRenderer.ShapeType.Filled;
        shape.Draw(renderer,pos.x,pos.y);
        renderer.flush();

        /* Enable RGBA color writing. */
        Gdx.gl.glColorMask(true, true, true, true);

        /* Set the depth function to LESS. */
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

        drawMasked.run();
        /* Disable depth writing. */
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        if (actor.getDebug())
        {
            shape.type = ShapeRenderer.ShapeType.Line;
            shape.Draw(renderer,pos.x,pos.y);
        }

        renderer.end();
    }
}
