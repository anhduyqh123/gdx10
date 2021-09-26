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
    protected TextureRegion tRegion = new TextureRegion();

    public void SetTexture(String name)
    {
        SetTexture(Assets.GetTexture(name));
    }
    public void SetTexture(Texture tr) //not from atlas
    {
        tRegion.setRegion(tr);
    }
    public void SetTexture(TextureRegion tr) //not from atlas
    {
        tRegion.setRegion(tr);
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
        if (tRegion.getTexture()!=null) Draw(batch,parentAlpha);
    }
    protected void Draw(Batch batch, float parentAlpha)
    {
        Color color = new Color(getColor());
        color.a*=parentAlpha;
        batch.setColor(color);
        batch.draw(tRegion,getX(),getY(),getOriginX(),getOriginY(),GetDrawWith(),GetDrawHeight()
                ,getScaleX(),getScaleY(),getRotation());
    }
}
