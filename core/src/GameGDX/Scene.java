package GameGDX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    public static float mWidth, mHeight,scaleX,scaleY,scale;
    public static int width,height;
    public static Group bg,game,ui,ui2;
    public static Stage stage,gStage;

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
    protected void Init(Batch batch)
    {
        gStage = new Stage(new ExtendViewport(mWidth, mHeight),batch);
        stage = new Stage(new ExtendViewport(mWidth, mHeight),batch);
        bg = NewGroup(gStage);
        game = NewGroup(gStage);
        ui = NewGroup(stage);
        ui2 = NewGroup(stage);

        Gdx.input.setInputProcessor(stage);
    }
    private Group NewGroup(Stage stage)
    {
        Group group = new Group();
        stage.addActor(group);
        return group;
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
    public void Act(float delta)
    {
        gStage.act(delta);
        stage.act(delta);
    }
    public void Dispose()
    {
        gStage.dispose();
        stage.dispose();
    }
    public void Render()
    {
        gStage.draw();
        stage.draw();
    }
    public void Resize(int width,int height)
    {
        gStage.getViewport().update(width, height);
        stage.getViewport().update(width,height);
    }
    //static
    public static OrthographicCamera GetUICamera()
    {
        return (OrthographicCamera)stage.getCamera();
    }
    public static OrthographicCamera GetGCamera()
    {
        return (OrthographicCamera)gStage.getCamera();
    }
    //extend
    public static Vector2 GetMousePos()
    {
        Vector2 pos = new Vector2(Gdx.input.getX(),Gdx.input.getY());
        return stage.screenToStageCoordinates(pos);
    }
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
    public static Vector2 GetLocalPosition(Actor actor,Vector2 pos)//pos = stage position
    {
        return actor.stageToLocalCoordinates(pos);
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
    public static void SetStagePosition(Actor actor,Vector2 pos)
    {
        SetStagePosition(actor,pos,Align.bottomLeft);
    }
    public static void SetStagePosition(Actor actor,Vector2 pos,int align)
    {
        actor.stageToLocalCoordinates(pos);
        actor.setPosition(pos.x,pos.y,align);
    }
    public static Rectangle GetRect(Actor actor)
    {
        return new Rectangle(actor.getX(),actor.getY(),actor.getWidth(),actor.getHeight());
    }
    //Bounds
    public static void SetBounds(Actor actor,float x,float y,int align,float width,float height,Group parent)
    {
        actor.setSize(width, height);
        actor.setPosition(x,y,align);
        parent.addActor(actor);
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
