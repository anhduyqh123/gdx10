package com.game;

import GameGDX.Assets;
import GameGDX.GDXGame;
import GameGDX.GUIData.IImage;
import GameGDX.Scene;
import ISdk.AdMob;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class TestGame extends GDXGame {
    public AdMob adMob;
    public void create() {
        Init();
        UnZip(this::DoneLoading);
    }

    private void UnZip(Runnable done)
    {
        //GDX.Log(zip.length());
        //FileZip.Unzip(zip.file(),direction.file());
        //done.run();
    }
    @Override
    public void DoneLoading() {
        assets.SetData(GetGameData(false));
        Assets.LoadPackages(()->{
            //done loading
            IImage.NewImage(Color.BROWN,100,500, Align.bottomLeft,200,200,Scene.ui).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("xxx-fullscreen");
                    adMob.ShowFullscreen();
                }
            });
            IImage.NewImage(Color.BLUE,700,500, Align.bottomLeft,200,200,Scene.ui).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("xxx-videoreward");
                    adMob.ShowVideoReward(result->{

                    });
                }
            });
        },"first");//load first package
    }

    @Override
    protected Scene NewScene() {
        return new Scene(720,1280,new PolygonSpriteBatch());
    }
}
