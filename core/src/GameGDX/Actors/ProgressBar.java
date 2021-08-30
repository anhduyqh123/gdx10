package GameGDX.Actors;

import GameGDX.Actions.CountAction;
import GameGDX.GDX;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Align;

public class ProgressBar extends ScrollImage {
    public GDX.Runnable<Vector2> onChange;
    public float percent = 1f;

    @Override
    public void SetTexture(TextureRegion tr) {
        if (IsScroll()) super.SetTexture(tr);
        else InitTexture(tr);
    }
    public void SetValue(float percent)
    {
        this.percent = ValidPercent(percent);
        if (onChange!=null) onChange.Run(GetPos());
    }
    public void CountValue(float percent,float duration)
    {
        float start = this.percent;
        float end = ValidPercent(percent);
        Action ac = CountAction.Get(this::SetValue,start,end,duration);
        addAction(ac);
    }
    private Vector2 GetPos()
    {
        return new Vector2(getX(Align.left)+percent*getWidth(),getY(Align.left));
    }

    private float ValidPercent(float percent)
    {
        if (percent>1) return 1;
        if (percent<0) return 0;
        return percent;
    }

    @Override
    protected void Scroll(float percentX, float percentY, float scroll) {
        super.Scroll(percent, 1f, scroll);
    }

    @Override
    protected float GetDrawWith() {
        return super.GetDrawWith()*percent;
    }

    @Override
    protected void Draw(Batch batch, float alpha) {
        float width = percent*trWidth;
        tRegion.setRegionWidth((int)width);
        super.Draw(batch, alpha);
    }
}
