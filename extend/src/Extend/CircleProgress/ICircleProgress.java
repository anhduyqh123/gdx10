package Extend.CircleProgress;

import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ICircleProgress extends IImage {
    public float percent = 1f;

    @Override
    protected Actor NewActor() {
        return new CircleProgress();
    }

    @Override
    public void RefreshContent() {
        SetTexture(GetTexture());
    }
    @Override
    public void SetTexture(TextureRegion texture) {
        CircleProgress pro = GetActor();
        pro.SetTexture(texture);
        pro.SetValue(percent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ICircleProgress)) return false;
        if (!super.equals(o)) return false;

        ICircleProgress that = (ICircleProgress) o;

        return Float.compare(that.percent, percent) == 0;
    }
}
