package GameGDX.GUIData.IAction;

import GameGDX.Actions.CountAction;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.ILabel;
import com.badlogic.gdx.scenes.scene2d.Action;

public class ICountAction extends IDelay{
    public float start,end;
    public int tail;

    public ICountAction()
    {
        name = "count";
    }

    @Override
    public Action Get(IActor iActor) {
        ILabel iLabel = (ILabel) iActor;
        String text = iLabel.GetText();
        return CountAction.Get(vl->iLabel.SetNumber(Round(vl),text),start,end,duration);
    }
    public String Round(float value)
    {
        if (tail==0) return (long)value+"";
        long m = (long) Math.pow(10,tail);
        return Math.round(value*m)*1f/m+"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ICountAction)) return false;
        if (!super.equals(o)) return false;
        ICountAction that = (ICountAction) o;
        return Float.compare(that.start, start) == 0 && Float.compare(that.end, end) == 0 && tail == that.tail;
    }
}
