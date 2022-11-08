package Extend.GShape;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IGroup;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class IMaskGroup extends Component {
    private GDX.Func<ShapeRenderer> getRenderer;
    private GDX.Func<List<Actor>> before,after;

    @Override
    public void Refresh() {
        IGroup iGroup = GetIActor();
        if (!iGroup.Contain("mask")) return;
        int index = iGroup.GetChildren().indexOf(iGroup.GetIChild("mask"));
        List<Actor> l0 = new ArrayList<>();
        List<Actor> l1 = new ArrayList<>();
        iGroup.ForEach(i->{
            if (iGroup.GetChildren().indexOf(i)<index) l0.add(i.GetActor());
            else  l1.add(i.GetActor());
        });
        before = ()->l0;
        after = ()->l1;
    }
    private void DrawChild(Batch batch, float parentAlpha,List<Actor> list)
    {
        batch.begin();
        for (Actor a : list)
        {
            if (a.isVisible())
                a.draw(batch, parentAlpha);
        }
        batch.end();
    }

    @Override
    protected void Draw(Batch batch, float parentAlpha, Runnable onDraw) {
        if (getRenderer==null)
        {
            ShapeRenderer renderer = new ShapeRenderer();
            renderer.setAutoShapeType(true);
            getRenderer = ()->renderer;
        }
        IGroup iGroup = GetIActor();
        if (iGroup.Contain("mask"))
        {
            batch.end();
            DrawMasks(batch,parentAlpha);
            batch.begin();
        }
        else onDraw.run();
    }
    private void DrawMasks(Batch batch, float parentAlpha) {
        ShapeRenderer renderer = getRenderer.Run();
        renderer.begin();

        Actor actor = FindIChild("mask").GetActor();
        Shape shape = FindIChild("mask").GetComponent(IShape.class).shape;
        shape.getStagePos = actor::localToStageCoordinates;

        /* Clear our depth buffer info from previous frame. */
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        /* Set the depth function to LESS. */
        Gdx.gl.glDepthFunc(GL20.GL_LESS);

        /* Enable depth writing. */
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        /* Disable RGBA color writing. */
        Gdx.gl.glColorMask(false, false, false, false);
        /* Render mask elements. */
        renderer.setProjectionMatrix(GetActor().getStage().getCamera().combined);
        shape.type = ShapeRenderer.ShapeType.Filled;
        shape.Draw(renderer);
        renderer.flush();

        /* Enable RGBA color writing. */
        Gdx.gl.glColorMask(true, true, true, true);

        /* Set the depth function to LESS. */
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

        //drawMasked.run();
        DrawChild(batch,parentAlpha,before.Run());

        /* Disable depth writing. */
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        DrawChild(batch,parentAlpha,after.Run());

        if (actor.getDebug())
        {
            shape.type = ShapeRenderer.ShapeType.Line;
            shape.Draw(renderer);
        }

        renderer.end();
    }

}
