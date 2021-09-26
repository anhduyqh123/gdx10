package Extend.Box2d.IAction;

import Extend.Box2d.IBody;
import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IBodyAction extends IAction {
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
        return Actions.run(()->Run(iActor));
    }
    private void Run(IActor iActor)
    {
        IBody iBody = iActor.GetComponent(IBody.class);
        if (iBody==null) return;
        if (iBody.GetBody()!=null) Set(iBody);
        else
            iBody.AddListener(new IBody.IBodyListener(){
                @Override
                public void InitBody() {
                    Set(iBody);
                }
            });
    }
    protected void Set(IBody iBody){}
}
