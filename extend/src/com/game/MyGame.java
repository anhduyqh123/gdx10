package com.game;


import GameGDX.*;
import GameGDX.AssetLoading.GameData;
import GameGDX.Screens.Screen;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

public class MyGame extends GDXGame {
    @Override
    public void DoneLoading() {
        new Assets().SetData(GetGameData(true));
        Assets.LoadPackages(()->{
            new Screen("Game").Show();
        },"first");//load first package
    }

    @Override
    public void render() {
        super.render();
        //System.out.println(GDX.GetFPS());
    }

    @Override
    protected GameData LoadPackages(String path) {
        GameData data = new GameData();
        data.LoadPackage("first","first/");

        GDX.WriteToFile(path, Json.ToJsonData(data));
        return data;
    }

    @Override
    protected Scene NewScene() {
        return new Scene(720,1280,new PolygonSpriteBatch());
    }
}
