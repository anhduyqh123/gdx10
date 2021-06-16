package GameGDX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    public static float mWidth, mHeight,scaleX,scaleY,scale;
    public static int width,height;
    public static Group background,game,ui,ui2;
    public static Stage stage;

    public Scene(float gWidth,float gHeight)
    {
        this(gWidth,gHeight,new SpriteBatch());
    }
    public Scene(float gWidth,float gHeight,Batch batch)
    {
        mWidth = gWidth;
        mHeight = gHeight;
        Init(batch);
        InitSize();
    }

    private void InitSize()
    {
        width = (int)stage.getViewport().getWorldWidth();
        height = (int)stage.getViewport().getWorldHeight();
        scaleX = width/ mWidth;
        scaleY = height/ mHeight;
        scale = Math.max(scaleX,scaleY);
    }
    private void Init(Batch batch)
    {
        background = new Group();
        game = new Group();
        ui = new Group();
        ui2 = new Group();

        stage = new Stage(new ExtendViewport(mWidth, mHeight),batch);

        stage.addActor(background);
        stage.addActor(game);
        stage.addActor(ui);
        stage.addActor(ui2);

        Gdx.input.setInputProcessor(stage);
    }
    public void AddBackHandle(Runnable callback)
    {
        Gdx.input.setCatchBackKey(true);
        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) callback.run();
                return super.keyDown(event, keycode);
            }
        });
    }
    //static
    public static void AddActor(Actor actor)
    {
        stage.addActor(actor);
    }
    public static void Act()
    {
        stage.act(GDX.DeltaTime());
    }
    public static void Dispose()
    {
        stage.dispose();
    }
    public static void Render()
    {
        stage.draw();
    }
    public static void Resize(int width,int height)
    {
        stage.getViewport().update(width,height);
    }
    public static Camera GetCamera()
    {
        return stage.getCamera();
    }

    //extend
    public static void AddActorKeepPosition(Actor actor, Group group)
    {
        Vector2 pos1 = GetStagePosition(actor, Align.bottomLeft);
        Vector2 pos2 = group.stageToLocalCoordinates(pos1);
        actor.setPosition(pos2.x,pos2.y);
        group.addActor(actor);
    }
    public static Vector2 GetActorPosition(Actor actor,Actor target,int align)
    {
        Vector2 pos = GetPosition(target,align).sub(GetPosition(target,Align.bottomLeft));
        return target.localToActorCoordinates(actor,pos);
    }
    public static Vector2 GetStagePosition(Actor actor)
    {
        return GetStagePosition(actor,Align.bottomLeft);
    }
    public static Vector2 GetStagePosition(Actor actor, int align)
    {
        Vector2 pos = GetPosition(actor,align).sub(GetPosition(actor));
        return actor.localToStageCoordinates(pos);
    }
    public static Vector2 GetPosition(Actor actor)
    {
        return GetPosition(actor,Align.bottomLeft);
    }
    public static Vector2 GetPosition(Actor actor, int align)
    {
        return new Vector2(actor.getX(align),actor.getY(align));
    }
    public static void SetPosition(Actor actor,Vector2 pos,int align)
    {
        actor.setPosition(pos.x,pos.y,align);
    }
    public static void SetPosition(Actor actor,Vector2 pos)
    {
        SetPosition(actor,pos,Align.bottomLeft);
    }
    public static Rectangle GetRect(Actor actor)
    {
        return new Rectangle(actor.getX(),actor.getY(),actor.getWidth(),actor.getHeight());
    }
    //convert
    public static Group TableToGroup(Table table)
    {
        Group parent = table.getParent();
        int index = table.getZIndex();
        Group group = new Group();
        group.setSize(table.getWidth(),table.getHeight());
        group.setPosition(table.getX(),table.getY());
        parent.addActorAt(index,group);
        List<Actor> list = new ArrayList<>();
        for(Actor child : table.getChildren()) list.add(child);
        table.remove();
        for(Actor child : list)
            group.addActor(child);

        return group;
    }
}
