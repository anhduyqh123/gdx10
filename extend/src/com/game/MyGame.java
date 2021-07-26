package com.game;


import GameGDX.*;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class MyGame extends GDXGame {
    @Override
    public void DoneLoading() {
        new Assets().SetData(GetGameData(false));
        Assets.LoadPackages(()->{
            //done loading
            IImage.NewImage(Color.BROWN,100,100, Align.bottomLeft,200,200,Scene.ui);
            Image img = IImage.NewImage(Color.BLUE,0,0, Align.bottomLeft,Scene.width,Scene.height,Scene.game);
            GDX.Delay(()->{
                Scene.GetGCamera().zoom = 2;
            },2);


        },"first");//load first package
    }

    @Override
    protected Scene NewScene() {
        return new Scene(720,1280,new PolygonSpriteBatch());
    }
}
