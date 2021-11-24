package Extend.Frame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Map;

public class GFrame extends Actor {
    private float stateTime;
    private Map<String,Animation<TextureRegion>> map = new HashMap<>();
    private String current;
    private Runnable done;

    public GFrame() {}

    public void Add(String name,Animation<TextureRegion> animation)
    {
        map.put(name,animation);
    }
    public String[] GetAnimationNames()
    {
        return map.keySet().toArray(new String[0]);
    }
    public void SetAnimation(String name)
    {
        current = name;
        stateTime = 0;
    }
    public void SetAnimation(String name,Runnable done)
    {
        SetAnimation(name);
        this.done = done;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime+=delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (current==null) return;
        Animation<TextureRegion> animation = map.get(current);
        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
        Color color = new Color(getColor());
        color.a*=parentAlpha;
        batch.setColor(color);
        batch.draw(currentFrame,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight()
                ,getScaleX(),getScaleY(),getRotation());
        if (animation.isAnimationFinished(stateTime))
        {
            if (done!=null) done.run();
            done = null;
        }
    }
}
