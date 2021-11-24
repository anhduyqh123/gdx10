package GameGDX.GUIData.IAction;

import GameGDX.Actions.CountAction;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.ILabel;
import com.badlogic.gdx.scenes.scene2d.Action;

public class ICountAction extends IDelay{
    public float start,end;
    public int tail;
    public GDX.Func1<String,Float> format;

    public ICountAction()
    {
        name = "count";
    }

    @Override
    public Action Get(IActor iActor) {
        ILabel iLabel = (ILabel) iActor;
        return CountAction.Get(vl->iLabel.ReplaceText(Format(vl)),start,end,duration);
    }
    private String Format(float value)
    {
        if (format!=null) return format.Run(value);
        else return Round(value);
    }
    private String Round(float value)
    {
        if (tail==0) return (long)value+"";
        long m = (long) Math.pow(10,tail);
        return Math.round(value*m)*1f/m+"";
    }
    public void Set(float start,float end)
    {
        this.start = start;
        this.end = end;
    }
}