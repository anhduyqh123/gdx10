package GameGDX.GUIData.IAction;

import GameGDX.Actions.CountAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;

public class IParamCount extends IBaseAction{
    public float start,end;
    public String paramName = "";

    public IParamCount()
    {
        name = "param";
    }

    @Override
    public Action Get(IActor iActor) {
        return CountAction.Get(f->iActor.SetParam(paramName,f),start,end,GetDuration(),iInter.value);
    }
}
