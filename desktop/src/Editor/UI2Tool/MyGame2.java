package Editor.UI2Tool;

import Editor.UITool.MyGame;
import Extend.Spine.Assets2;
import GameGDX.AssetLoading.GameData;
import GameGDX.Assets;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

public class MyGame2 extends MyGame {
    public MyGame2(int width, int height, Runnable done)
    {
        super(width, height, done);
    }
    @Override
    public void DoneLoading() {
        GameData data = GetGameData(true);
        new Assets2(data);
        Assets.LoadPackages(done,data.GetKeys().toArray(new String[0]));
        //UI.NewImage(Color.BLACK,0,0, Align.bottomLeft,Scene.width,Scene.height,Scene.background);
    }
    @Override
    protected Scene NewScene() {
        return new Scene(width,height,new PolygonSpriteBatch());
    }
}
