package GameGDX.GUIData.IAction;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class IAction {
    public String name = "";
    private GDX.Func<IActor> getIActor;
    public abstract Action Get(IActor iActor);
    public abstract void Run(IActor iActor);

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
    protected IActor GetIActor()
    {
        return getIActor.Run();
    }

    public void Init(IActor iActor){
        SetIActor(iActor);
    }
    protected void SetIActor(IActor iActor)
    {
        getIActor = ()->iActor;
    }
    //param
    protected int GetInit(String value)
    {
        if (value.contains(",")) //0,10 random 0->10
        {
            String[] arr = value.split(",");
            return MathUtils.random((int)GetFloatValue(arr[0]),(int)GetFloatValue(arr[1]));
        }
        return Integer.parseInt(value);
    }
    protected float GetFloat(String value)
    {
        if (value.contains(",")) //0,10 random 0->10
        {
            String[] arr = value.split(",");
            return MathUtils.random(GetFloatValue(arr[0]),GetFloatValue(arr[1]));
        }
        if (value.contains("index")) return GetFloatFromIndex(value);//index_0.1 ->index*0.1
        return GetFloatValue(value);
    }
    private float GetFloatFromIndex(String value)//index_0.1;
    {
        Actor actor = GetIActor().GetActor();
        String[] arr = value.split("_");
        return actor.getZIndex()*Float.parseFloat(arr[1]);
    }
    private float GetFloatValue(String value)//(-pw,pw)
    {
        Actor actor = GetIActor().GetActor().getParent();
        value = value.replace("pw",actor.getWidth()+"");//parent width
        value = value.replace("ph",actor.getHeight()+"");//parent height
        return GetExFloat(value);
    }
    private float GetExFloat(String value)
    {
        if (GetIActor().HasParam(value)) return GetIActor().GetParam(value,0f);
        return GetCalculateFloat(value);
    }
    //static
    public static int RandomNumber(String value)
    {
        if (value.contains(",")) //0,10 random 0->10
        {
            String[] arr = value.split(",");
            return MathUtils.random(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]));
        }
        return Integer.parseInt(value);
    }
    //Get Float
    private static float GetCalculateFloat(String value)//a+b+c;
    {
        if (value.charAt(0)=='-') value = "0"+value;
        if (value.contains("+")) return GetCalculateFloat(value,"\\+");
        if (value.contains("-")) return GetCalculateFloat(value,"\\-");
        if (value.contains("*")) return GetCalculateFloat(value,"\\*");
        if (value.contains("/")) return GetCalculateFloat(value,"\\/");
        return Float.parseFloat(value);
    }
    private static float GetCalculateFloat(String value, String sign)
    {
        String[] arr = value.split(sign);
        float vl = GetCalculateFloat(arr[0]);
        for (int i=1;i<arr.length;i++)
        {
            if (sign.equals("\\+")) vl=vl+ GetCalculateFloat(arr[i]);
            if (sign.equals("\\-")) vl=vl- GetCalculateFloat(arr[i]);
            if (sign.equals("\\*")) vl=vl* GetCalculateFloat(arr[i]);
            if (sign.equals("\\/")) vl=vl/ GetCalculateFloat(arr[i]);
        }
        return vl;
    }
}
