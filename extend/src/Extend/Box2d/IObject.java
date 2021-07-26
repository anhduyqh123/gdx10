package Extend.Box2d;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IObject extends IActor {

    public IBody iBody = new IBody();
    public String sprite = "";

    @Override
    protected Actor NewActor() {
        return new GObject();
    }

    @Override
    public void Refresh() {
        super.Refresh();

        GObject gObject = GetActor();
        TextureRegion tr = GetTexture();
        gObject.SetTexture(tr);
        gObject.category = GBox2d.GetCategory((short) iBody.category);
        gObject.SetBody(iBody.Get());
        gObject.Refresh();
    }

    public TextureRegion GetTexture()
    {
        try {
            return Assets.GetTexture(sprite);
        }catch (Exception e)
        {
            return new TextureRegion(IImage.emptyTexture);
        }
    }
    @Override
    public void SetConnect(GDX.Func1 connect) {
        super.SetConnect(connect);
        iSize.getDefaultSize = ()->GetDefaultSize();
    }
    protected Vector2 GetDefaultSize() {
        Vector2 size = new Vector2();
        TextureRegion tr = GetTexture();
        size.set(tr.getRegionWidth(),tr.getRegionHeight());
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IObject)) return false;
        if (!super.equals(o)) return false;
        IObject iObject = (IObject) o;
        return iBody.equals(iObject.iBody) && sprite.equals(iObject.sprite);
    }
}
