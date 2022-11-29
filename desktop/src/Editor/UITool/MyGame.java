package Editor.UITool;

import Extend.Box2d.GBox2d;
import Extend.GShape.GShapeRenderer;
import Extend.Spine.Assets2;
import GameGDX.*;
import GameGDX.AssetLoading.GameData;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MyGame extends GDXGame {
    public static MyGame i;

    protected Runnable done;
    protected int width;
    protected int height;
    public Color bg = Color.BLACK;

    private GBox2d gBox2d;
    public GShapeRenderer renderer;

    public MyGame(int width, int height, Runnable done)
    {
        i = this;
        this.width = width;
        this.height = height;
        this.done = done;
    }

    @Override
    protected void Init() {
        super.Init();
        gBox2d = new GBox2d();
        Scene.stage.addActor(gBox2d);
        gBox2d.setDebug(true);
        GBox2d.SetActive(false);

        renderer = new GShapeRenderer(Scene.ui2);

        DebugBorder();
        //Scene.stage.addActor(gBox2d);

        //screen
//        gBox2d.setSize(Scene.width,Scene.height);
//        gBox2d.setTouchable(Touchable.disabled);
//        gBox2d.setDebug(true);
    }
    private void DebugBorder()
    {
        Actor actor = new Actor();
        actor.setSize(Scene.width,Scene.height);
        actor.setTouchable(Touchable.disabled);
        actor.setDebug(true);
        Scene.ui2.addActor(actor);
    }

    @Override
    protected Assets NewAssets() {
        return new Assets2();
    }

    @Override
    public void DoneLoading() {
        GameData data = GetGameData(true);
        assets.SetData(data);

        IImage.NewImage(Color.GRAY);
        //IImage.NewImage(Color.CYAN,0,0, Align.bottomLeft,Scene.width,Scene.height,Scene.ui);
        Assets.LoadPackages(done,data.GetKeys().toArray(new String[0]));

        InitControlCamera();
    }
    private void InitControlCamera()
    {
        OrthographicCamera camera  = Scene.GetUICamera();
        Vector2 pos0 = new Vector2();
        Vector2 camPos = new Vector2();
        Scene.stage.addListener(new ClickListener(){
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                float delta = amountY*GDX.DeltaTime();
                camera.zoom+=delta;
                return super.scrolled(event, x, y, amountX, amountY);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button!=1) return false;
                pos0.set(Gdx.input.getX(),Gdx.input.getY());
                camPos.set(camera.position.x,camera.position.y);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Vector2 p = new Vector2(Gdx.input.getX(),Gdx.input.getY());
                Vector2 dir = Util.GetDirect(p,pos0);
                float len = dir.len()*camera.zoom*1.5f;
                dir.setLength(len);
                Vector2 cPos = new Vector2(camPos).add(dir.x,-dir.y);
                camera.position.set(cPos,0);
            }
        });
    }

    @Override
    protected GameData LoadPackages(String path) {
        GameData data = new GameData();
        data.LoadPackages();
        return data;
    }
    @Override
    protected Scene NewScene() {
        return new Scene(width,height,new PolygonSpriteBatch());
    }

    @Override
    public void render() {
        scene.Act(GDX.DeltaTime());
        Gdx.gl.glClearColor(bg.r,bg.g,bg.b,bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Util.Try(scene::Render);
    }
}
