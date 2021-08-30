package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ISize {
    public float width,height,scale=1f,scaleX,scaleY,rotate;
    public IAlign origin = IAlign.bottomLeft;
    public boolean extendScreen,fillW,fillH;//fill screen
    public GDX.Func<Vector2> getDefaultSize;
    public GDX.Func1<Actor,String> getTarget;

    private Vector2 GetDefault()
    {
        if (getDefaultSize==null) return null;
        return getDefaultSize.Run();
    }
    private float GetFillX()
    {
        if (getTarget!=null){
            float w = getTarget.Run("").getWidth();
            if (w>0) return w;
        }
        return Scene.width;
    }
    private float GetFillY()
    {
        if (getTarget!=null){
            float h = getTarget.Run("").getHeight();
            if (h>0) return h;
        }
        return Scene.height;
    }
    public float GetWidth()
    {
        if (fillW) return GetFillX();
        if (width>0) return width;
        Vector2 defaultSize = GetDefault();
        if (defaultSize!=null) return defaultSize.x;
        return width;
    }
    public float GetHeight()
    {
        if (fillH) return GetFillY();
        if (height>0) return height;
        Vector2 defaultSize = GetDefault();
        if (defaultSize!=null) return defaultSize.y;
        return height;
    }
    public float GetScale()
    {
        if (extendScreen) return Scene.scale*scale;
        return scale;
    }
    public float GetScaleX()
    {
        return scaleX!=0?scaleX:GetScale();
    }
    public float GetScaleY()
    {
        return scaleY!=0?scaleY:GetScale();
    }
    public void Set(float width,float height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
