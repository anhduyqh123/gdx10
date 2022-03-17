package GameGDX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.*;

public class Scene {

    public static Scene i;
    public static float scaleX,scaleY,scale;
    public static int width,height,mWidth,mHeight;
    public static Group ui,ui2;
    public static Stage stage;

    private final Map<String,Stage> mapStage = new HashMap<>();
    private final List<String> stageNames = new ArrayList<>();
    private final Batch batch;

    public Scene(int gWidth,int gHeight)
    {
        this(gWidth,gHeight,new SpriteBatch());
    }
    public Scene(int gWidth,int gHeight,Batch batch)
    {
        i = this;
        this.batch = batch;

        mWidth = gWidth;
        mHeight = gHeight;
        Init();
        InitSize();
    }

    private void InitSize()
    {
        width = (int)stage.getViewport().getWorldWidth();
        height = (int)stage.getViewport().getWorldHeight();
        scaleX = width*1f/ mWidth;
        scaleY = height*1f/ mHeight;
        scale = Math.max(scaleX,scaleY);
    }
    protected void Init()
    {
        stage = NewStage();
        AddStage("stage",stage);
        ui = NewGroup(stage);
        ui2 = NewGroup(stage);

        Gdx.input.setInputProcessor(stage);
    }
    public Stage NewStage()
    {
        return NewStage(mWidth,mHeight);
    }
    public Stage NewStage(int width,int height)
    {
        return new Stage(new ExtendViewport(width, height),batch);
    }
    public void AddStageAtFirst(String name,Stage newStage)
    {
        stageNames.add(0,name);
        mapStage.put(name,newStage);
    }
    public void AddStage(String name,Stage newStage)
    {
        stageNames.add(name);
        mapStage.put(name,newStage);
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
        for (String n : stageNames)
            mapStage.get(n).act(delta);
    }
    public void Dispose()
    {
        for (String n : stageNames)
            mapStage.get(n).dispose();
    }
    public void Render()
    {
        for (String n : stageNames)
            mapStage.get(n).draw();
    }
    public void Resize(int width,int height)
    {
        for (String n : stageNames)
            mapStage.get(n).
                    getViewport().update(width,height);
    }
    //static
    public static OrthographicCamera GetUICamera()
    {
        return (OrthographicCamera)stage.getCamera();
    }
    //extend
    public static Vector2 GetMousePos()
    {
        Vector2 pos = new Vector2(Gdx.input.getX(),Gdx.input.getY());
        return stage.screenToStageCoordinates(pos);
    }
    public static void AddActorKeepPosition(Actor actor, Group group)
    {
        Vector2 pos1 = GetStagePosition(actor, Align.center);
        Vector2 pos2 = group.stageToLocalCoordinates(pos1);
        actor.setPosition(pos2.x,pos2.y,Align.center);
        group.addActor(actor);
    }
    //Rotate
    public static float GetStageRotation(Actor actor)
    {
        float rotation = 0.00f;
        while (actor!=null) {
            rotation += actor.getRotation();
            actor = actor.getParent();
        }
        return rotation;
    }
    public static float SetStageRotation(Actor actor,float rotation)
    {
        Actor parent = actor.getParent();
        while (parent!=null) {
            rotation -= parent.getRotation();
            parent = parent.getParent();
        }
        actor.setRotation(rotation);
        return rotation;
    }
    //Position
    public static Vector2 GetLocalOrigin(Actor actor)
    {
        return new Vector2(actor.getOriginX(),actor.getOriginY());
    }
    public static Vector2 GetOrigin(Actor actor)
    {
        return GetPosition(actor).add(GetLocalOrigin(actor));
    }
    public static Vector2 GetStageOrigin(Actor actor)
    {
        return GetStagePosition(actor).add(GetLocalOrigin(actor));
    }

    public static Vector2 GetStagePosition(Actor actor)
    {
        return GetStagePosition(actor,Align.bottomLeft);
    }
    public static Vector2 GetStagePosition(Actor actor, int align)
    {
        //Vector2 pos = GetPosition(actor,align).sub(GetPosition(actor));
        Vector2 local = GetLocal(actor,align);
        return GetStagePosition(actor,local);
    }
    public static Vector2 GetStagePosition(Actor actor, Vector2 local)
    {
        //Vector2 pos = new Vector2(local).sub(GetPosition(actor));
        return actor.localToStageCoordinates(local);
    }

    public static Vector2 GetLocal(Actor actor, int align)
    {
        return GetPosition(actor,align).sub(GetPosition(actor));
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
        Vector2 p = new Vector2(pos);
        actor.getParent().stageToLocalCoordinates(p);
        actor.setPosition(p.x,p.y,align);
    }
    //convert
    public static Vector2 StageToParent(Actor actor, Vector2 stage)
    {
        return actor.getParent().stageToLocalCoordinates(new Vector2(stage));
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
}
