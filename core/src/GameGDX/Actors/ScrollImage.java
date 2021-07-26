package GameGDX.Actors;

import GameGDX.GDX;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScrollImage extends GSprite {
    protected float scroll;
    public float speed = 1f;
    public boolean isScrollX,isScrollY,keepSize;
    protected float trWidth,trHeight;

    public ScrollImage(){}
    public void SetTexture(TextureRegion tr) //not from atlas
    {
        InitTexture(tr);
        GDX.PostRunnable(()->tr.getTexture().
                setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat));
    }
    protected void InitTexture(TextureRegion tr)
    {
        trWidth = tr.getRegionWidth();
        trHeight = tr.getRegionHeight();
        sprite.setRegion(tr);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        scroll += delta*speed;
        if (scroll > 1.0f)  scroll = 0.0f;
        float percentX = 1;
        float percentY = 1;
        if (keepSize){
            percentX = getWidth()/trWidth;
            percentY = getHeight()/trHeight;
        }
        if (IsScroll()) Scroll(percentX,percentY,scroll);
    }
    protected boolean IsScroll()
    {
        return isScrollX || isScrollY;
    }
    protected void Scroll(float percentX,float percentY,float scroll)
    {
        if (isScrollX)
        {
            sprite.setU(-scroll);
            sprite.setU2(percentX-scroll);
        }
        if (isScrollY)
        {
            sprite.setV(-scroll);
            sprite.setV2(percentY-scroll);
        }
    }
}
