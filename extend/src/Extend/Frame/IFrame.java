package Extend.Frame;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Map;

public class IFrame extends IActor {
    public Map<String,IAnim> iAniMap = new HashMap<>();
    public String frame = "";

    @Override
    protected Actor NewActor() {
        return new GFrame(){
            @Override
            public void act(float delta) {
                super.act(delta);
                Update(delta);
            }
        };
    }

    @Override
    public void SetConnect(GDX.Func1<Actor, String> connect) {
        super.SetConnect(connect);
        iSize.getDefaultSize = ()->GetDefaultSize();
    }

    protected Vector2 GetDefaultSize() {
        try {
            Vector2 size = new Vector2();
            TextureRegion tr = Assets.GetTexture(frame);
            size.set(tr.getRegionWidth(),tr.getRegionHeight());
            return size;
        }catch (Exception e){}
        return new Vector2();
    }

    @Override
    public void RefreshContent() {
        GFrame frame = GetActor();
        try {
            for (String k : iAniMap.keySet())
                frame.Add(k,iAniMap.get(k).Get());
            if (iAniMap.containsKey("idle"))
                frame.SetAnimation("idle");
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void Refresh() {
        super.Refresh();
        RefreshContent();
    }
}
