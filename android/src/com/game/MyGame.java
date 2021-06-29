package com.game;

import GameGDX.Assets;
import GameGDX.GDXGame;
import GameGDX.GUIData.IImage;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyGame extends GDXGame {
    @Override
    public void DoneLoading() {
        new Assets(GetGameData(false));
        Assets.LoadPackages(()->{
            //done loading
            Vector3 size = new Vector3(728,90,0);
            Vector3 gSize = Scene.GetCamera().unproject(size);
            IImage.NewImage(Color.BROWN,Scene.width,Scene.height,Scene.ui);
            IImage.NewImage(Color.BLUE,gSize.x,gSize.y,Scene.ui);
            System.out.println("xxx "+gSize);
        },"first");//load first package
    }

    @Override
    protected Scene NewScene() {
        return new Scene(720,1280,new PolygonSpriteBatch());
    }
}
