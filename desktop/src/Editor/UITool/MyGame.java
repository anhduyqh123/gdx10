package Editor.UITool;

import GameGDX.*;
import GameGDX.AssetLoading.GameData;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

public class MyGame extends GDXGame {
    protected Runnable done;
    protected int width;
    protected int height;

    public MyGame(int width, int height, Runnable done)
    {
        this.width = width;
        this.height = height;
        this.done = done;
    }
    @Override
    public void DoneLoading() {
        GameData data = GetGameData(true);
        new Assets(data);
        Assets.LoadPackages(done,data.GetKeys().toArray(new String[0]));
        IImage.NewImage(Color.BLACK,0,0, Align.bottomLeft,Scene.width,Scene.height,Scene.background);
    }

    @Override
    protected GameData LoadPackages(String path) {
        GameData data = new GameData();
        data.LoadPackages();
        return data;
    }
    @Override
    protected Scene NewScene() {
        return new Scene(width,height);
    }
}
