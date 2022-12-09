package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IClone extends IParallel{

    public String childName = "";
    public int poolSize = 1;

    public IClone()
    {
        name = "clone";
    }

    public void Init(IActor iActor) {
        super.Init(iActor);
        IGroup iGroup = (IGroup) iActor;
        iGroup.NewPool(childName,poolSize);
    }
    @Override
    public Action Get(IActor iActor) {
        IGroup iGroup = (IGroup) iActor;
        return Actions.run(()->{
            IActor iClone = iGroup.Obtain(childName);
            iClone.GetActor().addAction(super.Get(iClone));
        });
    }
}
