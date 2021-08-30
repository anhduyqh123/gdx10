package Extend.Box2d.IAction;

import Extend.Box2d.IBody;
import GameGDX.GDX;
import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;

public class IBodyAction extends IAction {

    private GDX.Func<IBody> getIBody;
    public IBodyAction()
    {
        name = "iBody";
    }
    @Override
    public Action Get() {
        return null;
    }
    @Override
    public Action Get(IActor iActor) {
        getIBody = ()->iActor.GetComponent(IBody.class);
        return null;
    }
    protected IBody GetIBody()
    {
        return getIBody.Run();
    }
}
