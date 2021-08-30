package com.game;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Test extends Image {

    public Test()
    {
        TextureRegion tr = Assets.GetTexture("badlogic");
        setDrawable(IImage.NewDrawable(tr));
        setSize(tr.getRegionWidth(),tr.getRegionHeight());
    }
}
