package GameGDX;

import GameGDX.AssetLoading.GameData;
import GameGDX.GUIData.ILabel;
import GameGDX.Screens.Screen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GDXGame extends ApplicationAdapter {
    protected Scene scene;
    protected Assets assets;
    protected GAudio audio;

    @Override
    public void pause() {
        if (audio!=null)
            audio.PauseMusic();
    }

    @Override
    public void resume() {
        if (audio!=null)
            audio.ResumeMusic();
    }
    @Override
    public void create() {
        Init();
        DoneLoading();
    }
    protected void Init()
    {
        new GDX();
        audio = new GAudio();
        scene = NewScene();
        scene.AddBackHandle(Screen::BackButtonEvent);
        assets = NewAssets();
        Scene.stage.addActor(assets);
    }

    @Override
    public void resize(int width, int height) {
        scene.Resize(width, height);
    }

    @Override
    public void render() {
        scene.Act(GDX.DeltaTime());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        scene.Render();
    }

    @Override
    public void dispose() {
        scene.Dispose();
    }

    public void DoneLoading() //need to Override
    {
        assets.SetData(GetGameData(true));
        Assets.LoadPackages(()->{
            //done loading
        },"first");//load first package
    }
    protected GameData LoadPackages(String path)
    {
        GameData data = new GameData();
        data.LoadPackage("first","first/");

        GDX.WriteToFile(path, Json.ToJsonData(data));
        return data;
    }
    protected Scene NewScene()
    {
        return new Scene(720,1280);
    }
    protected Assets NewAssets()
    {
        return new Assets();
    }

    protected GameData GetGameData(boolean makeNew)
    {
        String path = "gameAssets.txt";
        try {
            GameData data = makeNew?LoadPackages(path): Json.FromJson(GameData.class,GDX.GetString(path));
            if (data!=null) return data;
        }catch (Exception e){e.printStackTrace(); }
        return new GameData();
    }
    protected void ShowFPS()
    {
        Label lb = ILabel.New("");
        lb.setPosition(10,20);
        Scene.ui2.addActor(lb);
        lb.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                lb.setText(GDX.GetFPS()+"");
                return false;
            }
        });
    }
}
