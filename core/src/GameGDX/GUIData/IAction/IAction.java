package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class IAction {
    public String name = "";
    public abstract Action Get(IActor iActor);
    public abstract void Run(IActor iActor);

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }

    protected void Init(IActor iActor){}
    //param
    public static int GetInit(String value)
    {
        if (value.contains(",")) //0,10 random 0->10
        {
            String[] arr = value.split(",");
            return MathUtils.random(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]));
        }
        return Integer.parseInt(value);
    }
    public static float GetFloat(String value)
    {
        if (value.contains(",")) //0,10 random 0->10
        {
            String[] arr = value.split(",");
            return MathUtils.random(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]));
        }
        return Float.parseFloat(value);
    }
}
