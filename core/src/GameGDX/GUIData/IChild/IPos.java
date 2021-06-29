package GameGDX.GUIData.IChild;

import GameGDX.GDX;
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
    private float GetX0()
    {
        if (x instanceof Value) return ((Value) x).value;
        if (x instanceof Ratio) return ((Ratio) x).ratio*GetWidth();
        Target target = (Target)x;
        return getTarget.Run(target.name).getX(target.align.value);
    }
    private float GetY0()
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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof IPos)) return false;

        IPos iPos = (IPos) object;

        if (Float.compare(iPos.delX, delX) != 0) return false;
        if (Float.compare(iPos.delY, delY) != 0) return false;
        if (!x.equals(iPos.x)) return false;
        if (!y.equals(iPos.y)) return false;
        return align == iPos.align;
    }

    public static class Unit{
        @Override
        public boolean equals(Object obj) {
            return getClass().equals(obj.getClass());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Value)) return false;
            if (!super.equals(o)) return false;
            Value value1 = (Value) o;
            return Float.compare(value1.value, value) == 0;
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

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Ratio)) return false;
            if (!super.equals(object)) return false;
            Ratio ratio1 = (Ratio) object;
            return Float.compare(ratio1.ratio, ratio) == 0;
        }
    }
    public static class Target extends Unit
    {
        public String name = "";
        public IAlign align = IAlign.bottomLeft;

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Target)) return false;
            if (!super.equals(object)) return false;
            Target target = (Target) object;
            if (!name.equals(target.name)) return false;
            return align == target.align;
        }
    }
}
