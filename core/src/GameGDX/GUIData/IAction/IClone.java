package GameGDX.GUIData.IAction;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;

public class IClone extends IParallel{

    public String childName = "";
    public int poolSize = 1;
    private GDX.Func<Pool> getPool;

    public IClone()
    {
        name = "clone";
    }

    @Override
    protected void Init(IActor iActor) {
        InitPool((IGroup) iActor);
    }

    private void InitPool(IGroup iGroup)
    {
        if (getPool!=null) return;
        IActor iChild = iGroup.GetIChild(childName);
        Pool<IActor> pool = new Pool<IActor>() {
            @Override
            protected IActor newObject() {
                IActor iClone = iChild.Clone();
                iClone.InitActor();
                iClone.SetConnect(iGroup::GetChild);
                return iClone;
            }
        };
        pool.fill(poolSize);
        getPool = ()->pool;
    }
    @Override
    public Action Get(IActor iActor) {
        IGroup iGroup = (IGroup) iActor;
        IActor iChild = iGroup.GetIChild(childName);

        return Actions.run(()->{
            IActor iClone = (IActor)getPool.Run().obtain();
            iClone.Refresh();
            iClone.AddComponent("remove",new Component(){
                @Override
                public void Remove() {
                    getPool.Run().free(iClone);
                }
            });
            iClone.GetActor().setZIndex(iChild.GetActor().getZIndex());
            iClone.GetActor().addAction(super.Get(iClone));
        });
    }
}
