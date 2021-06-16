package com.game;


import GameGDX.*;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

public class MyGame extends GDXGame {
    @Override
    protected Scene NewScene() {
        return new Scene(720,1280,new PolygonSpriteBatch());
    }
}
