package GameGDX.GUIData;

import GameGDX.Actors.ProgressBar;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IProgressBar extends IScrollImage{
    public float percent = 1;

    @Override
    protected Actor NewActor() {
        return new ProgressBar();
    }

    @Override
    public void SetTexture(TextureRegion texture) {
        super.SetTexture(texture);
        ProgressBar pro = GetActor();
        pro.SetValue(percent);
    }
}
