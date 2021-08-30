package Extend.Frame;

import GameGDX.Assets;
import GameGDX.Reflect;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class IAnim {
    public int start,end;
    public float frameDuration = 0.2f;
    public Animation.PlayMode mode = Animation.PlayMode.NORMAL;

    public Animation<TextureRegion> Get(String name)
    {
        TextureRegion[] arr = new TextureRegion[end-start+1];
        for (int i=start;i<=end;i++)
            arr[i-start] = Assets.GetTexture(name+i);
        Animation<TextureRegion> animation = new Animation<>(frameDuration,arr);
        animation.setPlayMode(mode);
        return animation;
    }
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
