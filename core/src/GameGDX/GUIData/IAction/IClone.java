package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;

public class IClone extends IParallel{

    public String childName = "";
    public int poolSize = 1;

    public IClone()
    {
        name = "clone";
    }

    @Override
    public Action Get(IActor iActor) {
        IGroup iGroup = (IGroup) iActor;
        IActor iChild = iGroup.GetIChild(childName);
        Pool<IActor> pool = new Pool<IActor>(poolSize) {
            @Override
            protected IActor newObject() {
                IActor iClone = iChild.Clone();
                iClone.SetConnect(iGroup::GetChild);
                return iClone;
            }
        };

        return Actions.run(()->{
            IActor iClone = pool.obtain();
            iClone.Refresh();
            iClone.AddComponent("remove",new Component(){
                @Override
                public void Remove() {
                    pool.free(iClone);
                }
            });
            iClone.GetActor().setZIndex(iChild.GetActor().getZIndex());
            iClone.GetActor().addAction(super.Get(iClone));
        });
    }
}
