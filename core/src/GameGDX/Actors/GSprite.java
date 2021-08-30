package GameGDX.Actors;

import GameGDX.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GSprite extends Actor {
    //protected Sprite sprite = new Sprite();
    protected TextureRegion tRegion = new TextureRegion();

    public void SetTexture(String name)
    {
        SetTexture(Assets.GetTexture(name));
    }
    public void SetTexture(Texture tr) //not from atlas
    {
        tRegion.setRegion(tr);
        //sprite.setRegion(tr);
    }
    public void SetTexture(TextureRegion tr) //not from atlas
    {
        tRegion.setRegion(tr);
        //sprite.setRegion(tr);
    }
    protected float GetDrawWith()
    {
        return getWidth();
    }
    protected float GetDrawHeight()
    {
        return getHeight();
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        //if (sprite.getTexture()!=null) Draw(batch,parentAlpha);
        if (tRegion.getTexture()!=null) Draw(batch,parentAlpha);
    }
    protected void Draw(Batch batch, float parentAlpha)
    {
        Color color = new Color(getColor());
        color.a*=parentAlpha;
        batch.setColor(color);
        batch.draw(tRegion,getX(),getY(),getOriginY(),getOriginY(),GetDrawWith(),GetDrawHeight()
                ,getScaleX(),getScaleY(),getRotation());
//        sprite.setColor(getColor());
//        sprite.setOrigin(getOriginX(),getOriginY());
//        sprite.draw(batch,alpha);
    }
    //override
//    @Override
//    protected void positionChanged() {
//        sprite.setPosition(getX(),getY());
//    }
//
//    @Override
//    protected void sizeChanged() {
//        sprite.setSize(getWidth(),getHeight());
//    }
//
//    @Override
//    protected void rotationChanged() {
//        sprite.setRotation(getRotation());
//    }
//
//    @Override
//    protected void scaleChanged() {
//        sprite.setScale(getScaleX(),getScaleY());
//    }
}
