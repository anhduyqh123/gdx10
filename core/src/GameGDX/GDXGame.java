package GameGDX;

import GameGDX.AssetLoading.GameData;
import GameGDX.Screens.Screen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GDXGame extends ApplicationAdapter {
    @Override
    public void pause() {
        GMusic.OnPause();
    }

    @Override
    public void resume() {
        GMusic.OnResume();
    }
    @Override
    public void create() {
        new GDX();
        new GMusic();
        NewScene().AddBackHandle(Screen::BackButtonEvent);
        DoneLoading();
    }

    @Override
    public void resize(int width, int height) {
        Scene.Resize(width, height);
    }

    @Override
    public void render() {
        Scene.Act();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Scene.Render();
    }

    @Override
    public void dispose() {
        Scene.Dispose();
    }

    public void DoneLoading() //need to Override
    {
        new Assets(GetGameData(true));
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

    protected GameData GetGameData(boolean makeNew)
    {
        String path = "gameAssets.txt";
        try {
            GameData data = makeNew?LoadPackages(path): Json.FromJson(GameData.class,GDX.GetString(path));
            if (data!=null) return data;
        }catch (Exception e){e.printStackTrace(); }
        return new GameData();
    }
}
