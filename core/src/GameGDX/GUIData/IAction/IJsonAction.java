package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.Json;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IJsonAction extends IAction{
    public enum Kind{
        IActor,
        Component
    }
    public Kind kind = Kind.IActor;
    public String json = "";

    public IJsonAction()
    {
        name = "json";
    }

    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }
    private void Run(IActor iActor)
    {
        Object ob = iActor;
        if (kind == Kind.Component) ob = iActor.GetComponent(name);
        Json.ReadFields(ob,json);
    }
}
