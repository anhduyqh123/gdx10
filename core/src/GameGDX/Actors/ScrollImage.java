package GameGDX.Actors;

import GameGDX.GDX;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScrollImage extends Actor {
    protected Sprite sprite = new Sprite();
    protected float scroll;
    public float speed = 1f;
    public boolean isScrollX,isScrollY;

    public ScrollImage(){}
    public void SetTexture(TextureRegion tr) //not from atlas
    {
        sprite.setRegion(tr);
        GDX.PostRunnable(()->tr.getTexture().
                setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        scroll += delta*speed;
        if (scroll > 1.0f)  scroll = 0.0f;
        Scroll(1,scroll);
    }
    protected void Scroll(float percent,float scroll)
    {
        if (isScrollX)
        {
            sprite.setU(-scroll);
            sprite.setU2(percent-scroll);
        }
        if (isScrollY)
        {
            sprite.setV(-scroll);
            sprite.setV2(percent-scroll);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite!=null) Draw(batch,parentAlpha);
    }
    protected void Draw(Batch batch, float alpha)
    {
        sprite.setColor(getColor());
        sprite.setOrigin(getOriginX(),getOriginY());
        sprite.draw(batch,alpha);
    }

    //override
    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(),getY());
    }

    @Override
    protected void sizeChanged() {
        sprite.setSize(getWidth(),getHeight());
    }

    @Override
    protected void rotationChanged() {
        sprite.setRotation(getRotation());
    }

    @Override
    protected void scaleChanged() {
        sprite.setScale(getScaleX(),getScaleY());
    }
}
