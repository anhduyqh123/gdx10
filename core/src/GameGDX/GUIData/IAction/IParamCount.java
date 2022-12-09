package GameGDX.GUIData.IAction;

import GameGDX.Actions.CountAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IParamCount extends IBaseAction{
    public float start,end;
    public String paramName = "";

    public IParamCount()
    {
        name = "param";
    }

    @Override
    public Action Get(IActor iActor) {
        float duration = GetDuration();
        if (duration<=0) return Actions.run(()->Run(iActor));
        return CountAction.Get(f->iActor.SetParam(paramName,f),start,end,duration,iInter.value);
    }

    @Override
    public void Run(IActor iActor) {
        iActor.SetParam(paramName,end);
    }
}
