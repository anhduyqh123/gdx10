package com.game;


import GameGDX.AssetLoading.GameData;
import GameGDX.*;
import GameGDX.GUIData.ILabel;
import GameGDX.Screens.Screen;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyGame extends GDXGame {
    private ILabel lb;
    @Override
    public void DoneLoading() {
        new Assets().SetData(GetGameData(true));
        Assets.LoadPackages(()->{
            Screen screen = new Screen("Game1");
            Actor actor = screen.FindActor("img");
            Scene.AddActorKeepPosition(actor,screen.FindActor("box1"));
            screen.FindActor("box1").setDebug(true);
            screen.FindActor("box").setDebug(true);

            screen.Show();
        },"first");//load first package
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
