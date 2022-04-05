package GameGDX.GUIData.IChild;

import GameGDX.GDX;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

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

    public GDX.Func<IActor> getIActor;
    //public GDX.Func1<Actor,String> getTarget;

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
    private Group GetParent()
    {
        return GetActor().getParent();
    }

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
        return GetActor(target.name).getX(target.align.value);
    }
    public float GetY0()
    {
        if (y instanceof Value) return ((Value) y).value;
        if (y instanceof Ratio) return ((Ratio) y).ratio*GetHeight();
        Target target = (Target)y;
        return GetActor(target.name).getY(target.align.value);
    }
    private float GetWidth()
    {
        if (type==Type.Global) return GetStage().getWidth();
        float width = GetParent().getWidth();
        float w0 = GetStage().getWidth();
        return width<=0?w0:width;
    }
    private float GetHeight()
    {
        if (type==Type.Global) return GetStage().getHeight();
        float height = GetParent().getHeight();
        float h0 = GetStage().getHeight();
        return height<=0?h0:height;
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
        if (type==Type.Local) return pos;
        return GetParent().stageToLocalCoordinates(pos);
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
