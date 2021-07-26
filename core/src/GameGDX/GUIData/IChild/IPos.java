package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IPos {
    public enum Type{
        Local,
        Global
    }

    public Unit x = new Value();
    public Unit y = new Value();
    public IAlign align = IAlign.bottomLeft;
    public float delX,delY;
    public Type type = Type.Local;
    public GDX.Func1<Actor,String> getTarget;

    public float GetX()
    {
        return GetX0()+delX;
    }
    public float GetY()
    {
        return GetY0()+delY;
    }
    public float GetX0()
    {
        if (x instanceof Value) return ((Value) x).value;
        if (x instanceof Ratio) return ((Ratio) x).ratio*GetWidth();
        Target target = (Target)x;
        return getTarget.Run(target.name).getX(target.align.value);
    }
    public float GetY0()
    {
        if (y instanceof Value) return ((Value) y).value;
        if (y instanceof Ratio) return ((Ratio) y).ratio*GetHeight();
        Target target = (Target)y;
        return getTarget.Run(target.name).getY(target.align.value);
    }
    private float GetWidth()
    {
        if (getTarget==null || type==Type.Global) return Scene.width;
        float width = getTarget.Run("").getWidth();
        return width<=0?Scene.width:width;
    }
    private float GetHeight()
    {
        if (getTarget==null || type==Type.Global) return Scene.height;
        float height = getTarget.Run("").getHeight();
        return height<=0?Scene.height:height;
    }
    public void Set(Vector2 pos)
    {
        Set(pos,align);
    }
    public void Set(Vector2 pos, IAlign align)
    {
        x = new Value(pos.x);
        y = new Value(pos.y);
        this.align = align;
    }
    public Vector2 Get()
    {
        Vector2 pos = new Vector2(GetX(),GetY());
        if (getTarget==null || type==Type.Local) return pos;
        pos = getTarget.Run("").stageToLocalCoordinates(pos);
        return pos;
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }

    public static class Unit{
        @Override
        public boolean equals(Object obj) {
            return Reflect.equals(this,obj);
        }
    }
    public static class Value extends Unit
    {
        public float value;

        public Value(){}
        public Value(float value)
        {
            this.value = value;
        }
    }
    public static class Ratio extends Unit
    {
        public float ratio;
        public Ratio(){}
        public Ratio(float ratio)
        {
            this.ratio = ratio;
        }
    }
    public static class Target extends Unit
    {
        public String name = "";
        public IAlign align = IAlign.bottomLeft;
    }
}
