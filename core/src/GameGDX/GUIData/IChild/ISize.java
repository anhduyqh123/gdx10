package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ISize {
    //width,height 0->1 => ratio
    public float width,height,scale=1f,scaleX,scaleY,rotate;
    public float delW,delH;
    public float delOriX, delOriY; //delta origin
    public IAlign origin = IAlign.bottomLeft;
    public boolean extendScreen;//fill screen

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
        if (GetActor()==null || GetActor().getStage()==null) return Scene.stage;
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
    private float GetWidth0()
    {
        //if (fillW) return GetFillW();
        if (width==-1) return GetStage().getWidth();
        if (width>1) return width;
        if (width>0) return GetFillW()*width;
        Vector2 defaultSize = GetDefault();
        if (defaultSize!=null) return defaultSize.x;
        return width;
    }
    public float GetWidth()
    {
        return GetWidth0()+delW;
    }
    private float GetHeight0()
    {
        //if (fillH) return GetFillH();
        if (height==-1) return GetStage().getHeight();
        if (height>1) return height;
        if (height>0) return GetFillH()*height;
        Vector2 defaultSize = GetDefault();
        if (defaultSize!=null) return defaultSize.y;
        return height;
    }
    public float GetHeight()
    {
        return GetHeight0()+delH;
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
        actor.setOrigin(origin.value);
        actor.setOriginX(actor.getOriginX()+ delOriX);
        actor.setOriginY(actor.getOriginY()+ delOriY);
//        if (originX!=0 || originY!=0)
//            actor.setOrigin(originX,originY*actor.getHeight());
//        else actor.setOrigin(origin.value);
        actor.setScale(GetScaleX(), GetScaleY());
        actor.setRotation(rotate);
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
