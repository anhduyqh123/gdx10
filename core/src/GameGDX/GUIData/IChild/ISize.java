package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ISize {
    public float width,height,scale=1f,scaleX,scaleY,rotate;
    public float originX,originY; //percent 0->1
    public IAlign origin = IAlign.bottomLeft;
    public boolean extendScreen,fillW,fillH;//fill screen

    //interface
    public GDX.Func<Vector2> getDefaultSize;
    public GDX.Func<IActor> getIActor;
    //public GDX.Func1<Actor,String> getTarget;

    private Vector2 GetDefault()
    {
        if (getDefaultSize==null) return null;
        return getDefaultSize.Run();
    }
    private IActor GetIActor()
    {
        return getIActor.Run();
    }
    private Actor GetActor()
    {
        return GetIActor().GetActor();
    }
    private Actor GetActor(String name)
    {
        return GetIActor().GetActor(name);
    }
    private Stage GetStage()
    {
        if (GetActor().getStage()==null) return Scene.stage;
        return GetActor().getStage();
    }

    private float GetFillW()
    {
        float w = GetActor("").getWidth();
        if (w>0) return w;
        return GetStage().getWidth();
    }
    private float GetFillH()
    {
        float h = GetActor("").getHeight();
        if (h>0) return h;
        return GetStage().getHeight();
    }
    public float GetWidth()
    {
        if (fillW) return GetFillW();
        if (width>0) return width;
        Vector2 defaultSize = GetDefault();
        if (defaultSize!=null) return defaultSize.x;
        return width;
    }
    public float GetHeight()
    {
        if (fillH) return GetFillH();
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
    public void Set(Actor actor)
    {
        actor.setSize(GetWidth(),GetHeight());
        if (originX!=0 || originY!=0)
            actor.setOrigin(originX*actor.getWidth(),originY*actor.getHeight());
        else actor.setOrigin(origin.value);
        actor.setScale(GetScaleX(), GetScaleY());
        actor.setRotation(rotate);
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
