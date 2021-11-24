package Extend.Frame;

import GameGDX.Assets;
import GameGDX.Reflect;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class IAnim {
    public String key = "";
    public int start,end;
    public float frameDuration = 0.1f;
    public Animation.PlayMode mode = Animation.PlayMode.NORMAL;

    public Animation<TextureRegion> Get()
    {
        TextureRegion[] arr = new TextureRegion[end-start+1];
        for (int i=start;i<=end;i++)
            arr[i-start] = Assets.GetTexture(key+i);
        Animation<TextureRegion> animation = new Animation<>(frameDuration,arr);
        animation.setPlayMode(mode);
        return animation;
    }
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
