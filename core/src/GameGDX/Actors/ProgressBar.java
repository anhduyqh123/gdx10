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
        else sprite.setRegion(tr);
    }
    private boolean IsScroll()
    {
        return isScrollX || isScrollY;
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
    protected void Scroll(float percent, float scroll) {
        super.Scroll(this.percent, scroll);
    }

    @Override
    protected void Draw(Batch batch, float alpha) {
        float width = percent*sprite.getTexture().getWidth();
        sprite.setRegionWidth((int)width);
        sprite.setSize(percent*getWidth(),getHeight());
        super.Draw(batch, alpha);
    }
}
