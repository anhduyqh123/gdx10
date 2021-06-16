package GameGDX.Actors;

import GameGDX.Actions.CountAction;
import GameGDX.GDX;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class ProgressBar extends Actor {
    public GDX.Runnable<Vector2> onChange;
    private float percent;
    private TextureRegion tr;
    private Sprite sprite;

    public ProgressBar(){}
    public ProgressBar(TextureRegion tr)
    {
        SetTexture(tr);
    }
    public void SetTexture(TextureRegion tr)
    {
        this.tr = tr;
        sprite = new Sprite(tr);
        setSize(tr.getRegionWidth(),tr.getRegionHeight());
    }
    public float GetPercent()
    {
        return percent;
    }
    private float ValidPercent(float percent)
    {
        if (percent>1) return 1;
        if (percent<0) return 0;
        return percent;
    }
    public void SetValue(float percent)
    {
        this.percent = ValidPercent(percent);
        float width = this.percent*getWidth();
        sprite.setRegion(tr,0,0,(int)width,(int)getHeight());
        sprite.setSize((int)width,getHeight());
        if (onChange!=null) onChange.Run(GetPos());
    }
    public void CountValue(float percent,float duration)
    {
        float start = this.percent;
        float end = ValidPercent(percent);
        Action ac = CountAction.Get(this::SetValue,start,end,duration);
        addAction(ac);
    }
    public void CountValue(float percent)
    {
        CountValue(percent,0.1f);
    }
    private Vector2 GetPos()
    {
        return new Vector2(getX(Align.left)+sprite.getWidth(),getY(Align.left));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(),getY());
        sprite.draw(batch,parentAlpha);
    }
}
