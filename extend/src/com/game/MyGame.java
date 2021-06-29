package com.game;


import GameGDX.*;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

public class MyGame extends GDXGame {
    @Override
    public void DoneLoading() {
        new Assets(GetGameData(false));
        Assets.LoadPackages(()->{
            //done loading
            IImage.NewImage(Color.BROWN,Scene.width,Scene.height,Scene.ui);
        },"first");//load first package
    }

    @Override
    protected Scene NewScene() {
        return new Scene(720,1280,new PolygonSpriteBatch());
    }
}
