package com.game;


import GameGDX.*;
import GameGDX.AssetLoading.AssetNode;
import GameGDX.AssetLoading.GameData;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

public class MyGame extends GDXGame {
    @Override
    public void DoneLoading() {
        new Assets().SetData(GetGameData(true));
        Assets.LoadPackages(()->{
            Assets.GetTexture("back1").getTexture().dispose();
            IImage.NewImage(Assets.GetTexture("back1"),Scene.ui);
            //done loading
//            GFrame frame = new GFrame();
//            //frame.Set("water1_",1,10,0.1f);
//            frame.setSize(710,23);
//            frame.setPosition(200,200);
//            Scene.ui.addActor(frame);

        },"default","levels");//load first package
    }

    @Override
    public void render() {
        super.render();
        //System.out.println(GDX.GetFPS());
    }

    @Override
    protected GameData LoadPackages(String path) {
        GameData data = new GameData();
        data.LoadPackage("default","default/");
        data.LoadPackage("levels","levels/");

        GDX.WriteToFile(path, Json.ToJsonData(data));
        return data;
    }

    @Override
    protected Scene NewScene() {
        return new Scene(720,1280,new PolygonSpriteBatch());
    }
}
