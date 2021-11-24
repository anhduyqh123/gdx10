package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IEvent extends IParallel{
    public enum Event
    {
        OnUpdate,
        OnRemove,
        OnAfterRemove
    }
    public Event event = Event.OnRemove;

    public IEvent() {
        name = "event";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Switch(iActor));
    }
    private void Switch(IActor iActor)
    {
        switch (event)
        {
            case OnUpdate:
                iActor.AddComponent(name,new Component(){
                    @Override
                    protected void Update(float delta) {
                        Run(iActor);
                    }
                });
                break;
            case OnRemove:
                iActor.AddComponent(name,new Component(){
                    @Override
                    public void Remove() {
                        Run(iActor);
                    }
                });
                break;
            case OnAfterRemove:
                iActor.AddComponent(name,new Component(){
                    @Override
                    public void AfterRemove() {
                        Run(iActor);
                    }
                });
                break;
        }
    }

}
