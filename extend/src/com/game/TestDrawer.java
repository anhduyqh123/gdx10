package com.game;

import GameGDX.Scene;
import com.badlogic.gdx.scenes.scene2d.Group;

public class TestDrawer extends Group {
    public TestDrawer()
    {
        setSize(Scene.width,Scene.height);

//        TrailRenderer trail = new TrailRenderer(100,new Texture("gradient.png"));
//        trail.setDebug(true);
//        this.addActor(trail);
//
//        addListener(new ClickListener(){
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                trail.setPosition(x,y);
//                trail.Start();
//                return true;
//            }
//
//            @Override
//            public void touchDragged(InputEvent event, float x, float y, int pointer) {
//                trail.setPosition(x,y);
//            }
//        });
    }
}
