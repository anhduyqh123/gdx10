package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IUpdate extends IParallel{

    public IUpdate() {
        name = "update";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->{
            iActor.AddComponent(name,new Component(){
                @Override
                protected void Update(float delta) {
                    Run(iActor);
                }
            });
        });
    }
}
