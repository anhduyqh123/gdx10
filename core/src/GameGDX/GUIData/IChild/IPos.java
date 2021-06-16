package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Scene;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IPos {
    public Unit x = new Value();
    public Unit y = new Value();
    public IAlign align = IAlign.bottomLeft;
    public float delX,delY;
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
        if (x instanceof Ratio){
            float width = getTarget.Run("").getWidth();
            if (width<=0) width = Scene.width;
            return ((Ratio) x).ratio*width;
        }
        Target target = (Target)x;
        return getTarget.Run(target.name).getX(target.align.value);
    }
    private float GetY0()
    {
        if (y instanceof Value) return ((Value) y).value;
        if (y instanceof Ratio){
            float height = getTarget.Run("").getHeight();
            if (height<=0) height = Scene.height;
            return ((Ratio) y).ratio*height;
        }
        Target target = (Target)y;
        return getTarget.Run(target.name).getY(target.align.value);
    }
    public void Set(float fx,float fy,IAlign align)
    {
        x = new Value(fx);
        y = new Value(fy);
        this.align = align;
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
            return this.getClass().equals(obj.getClass());
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
        public boolean equals(Object object) {
            if (!(object instanceof Value)) return false;
            if (!super.equals(object)) return false;
            Value value1 = (Value) object;
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
