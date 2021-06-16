package GameGDX.GUIData;

import GameGDX.Actors.ProgressBar;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IProgressBar extends IImage{

    public float percent = 1;

    @Override
    protected Actor NewActor() {
        return new ProgressBar();
    }

    @Override
    public void SetTexture(TextureRegion texture) {
        ProgressBar pro = GetActor();
        pro.SetTexture(texture);
        pro.SetValue(percent);
    }

    @Override
    public void SetTexture(Texture texture) {
        SetTexture(new TextureRegion(texture));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IProgressBar)) return false;
        if (!super.equals(o)) return false;
        IProgressBar that = (IProgressBar) o;
        return Float.compare(that.percent, percent) == 0;
    }
}
