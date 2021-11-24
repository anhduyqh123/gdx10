package Extend.Spine;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.SkeletonData;

public class ISpine extends IActor {
    public String name = "",skin="",animation="";
    public float delX,delY;

    @Override
    protected Actor NewActor() {
        return new GSpine(){
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
        SetSize();
    }

    @Override
    public void Refresh() {
        SetSize();
        super.Refresh();
        SetData();
    }
    private void SetSize()
    {
        try {
            SkeletonData data = Assets.Get(name,SkeletonData.class);
            iSize.getDefaultSize = ()->new Vector2(data.getWidth(),data.getHeight());
        }catch (Exception e){}
    }
    private void SetData()
    {
        try {
            SkeletonData data = Assets.Get(name,SkeletonData.class);
            GSpine gSpine = GetActor();
            gSpine.SetData(data);
            gSpine.SetSkin(skin);
            gSpine.SetSpinePositionBy(delX,delY);
            if (!animation.equals(""))
                gSpine.SetAnimation(animation,true);
        }catch (Exception e){}
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ISpine)) return false;
        if (!super.equals(o)) return false;
        ISpine iSpine = (ISpine) o;
        return Float.compare(iSpine.delX, delX) == 0 && Float.compare(iSpine.delY, delY) == 0 && name.equals(iSpine.name);
    }
}
