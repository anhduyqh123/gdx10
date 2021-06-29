package GameGDX.GUIData;

import GameGDX.Actors.ScrollImage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IScrollImage extends IImage{
    public float speed = 1f;
    public boolean isScrollX,isScrollY;
    @Override
    protected Actor NewActor() {
        return new ScrollImage();
    }
    @Override
    public void SetTexture(TextureRegion texture) {
        ScrollImage img = GetActor();
        img.speed = speed;
        img.isScrollX = isScrollX;
        img.isScrollY = isScrollY;
        img.SetTexture(texture);
    }
}
